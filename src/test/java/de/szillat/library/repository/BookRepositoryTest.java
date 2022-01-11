package de.szillat.library.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.szillat.library.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    public void testFindById() {
        Book book = new Book();
        book.setTitle( "Ready Player Two");
        book.originalTitle = "Ready Player Two";
        book.setIsbn ("978-0-593-35634-0");
        book.publishedYear = 2019;

        Book storedBook = bookRepository.save(book);
        assertNotNull(storedBook);
        assertNotNull(storedBook.id);
        assertTrue(storedBook.id > 0);

        Optional<Book> loadedBookFromDb = bookRepository.findById(storedBook.id);
        assertNotNull(loadedBookFromDb);
        assertTrue(loadedBookFromDb.isPresent());
        assertEquals(book.getTitle(), loadedBookFromDb.get().getTitle());

        Optional<Book> thisBookIsNotFound = bookRepository.findById(Long.valueOf(-1));
        assertNotNull(thisBookIsNotFound);
        assertFalse(thisBookIsNotFound.isPresent());
    }

    @Test
    public void testFindByIsbn() {
        Book book = new Book();
        book.setTitle ("Ready Player Two");
        book.originalTitle = "Ready Player Two";
        book.setIsbn ( "978-0-593-35634-0");
        book.publishedYear = 2019;

        Book storedBook = bookRepository.save(book);
        assertNotNull(storedBook);
        assertNotNull(storedBook.id);
        assertTrue(storedBook.id > 0);

        Optional<Book> bookFoundByIsbn = bookRepository.findByIsbn(book.getIsbn());
        assertNotNull(bookFoundByIsbn);
        assertTrue(bookFoundByIsbn.isPresent());
        assertEquals(book.getTitle(), bookFoundByIsbn.get().getTitle());
        assertEquals(book.getIsbn(), bookFoundByIsbn.get().getIsbn());

        Optional<Book>thisBookIsNotFound = bookRepository.findByIsbn("XXXX");
        assertNotNull(thisBookIsNotFound);
        assertFalse(thisBookIsNotFound.isPresent());
    }
}
