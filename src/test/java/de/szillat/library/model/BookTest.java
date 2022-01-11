package de.szillat.library.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BookTest {
    @Test
    public void testBookSetTitleNull() {
        final String title = "Ready Player One";

        final Book book = new Book();
        book.setTitle(title);
        assertEquals(title, book.getTitle());

        book.setTitle(null);
        assertEquals(title, book.getTitle());

        book.setTitle("");
        assertEquals(title, book.getTitle());

        final String title2 = "Ready Player Two";
        book.setTitle(title2);
        assertEquals(title2, book.getTitle());
    }
}
