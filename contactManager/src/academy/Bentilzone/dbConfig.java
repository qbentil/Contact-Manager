package academy.Bentilzone;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConfig {

    public static Connection getConnection(){
        Connection con = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/DATABASE_NAME", "DB_USER", "DB_PASSWORD"); 
            /*
            INSTANCE
            if Database_name =  contactmanager, DB_USER: "root" & Password: "myPass"
            con = DriverManager.getConnection("jdbc:mysql://localhost/contactmanager", "root", "myPass"); 
            */
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        return con;
    }
}
