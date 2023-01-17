import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.BookDAO;
import com.my.library.dao.impl.AuthorDaoImpl;
import com.my.library.dao.impl.BookDaoImpl;
import com.my.library.entities.Author;
import com.my.library.entities.Book;
import com.my.library.exceptions.DaoException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Properties;

public class HelloWorldServlet {
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
                authorsDao.findAll().forEach(x -> {
                    if (x.getBookList().isEmpty()) {
                        System.out.println(x);
                    }
                });

            } catch (DaoException e) {
                throw new RuntimeException(e);
            }

        } finally {
            ConnectionPool.getInstance().destroyPool();
        }


    }
}
