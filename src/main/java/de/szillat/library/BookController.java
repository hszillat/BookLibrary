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

    public static void main(String[] args) {
        SpringApplication.run(BookController.class, args);
    }

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/books")
    public CollectionModel<EntityModel<Book>> all() {
        assert bookRepository != null;

        List<EntityModel<Book>> books =
                StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                        .map(book -> EntityModel.of(book,
                                linkTo(methodOn(BookController.class).one(book.id)).withSelfRel(),
                                linkTo(methodOn(BookController.class).all()).withRel("books")))
                        .collect(Collectors.toList());

        return CollectionModel.of(books, linkTo(methodOn(BookController.class).all()).withSelfRel());
    }

    @GetMapping("/books/{id}")
    public EntityModel<Book> one(@PathVariable Long id) {
        assert bookRepository != null;

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

        if (book.isPresent()) {
            return EntityModel.of(book.get(),
                    linkTo(methodOn(BookController.class).one(id)).withSelfRel(),
                    linkTo(methodOn(BookController.class).all()).withRel("books"));
        }

        throw new BookNotFoundException(id);
    }

    @PostMapping("/books")
    public EntityModel<Book> newBook(@RequestBody Book book) {
        assert bookRepository != null;

        _log.debug("Storing book: '{}'", book);

        return EntityModel.of(bookRepository.save(book),
                linkTo(methodOn(BookController.class).one(book.id)).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books"));
    }

    @PutMapping("/books/{id}")
    public EntityModel<Book> updateBook(@RequestBody Book newBook, @PathVariable Long id) {
        _log.debug("Updating book = '{}' with ID = '{}'", newBook, id);

        assert bookRepository != null;

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

        return EntityModel.of(storedBook,
                linkTo(methodOn(BookController.class).one(storedBook.id)).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books"));
    }

    @DeleteMapping("/books/{id}")
    public EntityModel<Void> deleteBook(@PathVariable Long id) {
        assert bookRepository != null;

        if (id != null)
            bookRepository.deleteById(id);
        else throw new BookNotFoundException(id);

        return EntityModel.of(null,
                linkTo(methodOn(BookController.class).one(id)).withSelfRel(),
                linkTo(methodOn(BookController.class).all()).withRel("books"));
    }
}
