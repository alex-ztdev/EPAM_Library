package com.my.library.controller.command.impl.librarian;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.RedirectToPage;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcceptOrderCommandTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private OrderService orderService;
    private AcceptOrderCommand acceptOrderCommand;

    @BeforeEach
    public void setUp() {
        acceptOrderCommand = new AcceptOrderCommand(orderService);
    }

    @Test
    public void execute_WithValidOrderId_ShouldReturnCommandWithRedirectionToPreviousPage() throws CommandException, ServiceException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter(Parameters.ORDER_ID)).thenReturn("1");

        CommandResult result = acceptOrderCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.DISPLAY_USERS_REQUESTS);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(request).getSession();
        verify(request).getParameter(Parameters.ORDER_ID);

        verify(orderService).acceptOrder(anyLong());
    }

    @Test
    public void execute_WithInvalidOrderId_ShouldReturnCommandResultThatRedirectToUnsupportedOperationPage() throws CommandException, ServiceException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter(Parameters.ORDER_ID)).thenReturn("NaN");

        CommandResult result = acceptOrderCommand.execute(request);

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(RedirectToPage.UNSUPPORTED_OPERATION);
        assertThat(result.getAction()).isEqualTo(CommandDirection.REDIRECT);

        verify(request).getSession();
        verify(request).getParameter(Parameters.ORDER_ID);

        verify(orderService, times(0)).acceptOrder(anyLong());
    }

    @Test
    void execute_OrderServiceThrowsServiceException_ShouldThrowCommandException() throws ServiceException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter(Parameters.ORDER_ID)).thenReturn("1");

        doThrow(ServiceException.class).when(orderService).acceptOrder(anyLong());

        assertThatThrownBy(() -> acceptOrderCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);

        verify(request).getSession();
        verify(request).getParameter(Parameters.ORDER_ID);

        verify(orderService, times(1)).acceptOrder(anyLong());
    }

}