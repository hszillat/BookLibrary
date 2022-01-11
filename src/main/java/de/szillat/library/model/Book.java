package de.szillat.library.model;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "books", indexes = @Index(columnList = "isbn"))
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @NonNull
    public String title;

    public String originalTitle;

    @NonNull
    public String isbn;

    public int publishedYear;

    public Book() {
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
