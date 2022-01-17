package de.szillat.library;

import de.szillat.library.controller.BookRestController;
import de.szillat.library.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@Controller
public class BookWebController {
    private static final Logger _log = LoggerFactory.getLogger(BookWebController.class);

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MMMM yyyy");

    private final BookRestController bookRestController;

    public BookWebController(BookRestController bookRestController) {
        this.bookRestController = bookRestController;
    }

    @GetMapping("/books")
    ModelAndView books() {
        assert bookRestController != null;

        ResponseEntity<CollectionModel<EntityModel<Book>>> allBooks = bookRestController.all();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("books");
        mav.addObject("title", "My library application");
        mav.addObject("allBooks", allBooks);
        mav.addObject("today", DATE_FORMAT.format(LocalDateTime.now()));

        return mav;
    }

    @GetMapping("/add_book")
    ModelAndView addBook() {
        Book book = new Book();
        book.publishedYear = LocalDateTime.now().getYear();

        ModelAndView mav = new ModelAndView();
        mav.setViewName("add_book");
        mav.addObject("title", "My library application");
        mav.addObject("book", book);

        return mav;
    }

    @PostMapping("/submit_book")
    ModelAndView saveBook(@ModelAttribute Book book) {
        assert bookRestController != null;

        _log.debug("Got Book = '{}'", book);

        ResponseEntity<?> savedBook = bookRestController.newBook(book);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("show_book");
        mav.addObject("title", "My library application");
        mav.addObject("book", ((EntityModel) savedBook.getBody()).getContent());

        return mav;
    }
}
