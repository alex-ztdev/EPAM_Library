package com.my.library.controller.command.impl.user;

import com.my.library.controller.command.CommandResult;
import com.my.library.controller.command.constant.CommandDirection;
import com.my.library.controller.command.constant.parameters.Parameters;
import com.my.library.controller.command.constant.parameters.UserParameters;
import com.my.library.dao.constants.OrderStatus;
import com.my.library.dto.UserDTO;
import com.my.library.entities.Book;
import com.my.library.entities.Order;
import com.my.library.entities.User;
import com.my.library.exceptions.CommandException;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.OrderService;
import com.my.library.services.UserService;
import com.my.library.utils.Pages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DisplayMyOrdersCommandTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private OrderService orderService;
    @Mock
    private BookService bookService;
    @Mock
    private UserService userService;

    @InjectMocks
    private DisplayMyOrdersCommand displayMyOrdersCommand;


    @Test
    public void execute_ValidParameters_ShouldReturnCommandResultToOrdersPage() throws CommandException, ServiceException {
        User user = new User();
        user.setUserId(1L);

        UserDTO userDTO = mock(UserDTO.class);

        doReturn(1L).when(userDTO).getUserId();

        when(request.getParameter(Parameters.GENERAL_CURR_PAGE)).thenReturn("1");
        when(request.getParameter(Parameters.ORDER_SUCCESSFUL_MSG)).thenReturn(Parameters.ORDER_SUCCESSFUL_MSG);
        when(request.getSession()).thenReturn(session);

        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(userDTO);
        when(userService.find(anyLong())).thenReturn(Optional.of(user));
        when(bookService.find(anyLong())).thenReturn(Optional.of(mock(Book.class)));


        List<Order> orderList = List.of(
                new Order(1L, 1L, 1L, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), OrderStatus.ACCEPTED, false),
                new Order(1L, 1L, 1L, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), OrderStatus.ACCEPTED, false)
        );


        doReturn(orderList).when(orderService).findAllUsersOrders(1L, 0, 10, OrderStatus.ACCEPTED);
        doReturn(2).when(orderService).countUsersOrders(1L, OrderStatus.ACCEPTED);

        CommandResult result = displayMyOrdersCommand.execute(request);

        verify(userService, atLeastOnce()).find(anyLong());
        verify(bookService, atLeastOnce()).find(anyLong());
        verify(request, atLeastOnce()).getParameter(anyString());

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.DISPLAY_ORDERS_PAGE);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);
    }

    @Test
    public void execute_orderServiceThrowsException_ShouldReturnCommandResultThatRedirectToUnsupportedOperationPage() throws ServiceException {
        User user = new User();
        user.setUserId(1L);

        UserDTO userDTO = mock(UserDTO.class);

        doReturn(1L).when(userDTO).getUserId();


        when(request.getParameter(Parameters.GENERAL_CURR_PAGE)).thenReturn("1");
        when(request.getParameter(Parameters.ORDER_SUCCESSFUL_MSG)).thenReturn(Parameters.ORDER_SUCCESSFUL_MSG);
        when(request.getSession()).thenReturn(session);

        when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(userDTO);

        doThrow(ServiceException.class).when(orderService).findAllUsersOrders(1L, 0, 10, OrderStatus.ACCEPTED);

        assertThatThrownBy(() -> displayMyOrdersCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);


        verify(request, atLeastOnce()).getParameter(anyString());
    }
}