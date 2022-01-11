package de.szillat.library.model;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "books", indexes = @Index(columnList = "isbn"))
public class Book {
    private static final Logger _log = LoggerFactory.getLogger(Book.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @NonNull
    private String title;

    public String originalTitle;

    @NonNull
    private String isbn;

    public int publishedYear;

    public Book() {
        id = null;
        title = isbn = null;
        originalTitle = null;
        publishedYear = 0;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        if (StringUtils.isNotBlank(title))
            this.title = title;

        _log.debug("Given title is empty!");
    }

    @NonNull
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(@NonNull String isbn) {
        if (StringUtils.isNotBlank(isbn))
            this.isbn = isbn;

        _log.debug("Given isbn is empty!");
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
