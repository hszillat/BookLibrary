package de.szillat.library;

import de.szillat.library.controller.BookRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
@Controller
public class BookWebController {
    private static final Logger _log = LoggerFactory.getLogger(BookWebController.class);

    private final BookRestController bookRestController;

    public BookWebController(BookRestController bookRestController) {
        this.bookRestController = bookRestController;
    }

    @GetMapping("/books")
    ModelAndView books() {
        assert bookRestController != null;

        ModelAndView mav = new ModelAndView();
        mav.setViewName("books");

        return mav;
    }
}
