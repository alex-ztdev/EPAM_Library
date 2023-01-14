import com.library.connection_pool.DBManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class HelloWorldServlet {
    public static void main(String[] args) {
        var db = DBManager.getInstance();
    }
}
