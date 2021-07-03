package academy.Bentilzone;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConfig {

    public static Connection getConnection(){
        Connection con = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/contact_manager", "root", ""); // Database name: contact_manager, MySQL user: root & Password: ""
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        return con;
    }
}
