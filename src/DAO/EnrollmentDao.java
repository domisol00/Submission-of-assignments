package DAO;
/*
 * 작성자 : 도현호
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import DTO.EnrollmentDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.JdbcUtil;

public class EnrollmentDao {
	
	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private JdbcUtil ju;

	//DB연결
	private void connectionJDBC() {
		
		try {
			
			ju = new JdbcUtil();
			connection = ju.getConnection();
			System.out.println("드라이버 로딩 성공 : EnrollentDao");
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("드라이버 로딩 실패 : EnrollentDao");
		}
	}

	//수강신청
	public void submit_Enrollment(String stu_id, int class_no) throws SQLException{
	
		//DB연결
		connectionJDBC();
	
		try {
			
			String sql = "insert into request_class values (?,?);";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);			//학생 ID
			pstmt.setInt(2, class_no);			//강의 no
			pstmt.executeUpdate();
			System.out.println("강의 신청 성공");
			//PK, FK 설정으로 학생테이블이나 강의테이블에 값이 없으면 들어가지 않음
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}

	//전체조회
	public ObservableList<EnrollmentDto> enrollment_selectAll() {
		
		//DB연결
		connectionJDBC();
		
		ObservableList<EnrollmentDto> list = FXCollections.observableArrayList();
		String sql = "select c.*, count(rc.student_id), u.user_name "
			       + "  from class c"
			       + "  left outer join request_class rc"
			       + "    on c.class_no = rc.class_no"
			       + "	left outer join user u"
			       + "	  on c.teacher_id = u.user_id"
			       + " where end_date >= curdate()"
			       + " group by c.class_no"
			       + " order by c.start_date asc, c.end_date asc, c.class_no asc;";
		
		try {
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();
		
			while(rs.next()) {
				
				EnrollmentDto enroll = new EnrollmentDto();
				enroll.setClassno(rs.getInt(1));
				enroll.setClassname(rs.getString(2));
				enroll.setTeachername(rs.getString(9));
				enroll.setTeacherid(rs.getString(3));
				enroll.setClassdescription(rs.getString(4));
				enroll.setPeriod(rs.getDate(5)+" ~ "+rs.getDate(6));
				enroll.setLimitstudent(rs.getInt(7));
				enroll.setCurrentstudent(rs.getInt(8));
				enroll.setStr(rs.getInt(8) + " / " + rs.getInt(7));
				list.add(enroll);
			}
			System.out.println("수강신청 전체 강좌 조회 성공");
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("수간신청 전체 강좌 조회 실패");
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	
		return list;
	}
	
	//한 가지만 조회
	public EnrollmentDto enrollment_selectOne(int classno) {
	
		//DB연결
		connectionJDBC();
		
		EnrollmentDto enroll = new EnrollmentDto();
		String sql = "select c.*, count(rc.student_id), u.user_name "
				   + "  from class c"
				   + "  left outer join request_class rc"
				   + "    on c.class_no=rc.class_no"
				   + "	left outer join user u"
			       + "	  on c.teacher_id = u.user_id"
				   + " where c.class_no = ?";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, classno);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				enroll.setClassno(rs.getInt(1));
				enroll.setClassname(rs.getString(2));
				enroll.setTeachername(rs.getString(9));
				enroll.setTeacherid(rs.getString(3));
				enroll.setClassdescription(rs.getString(4));
				enroll.setStartdate(rs.getDate(5));
				enroll.setEnddate(rs.getDate(6));
				enroll.setLimitstudent(rs.getInt(7));
				enroll.setCurrentstudent(rs.getInt(8));
				enroll.setStr(rs.getInt(8) + " / " + rs.getInt(7));
			}
			System.out.println("강좌 조회 성공");
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("강좌 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return enroll;
	}
	
	//현재 내 강좌 리스트 조회
	public ObservableList<EnrollmentDto> current_selectAll(String stu_id) {
	
		//DB연결
		connectionJDBC();
		
		ObservableList<EnrollmentDto> list = FXCollections.observableArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date currentTime = new Date();
		String current = sdf.format(currentTime);
		Date today = null;
		try {
			today = sdf.parse(current);
		} catch (ParseException e1) {}
		
		String sql = "select rc.class_no, c.class_name, c.start_date, c.end_date, u.user_name, c.limitstudent, count(t.task_no)"
					+"  from request_class rc"
					+" inner join class c"
					+"    on rc.class_no = c.class_no"
					+"	left outer join user u"
				    +"	  on c.teacher_id = u.user_id"
					+"  left outer join task t"
					+"   on c.class_no=t.class_no"
					+" where rc.student_id = ?"
					+"   and c.end_date >= curdate()"
					+" group by c.class_no"
					+" order by c.start_date asc, rc.class_no;";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EnrollmentDto enroll = new EnrollmentDto();					//DTO객체가 밖에 있는 경우 출력값이 통일됨
				enroll.setClassno(rs.getInt(1));							//강의번호
				enroll.setClassname(rs.getString(2));						//강의명
				enroll.setStartdate(rs.getDate(3));							//강의 시작일
				enroll.setEnddate(rs.getDate(4));							//강의 마지막일
				enroll.setPeriod(rs.getDate(3)+" ~ "+rs.getDate(4));		//강의 기간
				enroll.setTeachername(rs.getString(5));						//강사명
				enroll.setLimitstudent(rs.getInt(6));						//수강가능인원
				enroll.setTaskCount(rs.getInt(7));							//과제수
	
				//날짜비교
				int date_compare = today.compareTo(enroll.getStartdate());
				if(date_compare>=0) {
					
					enroll.setStatus("진행 중");
				} else {
					
					enroll.setStatus("수업 대기 중");
				}
				list.add(enroll);
			}
			System.out.println("현재 수강 중인 강좌 조회 성공");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("현재 수강 중인 강좌 조회 실패");
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}
	
	//지난 내 강좌 리스트 조회
	public ObservableList<EnrollmentDto> past_selectAll(String stu_id) {
	
		//DB연결
		connectionJDBC();
		
		ObservableList<EnrollmentDto> list = FXCollections.observableArrayList();
		String sql = "select rc.class_no, c.class_name, c.start_date, c.end_date, u.user_name, c.limitstudent, count(t.task_no)"
					+"  from request_class rc"
					+" inner join class c"
					+"    on rc.class_no = c.class_no"
					+" left outer join user u"
				    +"	  on c.teacher_id = u.user_id"
					+"  left outer join task t"
					+ "   on c.class_no=t.class_no"
					+" where rc.student_id = ?"
					+"   and c.end_date < curdate()"
					+" group by c.class_no;";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EnrollmentDto enroll = new EnrollmentDto();					//DTO객체가 밖에 있는 경우 출력값이 통일됨
				enroll.setClassno(rs.getInt(1));							//강의번호
				enroll.setClassname(rs.getString(2));						//강의명
				enroll.setStartdate(rs.getDate(3));							//강의 시작일
				enroll.setEnddate(rs.getDate(4));							//강의 마지막일
				enroll.setPeriod(rs.getDate(3)+" ~ "+rs.getDate(4));		//강의 기간
				enroll.setTeachername(rs.getString(5));						//강사명
				enroll.setLimitstudent(rs.getInt(6));						//수강가능인원
				enroll.setTaskCount(rs.getInt(7));							//과제수
				enroll.setStatus("강의 종료");
				
				list.add(enroll);
			}
			System.out.println("지난 강좌 조회 성공");
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("지난 강좌 조회 실패");
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}
	
	//검색(강의 또는 강사명)
	public ObservableList<EnrollmentDto> search_selectAll(String search_word){
	
		//DB연결
		connectionJDBC();
		
		ObservableList<EnrollmentDto> list = FXCollections.observableArrayList();
		String sql = "select c.*, count(rc.student_id),u.user_name "
				   + "  from class c"
				   + "  left outer join request_class rc"
				   + "    on c.class_no = rc.class_no"
				   + " left outer join user u"
				   + "	  on c.teacher_id = u.user_id"
				   + " where (end_date >= curdate())"
				   + "   and (c.class_name like ?"
				   + "    or u.user_name like ?)"
				   + " group by c.class_no"
				   + " order by c.start_date asc, c.end_date asc, c.class_no asc;";
		String like_word = "%"+search_word+"%";
	
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, like_word);
			pstmt.setString(2, like_word);
			rs = pstmt.executeQuery();
		
			while(rs.next()) {
				
				EnrollmentDto enroll = new EnrollmentDto();				//DTO객체가 밖에 있는 경우 출력값이 통일됨
				enroll.setClassno(rs.getInt(1));						//강의번호
				enroll.setClassname(rs.getString(2));					//강의명
				enroll.setTeachername(rs.getString(9));					//강사명
				enroll.setTeacherid(rs.getString(3));					//강사ID
				enroll.setClassdescription(rs.getString(4));			//수업설명
				enroll.setPeriod(rs.getDate(5)+" ~ "+rs.getDate(6));	//강의기간
				enroll.setLimitstudent(rs.getInt(7));					//수업인원
				enroll.setCurrentstudent(rs.getInt(8));
				enroll.setStr(rs.getInt(8) + " / " + rs.getInt(7));
				list.add(enroll);
			}
			System.out.println("수강신청 전체 강좌 조회 성공");
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("수간신청 전체 강좌 조회 실패");
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		
		return list;
	}
	
	//검색(강의 또는 강사명)_현재강의 페이지
	public ObservableList<EnrollmentDto> search_current_selectAll(String stu_id, String search_word){
	
		//DB연결
		connectionJDBC();
		
		ObservableList<EnrollmentDto> list = FXCollections.observableArrayList();
		String sql = "select rc.class_no, c.class_name, c.start_date, c.end_date, u.user_name, c.limitstudent, count(t.task_no)"
				   + "  from request_class rc"
				   + " inner join class c"
				   + "    on rc.class_no = c.class_no"
				   + " left outer join user u"
				   + "	  on c.teacher_id = u.user_id"
				   + "  left outer join task t"
				   + "   on c.class_no=t.class_no"
				   + " where (rc.student_id = ?"
				   + "   and c.end_date >= curdate())"
				   + "   and (c.class_name like ?"
				   + "    or u.user_name like ?)";
		String like_word = "%"+search_word+"%";
	
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			pstmt.setString(2, like_word);
			pstmt.setString(3, like_word);
			rs = pstmt.executeQuery();
		
			while(rs.next()) {
				
				EnrollmentDto enroll = new EnrollmentDto();					//DTO객체가 밖에 있는 경우 출력값이 통일됨
				enroll.setClassno(rs.getInt(1));							//강의번호
				enroll.setClassname(rs.getString(2));						//강의명
				enroll.setStartdate(rs.getDate(3));							//강의 시작일
				enroll.setEnddate(rs.getDate(4));							//강의 마지막일
				enroll.setPeriod(rs.getDate(3)+" ~ "+rs.getDate(4));			//강의 기간
				enroll.setTeachername(rs.getString(5));						//강사명
				enroll.setLimitstudent(rs.getInt(6));						//수강가능인원
				enroll.setTaskCount(rs.getInt(7));							//과제수
				enroll.setStatus("진행 중");
				
				list.add(enroll);
			}
			System.out.println("현재강좌 전체 조회 성공");
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("현재강좌 전체 조회 실패");
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		
		return list;
	}
	
	//검색(강의 또는 강사명)_지난강의 페이지
	public ObservableList<EnrollmentDto> search_past_selectAll(String stu_id, String search_word){
	
		//DB연결
		connectionJDBC();
		
		ObservableList<EnrollmentDto> list = FXCollections.observableArrayList();
		String sql = "select rc.class_no, c.class_name, c.start_date, c.end_date, u.user_name, c.limitstudent, count(t.task_no)"
				   + "  from request_class rc"
				   + " inner join class c"
				   + "    on rc.class_no = c.class_no"
				   + " left outer join user u"
				   + "	  on c.teacher_id = u.user_id"
				   + "  left outer join task t"
				   + "   on c.class_no=t.class_no"
				   + " where (rc.student_id = ?"
				   + "   and c.end_date < curdate())"
				   + "   and (c.class_name like ?"
				   + "    or u.user_name like ?)";
		String like_word = "%"+search_word+"%";
	
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			pstmt.setString(2, like_word);
			pstmt.setString(3, like_word);
			rs = pstmt.executeQuery();
		
			while(rs.next()) {
				
				EnrollmentDto enroll = new EnrollmentDto();					//DTO객체가 밖에 있는 경우 출력값이 통일됨
				enroll.setClassno(rs.getInt(1));							//강의번호
				enroll.setClassname(rs.getString(2));						//강의명
				enroll.setStartdate(rs.getDate(3));							//강의 시작일
				enroll.setEnddate(rs.getDate(4));							//강의 마지막일
				enroll.setPeriod(rs.getDate(3)+" ~ "+rs.getDate(4));			//강의 기간
				enroll.setTeachername(rs.getString(5));						//강사명
				enroll.setLimitstudent(rs.getInt(6));						//수강가능인원
				enroll.setTaskCount(rs.getInt(7));							//과제수
				enroll.setStatus("강의 종료");
				
				list.add(enroll);
			}
			System.out.println("지난강좌 전체 조회 성공");
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("지난강좌 전체 조회 실패");
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		
		return list;
	}
	
	//현재 수강인원 조회
	public void currentStudent_selectAll(int class_no) {
	
		//DB연결
		connectionJDBC();
		
		String sql = "select count(rc.student_id), c.limitstudent"
				   + "  from request_class rc"
				   + " inner join class c"
				   + "    on rc.class_no = c.class_no"
				   + " where rc.class_no=?;";
	
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, class_no);
			rs = pstmt.executeQuery();
		
			while(rs.next()) {
				
				EnrollmentDto enroll = new EnrollmentDto();					//DTO객체가 밖에 있는 경우 출력값이 통일됨
				enroll.setCurrentstudent(rs.getInt(1));						//현재수강신청인원
				enroll.setLimitstudent(rs.getInt(2));						//수강신청가능인원
//				enroll.setClassPeople(rs.getInt(1)+" / " + rs.getInt(2));
			}
			System.out.println("현재 수강인원 조회 성공");
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("현재 수강인원 조회 실패");
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}
	
	//수강신청 여부 확인
	public EnrollmentDto check_requestClass(int class_no, String stu_id) {
	
		//DB연결
		connectionJDBC();
		
		EnrollmentDto enroll = new EnrollmentDto();
		String sql = "select *"
				   + "  from request_class"
				   + " where class_no = ?"
				   + "   and student_id = ?";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, class_no);
			pstmt.setString(2, stu_id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				enroll.setCheck(true);
			}
			System.out.println("수강신청여부 조회 성공");
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("수강신청여부 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return enroll;
	}
	
	//수강신청 여부 확인
	public void delete_Enrollment(String stu_id, int class_no) {
	
		//DB연결
		connectionJDBC();
		
		String sql = "delete from request_class"
				   + " where class_no = ?"
				   + "   and student_id = ?";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, class_no);
			pstmt.setString(2, stu_id);
			pstmt.execute();
			
			System.out.println("수강취소 성공");
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("수강취소 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}
	
}