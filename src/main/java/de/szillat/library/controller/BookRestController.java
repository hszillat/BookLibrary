package de.szillat.library.controller;

import de.szillat.library.model.Book;
import de.szillat.library.repository.BookNotFoundException;
import de.szillat.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class BookRestController {
    private static final Logger _log = LoggerFactory.getLogger(BookRestController.class);

    private final BookRepository bookRepository;

    public BookRestController(@NonNull BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/service/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/service/books")
    public List<Book> all() {
        return StreamSupport
                .stream(bookRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/service/books/{id}")
    public Book one(@PathVariable Long id) {
        _log.debug("id = {}", id);

        if (id == null) {
            _log.info("No ID given!");

            throw new BookNotFoundException();
        }

        Optional<Book> book = bookRepository.findById(id);
        _log.debug("Found book = '{}'", book);

        return book.orElseThrow(BookNotFoundException::new);
    }

    @PostMapping("/service/books")
    public Book newBook(@RequestBody Book book) {
        _log.debug("Storing book: '{}'", book);

        return bookRepository.save(book);
    }

    @PutMapping("/service/books/{id}")
    public Book updateBook(@RequestBody Book newBook, @PathVariable Long id) {
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

        _log.debug("storedBook = '{}'", storedBook);

        return storedBook;
    }

    @DeleteMapping("/service/books/{id}")
    public void deleteBook(@PathVariable Long id) {
        if (id != null)
            bookRepository.deleteById(id);
        else throw new BookNotFoundException(id);
    }
}
