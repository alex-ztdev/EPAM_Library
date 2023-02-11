package com.my.library.services.impl;

import com.my.library.dao.PublisherDAO;
import com.my.library.entities.Publisher;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublisherServiceImplTest {

    @Mock
    private PublisherDAO publisherDAO;

    @InjectMocks
    private PublisherServiceImpl publisherServiceImpl;

    @Test
    public void findAll_ShouldReturnListOfPublishers() throws ServiceException, DaoException {
        List<Publisher> expectedPublishers = Arrays.asList(new Publisher(1L, "Publisher1"),
                new Publisher(2L, "Publisher2"));

        when(publisherDAO.findAll()).thenReturn(expectedPublishers);

        List<Publisher> actualPublishers = publisherServiceImpl.findAll();

        assertThat(actualPublishers).isEqualTo(expectedPublishers);
    }

    @Test
    public void findAll_WhenDaoExceptionIsThrown_ShouldThrowServiceException() throws DaoException {
        when(publisherDAO.findAll()).thenThrow(new DaoException("Error while finding publishers"));

        assertThatThrownBy(() -> publisherServiceImpl.findAll()).isExactlyInstanceOf(ServiceException.class);
    }
}