package com.my.library.dto.mapper;

import com.my.library.dto.BookDTO;
import com.my.library.entities.Book;
import com.my.library.exceptions.ServiceException;
import com.my.library.services.BookService;

import java.util.ArrayList;
import java.util.List;

public class BookMapper {
    private BookService service;

    public BookMapper(BookService service) {
        this.service = service;
    }

    public List<BookDTO> toDTOList(List<Book> books) throws ServiceException {

        List<BookDTO> bookDTOList = new ArrayList<>();

        for (var book : books) {
            bookDTOList.add(toDTO(book, service.getQuantity(book.getBookId()), service.isRemoved(book.getBookId())));
        }
        return bookDTOList;
    }


    public BookDTO toDTO(Book book, int copies, boolean isRemoved) {

        return new BookDTO(book.getBookId(),
                book.getTitle(),
                book.getPublisherTitle(),
                book.getGenre(),
                book.getPageNumber(),
                book.getPublicationDate(),
                book.getAuthor().getFirstName(), book.getAuthor().getSecondName(),
                isRemoved,
                copies);
    }
}
