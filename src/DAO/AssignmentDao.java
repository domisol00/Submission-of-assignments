package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DTO.AssignmentDto;
import DTO.AssignmentPopupDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.JdbcUtil;

public class AssignmentDao {
	
	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private JdbcUtil ju;

	//DB연결
	private void connectionJDBC() {
		
		try {
			
			ju = new JdbcUtil();
			connection = ju.getConnection();
			System.out.println("드라이버 로딩 성공 : AssignmentDao");
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("드라이버 로딩 실패 : AssignmentDao");
		}
	}

	//과제 제출
	public void submit_Assignment(AssignmentPopupDto sTask) throws SQLException{
	
		//DB연결
		connectionJDBC();
	
		try {
			//강의번호, 과제번호, 학생ID, 과제제출여부, 과제제출일시, 문의사항, 과제파일, 과제파일명
			String sql = "insert into submission_task("
					+ "class_no,task_no, student_id, tasksubmit, tasksubmit_date,"
					+ "taskquestion, taskfile, taskfile_name)"
					+ "values (?, ?, ?, 'Y' ,sysdate(),? ,?, ?);";
			pstmt = connection.prepareStatement(sql);

			pstmt.setInt(1, sTask.getClass_no());
			pstmt.setInt(2, sTask.getTask_no());
			pstmt.setString(3, sTask.getStudent_id());
			pstmt.setString(4, sTask.getTaskQuestion());
			pstmt.setBytes(5, sTask.getTaskFile());
			pstmt.setString(6, sTask.getTaskFile_name());
			pstmt.executeUpdate();
			System.out.println("과제 제출 성공");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}
	
	//과제 재 제출
	public void resubmit_Assignment(AssignmentPopupDto sTask) throws SQLException{
	
		//DB연결
		connectionJDBC();

		try {
			//과제제출일시, 문의사항, 과제파일, 과제파일명, 과제번호, 학생ID
			String sql = "update submission_task"
							+ " set tasksubmit_date = sysdate(),"
							+ " taskquestion = ?, "
							+ " taskfile = ?, "
							+ " taskfile_name=? "
							+ " where class_no = ? and task_no = ? and student_id = ?;";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, sTask.getTaskQuestion());
			pstmt.setBytes(2, sTask.getTaskFile());
			pstmt.setString(3, sTask.getTaskFile_name());
			pstmt.setInt(4, sTask.getClass_no());
			pstmt.setInt(5, sTask.getTask_no());
			pstmt.setString(6, sTask.getStudent_id());
			pstmt.executeUpdate();
			System.out.println("과제 제출 성공");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}
	
	//전체과제 탭 - 전체과제 조회
	public ObservableList<AssignmentDto> assignment_selectAll_Full(String stu_id){
		
		//DB연결
		connectionJDBC();
	
		ObservableList<AssignmentDto> list = FXCollections.observableArrayList();
		String sql = "select task_no, task_name, tasksubmit, tasksubmit_date, expire_date, taskscore, perfect_score, class_name, class_no, start_date,end_date"
				   + "  from class_submission_status" 
				   + " where student_id = ?;";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			rs = pstmt.executeQuery();
			int i = 0;										//연번을 나타내기 위한 변수
		
			while(rs.next()) {
				i++;
				AssignmentDto assign = new AssignmentDto();			//DTO객체가 밖에 있는 경우 출력값이 통일됨
				//제출여부 표시(null이거나 N 이면 N표시, 아니면 Y표시)
				String yorn = "";
				if(rs.getString(3)==null ||rs.getString(3).equals("N")) {
					yorn = "N";
				} else {
					yorn = "Y";
				}
				
				assign.setTaskList_no(i);
				assign.setTask_no(rs.getInt(1));
				assign.setTask_name(rs.getString(2));
				assign.setSubmitornot(yorn);
				assign.setReg_date(rs.getDate(4));
				assign.setExpire_date(rs.getDate(5));
				if(rs.getInt(6)>=0) {									//내 점수를 확인해서 -1이면 점수책정여부(CheckTask값) false, 0이상이면 true
					assign.setScore(rs.getInt(6)+" / "+rs.getInt(7));	//그리고 -1이면 점수를 0점으로 변환하여 화면에 출력
				} else {
					assign.setScore(0+" / "+rs.getInt(7));
				}
				assign.setClass_name(rs.getString(8));
				assign.setClass_no(rs.getInt(9));
				assign.setStart_date(rs.getDate(10));
				assign.setEnd_date(rs.getDate(11));
				list.add(assign);
			}
			System.out.println("전체 과제 조회 성공");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("전체 과제 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}
	
	//전체과제탭 - 강의별 과제 조회
	public ObservableList<AssignmentDto> assignment_selectAll_Full(int class_no, String stu_id){
		
		//DB연결
		connectionJDBC();
	
		ObservableList<AssignmentDto> list = FXCollections.observableArrayList();
		String sql = "select task_no, task_name, tasksubmit, tasksubmit_date, expire_date, taskscore, perfect_score, class_name, class_no, start_date, end_date"
				   + "  from class_submission_status"
				   + " where (class_no = ?)"
				   + "   and student_id = ?;";
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, class_no);
			pstmt.setString(2, stu_id);
			rs = pstmt.executeQuery();
			int i = 0;												//연번을 나타내기 위한 변수
		
			while(rs.next()) {
				i++;
				AssignmentDto assign = new AssignmentDto();			//DTO객체가 밖에 있는 경우 출력값이 통일됨
				//제출여부 표시(null이거나 N 이면 N표시, 아니면 Y표시)
				String yorn = "";
				if(rs.getString(3)==null ||rs.getString(3).equals("N")) {
					yorn = "N";
				} else {
					yorn = "Y";
				}
				
				assign.setTaskList_no(i);
				assign.setTask_no(rs.getInt(1));
				assign.setTask_name(rs.getString(2));
				assign.setSubmitornot(yorn);
				assign.setReg_date(rs.getDate(4));
				assign.setExpire_date(rs.getDate(5));
				if(rs.getInt(6)>=0) {								//내 점수를 확인해서 -1이면 점수책정여부(CheckTask값) false, 0이상이면 true
					assign.setScore(rs.getInt(6)+" / "+rs.getInt(7));//그리고 -1이면 점수를 0점으로 변환하여 화면에 출력
				} else {
					assign.setScore(0+" / "+rs.getInt(7));
				}
				assign.setClass_name(rs.getString(8));
				assign.setClass_no(rs.getInt(9));
				assign.setStart_date(rs.getDate(10));
				assign.setEnd_date(rs.getDate(11));
				list.add(assign);
			}
			System.out.println("전체 과제 조회 성공");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("전체 과제 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}
	
	//진행중 과제 탭 - 전체과제
	public ObservableList<AssignmentDto> assignment_selectAll(String stu_id){
		
		//DB연결
		connectionJDBC();
	
		ObservableList<AssignmentDto> list = FXCollections.observableArrayList();
		String sql = "select task_no, task_name, tasksubmit, tasksubmit_date, expire_date, taskscore, perfect_score, class_name, class_no, start_date, end_date"
				   + "  from class_submission_status" 
				   + " where student_id = ?"
				   + "   and (expire_date >= curdate())"
				   + "   and (start_date < curdate()"
				   + "   and end_date > curdate())"
				   + " order by expire_date, class_name, task_name;";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			rs = pstmt.executeQuery();
			int i = 0;										//연번을 나타내기 위한 변수
		
			while(rs.next()) {
				i++;
				AssignmentDto assign = new AssignmentDto();			//DTO객체가 밖에 있는 경우 출력값이 통일됨
				//제출여부 표시(null이거나 N 이면 N표시, 아니면 Y표시)
				String yorn = "";
				if(rs.getString(3)==null ||rs.getString(3).equals("N")) {
					yorn = "N";
				} else {
					yorn = "Y";
				}
				
				assign.setTaskList_no(i);
				assign.setTask_no(rs.getInt(1));
				assign.setTask_name(rs.getString(2));
				assign.setSubmitornot(yorn);
				assign.setReg_date(rs.getDate(4));
				assign.setExpire_date(rs.getDate(5));
				if(rs.getInt(6)>=0) {								//내 점수를 확인해서 -1이면 점수책정여부(CheckTask값) false, 0이상이면 true
					assign.setScore(rs.getInt(6)+" / "+rs.getInt(7));//그리고 -1이면 점수를 0점으로 변환하여 화면에 출력
				} else {
					assign.setScore(0+" / "+rs.getInt(7));
				}
				assign.setClass_name(rs.getString(8));
				assign.setClass_no(rs.getInt(9));
				list.add(assign);
			}
			System.out.println("전체 과제 조회 성공");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("전체 과제 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}
	
	//진행 중 과제 탭 - 강의별
	public ObservableList<AssignmentDto> assignment_selectAll(int class_no, String stu_id){
		
		//DB연결
		connectionJDBC();
	
		ObservableList<AssignmentDto> list = FXCollections.observableArrayList();
		String sql = "select task_no, task_name, tasksubmit, tasksubmit_date, expire_date, taskscore, perfect_score, class_name, class_no"
				   + "  from class_submission_status"
				   + " where (class_no = ?)"
				   + "   and student_id = ?"
				   + "   and (expire_date >= curdate())"
				   + "   and (start_date < curdate()"
				   + "   and end_date > curdate())"
				   + " order by expire_date, task_name;";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, class_no);
			pstmt.setString(2, stu_id);
			rs = pstmt.executeQuery();
			int i = 0;										//연번을 나타내기 위한 변수
		
			while(rs.next()) {
				i++;
				AssignmentDto assign = new AssignmentDto();			//DTO객체가 밖에 있는 경우 출력값이 통일됨
				//제출여부 표시(null이거나 N 이면 N표시, 아니면 Y표시)
				String yorn = "";
				if(rs.getString(3)==null ||rs.getString(3).equals("N")) {
					yorn = "N";
				} else {
					yorn = "Y";
				}
				
				assign.setTaskList_no(i);
				assign.setTask_no(rs.getInt(1));
				assign.setTask_name(rs.getString(2));
				assign.setSubmitornot(yorn);
				assign.setReg_date(rs.getDate(4));
				assign.setExpire_date(rs.getDate(5));
				if(rs.getInt(6)>=0) {								//내 점수를 확인해서 -1이면 점수책정여부(CheckTask값) false, 0이상이면 true
					assign.setScore(rs.getInt(6)+" / "+rs.getInt(7));//그리고 -1이면 점수를 0점으로 변환하여 화면에 출력
				} else {
					assign.setScore(0+" / "+rs.getInt(7));
				}
				assign.setClass_name(rs.getString(8));
				assign.setClass_no(rs.getInt(9));
				list.add(assign);
			}
			System.out.println("전체 과제 조회 성공");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("전체 과제 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}
	
	
	
	//제출한 적이 있는 학생 데이터 조회
	public AssignmentDto assignment_selectOne(int task_no, String stu_id) {
	
		//DB연결
		connectionJDBC();
		
		AssignmentDto assign = new AssignmentDto();
		String sql = "select task_no, task_name, task_desc, taskscore, perfect_score, expire_date,"
				   + "       attachedfile, attachedfile_name, taskquestion, taskanswer, taskfile, taskfile_name, start_date, end_date"
				   + "  from class_submission_status"
				   + " where task_no = ?"
				   + "   and student_id = ?;";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, task_no);
			pstmt.setString(2, stu_id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				assign.setTask_name(rs.getString(2));					// 과제명
				assign.setTask_desc(rs.getString(3));					// 과제설명

				if(rs.getInt(4)>=0) {									//내 점수를 확인해서 -1이면 점수책정여부(CheckTask값) false, 0이상이면 true
					assign.setCheckTask(true);							//그리고 -1이면 점수를 0점으로 변환하여 화면에 출력
					assign.setMyScore(rs.getInt(4));
				} else {
					assign.setCheckTask(false);
					assign.setMyScore(0);
				}
				assign.setPerfect_score(rs.getInt(5));					// 만점
				assign.setScore(rs.getInt(4)+" / "+rs.getInt(5));		// 과제 점수 / 만점
				assign.setExpire_date(rs.getDate(6));					// 제출기한
				assign.setAttachedFile(rs.getBinaryStream(7));			// 과제 참고 파일		-- 추후 7로 변경해야됨
				assign.setAttachedFile_name(rs.getString(8));			// 과제 참고 파일명		-- 추후 8로 변경해야됨
				assign.setTaskQuestion(rs.getString(9));				// 문의사항
				assign.setTaskAnswer(rs.getString(10));					// 답변
				assign.setTaskFile(rs.getBytes(11));					// 과제제출파일
				assign.setTaskFile_name(rs.getString(12));				// 과제제출파일명
				assign.setStart_date(rs.getDate(13));					// 강의시작일
				assign.setEnd_date(rs.getDate(14));						// 강의종료일
			}
			System.out.println("과제 조회 성공");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("과제 조회 실패");
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return assign;
	}

	//제출한적 없는 학생이 과제 조회할 때
	public AssignmentDto assignment_selectOne(int class_no, int task_no) {
	
		//DB연결
		connectionJDBC();
		
		AssignmentDto assign = new AssignmentDto();
		ResultSet rs = null;
		String sql = "select task_no, task_name, task_desc, taskscore, perfect_score, expire_date,"
				   + "       attachedfile, attachedfile_name, taskanswer, taskfile, taskfile_name, start_date, end_date"
				   + "  from class_submission_status"
				   + " where task_no = ?"
				   + "   and class_no = ?;";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, task_no);
			pstmt.setInt(2, class_no);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				assign.setTask_name(rs.getString(2));					// 과제명
				assign.setTask_desc(rs.getString(3));					// 과제설명
				if(rs.getInt(4)==-1||rs.getObject(4)==null){			//내 점수를 확인해서 -1이면 점수책정여부(CheckTask값) false, 0이상이면 true
					System.out.println("aaaa"+rs.getInt(4));			//그리고 -1이면 점수를 0점으로 변환하여 화면에 출력
					assign.setCheckTask(false);
					assign.setMyScore(0);
				} else if(rs.getInt(4)>=0) {							
					System.out.println("aaaa"+rs.getInt(4));
					assign.setCheckTask(true);							
					assign.setMyScore(rs.getInt(4));
				} 
				assign.setPerfect_score(rs.getInt(5));					// 만점
				assign.setScore(rs.getInt(4)+" / "+rs.getInt(5));		// 과제 점수 / 만점
				assign.setExpire_date(rs.getDate(6));					// 제출기한
				assign.setAttachedFile(rs.getBinaryStream(7));			// 과제 참고 파일
				assign.setAttachedFile_name(rs.getString(8));			// 과제 참고 파일명
				assign.setTaskQuestion(rs.getString(9));				// 문의사항
				assign.setTaskAnswer(rs.getString(10));					// 답변
				assign.setTaskFile(rs.getBytes(11));					// 과제제출
				assign.setStart_date(rs.getDate(12));					// 강의시작일
				assign.setEnd_date(rs.getDate(13));						// 강의종료일
			}
			System.out.println("과제 조회 성공");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("과제 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return assign;
	}

	//과제 점수 조회
	public AssignmentDto score_select(int class_no, String stu_id) {
		
		//DB연결
		connectionJDBC();
		
		AssignmentDto assign = new AssignmentDto();
		String sql = "select sum(taskscore), (select sum(perfect_Score) from task where class_no = ?)"
						+ " from submission_task"
					    + " where class_no = ?"
					    + " and student_id = ?;";
			
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, class_no);
			pstmt.setInt(2, class_no);
			pstmt.setString(3, stu_id);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
	
				//내 점수가 -1이면 0으로 표시, 아니면 해당점수로 표시
				if(rs.getInt(1)>=0) {
					
					assign.setSumMyScore(rs.getInt(1));
				} else {
					
					assign.setSumMyScore(0);
				}
				assign.setSumPerfectScore(rs.getInt(2));
				System.out.println("점수합계 조회 완료");
			}
			
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("점수합계 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return assign;
	}
	
	//전체과제 탭, 진행중 과제 탭 - 과제 제출 갯수 조회 - 전체
	public int myCount_ing_total_select(String stu_id) {
		
		//DB연결
		connectionJDBC();
		int cnt = 0;
		String sql = "select count(*)"
				   + "  from class_submission_status"
				   + " where student_id = ?"
				   + "   and (start_date < curdate()"
				   + "   and end_date > curdate()"
				   + "   and expire_date >= curdate());";
			
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				cnt = rs.getInt(1);
				System.out.println("과제 개수 조회 완료");
			}
			
		} catch (SQLException e) {
//			e.printStackTrace();
				System.out.println("과제 개수 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return cnt;
	}
	
	//전체과제 탭 - 과제 제출 갯수 조회 - 강의별
	public AssignmentDto myCount_select(int class_no, String stu_id) {
		
		//DB연결
		connectionJDBC();
		AssignmentDto assign = new AssignmentDto();
		String sql = "select count(*), (select count(*) from task where class_no = ?)"
						+ " from submission_task"
					    + " where class_no = ?"
					    + " and student_id = ?"
					    + " and tasksubmit = 'Y';";
			
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, class_no);
			pstmt.setInt(2, class_no);
			pstmt.setString(3, stu_id);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				assign.setCntMyAssign(rs.getInt(1));
				assign.setCntTotalAssign(rs.getInt(2));
				System.out.println("과제 개수 조회 완료");
			}
			
		} catch (SQLException e) {
//			e.printStackTrace();
				System.out.println("과제 개수 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return assign;
	}
	
	//진행 중 과제 탭 - 제출 과제 수 조회 - 전체
	public int myCount_ing_submit_select(String stu_id) {
		
		//DB연결
		connectionJDBC();
		int cnt = 0;
		String sql = "select count(*)"
				   + "  from class_submission_status"
				   + " where student_id = ?"
				   + "   and tasksubmit = 'Y'"
				   + "   and (start_date < curdate()"
				   + "   and end_date > curdate()"
				   + "   and expire_date >= curdate());";
			
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				cnt = rs.getInt(1);
				System.out.println("진행 과제 중 제출한 과제 수 조회 완료");
			}
			
		} catch (SQLException e) {
//			e.printStackTrace();
				System.out.println("진행 과제 중 제출한 과제 수 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return cnt;
	}
	
	//진행 중 과제 탭 - 제출 과제 수 조회 - 강의별
	public int myCount_ing_submit_select(String stu_id, int class_no) {
		
		//DB연결
		connectionJDBC();
		int cnt = 0;
		String sql = "select count(*)"
				   + "  from class_submission_status"
				   + " where student_id = ?"
				   + "   and class_no = ?"
				   + "   and tasksubmit = 'Y'"
				   + "   and (start_date < curdate()"
				   + "   and end_date > curdate()"
				   + "   and expire_date >= curdate());";
			
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			pstmt.setInt(2, class_no);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				cnt = rs.getInt(1);
				System.out.println("진행 과제 중 제출한 과제 수 조회 완료");
			}
			
		} catch (SQLException e) {
//			e.printStackTrace();
				System.out.println("진행 과제 중 제출한 과제 수 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return cnt;
	}
	
	//진행 중 과제 탭 - 전체 과제 수 조회 - 강의별
	public int myCount_ing_class_select(String stu_id, int class_no) {
		
		System.out.println(stu_id);
		System.out.println(class_no);
		//DB연결
		connectionJDBC();
		int cnt = 0;
		String sql = "select count(*)"
				   + "  from class_submission_status"
				   + " where student_id = ?"
				   + "   and class_no = ?"
				   + "   and (start_date < curdate()"
				   + "   and end_date > curdate()"
				   + "   and expire_date >= curdate());";
			
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			pstmt.setInt(2, class_no);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				
				cnt = rs.getInt(1);
				System.out.println("과제 개수 조회 완료");
			}
			
		} catch (SQLException e) {
//			e.printStackTrace();
				System.out.println("과제 개수 조회 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return cnt;
	}
	
	//콤보박스 과제명 출력-현재
	public ObservableList<String> current_className_selectAll(String stu_id) {
	
		//DB연결
		connectionJDBC();
		
		ObservableList<String> list = FXCollections.observableArrayList();
		String sql = "select c.class_no, c.class_name"
					+"  from request_class rc"
					+" inner join class c"
					+"    on rc.class_no = c.class_no"
					+" where rc.student_id = ?"
					+"   and c.end_date >= curdate();";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			rs = pstmt.executeQuery();
		
			int class_No = 0;
			String class_Name = null;
			String class_Info = null;
			while(rs.next()) {
				
				class_No = rs.getInt(1);							//강의번호
				class_Name = rs.getString(2);						//강의명
				class_Info = "[" + class_No + "] " + class_Name;	//강의정보
				
				list.add(class_Info);
			}
			System.out.println("현재 과제 조회 성공");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("현재 과제 조회 실패");
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}
	
	//콤보박스 과제명 출력-전체
	public ObservableList<String> full_className_selectAll(String stu_id) {
	
		//DB연결
		connectionJDBC();
		
		ObservableList<String> list = FXCollections.observableArrayList();
		String sql = "select c.class_no, c.class_name"
					+"  from request_class rc"
					+" inner join class c"
					+"    on rc.class_no = c.class_no"
					+" where rc.student_id = ?;";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			rs = pstmt.executeQuery();
		
			int class_No = 0;
			String class_Name = null;
			String class_Info = null;
			while(rs.next()) {
				
				class_No = rs.getInt(1);							//강의번호
				class_Name = rs.getString(2);						//강의명
				class_Info = "[" + class_No + "] " + class_Name;	//강의정보
				
				list.add(class_Info);
			}
			System.out.println("현재 과제 조회 성공");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("현재 과제 조회 실패");
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}

	//강의 날짜 조회
	public AssignmentDto select_classDate(String stu_id) {
	
		//DB연결
		connectionJDBC();
	
		AssignmentDto assign = new AssignmentDto();
		String sql = "select c.start_date, c.end_date"
					+"  from request_class rc"
					+" inner join class c"
					+"    on rc.class_no = c.class_no"
					+" where rc.student_id = ?;";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, stu_id);
			rs = pstmt.executeQuery();
		
			while(rs.next()) {
				
				assign.setStart_date(rs.getDate(1));
				assign.setEnd_date(rs.getDate(2));
				
			}
			System.out.println("강의 날짜 조회 성공");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("강의 날짜 조회 실패");
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	
		return assign;
	}
	
	//첨부파일 삭제
	public void delete_file(String student_id, int class_no, int task_no) {
		
		//DB연결
		connectionJDBC();
		
		String sql = "update submission_task" + 
					 "   set taskfile = null, taskfile_name = null" + 
					 " where student_id = ?" + 
					 "   and class_no = ?" + 
					 "   and task_no = ?;";
		
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, student_id);
			pstmt.setInt(2, class_no);
			pstmt.setInt(3, task_no);
			pstmt.executeUpdate();		
			System.out.println("첨부파일 삭제 성공");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("첨부파일 삭제 실패");
		} finally {
			
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}
}