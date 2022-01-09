package de.szillat.library.model;

import java.util.Optional;

public class Book {
    public Long id;
    public String title;
    public Optional<String> originalTitle;
    public String isbn;
    public int publishedYear;

    private Book() {
        id = null;
        title = isbn = "";
        originalTitle = Optional.empty();
        publishedYear = 0;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle.orElse("") + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publishedYear=" + publishedYear +
                '}';
    }
}
