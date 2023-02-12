package com.my.library.services.impl;

import com.my.library.dao.BookDAO;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookDAO bookDAO;

    @InjectMocks
    private BookServiceImpl bookService;

    @Nested
    @DisplayName("deleteById")
    class DeleteById {
        @Test
        void deleteById_SuccessfulDeletion_ShouldCallDeleteByIdOnDAOAndReturnTrue() throws ServiceException, DaoException, ServiceException, DaoException {
            long id = 1L;
            doReturn(true).when(bookDAO).deleteById(id);

            assertThat(bookService.deleteById(id)).isTrue();

            verify(bookDAO, times(1)).deleteById(id);
        }

        @Test
        void deleteById_BookDaoThrowsException_ShouldThrowServiceException() throws DaoException {
            long id = 1L;
            doThrow(DaoException.class).when(bookDAO).deleteById(id);

            assertThatThrownBy(() -> bookService.deleteById(id))
                    .isInstanceOf(ServiceException.class)
                    .hasCauseExactlyInstanceOf(DaoException.class);

            verify(bookDAO).deleteById(eq(id));
        }
        @Test
        void deleteById_UnsuccessfulDeletion_ShouldReturnFalse() throws DaoException, ServiceException {
            long id = 1L;
            doReturn(false).when(bookDAO).deleteById(id);

            assertThat(bookService.deleteById(id)).isFalse();

            verify(bookDAO).deleteById(eq(id));
        }
    }


}