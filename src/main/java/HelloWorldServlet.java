import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class HelloWorldServlet {
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
//        var authorsDao = AuthorDaoImpl.getInstance();
//        var start = System.currentTimeMillis();
//        try {
//            authorsDao.findAll().forEach(System.out::println);
//        } catch (DaoException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(System.currentTimeMillis()- start);
//
//
//        var authorToInsert = new Author();
//        authorToInsert.setFirstName("Amo");
//        authorToInsert.setSecondName("Gus");
//
//        authorsDao.save(authorToInsert);


        Properties properties = new Properties();
//        for (int i = 0; i < 1; i++) {
//            try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("database.properties")) {
//                properties.load(inputStream);
//                String s = properties.getProperty("db.url");
//                String username = properties.getProperty("db.username");
//                String pass = properties.getProperty("db.password");
//
//            } catch (Exception e) {
//
//                logger.log(Level.ERROR, "No database property file found! "+ e.getLocalizedMessage(), e);
////                throw new RuntimeException(e);
//            }
//        }
    }
}
