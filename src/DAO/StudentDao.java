package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import DTO.StudentDto;
import DTO.TaskDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.JdbcUtil;

/**
 * @author 김지현
 */
public class StudentDao {

	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private JdbcUtil ju;
//	private int i = 1;


	// DB연결
	private void connectionJDBC() {

		try {

			ju = new JdbcUtil();
			connection = ju.getConnection();
			System.out.println("드라이버 로딩 성공 :StudentDao");
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("드라이버 로딩 실패 : StudentDao");
		}
	}
	
	public ObservableList<StudentDto> selectUserStudentList(int classno) {

		// DB연결
		connectionJDBC();

		ObservableList<StudentDto> list = FXCollections.observableArrayList();
		try {
			String sql = "select * from class_student where class_no=? order by user_name";

			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, classno);

			ResultSet re = pstmt.executeQuery();
			int i = 1;
			while (re.next()) {
				StudentDto st = new StudentDto();

				st.setStNo(i);
				st.setStTitle(re.getString("user_name"));
				st.setStDesc(re.getString("student_id"));
//				st.setStFile("sdfsdf");
//				st.setstRegdate(LocalDate.parse(re.getString("reg_date"), DateTimeFormatter.ISO_DATE));

//				if (re.getString("expire_date") == null) {
//					System.out.println("selectList-expireDate-null");
//				} else {
//					st.setstExpireDate(LocalDate.parse(re.getString("expire_date"), DateTimeFormatter.ISO_DATE));
//				}
//
//				st.setstFile(re.getString("attachedFile_name"));
//				st.setPerfectScore((Integer) re.getObject("perfect_score"));
//				st.setClassNo(re.getInt("class_no"));
				list.add(st);	
				i++;
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {

			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}
}