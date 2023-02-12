package com.my.library.services.impl;

import com.my.library.dao.OrderDAO;
import com.my.library.dao.constants.OrderStatus;
import com.my.library.entities.Order;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    void findAllUsersOrders_ValidInput_ShouldReturnOrdersList() throws ServiceException, DaoException {
        long userId = 1L;
        int start = 0;
        int offset = 10;
        OrderStatus orderStatus = OrderStatus.ACCEPTED;
        List<Order> expectedOrders = List.of(new Order(), new Order(), new Order());

        when(orderDAO.findAllUsersOrders(userId, start, offset, orderStatus)).thenReturn(expectedOrders);

        List<Order> actualOrders = orderService.findAllUsersOrders(userId, start, offset, orderStatus);

        assertThat(actualOrders).isEqualTo(expectedOrders);
        verify(orderDAO, times(1)).findAllUsersOrders(userId, start, offset, orderStatus);
    }

    @Test
    void findAllUsersOrders_ArrayIsEmpty_ShouldThrowServiceException() {
        long userId = 1L;
        int start = 0;
        int offset = 10;

        assertThatThrownBy(() -> orderService.findAllUsersOrders(userId, start, offset))
                .isExactlyInstanceOf(ServiceException.class);

        verifyNoInteractions(orderDAO);
    }

    @Test
    void findAllUsersOrders_ArrayLengthIsGreaterOrdersStatusValues_ShouldThrowServiceException() {
        long userId = 1L;
        int start = 0;
        int offset = 10;
        OrderStatus[] orderStatus = {OrderStatus.ACCEPTED, OrderStatus.REJECTED, OrderStatus.PROCESSING, OrderStatus.PROCESSING};

        assertThatThrownBy(() -> orderService.findAllUsersOrders(userId, start, offset, orderStatus))
                .isExactlyInstanceOf(ServiceException.class);

        verifyNoInteractions(orderDAO);
    }

    @Test
    void findAllUsersOrders_DaoException_ShouldThrowServiceException() throws DaoException {
        long userId = 1L;
        int start = 0;
        int offset = 5;
        OrderStatus[] orderStatus = {OrderStatus.ACCEPTED};

        doThrow(DaoException.class).when(orderDAO).findAllUsersOrders(userId, start, offset, orderStatus);

        assertThatThrownBy(() -> orderService.findAllUsersOrders(userId, start, offset, orderStatus))
                .isExactlyInstanceOf(ServiceException.class)
                .hasCauseExactlyInstanceOf(DaoException.class);

        verify(orderDAO).findAllUsersOrders(userId, start, offset, orderStatus);
    }

    @Test
    public void findAll_ValidParams_ShouldReturnOrders() throws ServiceException, DaoException {
        int start = 0;
        int offset = 10;

        List<Order> expectedOrders = List.of(new Order(),new Order(), new Order());
        doReturn(expectedOrders).when(orderDAO).findAll(start, offset);

        List<Order> actualOrders = orderService.findAll(start, offset);

        assertThat(actualOrders).isEqualTo(expectedOrders);
        verify(orderDAO, times(1)).findAll(start, offset);
    }
    @Test
    public void findAll_DaoException_ShouldThrowServiceException() throws DaoException {
        int start = 0;
        int offset = 10;

        doThrow(DaoException.class).when(orderDAO).findAll(start, offset);

        assertThatThrownBy(() -> orderService.findAll(start, offset))
                .isExactlyInstanceOf(ServiceException.class)
                .hasCauseExactlyInstanceOf(DaoException.class);

        verify(orderDAO, times(1)).findAll(start, offset);
    }


}