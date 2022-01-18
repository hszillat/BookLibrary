package de.szillat.library.service;

import de.szillat.library.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BookValidationService {
    private static final Logger _log = LoggerFactory.getLogger(BookValidationService.class);

    private static final Pattern REGEX_ISBN_10 = Pattern.compile("^(?:ISBN(?:-10)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");

    private static final Pattern REGEX_ISBN_13 = Pattern.compile("^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$");

    public List<String> validateBook(Book book) {
        if (book == null) return Collections.emptyList();

        List<String> errors = new LinkedList<>();
        Matcher isbn10Matcher = REGEX_ISBN_10.matcher(book.getIsbn());
        if (!isbn10Matcher.matches()) {
            _log.debug("No match for ISBN-10: '{}'", book.getIsbn());

            Matcher isbn13Matcher = REGEX_ISBN_13.matcher(book.getIsbn());
            if (!isbn13Matcher.matches()) {
                _log.debug("No match for ISBN-13: '{}'", book.getIsbn());

                errors.add(String.format("'%s' is not ISBN-10/ISBN-13", book.getIsbn()));
            }
        }

        return errors;
    }
}
