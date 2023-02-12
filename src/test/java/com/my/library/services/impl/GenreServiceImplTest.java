package com.my.library.services.impl;

import com.my.library.dao.GenreDAO;
import com.my.library.entities.Genre;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenreServiceImplTest {

    @Mock
    private GenreDAO genreDAO;

    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    public void findAll_ShouldReturnListOfGenres() throws ServiceException, DaoException {
        List<Genre> expectedGenres = Arrays.asList(
                new Genre(1L, "Action"),
                new Genre(2L, "Adventure"),
                new Genre(3L, "Comedy")
        );
        doReturn(expectedGenres).when(genreDAO).findAll();

        List<Genre> actualGenres = genreService.findAll();

        assertThat(actualGenres).isEqualTo(expectedGenres);
    }

    @Test()
    public void findAll_WhenDaoExceptionIsThrown_ShouldThrowServiceException() throws DaoException {
        doThrow(DaoException.class).when(genreDAO).findAll();

        assertThatThrownBy(() -> genreService.findAll()).isExactlyInstanceOf(ServiceException.class);
    }
}