package de.szillat.library.controller;

import de.szillat.library.LibraryApplication;
import de.szillat.library.service.BookValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = LibraryApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
public class BookWebControllerTest {
    @Autowired
    private BookRestController bookRestController;

    @Autowired
    private BookValidationService bookValidationService;

    private BookWebController bookWebController;

    @BeforeEach
    public void setup() {
        assertNotNull(bookRestController);
        assertNotNull(bookValidationService);

        bookWebController = new BookWebController(bookRestController, bookValidationService);
    }

    @Test
    public void testGetIndex() {
        assertNotNull(bookWebController);

        ModelAndView mavIndex = bookWebController.index();
        assertNotNull(mavIndex);
        assertEquals("redirect:/books", mavIndex.getViewName());
        assertNotNull(mavIndex.getModelMap());
        assertFalse(mavIndex.getModelMap().isEmpty());
    }

    @Test
    public void testGetBooks() {
        assertNotNull(bookWebController);

        ModelAndView mavBooks = bookWebController.books();
        assertNotNull(mavBooks);
        assertNotNull(mavBooks.getModelMap());
        assertFalse(mavBooks.getModelMap().isEmpty());

        Object allBooks = mavBooks.getModelMap().getAttribute("allBooks");
        assertNotNull(allBooks);
        assertTrue(allBooks instanceof ResponseEntity);
    }
}
