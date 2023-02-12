package com.my.library.services.impl;

import com.my.library.dao.OrderDAO;
import com.my.library.dao.constants.OrderStatus;
import com.my.library.entities.Order;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public void countUsersOrders_Successful_ShouldReturnCorrectValue() throws ServiceException, DaoException {
        long userId = 1L;
        int expectedCount = 10;

        doReturn(expectedCount).when(orderDAO).countUserOrders(userId, OrderStatus.ACCEPTED);

        int actualCount = orderService.countUsersOrders(userId, OrderStatus.ACCEPTED);

        assertThat(actualCount).isEqualTo(expectedCount);
        verify(orderDAO, times(1)).countUserOrders(userId, OrderStatus.ACCEPTED);
    }

    @Test
    public void countUsersOrders_DaoException_ShouldThrowServiceException() throws DaoException {
        long userId = 1L;
        OrderStatus[] orderStatuses = {OrderStatus.ACCEPTED, OrderStatus.PROCESSING};

        doThrow(DaoException.class).when(orderDAO).countUserOrders(userId, orderStatuses);

        assertThatThrownBy(() -> orderService.countUsersOrders(userId, orderStatuses))
                .isExactlyInstanceOf(ServiceException.class)
                .hasCauseExactlyInstanceOf(DaoException.class);
    }

    @ParameterizedTest
    @CsvSource({"2020-02-03T10:20, 2020-02-04T10:20, 10", "2020-03-03T10:20, 2020-04-04T10:20, 320","2020-02-12T10:30, 2021-02-12T10:31, 3660",  "2020-02-20T10:20, 2020-02-20T10:20, 0" })
    public void testCountFine_validDate_ShouldReturnCorrectFine(String orderEndDateStr, String orderReturnDateStr, double expectedFine) {
        Order order = mock(Order.class);

        LocalDateTime orderEndDate = LocalDateTime.parse(orderEndDateStr);
        LocalDateTime returnDate = LocalDateTime.parse(orderReturnDateStr);

        when(order.getOrderEndDate()).thenReturn(orderEndDate);
        when(order.getReturnDate()).thenReturn(returnDate);

        double actualFine = orderService.countFine(order);

        assertThat(actualFine).isEqualTo(expectedFine);
    }

    @Test
    public void testCountFine_ReturnDateIsNull()  {
        Order order = mock(Order.class);
        LocalDateTime orderEndDate = LocalDateTime.of(2023, 2, 1, 0, 0, 0);
        LocalDateTime now = LocalDateTime.of(2023, 2, 10, 0, 0, 0);

        when(order.getOrderEndDate()).thenReturn(orderEndDate);
        when(order.getReturnDate()).thenReturn(null);

        double fine = orderService.countFine(order);

        long daysPassed = ChronoUnit.DAYS.between(order.getOrderEndDate(), LocalDateTime.now());


        Field overdue = null;
        try {
            overdue = OrderServiceImpl.class.getDeclaredField("DAY_OVERDUE_FEE");

            overdue.setAccessible(true);

            assertThat(fine).isEqualTo(daysPassed *overdue.getDouble(overdue) );
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if (overdue != null) {
                overdue.setAccessible(false);
            }
        }

    }

}