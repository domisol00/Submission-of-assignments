package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import DTO.TaskDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.JdbcUtil;

/**
 * @author 김지현
 */
public class TaskDao {

	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private JdbcUtil ju;

	// DB연결
	private void connectionJDBC() {

		try {

			ju = new JdbcUtil();
			connection = ju.getConnection();
			System.out.println("드라이버 로딩 성공 : TaskDao");
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("드라이버 로딩 실패 : TaskDao");
		}
	}

	// 과제 등록
	public void insertTask(TaskDto taskDto, int classno) {

		// DB연결
		connectionJDBC();
		try {
			// insert into task values (null, ?, ?, ?, ?, ?, ?, ?, ?);
			String sql = "insert into task values (null, ?, ?, ?, ?, ?, ?, ?, ?);"; // 수정필요
			pstmt = connection.prepareStatement(sql);

			pstmt.setString(1, taskDto.getTcTitle());
			pstmt.setString(2, taskDto.getTcDesc());
			pstmt.setString(3, taskDto.getTcRegdate().toString());

			if (taskDto.getTcExpireDate() == null) {
				pstmt.setString(4, null);
			} else {
				pstmt.setString(4, taskDto.getTcExpireDate().toString());
			}

			pstmt.setBytes(5, taskDto.getAttachedFile());
			pstmt.setString(6, taskDto.getTcFile());
			pstmt.setInt(7, classno);
			pstmt.setObject(8, taskDto.getPerfectScore(), Types.INTEGER);
			pstmt.executeUpdate();

			System.out.println("**과제 저장 성공");

		} catch (Exception e) {
			System.out.println("**과제 저장 실패");
			e.printStackTrace();

		} finally {

			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}

	// 과제 수정
	public void updateReport(TaskDto task) {
		// DB연결
		connectionJDBC();

		try {
			String sql = "update task set task_name=?, task_desc=?, expire_date=?, attachedfile=?, attachedfile_name=?, perfect_Score=?	where task_no=?;";

			pstmt = connection.prepareStatement(sql);

			pstmt.setString(1, task.getTcTitle());
			pstmt.setString(2, task.getTcDesc());
			pstmt.setString(3, task.getTcExpireDate().toString());
			pstmt.setBytes(4, task.getAttachedFile());
			pstmt.setString(5, task.getTcFile());
			pstmt.setObject(6, task.getPerfectScore(), Types.INTEGER);
			pstmt.setInt(7, task.getTcNo());

			pstmt.executeUpdate();
			System.out.println("**과제 수정 성공");

		} catch (Exception e) {
			System.out.println("**과제 수정 실패");
			e.printStackTrace();
		} finally {

			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}

	// 과제 하나 조회
	public TaskDto selectTask(int tNo) {
		// DB연결
		connectionJDBC();

		TaskDto task = new TaskDto();
		try {
			String sql = "select * from task where task_no= ?;";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, tNo);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				task.setTcNo(rs.getInt("task_no"));
				task.setTcTitle(rs.getString("task_name"));
				task.setTcDesc(rs.getString("task_desc"));
				task.setTcRegdate(LocalDate.parse(rs.getString("reg_date"), DateTimeFormatter.ISO_DATE));
//				report.setTcExpireDate(LocalDate.parse(rs.getString("expireDate"), DateTimeFormatter.ISO_DATE));

				if (rs.getString("expire_Date") == null) {
					System.out.println("selectList-expireDate-null");
				} else {
					task.setTcExpireDate(LocalDate.parse(rs.getString("expire_date"), DateTimeFormatter.ISO_DATE));
				}
				task.setTcFile(rs.getString("attachedFile_name"));
				task.setPerfectScore((Integer) rs.getObject("perfect_score"));
				task.setClassNo(rs.getInt("class_no"));

			}
			System.out.println("**데이터  조회 성공");

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("**데이터 조회 실패");
		} finally {

			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return task;
	};

	// 과제 전체 조회
	public ObservableList<TaskDto> selectTaskList() {

		// DB연결
		connectionJDBC();

		ObservableList<TaskDto> list = FXCollections.observableArrayList();
		try {
			String sql = "select * from task;";
			pstmt = connection.prepareStatement(sql);

			ResultSet re = pstmt.executeQuery();

			while (re.next()) {
				TaskDto task = new TaskDto();

				task.setTcNo(re.getInt("task_no"));
				task.setTcTitle(re.getString("task_name"));
				task.setTcDesc(re.getString("task_desc"));
				task.setTcRegdate(LocalDate.parse(re.getString("reg_date"), DateTimeFormatter.ISO_DATE));
//				report.setTcExpireDate(LocalDate.parse(re.getString("expireDate"), DateTimeFormatter.ISO_DATE));

				if (re.getString("expire_date") == null) {
					System.out.println("selectList-expireDate-null");
				} else {
					task.setTcExpireDate(LocalDate.parse(re.getString("expire_date"), DateTimeFormatter.ISO_DATE));
				}

				task.setTcFile(re.getString("attachedFile_name"));
				task.setPerfectScore(re.getInt("perfect_score"));

				list.add(task);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}

	// user의 task만 보여준다
	public ObservableList<TaskDto> selectUserTaskList(String userid, int subBtnNo) {

		// DB연결
		connectionJDBC();

		ObservableList<TaskDto> list = FXCollections.observableArrayList();
		try {
			String sql = "select * from task t join class c on t.class_no = c.class_no where c.teacher_id=?";

			if (subBtnNo == 1) { // 현재 과제
				// c.teacher_id=123 and end_date >= sysdate();
				System.out.println("현재과제");
				sql += " and t.expire_date >= curdate();";
			} else if (subBtnNo == 2) { // 지난 과제
				System.out.println("지난과제");
				sql += " and t.expire_date < curdate();;";
			}

			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, userid);

			ResultSet re = pstmt.executeQuery();

			while (re.next()) {
				TaskDto task = new TaskDto();

				task.setTcNo(re.getInt("task_no"));
				task.setTcTitle(re.getString("task_name"));
				task.setTcDesc(re.getString("task_desc"));
				task.setTcRegdate(LocalDate.parse(re.getString("reg_date"), DateTimeFormatter.ISO_DATE));

				if (re.getString("expire_date") == null) {
					System.out.println("selectList-expireDate-null");
				} else {
					task.setTcExpireDate(LocalDate.parse(re.getString("expire_date"), DateTimeFormatter.ISO_DATE));
				}

				task.setTcFile(re.getString("attachedFile_name"));
				task.setPerfectScore((Integer) re.getObject("perfect_score"));
				task.setClassNo(re.getInt("class_no"));

				list.add(task);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {

			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}

	// 과제 삭제
	public void deleteTask(int rNo) {

		// DB연결
		connectionJDBC();

		String sql = "delete from task where task_no = ?;";
		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, rNo);
			pstmt.executeUpdate();
			System.out.println("**데이터 삭제 성공");
		} catch (SQLException e) {
			System.out.println("**데이터 삭제 실패");
			e.printStackTrace();
		} finally {
			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}

	// user의 과제 목록
	public ObservableList<String> selectUserTaskComoboList(int classno) {

		// DB연결
		connectionJDBC();
		ObservableList<String> list = FXCollections.observableArrayList();

		try {
			String sql = "select * from task where class_no=?;";

			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, classno);
			rs = pstmt.executeQuery();

			int taskNo = 0;
			String taskName = null;

			while (rs.next()) {
				taskNo = rs.getInt("task_no");
				taskName = rs.getString("task_name");

				String classInfo = "[" + taskNo + "] " + taskName;
				list.add(classInfo);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {

			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}

	// user의 강의 목록
	public ObservableList<String> selectUserClassList(String userid) {

		// DB연결
		connectionJDBC();
		ObservableList<String> list = FXCollections.observableArrayList();

		try {
			String sql = "select class_no, class_name from class where teacher_id=?;";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();

			int classNo = 0;
			String className = null;
			while (rs.next()) {
				classNo = rs.getInt("class_No");
				className = rs.getString("class_name");

				String classInfo = "[" + classNo + "] " + className;
				list.add(classInfo);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {

			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}

	// user의 현재 강의 목록
	public ObservableList<String> selectUserCurrentClassList(String userid) {

		// DB연결
		connectionJDBC();
		ObservableList<String> list = FXCollections.observableArrayList();

		try {
			String sql = "select class_no, class_name from class where teacher_id=? and end_date >= sysdate();";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();

			int classNo = 0;
			String className = null;
			while (rs.next()) {
				classNo = rs.getInt("class_No");
				className = rs.getString("class_name");

				String classInfo = "[" + classNo + "] " + className;
				list.add(classInfo);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {

			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;
	}

	// user - 선택한 강의 - 정보
	public ObservableList<TaskDto> selectUserClassTaskList(String userid, int classNo, int subBtnNo) {
		// DB연결
		connectionJDBC();

		ObservableList<TaskDto> list = FXCollections.observableArrayList();
		try {
//			select t.* from task t join class c on t.class_no = c.class_no where c.teacher_id=123 and c.class_no=1010;
			String sql = "select t.* from task t join class c on t.class_no = c.class_no where c.teacher_id=? and c.class_no=?";

			if (subBtnNo == 1) { // 현재 과제
				// select * from task t join class c on t.class_no = c.class_no where
				// c.teacher_id=123   AND t.expire_date >= curdate();
				sql += " and expire_date >= curdate();";
			} else if (subBtnNo == 2) { // 지난 과제
				sql += " and expire_date < curdate();";
			}

			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, userid);
			pstmt.setInt(2, classNo);

			ResultSet re = pstmt.executeQuery();

			while (re.next()) {
				TaskDto task = new TaskDto();

				task.setTcNo(re.getInt("task_no"));
				task.setTcTitle(re.getString("task_name"));
				task.setTcDesc(re.getString("task_desc"));
				task.setTcRegdate(LocalDate.parse(re.getString("reg_date"), DateTimeFormatter.ISO_DATE));

				if (re.getString("expire_date") == null) {
					System.out.println("selectList-expireDate-null");
				} else {
					task.setTcExpireDate(LocalDate.parse(re.getString("expire_date"), DateTimeFormatter.ISO_DATE));
				}

				task.setTcFile(re.getString("attachedFile_name"));
				task.setPerfectScore((Integer) re.getObject("perfect_score"));

				list.add(task);
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