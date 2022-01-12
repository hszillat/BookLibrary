package de.szillat.library;

import de.szillat.library.model.Book;
import de.szillat.library.repository.BookNotFoundException;
import de.szillat.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootApplication
@RestController
public class BookController {
    private static final Logger _log = LoggerFactory.getLogger(BookController.class);

    private final BookRepository bookRepository;

    private final BookModelAssembler assembler;

    public static void main(String[] args) {
        SpringApplication.run(BookController.class, args);
    }

    public BookController(BookRepository bookRepository, BookModelAssembler assembler) {
        this.bookRepository = bookRepository;
        this.assembler = assembler;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/books")
    public ResponseEntity<CollectionModel<EntityModel<Book>>> all() {
        assert bookRepository != null;
        assert assembler != null;

        List<EntityModel<Book>> books =
                StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                        .map(book -> assembler.toModel(book))
                        .collect(Collectors.toList());

        CollectionModel<EntityModel<Book>> bookEntities = CollectionModel.of(books, linkTo(methodOn(BookController.class).all()).withSelfRel());

        return ResponseEntity
                .ok()
                .body(bookEntities);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        assert bookRepository != null;
        assert assembler != null;

        _log.debug("id = {}", id);

        if (id == null) {
            _log.info("No ID given!");

            throw new BookNotFoundException();
        }

        Optional<Book> book;
        if (id.longValue() == 42) {
            book = Optional.of(new Book());
            book.get().id = 42L;
            book.get().setTitle("Ready Player Two");
            book.get().originalTitle = "Ready Player Two";
            book.get().setIsbn("978-0-593-35634-0");
            book.get().publishedYear = 2019;
        } else {
            book = bookRepository.findById(id);
        }

        EntityModel<Book> bookEntity = assembler.toModel(book.orElseThrow(() ->
                new BookNotFoundException(id)));

        return ResponseEntity.ok(bookEntity);
    }

    @PostMapping("/books")
    public ResponseEntity<?> newBook(@RequestBody Book book) {
        assert bookRepository != null;
        assert assembler != null;

        _log.debug("Storing book: '{}'", book);

        EntityModel<Book> bookEntity = assembler.toModel(bookRepository.save(book));

        return ResponseEntity
                .created(bookEntity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(bookEntity);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<?> updateBook(@RequestBody Book newBook, @PathVariable Long id) {
        assert bookRepository != null;
        assert assembler != null;

        _log.debug("Updating book = '{}' with ID = '{}'", newBook, id);

        Book storedBook = bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(newBook.getTitle());
                    book.originalTitle = newBook.originalTitle;
                    book.setIsbn(newBook.getIsbn());
                    book.publishedYear = newBook.publishedYear;

                    return bookRepository.save(book);
                })
                .orElseGet(() -> {
                    newBook.id = id;

                    return bookRepository.save(newBook);
                });

        EntityModel<Book> bookEntity = assembler.toModel(storedBook);

        return ResponseEntity
                .accepted()
                .body(bookEntity);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        assert bookRepository != null;
        assert assembler != null;

        if (id != null)
            bookRepository.deleteById(id);
        else throw new BookNotFoundException(id);

        return ResponseEntity.noContent().build();
    }
}
