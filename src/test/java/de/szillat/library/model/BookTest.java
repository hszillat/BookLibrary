package de.szillat.library.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BookTest {
    @Test
    public void testBookSetTitleNull() {
        final String title = "Ready Player One";

        final Book book = new Book();
        book.setTitle(title);
        assertEquals(title, book.getTitle());

        try {
            book.setTitle(null);
            fail("Unexpected failure of exception!");
        } catch (NullPointerException e) {
            // Book::setTitle is annotated as @NonNull, thus the exception is expected.
        }

        book.setTitle("");
        assertEquals("", book.getTitle());

        final String title2 = "Ready Player Two";
        book.setTitle(title2);
        assertEquals(title2, book.getTitle());
    }
}
