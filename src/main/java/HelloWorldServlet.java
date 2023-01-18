import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.BookDAO;
import com.my.library.dao.OrderDAO;
import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.UserStatus;
import com.my.library.dao.impl.AuthorDaoImpl;
import com.my.library.dao.impl.BookDaoImpl;
import com.my.library.dao.impl.OrderDaoImpl;
import com.my.library.dao.impl.UserDaoImpl;
import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.entities.Order;
import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;

public class HelloWorldServlet {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            try {
                var bookDao = BookDaoImpl.getInstance();
//                bookDao.find(13).ifPresent(System.out::println);
//                bookDao.findAll().forEach(x-> System.out.println(x));
//                var book = new Book(101L, "My title UPDATED 5.0!", "Hachette", "Satire", 129, LocalDate.now(), false, false);
//                System.out.println(bookDao.find(101L));
//
//                bookDao.save(book);
//                bookDao.findAll().forEach(System.out::println);
//                bookDao.update(book);
//                System.out.println(bookDao.find(book.getBookId()));
//
//
//                System.out.println(bookDao.find(10));
//
//                bookDao.delete(10);
//                System.out.println(bookDao.find(10));
//                bookDao.getBookAuthors(1).forEach(System.out::println);
                OrderDaoImpl orderDao = OrderDaoImpl.getInstance();
                var order = orderDao.find(100000).get();
//                orderDao.findAll().forEach(System.out::println);
                UserDaoImpl userDao = UserDaoImpl.getInstance();
//                var order = new Order(userDao.find(10000).get(),
//                        bookDao.find(13).get(),
//                        LocalDateTime.of(2000, 2, 23, 19, 1),
//                        LocalDateTime.of(2000, 3, 23, 19, 1),
//                        null
//                );
//                orderDao.save(order);
                System.out.println(order);
                order.setActualReturnDate(LocalDateTime.now());
                orderDao.delete(order);
                orderDao.find(order.getOrderId()).ifPresent(System.out::println);

            } catch (DaoException e) {
                throw new RuntimeException(e);
            }

        } finally {
            ConnectionPool.getInstance().destroyPool();
        }


    }
}
