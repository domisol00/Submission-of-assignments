/* 작성자 : 이성원
 * 설명 : 강의클래스와 DB를 연결(DAO)
 */

package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import DTO.ClassDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.JdbcUtil;

public class ClassDao {
	
	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private JdbcUtil ju;

	//DB연결
	private void connectionJDBC() {
		
		try {
			
			ju = new JdbcUtil();
			connection = ju.getConnection();
			System.out.println("드라이버 로딩 성공 : ClassDao");
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("드라이버 로딩 실패 : ClassDao");
		}
	
	}

	//강의생성
	public void insertClass(ClassDto classDto) {
		
		//DB연결
		connectionJDBC();
		
		String sql = "insert into class values(null,?,?,?,?,?,?);";
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, classDto.getClassName());
			pstmt.setString(2, classDto.getTeacherId());
			pstmt.setString(3, classDto.getClassDescription());
			pstmt.setObject(4, classDto.getStartDate());
			pstmt.setObject(5, classDto.getEndDate());
			pstmt.setInt(6, classDto.getLimitStudent());
			pstmt.executeUpdate();
			System.out.println("강의생성 성공");
		} catch (SQLException e) {
			System.out.println("강의생성 실패");
			e.printStackTrace();
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}

	// 강의 삭제
	public void deleteClass(int classno) {
		
		//DB연결
		connectionJDBC();
		
		String sql = "delete from class where class_no = ?;";
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, classno);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}

	// 강의 정보수정
	public void updateClass(String classname, String description, LocalDate startdate, LocalDate enddate,
			int limitstudent, int classno) {
		//DB연결
		connectionJDBC();
		
		String sql = "update class set class_name=?, class_desc=?, start_date=?, end_date=?, limitstudent=? where class_no = ?;";
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, classname);
			pstmt.setString(2, description);
			pstmt.setObject(3, startdate);
			pstmt.setObject(4, enddate);
			pstmt.setInt(5, limitstudent);
			pstmt.setInt(6, classno);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}

	// 강의 하나만 가져오기
	public ClassDto selectClassOne(int classno) {
		
		//DB연결
		connectionJDBC();
		
		String sql = "select C.class_no, C.class_name, U.user_name, C.teacher_id, C.class_desc, C.start_date, C.end_date, C.limitstudent, count(R.student_id)" + 
				"from class C " + 
				"left outer join request_class R " + 
				"on C.class_no = R.class_no " + 
				"left outer join user U " + 
				"on C.teacher_id = U.user_id " +
				"where C.class_no=? " +
				"group by C.class_no ";
		ClassDto classDto = new ClassDto();
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, classno);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				classDto.setClassNo(rs.getInt("class_no"));
				classDto.setClassName(rs.getString("class_name"));
				classDto.setTeacherName(rs.getString("user_name"));
				classDto.setTeacherId(rs.getString("teacher_id"));
				classDto.setClassDescription(rs.getString("class_desc"));
				classDto.setStartDate(LocalDate.parse(rs.getString("start_Date"), DateTimeFormatter.ISO_DATE));
				classDto.setEndDate(LocalDate.parse(rs.getString("end_Date"), DateTimeFormatter.ISO_DATE));
				classDto.setLimitStudent(rs.getInt("limitstudent"));
				classDto.setCurrentStudent(rs.getInt(9));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return classDto;
	}

	// 전체강의 조회
	public ObservableList<ClassDto> selectAllClassList() {
		//DB연결
		connectionJDBC();
		
		ObservableList<ClassDto> list = FXCollections.observableArrayList();
		try {
			String sql = "select C.class_no, C.class_name, U.user_name, C.teacher_id, C.class_desc, C.start_date, C.end_date, C.limitstudent, count(R.student_id)" + 
					"from class C " + 
					"left outer join request_class R " + 
					"on C.class_no = R.class_no " + 
					"left outer join user U " + 
					"on C.teacher_id = U.user_id " +
					"group by C.class_no;";
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ClassDto classDto = new ClassDto();
				classDto.setClassNo(rs.getInt("class_no"));
				classDto.setClassName(rs.getString("class_name"));
				classDto.setTeacherName(rs.getString("user_name"));
				classDto.setTeacherId(rs.getString("teacher_id"));
				classDto.setClassDescription(rs.getString("class_desc"));
				classDto.setStartDate(LocalDate.parse(rs.getString("start_Date"), DateTimeFormatter.ISO_DATE));
				classDto.setEndDate(LocalDate.parse(rs.getString("end_Date"), DateTimeFormatter.ISO_DATE));
				classDto.setLimitStudent(rs.getInt("limitstudent"));
				classDto.setCurrentStudent(rs.getInt(9));
				classDto.setStr((rs.getInt(9) + " / " + rs.getInt(8)));
				list.add(classDto);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}

	// 본인강의만 조회(id로 조회)
	public ObservableList<ClassDto> selectMyClassList(String teacherid, int btnNo) {
		//DB연결
		connectionJDBC();
		
		ObservableList<ClassDto> list = FXCollections.observableArrayList();
		try {
			//디폴트 값(btnNo==0)으로 현재강의를 조회
			String sql = "select C.class_no, C.class_name, U.user_name, C.teacher_id, C.class_desc, C.start_date, C.end_date, C.limitstudent, count(R.student_id)" + 
					"from class C " + 
					"left outer join request_class R " + 
					"on C.class_no = R.class_no " + 
					"left outer join user U " + 
					"on C.teacher_id = U.user_id " +
					"where teacher_id=? and end_date >= sysdate() " +
					"group by C.class_no ";
			//btnNo == 1이면 지난강의를 조회
			if(btnNo == 1) {
				sql = "select C.class_no, C.class_name, U.user_name, C.teacher_id, C.class_desc, C.start_date, C.end_date, C.limitstudent, count(R.student_id)" + 
						"from class C " + 
						"left outer join request_class R " + 
						"on C.class_no = R.class_no " + 
						"left outer join user U " + 
						"on C.teacher_id = U.user_id " +
						"where teacher_id=? and end_date < sysdate() " +
						"group by C.class_no ";
			}
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, teacherid);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ClassDto classDto = new ClassDto();

				classDto.setClassNo(rs.getInt("class_no"));
				classDto.setClassName(rs.getString("class_name"));
				classDto.setTeacherName(rs.getString("user_name"));
				classDto.setTeacherId(rs.getString("teacher_id"));
				classDto.setClassDescription(rs.getString("class_desc"));
				classDto.setStartDate(LocalDate.parse(rs.getString("start_date"), DateTimeFormatter.ISO_DATE));
				classDto.setEndDate(LocalDate.parse(rs.getString("end_date"), DateTimeFormatter.ISO_DATE));
				classDto.setLimitStudent(rs.getInt("limitstudent"));
				classDto.setCurrentStudent(rs.getInt(9));
				classDto.setStr((rs.getInt(9) + " / " + rs.getInt(8)));
				
				list.add(classDto);
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
	
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}

	// 전체 강의검색하기(강의명 or 선생님명)
	public ObservableList<ClassDto> searchClassList(String str) {
		//DB연결
		connectionJDBC();
		
		ObservableList<ClassDto> list = FXCollections.observableArrayList();
		
		try {
			String sql = "select C.class_no, C.class_name, U.user_name, C.teacher_id, C.class_desc, C.start_date, C.end_date, C.limitstudent, count(R.student_id)" + 
					"from class C " + 
					"left outer join request_class R " + 
					"on C.class_no = R.class_no " + 
					"left outer join user U " + 
					"on C.teacher_id = U.user_id " +
					"where C.class_name like ? or U.user_name like ? " +
					"group by C.class_no ";
			
			String temp = "%" + str + "%";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, temp);
			pstmt.setString(2, temp);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				ClassDto classDto = new ClassDto();

				classDto.setClassNo(rs.getInt("class_no"));
				classDto.setClassName(rs.getString("class_name"));
				classDto.setTeacherName(rs.getString("user_name"));
				classDto.setTeacherId(rs.getString("teacher_id"));
				classDto.setClassDescription(rs.getString("class_desc"));
				classDto.setStartDate(LocalDate.parse(rs.getString("start_date"), DateTimeFormatter.ISO_DATE));
				classDto.setEndDate(LocalDate.parse(rs.getString("end_date"), DateTimeFormatter.ISO_DATE));
				classDto.setLimitStudent(rs.getInt("limitstudent"));
				classDto.setCurrentStudent(rs.getInt(9));
				classDto.setStr((rs.getInt(9) + " / " + rs.getInt(8)));

				list.add(classDto);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}
	
	// 자신의 강의내에서 검색하기(강의명)
		public ObservableList<ClassDto> searchClassList(String userid,String str, int btnNo) {
			//DB연결
			connectionJDBC();
			
			ObservableList<ClassDto> list = FXCollections.observableArrayList();
			
			try {
				//디폴트값(btnNo=0)일때 현재강의 범위에서 검색
				String sql = "select C.class_no, C.class_name, U.user_name, C.teacher_id, C.class_desc, C.start_date, C.end_date, C.limitstudent, count(R.student_id)" + 
						"from class C " + 
						"left outer join request_class R " + 
						"on C.class_no = R.class_no " + 
						"left outer join user U " + 
						"on C.teacher_id = U.user_id " +
						"where teacher_id=? and class_name like ? and end_date >= sysdate() " +
						"group by C.class_no ";
				//btnNo == 1이면 지난강의 범위에서 검색
				if(btnNo == 1) {
					sql = "select C.class_no, C.class_name, U.user_name, C.teacher_id, C.class_desc, C.start_date, C.end_date, C.limitstudent, count(R.student_id)" + 
							"from class C " + 
							"left outer join request_class R " + 
							"on C.class_no = R.class_no " + 
							"left outer join user U " + 
							"on C.teacher_id = U.user_id " +
							"where teacher_id=? and class_name like ? and end_date < sysdate() " +
							"group by C.class_no ";
				} 
				String temp = "%" + str + "%";
				pstmt = connection.prepareStatement(sql);
				pstmt.setString(1, userid);
				pstmt.setString(2, temp);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					ClassDto classDto = new ClassDto();

					classDto.setClassNo(rs.getInt("class_no"));
					classDto.setClassName(rs.getString("class_name"));
					classDto.setTeacherName(rs.getString("user_name"));
					classDto.setTeacherId(rs.getString("teacher_id"));
					classDto.setClassDescription(rs.getString("class_desc"));
					classDto.setStartDate(LocalDate.parse(rs.getString("start_date"), DateTimeFormatter.ISO_DATE));
					classDto.setEndDate(LocalDate.parse(rs.getString("end_date"), DateTimeFormatter.ISO_DATE));
					classDto.setLimitStudent(rs.getInt("limitstudent"));
					classDto.setCurrentStudent(rs.getInt(9));
					classDto.setStr((rs.getInt(9) + " / " + rs.getInt(8)));
					
					list.add(classDto);
				}

			} catch (SQLException e) {
				e.printStackTrace();

			} finally {
				//접속종료
				ju.disconnect(connection, pstmt, rs);
			}
			return list;
		}
}