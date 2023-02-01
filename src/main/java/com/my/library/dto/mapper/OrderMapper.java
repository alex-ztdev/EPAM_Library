package com.my.library.dto.mapper;

import com.my.library.dto.OrderDTO;
import com.my.library.entities.Order;
import com.my.library.entities.User;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;
import com.my.library.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    private final BookService bookService;
    private final UserService userService;

    public OrderMapper(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    public List<OrderDTO> toDTOList(List<Order> orderList) throws ServiceException {
        List<OrderDTO> orderDTOList = new ArrayList<>();

        for (var order : orderList) {
            var userContainer = userService.find(order.getUserId());
            var bookContainer = bookService.find(order.getBookId());

            if (userContainer.isEmpty()) {
                throw new ServiceException("Error while mapping order to orderDTO, no User with such id found! User id:" + order.getUserId());
            }
            if (bookContainer.isEmpty()) {
                throw new ServiceException("Error while mapping order to orderDTO, no Book with such id found! Book id: " + order.getBookId());
            }

            var user = userContainer.get();
            var book = bookContainer.get();

            orderDTOList.add(new OrderDTO(order, userService.countFine(order), user.getFirstName() + user.getSecondName(), book.getTitle()));
        }
        return orderDTOList;
    }
}
