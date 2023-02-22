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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class DisplayMyRequestsCommandTest {

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
    private DisplayMyRequestsCommand displayMyRequestsCommand;


    @Test
    void execute_ValidParameters_ShouldReturnCommandResultToRequestsPage() throws CommandException, ServiceException {
        User user = new User();
        UserDTO userDTO = mock(UserDTO.class);

        doReturn(1L).when(userDTO).getUserId();

        user.setUserId(1L);

        Mockito.when(request.getParameter(Parameters.GENERAL_CURR_PAGE)).thenReturn("1");
        Mockito.when(request.getParameter(Parameters.ORDER_SUCCESSFUL_MSG)).thenReturn(Parameters.ORDER_SUCCESSFUL_MSG);
        Mockito.when(request.getSession()).thenReturn(session);

        Mockito.when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(userDTO);
        Mockito.when(userService.find(anyLong())).thenReturn(Optional.of(user));
        Mockito.when(bookService.find(anyLong())).thenReturn(Optional.of(mock(Book.class)));


        List<Order> orderList = List.of(
                new Order(1L, 1L, 1L, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), OrderStatus.PROCESSING, false),
                new Order(1L, 1L, 1L, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), OrderStatus.PROCESSING, false)
        );


        Mockito.doReturn(orderList).when(orderService).findAllUsersOrders(1L, 0, 10, OrderStatus.PROCESSING, OrderStatus.REJECTED);
        Mockito.doReturn(2).when(orderService).countUsersOrders(1L, OrderStatus.PROCESSING, OrderStatus.REJECTED);

        CommandResult result = displayMyRequestsCommand.execute(request);

        Mockito.verify(userService, Mockito.atLeastOnce()).find(anyLong());
        Mockito.verify(bookService, Mockito.atLeastOnce()).find(anyLong());
        Mockito.verify(request, Mockito.atLeastOnce()).getParameter(anyString());

        assertThat(result).isNotNull();
        assertThat(result.getPage()).isEqualTo(Pages.USERS_REQUESTS);
        assertThat(result.getAction()).isEqualTo(CommandDirection.FORWARD);
    }

    @Test
    void execute_orderServiceThrowsException_ShouldReturnCommandResultThatRedirectToUnsupportedOperationPage() throws ServiceException {
        UserDTO userDTO = mock(UserDTO.class);

        doReturn(1L).when(userDTO).getUserId();

        Mockito.when(request.getParameter(Parameters.GENERAL_CURR_PAGE)).thenReturn("1");
        Mockito.when(request.getSession()).thenReturn(session);

        Mockito.when(session.getAttribute(UserParameters.USER_IN_SESSION)).thenReturn(userDTO);

        Mockito.doThrow(ServiceException.class).when(orderService).findAllUsersOrders(1L, 0, 10, OrderStatus.PROCESSING, OrderStatus.REJECTED);

        assertThatThrownBy(() -> displayMyRequestsCommand.execute(request))
                .isExactlyInstanceOf(CommandException.class)
                .hasCauseExactlyInstanceOf(ServiceException.class);

        Mockito.verify(request, Mockito.atLeastOnce()).getParameter(anyString());
    }
}