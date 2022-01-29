package de.szillat.library.repository;

import de.szillat.library.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    public void testFindById() {
        Book book = new Book.BookBuilder<>()
                .withTitle("Ready Player Two")
                .withOriginalTitle("Ready Player Two")
                .withISBN("978-0-593-35634-0").
                publishedIn(2019).build();

        Book storedBook = bookRepository.save(book);
        assertNotNull(storedBook);
        assertNotNull(storedBook.getId());
        assertTrue(storedBook.getId() > 0);

        Optional<Book> loadedBookFromDb = bookRepository.findById(storedBook.getId());
        assertNotNull(loadedBookFromDb);
        assertTrue(loadedBookFromDb.isPresent());
        assertEquals(book.getTitle(), loadedBookFromDb.get().getTitle());

        Optional<Book> thisBookIsNotFound = bookRepository.findById(-1L);
        assertNotNull(thisBookIsNotFound);
        assertFalse(thisBookIsNotFound.isPresent());
    }

    @Test
    public void testFindByIsbn() {
        Book book = new Book.BookBuilder<>()
                .withTitle("Ready Player Two")
                .withOriginalTitle("Ready Player Two")
                .withISBN("978-0-593-35634-0").
                publishedIn(2019).build();

        Book storedBook = bookRepository.save(book);
        assertNotNull(storedBook);
        assertNotNull(storedBook.getId());
        assertTrue(storedBook.getId() > 0);

        Optional<Book> bookFoundByIsbn = bookRepository.findByIsbn(book.getIsbn());
        assertNotNull(bookFoundByIsbn);
        assertTrue(bookFoundByIsbn.isPresent());
        assertEquals(book.getTitle(), bookFoundByIsbn.get().getTitle());
        assertEquals(book.getIsbn(), bookFoundByIsbn.get().getIsbn());

        Optional<Book> thisBookIsNotFound = bookRepository.findByIsbn("XXXX");
        assertNotNull(thisBookIsNotFound);
        assertFalse(thisBookIsNotFound.isPresent());
    }
}
