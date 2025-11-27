package DB;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBC {
    public static final String databaseDriver = "com.mysql.cj.jdbc.Driver";
    public static final String databaseUrl = "jdbc:mysql://localhost:3306/ScheduleManagement?serverTimezone=UTC&characterEncoding=UTF8&useSSL=false";
    public static final String databaseUser = "root";
    public static final String databasePassword = "개인비밀번호";
    
    public static Connection connection = null;
    
    public static void main(String[] args) {
    	connect();
    	close();
    	
    	
    	
    }

	
}
