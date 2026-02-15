package utilies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    final String URL = "jdbc:mysql://localhost:3306/flehasmart";
    final String USER = "root";
    final String PASSWORD = "";

    private Connection connection;
    private static MyDataBase instance;

    public MyDataBase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database successfully");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if(instance == null){
            instance =  new MyDataBase();
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }
}
