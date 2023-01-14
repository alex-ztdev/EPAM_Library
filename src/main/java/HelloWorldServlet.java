import com.library.dao.impl.AuthorDaoImpl;


public class HelloWorldServlet {
    public static void main(String[] args) {
        var authorsDao = AuthorDaoImpl.getInstance();
        var start = System.currentTimeMillis();
        authorsDao.findAll().forEach(System.out::println);
        System.out.println(System.currentTimeMillis()- start);
    }
}
