package de.szillat.library.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Optional;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String title;
    public String originalTitle;
    public String isbn;
    public int publishedYear;

    protected Book() {
        id = null;
        title = isbn = null;
        originalTitle = null;
        publishedYear = 0;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publishedYear=" + publishedYear +
                '}';
    }
}
