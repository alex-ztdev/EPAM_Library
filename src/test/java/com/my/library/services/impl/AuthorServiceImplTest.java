package com.my.library.services.impl;

import com.my.library.dao.AuthorDAO;
import com.my.library.entities.Author;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {
    @Mock
    private AuthorDAO authorDAO;
    @InjectMocks
    private AuthorServiceImpl authorServiceImpl;

    @Nested
    @DisplayName("findByNames")
    class FindByNames {
        @Test
        public void findByNames_Success() throws ServiceException, DaoException {
            Author author = new Author("John", "Doe");

            doReturn(Optional.of(author)).when(authorDAO).findByNames("John", "Doe");

            Optional<Author> result = authorServiceImpl.findByNames("John", "Doe");

            assertThat(result).isPresent().get().isEqualTo(author);
            verify(authorDAO).findByNames("John", "Doe");
        }

        @Test
        public void findByNames_WhenDaoExceptionIsThrown_ShouldThrowServiceException() throws DaoException {
            when(authorDAO.findByNames("John", "Doe")).thenThrow(new DaoException("Error while executing findByNames method in AuthorDAO"));

            assertThatThrownBy(() -> authorServiceImpl.findByNames("John", "Doe"))
                    .isInstanceOf(ServiceException.class)
                    .hasMessage("Error while executing findByNames method in AuthorServiceImpl")
                    .hasCauseInstanceOf(DaoException.class);

            verify(authorDAO).findByNames("John", "Doe");
        }
    }


    @Nested
    @DisplayName("find")
    class Find {
        @Test
        public void find_validAuthorId_ReturnsAuthor() throws ServiceException, DaoException {
            Author author = new Author("John", "Doe");
            when(authorDAO.find(1L)).thenReturn(Optional.of(author));

            Optional<Author> result = authorServiceImpl.find(1L);

            assertThat(result).isPresent();
            assertThat(result).get().isEqualTo(author);
            verify(authorDAO).find(1L);
        }

        @Test
        public void find_WhenDaoExceptionIsThrown_ShouldThrowServiceException() throws Exception {
            long id = 1L;

            doThrow(DaoException.class).when(authorDAO).find(id);

            assertThatThrownBy(() -> authorServiceImpl.find(id)).isExactlyInstanceOf(ServiceException.class);

            verify(authorDAO, times(1)).find(id);
        }

        @Test
        public void find_AuthorIsNotPresent_ShouldReturnEmptyOptional() throws DaoException, ServiceException {
            long authorId = 1L;
            Optional<Author> expectedResult = Optional.empty();
            when(authorDAO.find(authorId)).thenReturn(expectedResult);

            Optional<Author> result = authorServiceImpl.find(authorId);

            assertThat(result).isEqualTo(expectedResult);
            verify(authorDAO).find(authorId);
            verifyNoMoreInteractions(authorDAO);
        }

    }

    @Nested
    @DisplayName("findAll")
    class FindAll {
        @Test
        public void findAll_ShouldReturnAllAuthors() throws ServiceException, DaoException {
            List<Author> expectedAuthors = List.of(
                    new Author(1L, "John", "Doe"),
                    new Author(2L, "Max", "Smith")
            );

            when(authorDAO.findAll()).thenReturn(expectedAuthors);

            List<Author> actualAuthors = authorServiceImpl.findAll();

            verify(authorDAO, times(1)).findAll();
            assertThat(actualAuthors).isEqualTo(expectedAuthors);
        }

        @Test
        public void findAll_ShouldThrowServiceException() throws DaoException {
            when(authorDAO.findAll()).thenThrow(DaoException.class);

            assertThatExceptionOfType(ServiceException.class)
                    .isThrownBy(() -> authorServiceImpl.findAll())
                    .withMessageContaining("Error while executing findAll method in AuthorServiceImpl");

            verify(authorDAO, times(1)).findAll();
        }

        @Test
        public void findAll_whenThrowsDaoException_shouldThrowServiceException() throws DaoException {
            doThrow(DaoException.class).when(authorDAO).findAll();

            assertThatExceptionOfType(ServiceException.class).isThrownBy(() -> authorServiceImpl.findAll());
        }
    }


    @Nested
    @DisplayName("save")
    class Save {
        @Test
        void save_ValidAuthor_ShouldSaveAuthor() throws ServiceException, DaoException {
            Author author = new Author(1L, "John", "Doe");
            doNothing().when(authorDAO).save(author);

            authorServiceImpl.save(author);

            verify(authorDAO, times(1)).save(author);
        }

        @ParameterizedTest
        @NullSource
        void save_NullAuthor_ShouldThrowServiceException(Author author) throws DaoException {
            doThrow(DaoException.class).when(authorDAO).save(author);

            assertThatThrownBy(() -> authorServiceImpl.save(author))
                    .isInstanceOf(ServiceException.class)
                    .hasMessage("Error while executing save method in AuthorServiceImpl");

            verify(authorDAO).save(author);
        }

        @Test
        void save_DaoException_ShouldThrowServiceException() throws DaoException {
            Author author = new Author(1L, "John", "Doe");
            doThrow(DaoException.class).when(authorDAO).save(author);

            assertThatThrownBy(() -> authorServiceImpl.save(author))
                    .isInstanceOf(ServiceException.class)
                    .hasMessage("Error while executing save method in AuthorServiceImpl");
        }
    }


    @Nested
    @DisplayName("update")
    class Update {
        @Test
        void update_validAuthor_ShouldReturnTrue() throws ServiceException, DaoException {
            Author author = new Author("Firstname", "SecondName");

            when(authorDAO.update(author)).thenReturn(true);

            boolean isUpdated = authorServiceImpl.update(author);

            assertThat(isUpdated).isTrue();
            verify(authorDAO).update(author);
        }

        @Test
        void update_nonExistingAuthor_ShouldReturnFalse() throws DaoException, ServiceException {
            Author author = new Author("Firstname", "SecondName");

            when(authorDAO.update(author)).thenReturn(false);

            boolean isUpdated = authorServiceImpl.update(author);

            assertThat(isUpdated).isFalse();
            verify(authorDAO).update(author);
        }

        @Test
        void update_throwsDaoException_ShouldThrowServiceException() throws DaoException {
            Author author = new Author("Firstname", "SecondName");

            doThrow(DaoException.class).when(authorDAO).update(author);

            assertThatThrownBy(() -> authorServiceImpl.update(author))
                    .isExactlyInstanceOf(ServiceException.class);

            verify(authorDAO).update(author);
        }
    }

}