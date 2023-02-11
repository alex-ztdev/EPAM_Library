package com.my.library.utils.builder;

import com.my.library.controller.command.constant.parameters.BookParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.entities.Book;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestBookBuilderTest {
    @Mock
    private HttpServletRequest request;

    private RequestBookBuilder requestBookBuilder = new RequestBookBuilder();

    @Test
    void buildBookForSave_ValidBook_ReturnsOptionalOfBook() {
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("F. Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.COPIES)).thenReturn("2");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        Optional<Book> optionalBook = requestBookBuilder.buildBookForSave(request);

        assertThat(optionalBook).isNotEmpty();

        Book book = optionalBook.get();

        assertThat(book.getTitle()).isEqualTo("The Great Gatsby");
        assertThat(book.getAuthor().getFirstName()).isEqualTo("F. Scott");
        assertThat(book.getAuthor().getSecondName()).isEqualTo("Fitzgerald");
        assertThat(book.getGenre()).isEqualTo("Novel");
        assertThat(book.getPublisherTitle()).isEqualTo("Scribner");
        assertThat(book.getPageNumber()).isEqualTo(180);
        assertThat(book.getPublicationDate()).isEqualTo(LocalDate.of(1925, 4, 10));
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    void buildBookForSave_InvalidTitle_ReturnsEmptyOptional(String title) {
        when(request.getParameter(BookParameters.TITLE)).thenReturn(title);
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("F. Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.COPIES)).thenReturn("2");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        Optional<Book> optionalBook = requestBookBuilder.buildBookForSave(request);

        assertThat(optionalBook).isEmpty();
    }

    @Test
    public void buildBookForUpdate_ValidData_ReturnsOptionalOfBook() {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");
        when(request.getParameter(BookParameters.COPIES)).thenReturn("10");
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("F. Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");


        Optional<Book> optionalBook = requestBookBuilder.buildBookForUpdate(request);

        assertThat(optionalBook).isPresent();

        Book book = optionalBook.get();

        assertThat(book.getBookId()).isEqualTo(1L);
        assertThat(book.getTitle()).isEqualTo("The Great Gatsby");
        assertThat(book.getAuthor().getFirstName()).isEqualTo("F. Scott");
        assertThat(book.getAuthor().getSecondName()).isEqualTo("Fitzgerald");
        assertThat(book.getGenre()).isEqualTo("Novel");
        assertThat(book.getPublisherTitle()).isEqualTo("Scribner");
        assertThat(book.getPageNumber()).isEqualTo(180);
        assertThat(book.getPublicationDate()).isEqualTo(LocalDate.of(1925, 4, 10));
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    public void buildBookForUpdate_EmptyOrNullBookId_ReturnsEmptyOptional(String invalidId) {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn(invalidId);
        when(request.getParameter(BookParameters.COPIES)).thenReturn("10");
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("F. Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        Optional<Book> optionalBook = requestBookBuilder.buildBookForUpdate(request);

        assertThat(optionalBook).isEmpty();

    }

    @ParameterizedTest
    @ValueSource(strings = {"not_a_number", "me_too", "random_string"})
    public void buildBookForUpdate_NaNBookId_ReturnsEmptyOptional(String invalidId) {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn(invalidId);
        when(request.getParameter(BookParameters.COPIES)).thenReturn("10");
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("F. Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        Optional<Book> optionalBook = requestBookBuilder.buildBookForUpdate(request);

        assertThat(optionalBook).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"-100", "-50", "-25", "-10","-5"})
    public void buildBookForUpdate_NegativeBookId_ReturnsEmptyOptional(String invalidId) {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn(invalidId);
        when(request.getParameter(BookParameters.COPIES)).thenReturn("10");
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("F. Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        Optional<Book> optionalBook = requestBookBuilder.buildBookForUpdate(request);

        assertThat(optionalBook).isEmpty();
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    public void buildBookForUpdate_EmptyOrNullCopies_ReturnsEmptyOptional(String invalidCopies) {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");
        when(request.getParameter(BookParameters.COPIES)).thenReturn(invalidCopies);
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("F. Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        Optional<Book> optionalBook = requestBookBuilder.buildBookForUpdate(request);

        assertThat(optionalBook).isEmpty();

    }

    @ParameterizedTest
    @ValueSource(strings = {"not_a_number", "me_too", "random_string"})
    public void buildBookForUpdate_NaNCopies_ReturnsEmptyOptional(String invalidCopies) {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");
        when(request.getParameter(BookParameters.COPIES)).thenReturn(invalidCopies);
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("F. Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        Optional<Book> optionalBook = requestBookBuilder.buildBookForUpdate(request);

        assertThat(optionalBook).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"-100", "-50", "-25", "-10","-5", "-1"})
    public void buildBookForUpdate_NegativeCopies_ReturnsEmptyOptional(String invalidId) {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn(invalidId);
        when(request.getParameter(BookParameters.COPIES)).thenReturn("10");
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("F. Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        Optional<Book> optionalBook = requestBookBuilder.buildBookForUpdate(request);

        assertThat(optionalBook).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"100", "50", "25", "10","5", "1", "0"})
    public void buildBookForUpdate_ValidCopies_ReturnsOptionalOfBook(String invalidId) {
        when(request.getParameter(Parameters.BOOK_ID)).thenReturn(invalidId);
        when(request.getParameter(BookParameters.COPIES)).thenReturn("10");
        when(request.getParameter(BookParameters.TITLE)).thenReturn("The Great Gatsby");
        when(request.getParameter(BookParameters.AUTHOR_FIRST_NAME)).thenReturn("F. Scott");
        when(request.getParameter(BookParameters.AUTHOR_SECOND_NAME)).thenReturn("Fitzgerald");
        when(request.getParameter(BookParameters.GENRE)).thenReturn("Novel");
        when(request.getParameter(BookParameters.PUBLISHER)).thenReturn("Scribner");
        when(request.getParameter(BookParameters.PAGES)).thenReturn("180");
        when(request.getParameter(BookParameters.PUBLICATION_DATE)).thenReturn("1925-04-10");

        Optional<Book> optionalBook = requestBookBuilder.buildBookForUpdate(request);

        assertThat(optionalBook).isPresent();
    }

}