package de.szillat.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "books", indexes = {@Index(columnList = "isbn"), @Index(columnList = "title")})
public class Book {
    private static final Logger _log = LoggerFactory.getLogger(Book.class);

    public static class BookBuilder<SELF extends BookBuilder<SELF>> {
        private Long id;
        private String title;
        private String originalTitle;
        private String isbn;
        private int publishedYear;

        public BookBuilder() {
            this.id = null;
            this.publishedYear = LocalDateTime.now().getYear();
        }

        @SuppressWarnings("unused")
        public BookBuilder(String title) {
            this();

            this.title = StringUtils.trim(title);
        }

        @SuppressWarnings({"unused", "unchecked"})
        public SELF withId(Long id) {
            if (id != null) this.id = id;

            return (SELF) this;
        }

        @SuppressWarnings("unchecked")
        public SELF withTitle(String title) {
            if (StringUtils.isNotBlank(title)) this.title = StringUtils.trim(title);

            return (SELF) this;
        }

        @SuppressWarnings("unchecked")
        public SELF withOriginalTitle(String originalTitle) {
            if (StringUtils.isNotBlank(originalTitle)) this.originalTitle = StringUtils.trim(originalTitle);

            return (SELF) this;
        }

        @SuppressWarnings("unchecked")
        public SELF withISBN(String isbn) {
            if (StringUtils.isNotBlank(isbn)) this.isbn = StringUtils.trim(isbn);

            return (SELF) this;
        }

        @SuppressWarnings("unchecked")
        public SELF publishedIn(int publishedYear) {
            this.publishedYear = publishedYear;

            return (SELF) this;
        }

        public Book build() {
            return new Book(this);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @NotEmpty(message = "A title may not be empty.")
    @Size(min = 1, max = 200, message = "A title must be at least one character long, but not more than 200 chars")
    private String title;

    private String originalTitle;

    @NonNull
    @NotEmpty(message = "An ISBN may not be empty.")
    @Size(min = 10, max = 25, message = "A valid ISBN is between 10 and 25 chars long")
    private String isbn;

    @Positive(message = "No books before AD")
    @Max(value = 9999, message = "No books after the Y9K-problem!")
    private int publishedYear;

    private Book(BookBuilder<?> builder) {
        this();

        setId(builder.id);
        if (StringUtils.isNotBlank(builder.title)) setTitle(builder.title);
        setOriginalTitle(builder.originalTitle);
        if (StringUtils.isNotBlank(builder.isbn)) setIsbn(builder.isbn);
        setPublishedYear(builder.publishedYear);
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
