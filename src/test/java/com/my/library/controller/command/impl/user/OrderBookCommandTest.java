package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.OrderParameters;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.TransactionManager;
import com.my.library.dto.UserDTO;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderBookCommandTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private OrderService orderService;
    @Mock
    private BookService bookService;
    @Mock
    private TransactionManager transactionManager;
    @Mock
    private UserDTO userDTO;
    private OrderBookCommand orderBookCommand;

    @BeforeEach
    public void setUp() {
        orderBookCommand = new OrderBookCommand(orderService, bookService, transactionManager);
    }

    @Test
    @DisplayName("When valid input return redirect to requests page")
    public void execute_validData_ShouldReturnCommandResultWithRedirectionTo() throws CommandException, ServiceException {
        doReturn(1L).when(userDTO).getUserId();
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");
        when(request.getParameter(OrderParameters.SUBSCRIPTION_TYPE)).thenReturn("true");
        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(userDTO);

        doReturn(10).when(bookService).getQuantity(1L);



        CommandResult result = orderBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.MY_REQUESTS_PAGE_WITH_SUCCESSFUL_MSG);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(request).getSession();
        verify(request).getParameter(Parameters.BOOK_ID);
        verify(request).getParameter(OrderParameters.SUBSCRIPTION_TYPE);
        verify(bookService).getQuantity(1);

        verify(orderService, times(1)).placeOrder(any(), any(), any());
    }

    @Test
    public void execute_InvalidBookId_ShouldReturnCommandResultWithRedirectionToUnsupportedOperationPage() throws CommandException {

        when(request.getSession()).thenReturn(session);

        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("NaN");
        when(request.getParameter(OrderParameters.SUBSCRIPTION_TYPE)).thenReturn("true");
//        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(userDTO);


        CommandResult result = orderBookCommand.execute(request);

        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(request).getSession();
        verify(request).getParameter(Parameters.BOOK_ID);
        verify(request).getParameter(OrderParameters.SUBSCRIPTION_TYPE);
    }

    @Test
    public void execute_InvalidSubscriptionType_ShouldReturnCommandResultWithRedirectionToUnsupportedOperationPage() throws CommandException {

        when(request.getSession()).thenReturn(session);

        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");
        when(request.getParameter(OrderParameters.SUBSCRIPTION_TYPE)).thenReturn("invalidInput");
//        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(userDTO);


        CommandResult result = orderBookCommand.execute(request);

        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(request).getSession();
        verify(request).getParameter(Parameters.BOOK_ID);
        verify(request).getParameter(OrderParameters.SUBSCRIPTION_TYPE);
    }


    @Test
    public void execute_BookQuantityIsLessThanOne_ShouldReturnCommandResultWithRedirectionToUnsupportedOperationPage() throws CommandException, ServiceException {
        doReturn(1L).when(userDTO).getUserId();
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");
        when(request.getParameter(OrderParameters.SUBSCRIPTION_TYPE)).thenReturn("true");
        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(userDTO);

        doReturn(0).when(bookService).getQuantity(1L);


        CommandResult result = orderBookCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(request).getSession();
        verify(request).getParameter(Parameters.BOOK_ID);
        verify(request).getParameter(OrderParameters.SUBSCRIPTION_TYPE);
        verify(bookService).getQuantity(1);

        verify(orderService, times(0)).placeOrder(any(), any(), any());
    }


    @Test
    void execute_OrderServiceThrowsServiceException_ShouldThrowCommandException() throws ServiceException {
        doReturn(1L).when(userDTO).getUserId();
        when(request.getSession()).thenReturn(session);

        when(request.getParameter(Parameters.BOOK_ID)).thenReturn("1");
        when(request.getParameter(OrderParameters.SUBSCRIPTION_TYPE)).thenReturn("true");
        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(userDTO);


        doThrow(ServiceException.class).when(bookService).getQuantity(anyLong());

        assertThatThrownBy(() -> orderBookCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);

        verify(request).getSession();
        verify(request).getParameter(Parameters.BOOK_ID);
        verify(request).getParameter(OrderParameters.SUBSCRIPTION_TYPE);
        verify(bookService).getQuantity(1);

    }


}