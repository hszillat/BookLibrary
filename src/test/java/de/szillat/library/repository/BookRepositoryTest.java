package de.szillat.library.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        book.title = "Ready Player Two";
        book.originalTitle = "Ready Player Two";
        book.isbn = "978-0-593-35634-0";
        book.publishedYear = 2019;

        Book storedBook = bookRepository.save(book);
        assertNotNull(storedBook);
        assertNotNull(storedBook.id);
        assertTrue(storedBook.id > 0);

        Optional<Book> loadedBookFromDb = bookRepository.findById(storedBook.id);
        assertNotNull(loadedBookFromDb);
        assertTrue(loadedBookFromDb.isPresent());
        assertEquals(book.title, loadedBookFromDb.get().title);
    }
}
