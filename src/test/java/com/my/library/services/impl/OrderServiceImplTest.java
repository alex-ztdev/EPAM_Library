package com.my.library.services.impl;

import com.my.library.dao.OrderDAO;
import com.my.library.dao.constants.OrderStatus;
import com.my.library.dao.impl.OrderDaoImpl;
import com.my.library.entities.Order;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderDAO orderDAO;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void find_existingOrder_ShouldReturnOrder() throws ServiceException, DaoException {
        Order order = new Order(1L, 1L, 2L, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), OrderStatus.ACCEPTED, false);
        when(orderDAO.find(1L)).thenReturn(Optional.of(order));
        Optional<Order> returnedOrder = orderService.find(1L);

        assertThat(returnedOrder)
                .isNotEmpty()
                .hasValue(order);
        verify(orderDAO).find(1L);
    }

    @Test
    void find_nonExistingOrder_ShouldReturnEmpty() throws ServiceException, DaoException {
        when(orderDAO.find(1L)).thenReturn(Optional.empty());
        Optional<Order> returnedOrder = orderService.find(1L);

        assertThat(returnedOrder)
                .isEmpty();

        verify(orderDAO).find(1L);
    }
    @Test
    void find_daoExceptionThrown_ShouldThrowServiceException() throws DaoException {
        doThrow(DaoException.class).when(orderDAO).find(1L);

        assertThatThrownBy(() -> orderService.find(1L))
                .isExactlyInstanceOf(ServiceException.class)
                .hasCauseExactlyInstanceOf(DaoException.class);

        verify(orderDAO).find(1L);
    }
}