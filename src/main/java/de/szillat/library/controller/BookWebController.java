package de.szillat.library.controller;

import de.szillat.library.model.Book;
import de.szillat.library.service.BookValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
@Controller
public class BookWebController {
    private static final Logger _log = LoggerFactory.getLogger(BookWebController.class);

    private static final String APP_NAME = "My Library Application";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MMMM yyyy");

    private final BookRestController bookRestController;

    private final BookValidationService bookValidationService;

    public BookWebController(BookRestController bookRestController, BookValidationService bookValidationService) {
        this.bookRestController = bookRestController;
        this.bookValidationService = bookValidationService;
    }

    @GetMapping("/")
    ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", APP_NAME);
        mav.setViewName("redirect:/books");

        return mav;
    }

    @GetMapping("/books")
    ModelAndView books() {
        assert bookRestController != null;

        ResponseEntity<CollectionModel<EntityModel<Book>>> allBooks = bookRestController.all();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("books");
        mav.addObject("title", APP_NAME);
        mav.addObject("allBooks", allBooks);
        mav.addObject("today", DATE_FORMAT.format(LocalDateTime.now()));

        return mav;
    }

    @GetMapping("/show_book/{id}")
    @SuppressWarnings({"unused", "unchecked"})
    ModelAndView showBook(@PathVariable(value = "id") Long id) {
        assert id != null;

        ResponseEntity<EntityModel<Book>> bookResponseEntity = (ResponseEntity<EntityModel<Book>>) bookRestController.one(id);
        if (bookResponseEntity.getStatusCode().value() == HttpStatus.OK.value()) {
            assert bookResponseEntity.getBody() != null;
            assert bookResponseEntity.getBody().getContent() != null;

            Book book = bookResponseEntity.getBody().getContent();

            ModelAndView mav = new ModelAndView("add_book", "book", book);
            mav.addObject("title", APP_NAME);

            return mav;
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/books");
        mav.addObject("title", APP_NAME);

        return mav;
    }

    @GetMapping("/add_book")
    ModelAndView addBook() {
        ModelAndView mav = new ModelAndView("add_book", "book", new Book.BookBuilder<>().build());
        mav.addObject("title", APP_NAME);

        return mav;
    }

    @PostMapping("/add_book")
    @SuppressWarnings("unused")
    ModelAndView addBook(@Valid Book book, BindingResult result, Model model) {
        assert bookRestController != null;
        assert bookValidationService != null;

        _log.debug("Got Book = '{}'", book);

        List<String> errors = bookValidationService.validateBook(book);
        if (!errors.isEmpty()) {
            for (String error : errors) {
                result.addError(new ObjectError("globalError", error));
            }
        }

        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("add_book", "book", book);
            mav.addObject("title", APP_NAME);
            mav.addObject(result);

            return mav;
        }

        ResponseEntity<?> savedBookResponse = bookRestController.newBook(book);
        if (savedBookResponse.getStatusCode().value() == HttpStatus.OK.value()
                || savedBookResponse.getStatusCode().value() == HttpStatus.CREATED.value()) {
            assert savedBookResponse.getBody() != null;

            ModelAndView mav = new ModelAndView();
            mav.setViewName("redirect:/books");
            mav.addObject("title", APP_NAME);
            mav.addObject("book", ((EntityModel<?>) savedBookResponse.getBody()).getContent());

            return mav;
        }

        // Error occurred.
        _log.error("Validation of Book = '{}' failed!", book);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("add_book");
        mav.addObject("title", APP_NAME);
        mav.addObject("book", book);

        result.addError(new ObjectError("globalError", "Could not store book!"));
        mav.addObject(result);

        return mav;
    }
}
