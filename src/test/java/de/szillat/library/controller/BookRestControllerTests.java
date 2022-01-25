package de.szillat.library.controller;

import de.szillat.library.LibraryApplication;
import de.szillat.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = LibraryApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
class BookRestControllerTests {
    @Autowired
    private BookRepository bookRepository;

    @Test
    void contextLoads() {
        assertNotNull(bookRepository);
    }
}
