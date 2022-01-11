package de.szillat.library;

import de.szillat.library.model.Book;
import de.szillat.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@SpringBootApplication
@RestController
public class LibraryApplication {
    private static final Logger _log = LoggerFactory.getLogger(LibraryApplication.class);

    @Autowired
    BookRepository bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/library/{id}")
    public Book getBookById(@PathVariable Long id) {
        assert bookRepository != null;

        _log.debug("id = {}", id);

        if (id == null) {
            _log.info("No ID given!");

            // TODO
            return null;
        }

        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) return book.get();

        // TODO
        _log.warn("No book with id={} found!", id);

        return null;

        // TODO
        // return repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @PostMapping("/library")
    public Book newBook(@RequestBody Book book) {
        _log.debug("Storing book: '{}'", book);

        assert bookRepository != null;
        return bookRepository.save(book);
    }

    @PutMapping("/library/{id}")
    public Book updateBook(@RequestBody Book newBook, @PathVariable Long id) {
        _log.debug("Updating book = '{}' with ID = '{}'", newBook, id);

        assert bookRepository != null;

        return bookRepository.findById(id)
                .map(book -> {
                    book.title = newBook.title;
                    book.originalTitle = newBook.originalTitle;
                    book.isbn = newBook.isbn;
                    book.publishedYear = newBook.publishedYear;

                    return bookRepository.save(book);
                })
                .orElseGet(() -> {
                    newBook.id = id;

                    return bookRepository.save(newBook);
                });
    }
}
