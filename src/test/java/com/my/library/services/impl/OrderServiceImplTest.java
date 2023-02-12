package com.my.library.services.impl;

import com.my.library.dao.OrderDAO;
import com.my.library.dao.TransactionManager;
import com.my.library.dao.constants.OrderStatus;
import com.my.library.entities.Order;
import com.my.library.exceptions.DaoException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.constant.SubscriptionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void findAllUsersOrders_IsGreaterOrdersStatusValuesLength_ShouldThrowServiceException() {
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
    void countUsersOrders_Successful_ShouldReturnCorrectValue() throws ServiceException, DaoException {
        long userId = 1L;
        int expectedCount = 10;

        doReturn(expectedCount).when(orderDAO).countUserOrders(userId, OrderStatus.ACCEPTED);

        int actualCount = orderService.countUsersOrders(userId, OrderStatus.ACCEPTED);

        assertThat(actualCount).isEqualTo(expectedCount);
        verify(orderDAO, times(1)).countUserOrders(userId, OrderStatus.ACCEPTED);
    }

    @Test
    void countUsersOrders_DaoException_ShouldThrowServiceException() throws DaoException {
        long userId = 1L;
        OrderStatus[] orderStatuses = {OrderStatus.ACCEPTED, OrderStatus.PROCESSING};

        doThrow(DaoException.class).when(orderDAO).countUserOrders(userId, orderStatuses);

        assertThatThrownBy(() -> orderService.countUsersOrders(userId, orderStatuses))
                .isExactlyInstanceOf(ServiceException.class)
                .hasCauseExactlyInstanceOf(DaoException.class);
    }

    @ParameterizedTest
    @CsvSource({"2020-02-03T10:20, 2020-02-04T10:20, 10", "2020-03-03T10:20, 2020-04-04T10:20, 320", "2020-02-12T10:30, 2021-02-12T10:31, 3660", "2020-02-20T10:20, 2020-02-20T10:20, 0"})
    void countFine_validDate_ShouldReturnCorrectFine(String orderEndDateStr, String orderReturnDateStr, double expectedFine) {
        Order order = mock(Order.class);

        LocalDateTime orderEndDate = LocalDateTime.parse(orderEndDateStr);
        LocalDateTime returnDate = LocalDateTime.parse(orderReturnDateStr);

        when(order.getOrderEndDate()).thenReturn(orderEndDate);
        when(order.getReturnDate()).thenReturn(returnDate);

        double actualFine = orderService.countFine(order);

        assertThat(actualFine).isEqualTo(expectedFine);
    }

    @ParameterizedTest
    @CsvSource({"2023-01-11T10:20", "2023-02-03T10:23", "2023-02-02T14:14"})
    void countFine_ReturnDateIsNull(String endDate) {
        Order order = mock(Order.class);
        LocalDateTime orderEndDate = LocalDateTime.parse(endDate);

        when(order.getOrderEndDate()).thenReturn(orderEndDate);
        when(order.getReturnDate()).thenReturn(null);


        long daysPassed = ChronoUnit.DAYS.between(order.getOrderEndDate(), LocalDateTime.now());
        double overdue = SubscriptionInfo.DAY_OVERDUE_FEE;

        double expectedFine = daysPassed * overdue;

        double actualFine = orderService.countFine(order);

        assertThat(actualFine).isEqualTo(expectedFine);
    }


    @Test
    void findAllByStatus_WithValidInputs_ShouldReturnListOfOrders() throws ServiceException, DaoException {
        int start = 0;
        int offset = 10;
        OrderStatus[] orderStatus = new OrderStatus[]{OrderStatus.ACCEPTED, OrderStatus.PROCESSING};

        List<Order> expectedOrders = List.of(new Order(), new Order(), new Order());
        when(orderDAO.findAllByStatus(start, offset, orderStatus)).thenReturn(expectedOrders);

        List<Order> actualOrders = orderService.findAllByStatus(start, offset, orderStatus);

        assertThat(actualOrders).isEqualTo(expectedOrders);

        verify(orderDAO).findAllByStatus(start, offset, orderStatus);
    }

    @Test
    void findAllByStatus_WithZeroOrderStatus_ShouldThrowServiceException() {
        int start = 0;
        int offset = 10;
        OrderStatus[] orderStatus = new OrderStatus[]{};

        assertThatThrownBy(() -> orderService.findAllByStatus(start, offset, orderStatus))
                .isExactlyInstanceOf(ServiceException.class);

        verifyNoInteractions(orderDAO);
    }

    @Test
    void findAllByStatus_IsGreaterOrdersStatusValuesLength_ShouldThrowServiceException() {
        int start = 0;
        int offset = 10;
        OrderStatus[] orderStatus = {OrderStatus.ACCEPTED, OrderStatus.PROCESSING, OrderStatus.REJECTED, OrderStatus.REJECTED};

        assertThatThrownBy(() -> orderService.findAllByStatus(start, offset, orderStatus))
                .isExactlyInstanceOf(ServiceException.class);

        verifyNoInteractions(orderDAO);
    }

    @Test
    void findAllByStatus_DaoException_ShouldThrowServiceException() throws DaoException {
        int start = 0;
        int offset = 10;
        OrderStatus[] orderStatus = {OrderStatus.ACCEPTED, OrderStatus.REJECTED};

        when(orderDAO.findAllByStatus(start, offset, orderStatus)).thenThrow(DaoException.class);

        assertThatThrownBy(() -> orderService.findAllByStatus(start, offset, orderStatus))
                .isExactlyInstanceOf(ServiceException.class)
                .hasCauseExactlyInstanceOf(DaoException.class);

        verify(orderDAO, times(1)).findAllByStatus(start, offset, orderStatus);
    }


    @Test
    void countOrdersByStatus_WithValidInputs_ShouldReturnOrdersCount() throws ServiceException, DaoException {
        int expectedCount = 5;
        OrderStatus[] orderStatus = {OrderStatus.ACCEPTED, OrderStatus.PROCESSING};

        when(orderDAO.countOrdersByStatus(orderStatus)).thenReturn(expectedCount);

        int actualCount = orderService.countOrdersByStatus(orderStatus);

        assertThat(actualCount).isEqualTo(expectedCount);

        verify(orderDAO).countOrdersByStatus(orderStatus);
    }

    @Test
    void countOrdersByStatus_WithZeroOrderStatus_ShouldThrowServiceException() {
        OrderStatus[] orderStatus = {};

        assertThatThrownBy(() -> orderService.countOrdersByStatus(orderStatus))
                .isExactlyInstanceOf(ServiceException.class);

        verifyNoInteractions(orderDAO);
    }

    @Test
    void countOrdersByStatus_IsGreaterOrdersStatusValuesLength_ShouldThrowServiceException() {
        OrderStatus[] orderStatus = {OrderStatus.ACCEPTED, OrderStatus.PROCESSING, OrderStatus.REJECTED, OrderStatus.REJECTED};

        assertThatThrownBy(() -> orderService.countOrdersByStatus(orderStatus))
                .isExactlyInstanceOf(ServiceException.class);

        verifyNoInteractions(orderDAO);
    }

    @Test
    void countOrdersByStatus_DaoException_ShouldThrowServiceException() throws DaoException {
        OrderStatus[] orderStatus = {OrderStatus.ACCEPTED, OrderStatus.REJECTED};

        when(orderDAO.countOrdersByStatus(orderStatus)).thenThrow(DaoException.class);

        assertThatThrownBy(() -> orderService.countOrdersByStatus(orderStatus))
                .isExactlyInstanceOf(ServiceException.class)
                .hasCauseExactlyInstanceOf(DaoException.class);

        verify(orderDAO, times(1)).countOrdersByStatus(orderStatus);
    }


    @Test
    void placeOrder_beginTransactionThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException {
        TransactionManager transactionManager = mock(TransactionManager.class);
        BookService bookService = mock(BookServiceImpl.class);
        Order order = mock(Order.class);

        doThrow(DaoException.class).when(transactionManager).beginTransaction();

        assertThatThrownBy(() -> orderService.placeOrder(order, bookService, transactionManager))
                .isExactlyInstanceOf(ServiceException.class)
                .hasCauseExactlyInstanceOf(DaoException.class);

        verify(transactionManager, times(1)).beginTransaction();
        verify(transactionManager, times(1)).rollback();
        verify(transactionManager, times(1)).endTransaction();
    }

    @Test
    void placeOrder_commitThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException {
        TransactionManager transactionManager = mock(TransactionManager.class);
        BookService bookService = mock(BookServiceImpl.class);
        Order order = mock(Order.class);

        doThrow(DaoException.class).when(transactionManager).commit();

        assertThatThrownBy(() -> orderService.placeOrder(order, bookService, transactionManager))
                .isExactlyInstanceOf(ServiceException.class)
                .hasCauseExactlyInstanceOf(DaoException.class);

        verify(transactionManager, times(1)).beginTransaction();
        verify(transactionManager, times(1)).rollback();
        verify(transactionManager, times(1)).endTransaction();
    }

    @Test
    void placeOrder_rollbackThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException {
        TransactionManager transactionManager = mock(TransactionManager.class);
        BookService bookService = mock(BookServiceImpl.class);
        Order order = mock(Order.class);

        doThrow(DaoException.class).when(transactionManager).commit();
        doThrow(DaoException.class).when(transactionManager).rollback();

        assertThatThrownBy(() -> orderService.placeOrder(order, bookService, transactionManager))
                .isExactlyInstanceOf(ServiceException.class)
                .hasCauseExactlyInstanceOf(DaoException.class);

        verify(transactionManager, times(1)).beginTransaction();
        verify(transactionManager, times(1)).rollback();
        verify(transactionManager, times(1)).endTransaction();
    }

    @Test
    void placeOrder_bookServiceThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException, ServiceException {
        TransactionManager transactionManager = mock(TransactionManager.class);
        BookService bookService = mock(BookServiceImpl.class);
        Order order = mock(Order.class);

        doNothing().when(transactionManager).beginTransaction();
        doNothing().when(transactionManager).rollback();
        doNothing().when(transactionManager).endTransaction();

        doThrow(ServiceException.class).when(bookService).decrementBookQuantity(anyLong());


        assertThatThrownBy(() -> orderService.placeOrder(order, bookService, transactionManager))
                .isExactlyInstanceOf(ServiceException.class);

        verify(bookService, times(1)).decrementBookQuantity(anyLong());

        verify(transactionManager, times(1)).beginTransaction();
        verify(transactionManager, times(1)).rollback();
        verify(transactionManager, times(1)).endTransaction();

    }


    @Test
    void placeOrder_orderDaoThrowsException_ShouldRollbackTransactionThrowServiceException() throws DaoException, ServiceException {
        TransactionManager transactionManager = mock(TransactionManager.class);
        BookService bookService = mock(BookServiceImpl.class);
        Order order = mock(Order.class);

        doNothing().when(transactionManager).beginTransaction();
        doNothing().when(transactionManager).rollback();
        doNothing().when(transactionManager).endTransaction();

        doThrow(DaoException.class).when(orderDAO).save(order);


        assertThatThrownBy(() -> orderService.placeOrder(order, bookService, transactionManager))
                .isExactlyInstanceOf(ServiceException.class);

        verify(orderDAO, times(1)).save(order);

        verify(transactionManager, times(1)).beginTransaction();
        verify(transactionManager, times(1)).rollback();
        verify(transactionManager, times(1)).endTransaction();

    }

    @Test
    void placeOrder_validData_shouldDecrementBooksQuantity() throws DaoException, ServiceException {
        TransactionManager transactionManager = mock(TransactionManager.class);
        BookService bookService = mock(BookServiceImpl.class);
        Order order = mock(Order.class);

        doReturn(1L).when(order).getBookId();

        orderService.placeOrder(order, bookService, transactionManager);


        verify(order, times(1)).setOrderStartDate(any());
        verify(order, times(1)).setOrderEndDate(any());

        verify(bookService, times(1)).decrementBookQuantity(anyLong());

        verify(transactionManager, times(1)).beginTransaction();
        verify(transactionManager, times(1)).commit();
        verify(transactionManager, times(1)).endTransaction();
    }


}