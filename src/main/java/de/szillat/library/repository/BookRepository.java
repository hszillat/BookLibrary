package de.szillat.library.repository;

import de.szillat.library.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
    Book findById(long id);
}
