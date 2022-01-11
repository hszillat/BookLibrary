package de.szillat.library.repository;

public class BookNotFoundException extends RuntimeException {
    final public Long id;

    public BookNotFoundException() {
        super();

        this.id = null;
    }

    public BookNotFoundException(final Long id) {
        super("Book with id = " + id + " not found");

        this.id = id;
    }
}
