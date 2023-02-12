package com.my.library.services.impl;

import com.my.library.controller.command.constant.OrderDir;
import com.my.library.dao.BookDAO;
import com.my.library.dao.constants.BooksOrderTypes;
import com.my.library.entities.Book;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookDAO bookDAO;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Nested
    @DisplayName("deleteById")
    class DeleteById {
        @Test
        void deleteById_SuccessfulDeletion_ShouldCallDeleteByIdOnDAOAndReturnTrue() throws ServiceException, DaoException {
            long id = 1L;
            doReturn(true).when(bookDAO).deleteById(id);

            assertThat(bookServiceImpl.deleteById(id)).isTrue();

            verify(bookDAO, times(1)).deleteById(id);
        }

        @Test
        void deleteById_BookDaoThrowsException_ShouldThrowServiceException() throws DaoException {
            long id = 1L;
            doThrow(DaoException.class).when(bookDAO).deleteById(id);

            assertThatThrownBy(() -> bookServiceImpl.deleteById(id))
                    .isInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO).deleteById(eq(id));
        }
        @Test
        void deleteById_UnsuccessfulDeletion_ShouldReturnFalse() throws DaoException, ServiceException {
            long id = 1L;
            doReturn(false).when(bookDAO).deleteById(id);

            assertThat(bookServiceImpl.deleteById(id)).isFalse();

            verify(bookDAO).deleteById(eq(id));
        }
    }

    @Nested
    @DisplayName("find")
    class Find {
        @Test
        public void find_ExistingBook_ShouldReturnOptionalOfBook() throws ServiceException, DaoException {
            long id = 1L;
            Book expectedBook = mock(Book.class);

            doReturn(Optional.of(expectedBook)).when(bookDAO).find(id);

            Optional<Book> actualBook = bookServiceImpl.find(id);

            assertThat(actualBook).isPresent().contains(expectedBook);

            verify(bookDAO, times(1)).find(id);
        }

        @Test
        public void find_NonExisting_ShouldReturnEmptyOptional() throws ServiceException, DaoException {
            long id = 1L;

            doReturn(Optional.empty()).when(bookDAO).find(id);

            Optional<Book> actualBook = bookServiceImpl.find(id);

            assertThat(actualBook).isEmpty();

            verify(bookDAO, times(1)).find(id);
        }
        @Test
        public void find_BookDaoThrowsException_ShouldThrowServiceException() throws DaoException {
            long id = 1L;
            doThrow(DaoException.class).when(bookDAO).find(id);


            assertThatThrownBy(() -> bookServiceImpl.find(id))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).find(id);
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {
        @Test
        void findAll_ReturnsCorrectBooks() throws DaoException, ServiceException {
            Book book = mock(Book.class);
            List<Book> expectedBooks = List.of(book, book, book);

            int start = 0;
            int offset = 5;

            when(bookDAO.findAll(start, offset, BooksOrderTypes.BY_ID, OrderDir.ASC, false)).thenReturn(expectedBooks);

            List<Book> actualBooks = bookServiceImpl.findAll(start, offset, BooksOrderTypes.BY_ID, OrderDir.ASC, false);

            assertThat(actualBooks).isEqualTo(expectedBooks);
            verify(bookDAO).findAll(start, offset, BooksOrderTypes.BY_ID, OrderDir.ASC, false);
        }

        @Test
        void findAll_BookDaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
            int start = 0;
            int offset = 5;

            doThrow(DaoException.class).when(bookDAO).findAll(start, offset, BooksOrderTypes.BY_ID, OrderDir.ASC, false);

            assertThatThrownBy(() -> bookServiceImpl.findAll(start, offset, BooksOrderTypes.BY_ID, OrderDir.ASC, false))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO).findAll(start, offset, BooksOrderTypes.BY_ID, OrderDir.ASC, false);
        }
    }


    @Nested
    @DisplayName("countBooks")
    class CountBooks {

        @Test
        void countBooks_ShouldReturnCorrectCount() throws ServiceException, DaoException {

            when(bookDAO.countBooks(false)).thenReturn(5);

            int count = bookServiceImpl.countBooks(false);

            assertThat(count).isEqualTo(5);

            verify(bookServiceImpl, times(1)).countBooks(false);
        }

        @Test
        void countBooks_IncludedRemovedBooks_ShouldReturnCorrectCount() throws ServiceException, DaoException {
            when(bookDAO.countBooks(true)).thenReturn(10);

            int count = bookServiceImpl.countBooks(true);

            assertThat(count).isEqualTo(10);
            verify(bookDAO, times(1)).countBooks(true);
            verifyNoMoreInteractions(bookDAO);
        }
        @Test
        void countBooks_WhenBookDaoThrowsDaoException_ShouldThrowServiceException() throws DaoException {
            when(bookDAO.countBooks(false)).thenThrow(DaoException.class);

            assertThatThrownBy(() -> bookServiceImpl.countBooks(false))
                    .isInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).countBooks(false);
        }
    }
}