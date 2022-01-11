package de.szillat.library;

import de.szillat.library.model.Book;
import de.szillat.library.repository.BookNotFoundException;
import de.szillat.library.repository.BookRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
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

    @GetMapping("/library")
    public List<Book> getAll() {
        assert bookRepository != null;

        final List<Book> books = new LinkedList();
        bookRepository.findAll().forEach(books::add);

        return books;
    }

    @GetMapping("/library/{id}")
    public Book getBookById(@PathVariable Long id) {
        assert bookRepository != null;

        _log.debug("id = {}", id);

        if (id == null) {
            _log.info("No ID given!");

            throw new BookNotFoundException();
        }

        if (id == Long.valueOf(42)) {
            Book book = new Book();
            book.id = Long.valueOf(42);
            book.setTitle("Ready Player Two");
            book.originalTitle = "Ready Player Two";
            book.setIsbn("978-0-593-35634-0");
            book.publishedYear = 2019;

            return book;
        }

        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
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
    }

    @DeleteMapping("/library/{id}")
    public void deleteBook(@PathVariable Long id) {
        assert bookRepository != null;

        if (id != null)
            bookRepository.deleteById(id);
        else throw new BookNotFoundException(id);
    }
}
