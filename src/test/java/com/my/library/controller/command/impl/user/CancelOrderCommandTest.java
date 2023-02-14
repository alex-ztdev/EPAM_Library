package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.Command;
import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.TransactionManager;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelOrderCommandTest {
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
    private User user;

    private CancelOrderCommand cancelOrderCommand;

    @BeforeEach
    public void setUp() {
        cancelOrderCommand = new CancelOrderCommand(orderService, bookService, transactionManager);
    }

    @Test
    public void execute_WithValidOrderId_ShouldReturnCommandResultWithCorrectPageAndRedirectDirection() throws CommandException, ServiceException {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(user);

        when(request.getParameter(Parameters.ORDER_ID)).thenReturn("1");
        when(session.getAttribute(Parameters.PREVIOUS_PAGE)).thenReturn("previous_page");
        when(user.getUserId()).thenReturn(2L);

        CommandResult result = cancelOrderCommand.execute(request);

        verify(orderService).cancelOrder(2L, 1L, bookService, transactionManager);
        assertThat(result.getPage()).isEqualTo("previous_page");
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }

    @Test
    public void execute_WithInvalidOrderId_ShouldReturnCommandResultThatRedirectToUnsupportedOperationPage() throws CommandException {
        when(request.getParameter(Parameters.ORDER_ID)).thenReturn("NaN");

        CommandResult result = cancelOrderCommand.execute(request);

        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);
    }

    @Test
    void cancelOrder_OrderServiceThrowsServiceException_ShouldThrowCommandException() throws ServiceException, CommandException {
        User user = mock(User.class);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter(Parameters.ORDER_ID)).thenReturn("1");
        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(user);

        doThrow(ServiceException.class).when(orderService).cancelOrder(anyLong(), anyLong(), any(), any());

        assertThatThrownBy(() -> cancelOrderCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);

        verify(orderService, times(1)).cancelOrder(user.getUserId(), 1L, bookService, transactionManager);
        verify(session, times(1)).getAttribute(UserParameters.USER_IN_SESSION);
    }

}
