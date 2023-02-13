package com.my.library.services.impl;

import com.my.library.controller.command.constant.OrderDir;
import com.my.library.dao.BookDAO;
import com.my.library.dao.TransactionManager;
import com.my.library.dao.constants.BooksOrderTypes;
import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.AuthorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
        void find_ExistingBook_ShouldReturnOptionalOfBook() throws ServiceException, DaoException {
            long id = 1L;
            Book expectedBook = mock(Book.class);

            doReturn(Optional.of(expectedBook)).when(bookDAO).find(id);

            Optional<Book> actualBook = bookServiceImpl.find(id);

            assertThat(actualBook).isPresent().contains(expectedBook);

            verify(bookDAO, times(1)).find(id);
        }

        @Test
        void find_NonExisting_ShouldReturnEmptyOptional() throws ServiceException, DaoException {
            long id = 1L;

            doReturn(Optional.empty()).when(bookDAO).find(id);

            Optional<Book> actualBook = bookServiceImpl.find(id);

            assertThat(actualBook).isEmpty();

            verify(bookDAO, times(1)).find(id);
        }

        @Test
        void find_BookDaoThrowsException_ShouldThrowServiceException() throws DaoException {
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

            verify(bookDAO, times(1)).countBooks(false);
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

    @Nested
    @DisplayName("isRemoved")
    class IsRemoved {
        @Test
        void isRemoved_WhenBookIsRemoved_ShouldReturnTrue() throws ServiceException, DaoException {
            long id = 1L;
            when(bookDAO.isRemoved(id)).thenReturn(true);

            boolean result = bookServiceImpl.isRemoved(id);

            assertThat(result).isTrue();

            verify(bookDAO, times(1)).isRemoved(id);
        }

        @Test
        void isRemoved_WhenBookIsNotRemoved_ShouldReturnFalse() throws ServiceException, DaoException {
            long id = 1L;
            when(bookDAO.isRemoved(id)).thenReturn(false);

            boolean result = bookServiceImpl.isRemoved(id);

            assertThat(result).isFalse();

            verify(bookDAO, times(1)).isRemoved(id);
        }

        @Test
        void isRemoved_WhenBookDaoExceptionThrown_ShouldThrowServiceException() throws DaoException {
            long id = 1L;

            doThrow(DaoException.class).when(bookDAO).isRemoved(id);

            assertThatThrownBy(() -> bookServiceImpl.isRemoved(id))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).isRemoved(id);
        }
    }

    @Nested
    @DisplayName("getQuantity")
    class GetQuantity {
        @Test
        void getQuantity_ValidId_ShouldReturnBookQuantity() throws ServiceException, DaoException {
            long id = 1L;
            int expectedQuantity = 10;

            doReturn(expectedQuantity).when(bookDAO).getQuantity(id);

            int actualQuantity = bookServiceImpl.getQuantity(id);

            assertThat(actualQuantity).isEqualTo(expectedQuantity);

            verify(bookDAO, times(1)).getQuantity(id);
        }

        @Test
        void getQuantity_IdNotFound_ShouldReturnMinusOne() throws ServiceException, DaoException {

            doReturn(-1).when(bookDAO).getQuantity((anyLong()));

            int result = bookServiceImpl.getQuantity(123);

            assertThat(result).isEqualTo(-1);

            verify(bookDAO, times(1)).getQuantity(anyLong());

        }

        @Test
        void getQuantity_DaoException_ShouldThrowServiceException() throws DaoException {
            doThrow(DaoException.class).when(bookDAO).getQuantity(anyLong());

            assertThatThrownBy(() -> bookServiceImpl.getQuantity(123))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).getQuantity(anyLong());
        }
    }

    @Nested
    @DisplayName("restore")
    class Restore {
        @Test
        void restore_withValidId_ShouldCallRestoreMethodOfBookDao() throws ServiceException, DaoException {
            long id = 1L;
            bookServiceImpl.restore(id);

            verify(bookDAO, times(1)).restore(id);
        }

        @Test
        void restore_BookDaoThrowsException_ShouldThrowServiceException() throws DaoException {
            long id = -1L;

            doThrow(DaoException.class).when(bookDAO).restore(id);

            assertThatThrownBy(() -> bookServiceImpl.restore(id))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).restore(id);
        }
    }

    @Nested
    @DisplayName("alreadyExists")
    class AlreadyExists {
        @Test
        void alreadyExists_BookAlreadyExists_ShouldReturnFalse() throws ServiceException, DaoException {
            Book book = mock(Book.class);

            doReturn(false).when(bookDAO).alreadyExists(book);

            boolean alreadyExistsRes = bookServiceImpl.alreadyExists(book);

            assertThat(alreadyExistsRes).isFalse();

            verify(bookDAO, times(1)).alreadyExists(book);
        }

        @Test
        void alreadyExists_BookDoesntExists_ShouldReturnTrue() throws ServiceException, DaoException {
            Book book = mock(Book.class);

            doReturn(true).when(bookDAO).alreadyExists(book);

            boolean alreadyExistsRes = bookServiceImpl.alreadyExists(book);

            assertThat(alreadyExistsRes).isTrue();

            verify(bookDAO, times(1)).alreadyExists(book);
        }

        @Test
        void alreadyExists_BookDaoThrowsException_ShouldThrowServiceException() throws DaoException {
            Book book = mock(Book.class);

            doThrow(DaoException.class).when(bookDAO).alreadyExists(book);

            assertThatThrownBy(() -> bookServiceImpl.alreadyExists(book))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO).alreadyExists(book);
        }
    }

    @Nested
    @DisplayName("decrementBookQuantity")
    class DecrementBookQuantity {
        @Test
        void decrementBookQuantity_BookExists_ShouldDecrementQuantity() throws ServiceException, DaoException {
            long bookId = 1L;

            bookServiceImpl.decrementBookQuantity(bookId);

            verify(bookDAO, times(1)).decrementBookQuantity(bookId);
        }

        @Test
        void decrementBookQuantity_BookDAoException_ShouldThrowServiceException() throws DaoException {
            long bookId = 1L;

            doThrow(DaoException.class).when(bookDAO).decrementBookQuantity(anyLong());

            assertThatThrownBy(() -> bookServiceImpl.decrementBookQuantity(bookId))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).decrementBookQuantity(bookId);
        }
    }

    @Nested
    @DisplayName("incrementBookQuantity")
    class IncrementBookQuantity {
        @Test
        void incrementBookQuantity_BookExists_ShouldincrementQuantity() throws ServiceException, DaoException {
            long bookId = 1L;

            bookServiceImpl.incrementBookQuantity(bookId);

            verify(bookDAO, times(1)).incrementBookQuantity(bookId);
        }

        @Test
        void incrementBookQuantity_BookDAoException_ShouldThrowServiceException() throws DaoException {
            long bookId = 1L;

            doThrow(DaoException.class).when(bookDAO).incrementBookQuantity(anyLong());

            assertThatThrownBy(() -> bookServiceImpl.incrementBookQuantity(bookId))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).incrementBookQuantity(bookId);
        }
    }

    @Nested
    @DisplayName("findByTitle")
    class FindByTitle {
        @Test
        void findByTitle_ValidData_ShouldReturnListOfBooksWithMatchingTitle() throws ServiceException, DaoException {
            String title = "My title";
            int start = 0;
            int offset = 5;
            OrderDir dir = OrderDir.ASC;
            BooksOrderTypes orderBy = BooksOrderTypes.BY_TITLE;

            Book book = mock(Book.class);

            var expectedBooksList = List.of(book, book, book);

            doReturn(expectedBooksList).when(bookDAO).findByTitle(title, start, offset, orderBy, dir, false);

            List<Book> actualBooksList = bookServiceImpl.findByTitle(title, start, offset, orderBy, dir, false);

            assertThat(actualBooksList).isEqualTo(expectedBooksList);

            verify(bookDAO, times(1)).findByTitle(title, start, offset, orderBy, dir, false);
        }

        @Test
        void findByTitle_StartIsNegative_ShouldThrowServiceException() {
            String title = "My title";
            int start = -5;
            int offset = 5;
            OrderDir dir = OrderDir.ASC;
            BooksOrderTypes orderBy = BooksOrderTypes.BY_TITLE;


            assertThatThrownBy(() -> bookServiceImpl.findByTitle(title, start, offset, orderBy, dir, false))
                    .isExactlyInstanceOf(ServiceException.class);

            verifyNoInteractions(bookDAO);
        }

        @Test
        void findByTitle_OffsetIsNegative_ShouldThrowServiceException() {
            String title = "My title";
            int start = 5;
            int offset = -5;
            OrderDir dir = OrderDir.ASC;
            BooksOrderTypes orderBy = BooksOrderTypes.BY_TITLE;

            assertThatThrownBy(() -> bookServiceImpl.findByTitle(title, start, offset, orderBy, dir, false))
                    .isExactlyInstanceOf(ServiceException.class);

            verifyNoInteractions(bookDAO);
        }


        @Test
        void findByTitle_BookDaoException_ShouldThrowServiceException() throws DaoException {
            String title = "My title";
            int start = 0;
            int offset = 5;
            OrderDir dir = OrderDir.ASC;
            BooksOrderTypes orderBy = BooksOrderTypes.BY_TITLE;

            doThrow(DaoException.class).when(bookDAO).findByTitle(title, start, offset, orderBy, dir, false);

            assertThatThrownBy(() -> bookServiceImpl.findByTitle(title, start, offset, orderBy, dir, false))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).findByTitle(title, start, offset, orderBy, dir, false);
        }

        @ParameterizedTest
        @NullSource
        void findByTitle_WhenTitleIsNull_ShouldThrowServiceException(String title) {
            int start = 0;
            int offset = 5;
            OrderDir dir = OrderDir.ASC;
            BooksOrderTypes orderBy = BooksOrderTypes.BY_TITLE;

            assertThatThrownBy(() -> bookServiceImpl.findByTitle(title, start, offset, orderBy, dir, false))
                    .isExactlyInstanceOf(ServiceException.class);

            verifyNoInteractions(bookDAO);
        }
    }

    @Nested
    @DisplayName("countFoundByTitle")
    class CountFoundByTitle {

        @Test
        void countFoundByTitle_ShouldReturnCorrectCount() throws ServiceException, DaoException {
            String title = "My title";

            when(bookDAO.countFoundByTitle(title, false)).thenReturn(5);

            int count = bookServiceImpl.countFoundByTitle(title, false);

            assertThat(count).isEqualTo(5);

            verify(bookDAO, times(1)).countFoundByTitle(title, false);
        }

        @Test
        void countFoundByTitle_IncludedRemovedBooks_ShouldReturnCorrectCount() throws ServiceException, DaoException {
            String title = "My title";
            when(bookDAO.countFoundByTitle(title, true)).thenReturn(10);

            int count = bookServiceImpl.countFoundByTitle(title, true);

            assertThat(count).isEqualTo(10);
            verify(bookDAO, times(1)).countFoundByTitle(title, true);
            verifyNoMoreInteractions(bookDAO);
        }

        @ParameterizedTest
        @NullSource
        void countFoundByTitle_WhenTitleIsNull_ShouldThrowServiceException(String title) {
            assertThatThrownBy(() -> bookServiceImpl.countFoundByTitle(title, false))
                    .isInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verifyNoInteractions(bookDAO);
        }

        @Test
        void countFoundByTitle_WhenBookDaoThrowsDaoException_ShouldThrowServiceException() throws DaoException {
            String title = "My title";

            doThrow(DaoException.class).when(bookDAO).countFoundByTitle(title, false);

            assertThatThrownBy(() -> bookServiceImpl.countFoundByTitle(title, false))
                    .isInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).countFoundByTitle(title, false);
        }
    }

    @Nested
    @DisplayName("findByAuthor")
    class FindByAuthor {
        @Test
        void findByAuthor_ValidData_ShouldReturnListOfBooksWithMatchingAuthor() throws ServiceException, DaoException {
            String author = "My author";
            int start = 0;
            int offset = 5;
            OrderDir dir = OrderDir.ASC;
            BooksOrderTypes orderBy = BooksOrderTypes.BY_AUTHOR;

            Book book = mock(Book.class);

            var expectedBooksList = List.of(book, book, book);

            doReturn(expectedBooksList).when(bookDAO).findByAuthor(author, start, offset, orderBy, dir, false);

            List<Book> actualBooksList = bookServiceImpl.findByAuthor(author, start, offset, orderBy, dir, false);

            assertThat(actualBooksList).isEqualTo(expectedBooksList);

            verify(bookDAO, times(1)).findByAuthor(author, start, offset, orderBy, dir, false);
        }

        @Test
        void findByAuthor_StartIsNegative_ShouldThrowServiceException() {
            String author = "My author";
            int start = -5;
            int offset = 5;
            OrderDir dir = OrderDir.ASC;
            BooksOrderTypes orderBy = BooksOrderTypes.BY_AUTHOR;


            assertThatThrownBy(() -> bookServiceImpl.findByAuthor(author, start, offset, orderBy, dir, false))
                    .isExactlyInstanceOf(ServiceException.class);

            verifyNoInteractions(bookDAO);
        }

        @Test
        void findByAuthor_OffsetIsNegative_ShouldThrowServiceException() {
            String author = "My author";
            int start = 5;
            int offset = -5;
            OrderDir dir = OrderDir.ASC;
            BooksOrderTypes orderBy = BooksOrderTypes.BY_AUTHOR;

            assertThatThrownBy(() -> bookServiceImpl.findByAuthor(author, start, offset, orderBy, dir, false))
                    .isExactlyInstanceOf(ServiceException.class);

            verifyNoInteractions(bookDAO);
        }


        @Test
        void findByAuthor_BookDaoException_ShouldThrowServiceException() throws DaoException {
            String author = "My author";
            int start = 0;
            int offset = 5;
            OrderDir dir = OrderDir.ASC;
            BooksOrderTypes orderBy = BooksOrderTypes.BY_AUTHOR;

            doThrow(DaoException.class).when(bookDAO).findByAuthor(author, start, offset, orderBy, dir, false);

            assertThatThrownBy(() -> bookServiceImpl.findByAuthor(author, start, offset, orderBy, dir, false))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).findByAuthor(author, start, offset, orderBy, dir, false);
        }

        @ParameterizedTest
        @NullSource
        void findByAuthor_WhenAuthorIsNull_ShouldThrowServiceException(String author) {
            int start = 0;
            int offset = 5;
            OrderDir dir = OrderDir.ASC;
            BooksOrderTypes orderBy = BooksOrderTypes.BY_AUTHOR;

            assertThatThrownBy(() -> bookServiceImpl.findByAuthor(author, start, offset, orderBy, dir, false))
                    .isExactlyInstanceOf(ServiceException.class);

            verifyNoInteractions(bookDAO);
        }
    }


    @Nested
    @DisplayName("countFoundByAuthor")
    class CountFoundByAuthor {
        @Test
        void countFoundByAuthor_ShouldReturnCorrectCount() throws ServiceException, DaoException {
            String author = "My author";

            when(bookDAO.countFoundByAuthor(author, false)).thenReturn(5);

            int count = bookServiceImpl.countFoundByAuthor(author, false);

            assertThat(count).isEqualTo(5);

            verify(bookDAO, times(1)).countFoundByAuthor(author, false);
        }

        @Test
        void countFoundByAuthor_IncludedRemovedBooks_ShouldReturnCorrectCount() throws ServiceException, DaoException {
            String author = "My author";
            when(bookDAO.countFoundByAuthor(author, true)).thenReturn(10);

            int count = bookServiceImpl.countFoundByAuthor(author, true);

            assertThat(count).isEqualTo(10);
            verify(bookDAO, times(1)).countFoundByAuthor(author, true);
            verifyNoMoreInteractions(bookDAO);
        }

        @ParameterizedTest
        @NullSource
        void countFoundByAuthor_WhenAuthorIsNull_ShouldThrowServiceException(String author) {
            assertThatThrownBy(() -> bookServiceImpl.countFoundByAuthor(author, false))
                    .isInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verifyNoInteractions(bookDAO);
        }

        @Test
        void countFoundByAuthor_WhenBookDaoThrowsDaoException_ShouldThrowServiceException() throws DaoException {
            String author = "My author";

            doThrow(DaoException.class).when(bookDAO).countFoundByAuthor(author, false);

            assertThatThrownBy(() -> bookServiceImpl.countFoundByAuthor(author, false))
                    .isInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).countFoundByAuthor(author, false);
        }
    }

    @Nested
    @DisplayName("save")
    class Save {

        Book validBook = new Book("Valid title",
                "Hachette",
                "Drama",
                100,
                LocalDate.parse("2023-02-06"),
                new Author("John", "Smith"));

        Book validBookAfterSave = new Book(1L, "Valid title",
                "Hachette",
                "Drama",
                100,
                LocalDate.parse("2023-02-06"),
                new Author("John", "Smith"));


        @Test
        void save_beginTransactionThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException {
            TransactionManager transactionManager = mock(TransactionManager.class);
            AuthorService authorService = mock(AuthorServiceImpl.class);
            Book book = mock(Book.class);

            doThrow(DaoException.class).when(transactionManager).beginTransaction();

            assertThatThrownBy(() -> bookServiceImpl.save(book, 1, authorService, transactionManager))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(transactionManager, times(1)).beginTransaction();
            verify(transactionManager, times(1)).rollback();
            verify(transactionManager, times(1)).endTransaction();
        }

        @Test
        void save_commitThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException {
            TransactionManager transactionManager = mock(TransactionManager.class);
            AuthorService authorService = mock(AuthorServiceImpl.class);

            doReturn(validBookAfterSave).when(bookDAO).save(validBook);

            doThrow(DaoException.class).when(transactionManager).commit();

            assertThatThrownBy(() -> bookServiceImpl.save(validBook, 1, authorService, transactionManager))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).save(validBook);

            verify(transactionManager, times(1)).beginTransaction();
            verify(transactionManager, times(1)).rollback();
            verify(transactionManager, times(1)).endTransaction();
        }

        @Test
        void save_rollbackThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException {
            TransactionManager transactionManager = mock(TransactionManager.class);
            AuthorService authorService = mock(AuthorServiceImpl.class);

            doReturn(validBookAfterSave).when(bookDAO).save(validBook);

            doThrow(DaoException.class).when(transactionManager).commit();
            doThrow(DaoException.class).when(transactionManager).rollback();

            assertThatThrownBy(() -> bookServiceImpl.save(validBook, 1, authorService, transactionManager))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);


            verify(bookDAO, times(1)).save(validBook);

            verify(transactionManager, times(1)).beginTransaction();
            verify(transactionManager, times(1)).rollback();
            verify(transactionManager, times(1)).endTransaction();
        }

        @Test
        void save_AuthorServiceThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException, ServiceException {
            TransactionManager transactionManager = mock(TransactionManager.class);
            AuthorService authorService = mock(AuthorServiceImpl.class);

            doReturn(validBookAfterSave).when(bookDAO).save(validBook);

            doThrow(ServiceException.class).when(authorService).findByNames(anyString(), anyString());


            assertThatThrownBy(() -> bookServiceImpl.save(validBook, 1, authorService, transactionManager))
                    .isExactlyInstanceOf(ServiceException.class);

            verify(transactionManager, times(1)).beginTransaction();
            verify(transactionManager, times(1)).rollback();
            verify(transactionManager, times(1)).endTransaction();
        }


        @Test
        void save_NoSuchAuthor_ShouldCallSaveMethodOnAuthorService() throws DaoException, ServiceException {
            TransactionManager transactionManager = mock(TransactionManager.class);
            AuthorService authorService = mock(AuthorServiceImpl.class);

            doReturn(validBookAfterSave).when(bookDAO).save(validBook);
            int copies = 1;


            bookServiceImpl.save(validBook, copies, authorService, transactionManager);

            verify(authorService, times(1)).save(validBookAfterSave.getAuthor());

            verify(bookDAO, times(1)).save(validBook);
            verify(bookDAO, times(1)).addToStorage(validBookAfterSave.getBookId(), copies);

            verify(transactionManager, times(1)).beginTransaction();
            verify(transactionManager, times(1)).endTransaction();
        }

        @Test
        void save_AuthorAlreadyExists_ShouldFindAuthorAndSetToBook() throws DaoException, ServiceException {
            TransactionManager transactionManager = mock(TransactionManager.class);
            AuthorService authorService = mock(AuthorServiceImpl.class);

            doReturn(Optional.of(validBookAfterSave.getAuthor())).when(authorService).findByNames(anyString(), anyString());

            doReturn(validBookAfterSave).when(bookDAO).save(validBook);
            int copies = 1;

            bookServiceImpl.save(validBook, copies, authorService, transactionManager);

            verify(authorService, times(0)).save(validBookAfterSave.getAuthor());

            verify(bookDAO, times(1)).save(validBook);
            verify(bookDAO, times(1)).addToStorage(validBookAfterSave.getBookId(), copies);

            verify(transactionManager, times(1)).beginTransaction();
            verify(transactionManager, times(1)).endTransaction();
        }
    }


    @Nested
    @DisplayName("update")
    class Update {

        Book validBook = new Book(1L,"Valid title",
                "Hachette",
                "Drama",
                100,
                LocalDate.parse("2023-02-06"),
                new Author("John", "Smith"));

        @Test
        void update_beginTransactionThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException {
            TransactionManager transactionManager = mock(TransactionManager.class);
            AuthorService authorService = mock(AuthorServiceImpl.class);
            Book book = mock(Book.class);

            doThrow(DaoException.class).when(transactionManager).beginTransaction();

            assertThatThrownBy(() -> bookServiceImpl.update(book, 1, authorService, transactionManager))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(transactionManager, times(1)).beginTransaction();
            verify(transactionManager, times(1)).rollback();
            verify(transactionManager, times(1)).endTransaction();
        }

        @Test
        void update_commitThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException {
            TransactionManager transactionManager = mock(TransactionManager.class);
            AuthorService authorService = mock(AuthorServiceImpl.class);

            doReturn(true).when(bookDAO).update(validBook);

            doThrow(DaoException.class).when(transactionManager).commit();

            assertThatThrownBy(() -> bookServiceImpl.update(validBook, 1, authorService, transactionManager))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO, times(1)).update(validBook);

            verify(transactionManager, times(1)).beginTransaction();
            verify(transactionManager, times(1)).rollback();
            verify(transactionManager, times(1)).endTransaction();
        }

        @Test
        void update_rollbackThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException {
            TransactionManager transactionManager = mock(TransactionManager.class);
            AuthorService authorService = mock(AuthorServiceImpl.class);

            doThrow(DaoException.class).when(transactionManager).beginTransaction();
            doThrow(DaoException.class).when(transactionManager).rollback();

            assertThatThrownBy(() -> bookServiceImpl.update(validBook, 1, authorService, transactionManager))
                    .isExactlyInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(transactionManager, times(1)).beginTransaction();
            verify(transactionManager, times(1)).rollback();
            verify(transactionManager, times(1)).endTransaction();
        }

        @Test
        void update_AuthorServiceThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException, ServiceException {
            TransactionManager transactionManager = mock(TransactionManager.class);
            AuthorService authorService = mock(AuthorServiceImpl.class);

            doReturn(true).when(bookDAO).update(validBook);

            doThrow(ServiceException.class).when(authorService).findByNames(anyString(), anyString());


            assertThatThrownBy(() -> bookServiceImpl.update(validBook, 1, authorService, transactionManager))
                    .isExactlyInstanceOf(ServiceException.class);

            verify(transactionManager, times(1)).beginTransaction();
            verify(transactionManager, times(1)).rollback();
            verify(transactionManager, times(1)).endTransaction();
        }


        @Test
        void update_NoSuchAuthor_ShouldCallUpdateMethodOnAuthorService() throws DaoException, ServiceException {
            TransactionManager transactionManager = mock(TransactionManager.class);
            AuthorService authorService = mock(AuthorServiceImpl.class);

            doReturn(true).when(bookDAO).update(validBook);
            int copies = 1;

            boolean updateRes = bookServiceImpl.update(validBook, copies, authorService, transactionManager);

            assertThat(updateRes).isTrue();

            verify(authorService, times(1)).findByNames(validBook.getAuthor().getFirstName(), validBook.getAuthor().getSecondName());
            verify(authorService, times(1)).save(validBook.getAuthor());

            verify(bookDAO, times(1)).update(validBook);

            verify(transactionManager, times(1)).beginTransaction();
            verify(transactionManager, times(1)).endTransaction();
        }

//        @Test
//        void update_AuthorAlreadyExists_ShouldFindAuthorAndSetToBook() throws DaoException, ServiceException {
//            TransactionManager transactionManager = mock(TransactionManager.class);
//            AuthorService authorService = mock(AuthorServiceImpl.class);
//
//            doReturn(Optional.of(true.getAuthor())).when(authorService).findByNames(anyString(), anyString());
//
//            doReturn(true).when(bookDAO).update(validBook);
//            int copies = 1;
//
//            bookServiceImpl.update(validBook, copies, authorService, transactionManager);
//
//            verify(authorService, times(0)).update(true.getAuthor());
//
//            verify(bookDAO, times(1)).update(validBook);
//            verify(bookDAO, times(1)).addToStorage(true.getBookId(), copies);
//
//            verify(transactionManager, times(1)).beginTransaction();
//            verify(transactionManager, times(1)).endTransaction();
//        }
    }
}
