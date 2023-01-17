import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.BookDAO;
import com.my.library.dao.constants.UserRole;
import com.my.library.dao.constants.UserStatus;
import com.my.library.dao.impl.AuthorDaoImpl;
import com.my.library.dao.impl.BookDaoImpl;
import com.my.library.dao.impl.UserDaoImpl;
import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.entities.User;
import com.my.library.exceptions.DaoException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Properties;

public class HelloWorldServlet  {
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        var authorsDao = AuthorDaoImpl.getInstance();
//        var start = System.currentTimeMillis();
//        try {
//            authorsDao.findAll().forEach(System.out::println);
//        } catch (DaoException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(System.currentTimeMillis()- start);


//        var authorToInsert = new Book();
//        authorToInsert.setFirstName("Amo");
//        authorToInsert.setSecondName("Gus");
//        authorToInsert.setBirthDate(LocalDate.of(2000, 10, 2));

//        try {
//            authorsDao.save(authorToInsert);
//        } catch (DaoException e) {
//            e.printStackTrace();
//        }

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
                UserDaoImpl userDao = UserDaoImpl.getInstance();
                var user = userDao.find(10000).get();
                System.out.println(user);
                user.setEmail("newMail");

//                user = new User("newLogin", DigestUtils.sha512Hex("user"), UserRole.USER, UserStatus.NORMAL, "mail", "123", "first", "sec", LocalDate.now());
                userDao.unblock(user);

                System.out.println(userDao.find(10000));
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }

        } finally {
            ConnectionPool.getInstance().destroyPool();
        }


    }
}
