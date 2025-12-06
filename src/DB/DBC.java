package DB;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBC {
    public static final String databaseDriver = "com.mysql.cj.jdbc.Driver";
    public static final String databaseUrl = "jdbc:mysql://localhost:3306/ScheduleManagement?serverTimezone=Asia/Seoul&characterEncoding=UTF8&useSSL=false";
    public static final String databaseUser = "root";
    public static final String databasePassword = "본인 비밀번호";
    
    public static Connection connection = null;
    
    
    // 테스트용 메인 메소드
    public static void main(String[] args) {
    	connect();
    	close();
    
    }
    
    //DB 연결 메소드
    public static Connection connect() {
    	try {
    		Class.forName(databaseDriver);
    		connection = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
    		if(connection != null) System.out.println("DB 연결 성공");
    		else System.out.println("DB 연결 실패");
    	}catch(Exception e) {
    		// 에러나면 팝업창을 띄움
    		JOptionPane.showMessageDialog(null, "데이터베이스 연결 실패! 비번이나 방이름을 확인하세요.", "경고", JOptionPane.WARNING_MESSAGE);
    		System.err.println("에러 내용: "+ e.getMessage());
    		e.printStackTrace();
    	}
    	return connection;
    }
   
    //DB 연결 해제 메소드
    public static void close() {
    	try {
    		if(connection != null) {
    			System.out.println("DB 연결 종료");
    			connection.close();
    		}
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    }
}
