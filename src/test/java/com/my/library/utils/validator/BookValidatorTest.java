package com.my.library.utils.validator;

import com.my.library.entities.Author;
import com.my.library.entities.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookValidatorTest {
    private BookValidator bookValidator = new BookValidator();

    @ParameterizedTest
    @ValueSource(strings = {"||+++***|", "Almost valid title@", "майже правильна назва/", "123 <>"})
    void validateBook_invalidTitle_ShouldReturnFalse(String title) {
        Author author = mock(Author.class);

        var res = bookValidator.validateBook(new Book(title,
                "publisher",
                "other",
                100,
                LocalDate.now(),
                author)
        );

        assertThat(res).isFalse();
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    void validateBook_emptyOrNullTitle_ShouldReturnFalse(String title) {
        Author author = mock(Author.class);

        var res = bookValidator.validateBook(new Book(title,
                "publisher",
                "other",
                100,
                LocalDate.now(),
                author)
        );

        assertThat(res).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"title", "valid title", "My valid title!!!", "Is this title valid?! Probably - yes"})
    void validateBook_validTitle_ShouldReturnTrue(String title) {
        Author author = mock(Author.class);
        when(author.getFirstName()).thenReturn("ValidName");
        when(author.getSecondName()).thenReturn("ValidName");


        var res = bookValidator.validateBook(new Book(title,
                "publisher",
                "other",
                100,
                LocalDate.now(),
                author)
        );

        assertThat(res).isTrue();

        verify(author, times(1)).getFirstName();
        verify(author, times(1)).getSecondName();
    }


    @ParameterizedTest
    @EmptySource
    @NullSource
    void validateBook_emptyOrNullPublisher_ShouldReturnFalse(String publisher) {
        Author author = mock(Author.class);

        var res = bookValidator.validateBook(new Book("Valid Title",
                publisher,
                "other",
                100,
                LocalDate.now(),
                author)
        );
        assertThat(res).isFalse();
    }

    @Test
    void validateBook_validPublisher_ShouldReturnTrue() {
        Author author = mock(Author.class);
        when(author.getFirstName()).thenReturn("ValidName");
        when(author.getSecondName()).thenReturn("ValidName");
        var res = bookValidator.validateBook(new Book("Valid Title",
                "publisher",
                "other",
                100,
                LocalDate.now(),
                author)
        );

        assertThat(res).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 2000, 3000, 7777, 9000, 10000})
    void validateBook_positivePageNumber_ShouldReturnTrue(int pages) {
        Author author = mock(Author.class);
        when(author.getFirstName()).thenReturn("ValidName");
        when(author.getSecondName()).thenReturn("ValidName");

        var res = bookValidator.validateBook(new Book("Valid Title",
                "publisher",
                "other",
                pages,
                LocalDate.now(),
                author)
        );

        assertThat(res).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -50, -25, -10, -5, 0})
    void validateBook_negativePageNumber_ShouldReturnFalse(int pages) {
        Author author = mock(Author.class);

        var res = bookValidator.validateBook(new Book("Valid Title",
                "publisher",
                "other",
                pages,
                LocalDate.now(),
                author)
        );

        assertThat(res).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void validateBook_nullPublicationDate_ShouldReturnFalse(LocalDate localDate) {
        Author author = mock(Author.class);

        var res = bookValidator.validateBook(new Book("Valid Title",
                "publisher",
                "other",
                100,
                localDate,
                author)
        );

        assertThat(res).isFalse();
    }

    @ParameterizedTest
    @MethodSource("invalidPublicationDateProvider")
    void validateBook_publicationDateMoreThenCurrDate_ShouldReturnFalse(LocalDate localDate) {
        Author author = mock(Author.class);
        when(author.getFirstName()).thenReturn("ValidName");
        when(author.getSecondName()).thenReturn("ValidName");

        var res = bookValidator.validateBook(new Book("Valid Title",
                "publisher",
                "other",
                100,
                localDate,
                author)
        );

        assertThat(res).isFalse();
    }

    private static Stream<LocalDate> invalidPublicationDateProvider() {
        return Stream.of(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4),
                LocalDate.now().plusDays(1000)
        );
    }

    @ParameterizedTest
    @MethodSource("validPublicationDateProvider")
    void validateBook_validPublicationDate_ShouldReturnTrue(LocalDate localDate) {
        Author author = mock(Author.class);
        when(author.getFirstName()).thenReturn("ValidName");
        when(author.getSecondName()).thenReturn("ValidName");

        var res = bookValidator.validateBook(new Book("Valid Title",
                "publisher",
                "other",
                100,
                localDate,
                author)
        );

        assertThat(res).isTrue();
    }


    private static Stream<LocalDate> validPublicationDateProvider() {
        return Stream.of(
                LocalDate.now().minusDays(1),
                LocalDate.now().minusDays(2),
                LocalDate.now().minusDays(3),
                LocalDate.now().minusDays(4),
                LocalDate.now().minusDays(1000)
        );
    }


    @ParameterizedTest
    @MethodSource("validAuthorsNames")
    void validateBook_validAuthorsFirstName_ShouldReturnTrue(String name) {
        Author author = mock(Author.class);
        when(author.getFirstName()).thenReturn(name);
        when(author.getSecondName()).thenReturn(name);

        var res = bookValidator.validateBook(new Book("Valid Title",
                "publisher",
                "other",
                100,
                LocalDate.now().minusDays(1),
                author)
        );

        assertThat(res).isTrue();
    }

    public static Stream<Arguments> validAuthorsNames() {
        return Stream.of(
                Arguments.of("ValidName"),
                Arguments.of("SecondValidName"),
                Arguments.of("UltraValidName")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"looksLikeThisNameIsTooLongToBeValidAuthorsNameAdditionalText"})
    void validateBook_nameLongerThanFiftySymbols_ShouldReturnFalse(String name) {
        Author author = mock(Author.class);
        when(author.getFirstName()).thenReturn(name);

        var res = bookValidator.validateBook(new Book("Valid Title",
                "publisher",
                "other",
                100,
                LocalDate.now().minusDays(1),
                author)
        );

        assertThat(res).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Invalid Name", "123", "|||", "\\qwe", ""})
    void validateBook_invalidSymbols_ShouldReturnFalse(String name) {
        Author author = mock(Author.class);
        when(author.getFirstName()).thenReturn(name);

        var res = bookValidator.validateBook(new Book("Valid Title",
                "publisher",
                "other",
                100,
                LocalDate.now().minusDays(1),
                author)
        );

        assertThat(res).isFalse();
    }


}