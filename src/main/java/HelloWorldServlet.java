import com.my.library.connection_pool.ConnectionPool;
import com.my.library.dao.impl.AuthorDaoImpl;
import com.my.library.dao.impl.UserDaoImpl;
import com.my.library.exceptions.DaoException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/hi")
public class HelloWorldServlet extends HttpServlet {
    AuthorDaoImpl authorDao = AuthorDaoImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("Hi");

        try {

            resp.getWriter().append(authorDao.find(1).get().toString());

        } catch (DaoException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroyPool();
        super.destroy();
    }
}
