package de.szillat.library.controller;

import de.szillat.library.BookModelAssembler;
import de.szillat.library.model.Book;
import de.szillat.library.repository.BookNotFoundException;
import de.szillat.library.repository.BookRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class BookHateosController {
    private static final Logger _log = LoggerFactory.getLogger(BookHateosController.class);

    private final BookRepository bookRepository;

    private final BookModelAssembler assembler;

    public BookHateosController(@NonNull BookRepository bookRepository,
                                @NonNull BookModelAssembler assembler) {
        this.bookRepository = bookRepository;
        this.assembler = assembler;
    }

    @GetMapping("/service/rest/books")
    public ResponseEntity<CollectionModel<EntityModel<Book>>> all() {
        List<EntityModel<Book>> books =
                StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                        .map(assembler::toModel)
                        .collect(Collectors.toList());

        CollectionModel<EntityModel<Book>> bookEntities = CollectionModel.of(books, linkTo(methodOn(BookRestController.class).all()).withSelfRel());

        return ResponseEntity
                .ok()
                .body(bookEntities);
    }

    @GetMapping("/service/rest/books/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        _log.debug("id = {}", id);

        if (id == null) {
            _log.info("No ID given!");

            throw new BookNotFoundException();
        }

        Optional<Book> book = bookRepository.findById(id);
        EntityModel<Book> bookEntity = assembler.toModel(book.orElseThrow(() ->
                new BookNotFoundException(id)));

        return ResponseEntity.ok(bookEntity);
    }

    @PostMapping("/service/rest/books")
    public ResponseEntity<?> newBook(@RequestBody Book book) {
        _log.debug("Storing book: '{}'", book);

        if (StringUtils.isBlank(book.getTitle())) {
            return ResponseEntity.accepted().build();
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        if (violations.isEmpty()) {
            EntityModel<Book> bookEntity = assembler.toModel(bookRepository.save(book));

            return ResponseEntity
                    .created(bookEntity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(bookEntity);
        }

        return ResponseEntity.accepted().body(violations);
    }

    @PutMapping("/service/rest/books/{id}")
    public ResponseEntity<?> updateBook(@RequestBody Book newBook, @PathVariable Long id) {
        _log.debug("Updating book = '{}' with ID = '{}'", newBook, id);

        Book storedBook = bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(newBook.getTitle());
                    book.setOriginalTitle(newBook.getOriginalTitle());
                    book.setIsbn(newBook.getIsbn());
                    book.setPublishedYear(newBook.getPublishedYear());

                    return bookRepository.save(book);
                })
                .orElseGet(() -> {
                    newBook.setId(id);

                    return bookRepository.save(newBook);
                });

        EntityModel<Book> bookEntity = assembler.toModel(storedBook);

        return ResponseEntity
                .accepted()
                .body(bookEntity);
    }

    @DeleteMapping("/service/rest/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (id != null)
            bookRepository.deleteById(id);
        else throw new BookNotFoundException(id);

        return ResponseEntity.noContent().build();
    }
}
