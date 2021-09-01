package academy.Bentilzone;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConfig {

    public static Connection getConnection(){
        Connection con = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("PATH_TO_SERVER/DB_NAME", "DB_USER_NAME", "DB_PASSWORD");
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        return con;
    }
}
