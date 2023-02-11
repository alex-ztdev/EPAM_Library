package com.my.library.services.impl;

import com.my.library.dao.AuthorDAO;
import com.my.library.entities.Author;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {
    @Mock
    private AuthorDAO authorDAO;
    @InjectMocks
    private AuthorServiceImpl authorServiceImpl;

    @Test
    public void findByNames_Success() throws ServiceException, DaoException {
        Author author = new Author("John", "Doe");

        doReturn(Optional.of(author)).when(authorDAO).findByNames("John", "Doe");

        Optional<Author> result = authorServiceImpl.findByNames("John", "Doe");

        assertThat(result).isPresent().get().isEqualTo(author);
        verify(authorDAO).findByNames("John", "Doe");
    }

    @Test
    public void findByNames_Exception() throws DaoException {
        when(authorDAO.findByNames("John", "Doe")).thenThrow(new DaoException("Error while executing findByNames method in AuthorDAO"));

        assertThatThrownBy(() -> authorServiceImpl.findByNames("John", "Doe"))
                .isInstanceOf(ServiceException.class)
                .hasMessage("Error while executing findByNames method in AuthorServiceImpl")
                .hasCauseInstanceOf(DaoException.class);
        verify(authorDAO).findByNames("John", "Doe");
    }
}