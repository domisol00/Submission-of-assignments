package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 도현호
 */
public class JdbcUtil {

	private Connection connection;
//	private Properties properties;
//	private String path;
	
	public JdbcUtil() {

		init();
	}
	public void init() {
		
		//db연동을 위한 properties 읽기
		try {
//			exe파일로 만들었을 때, properties를 읽지 못하기에 직접입력해야함	
//			path = Class.forName("util.JdbcUtil").getResource("mysql_connection/database.properties").getPath();
//			path = URLDecoder.decode(path, "UTF-8");
//			properties = new Properties();
//			properties.load(new FileReader(path));
//			
//			String driver = properties.getProperty("driver");
//			String url = properties.getProperty("url");
//			String username = properties.getProperty("username");
//			String password = properties.getProperty("password");
			
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://projectf.cqwdlyjz7pdl.ap-northeast-2.rds.amazonaws.com:3306/projectdb?useSSL=false";
			String username = "admin";
			String password = "12341234";
			
			Class.forName(driver);		//driver 가져오기
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
		}
	}

	//DB 접속
	public Connection getConnection() throws SQLException, ClassNotFoundException {
		
		if (connection != null && !connection.isClosed()) {
			return connection;
		} else {
			init();
			return connection;
		}
	}

	//DB 접속 종료
	public void disconnect(Connection connection, PreparedStatement pstmt, ResultSet rs) {
		
		try {
			if(rs != null && !rs.isClosed()) rs.close();									//ResultSet 종료
			if(pstmt != null && !pstmt.isClosed()) pstmt.close();						//PreparedStatement 종료
			if(connection != null && !connection.isClosed()) connection.close();		//Connection 종료
			System.out.println("드라이버 종료 성공");
			System.out.println("------------------------------");
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("드라이버 종료 실패");
		}
	}
}