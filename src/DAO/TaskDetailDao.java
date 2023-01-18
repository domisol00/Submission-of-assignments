package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import DTO.TaskDetailDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.JdbcUtil;

/**
 * @author 김지현
 */

public class TaskDetailDao {

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
//				e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("드라이버 로딩 실패 : TaskDao");
		}
	}

	// 선택한 과제의 제출정보
	public ObservableList<TaskDetailDto> selectTaskStudentList(int taskNo) {
		// DB연결
		connectionJDBC();

		ObservableList<TaskDetailDto> list = FXCollections.observableArrayList();
		try {
			String sql = "select * from class_task_status where task_no = ?;";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, taskNo);

			ResultSet re = pstmt.executeQuery();

			int num = 0;
			while (re.next()) {
				TaskDetailDto taskDetail = new TaskDetailDto();

				// 정렬순서대로 번호부여
				taskDetail.setColNum(++num);

				// 과제의 점수 표기(득점/총점)
				Integer taskScore = (Integer) re.getObject("taskscore");
				Integer perfect_score = (Integer) re.getObject("perfect_score");

				// 총점이 없는경우
				String pStr = "";
				if (perfect_score != null) {
					pStr = " / " + perfect_score;
				}

				// taskScore가 null / -1이면 채점전
				if (taskScore == null || taskScore == -1) {
					taskDetail.setColScore("0" + pStr);
					taskDetail.setColMarkStatus("채점 전");
				} else {
					//taskScore가 있으면 채점후
					taskDetail.setColScore(taskScore + pStr);
					taskDetail.setColMarkStatus("채점 완료");
				}

				taskDetail.setTaskNum(re.getInt("task_no"));
				taskDetail.setTaskTitle(re.getString("task_name"));
				taskDetail.setStudentId(re.getString("student_id"));
				taskDetail.setStudentName(re.getString("student_name"));

				// 제출여부 표시
				taskDetail.setSubmitStatus(re.getString("tasksubmit"));
				if (re.getString("tasksubmit") == null) {
					taskDetail.setSubmitStatus("N");
				}

				taskDetail.setSubmitDate((Timestamp) re.getObject("tasksubmit_date"));
				taskDetail.setTaskScore(re.getInt("taskscore"));
				taskDetail.setQuestion(re.getString("taskquestion"));
				taskDetail.setAnswer(re.getString("taskanswer"));
				taskDetail.setFile(re.getBytes("taskfile"));
				taskDetail.setFilename(re.getString("taskfile_name"));
				taskDetail.setFullscore(re.getInt("perfect_score"));

				list.add(taskDetail);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return list;

	}

	// 점수 채점
	public void markTask(TaskDetailDto selectedDto) {
		// DB연결
		connectionJDBC();

		try {
			String sql = " update submission_task " + " set  taskscore=?, taskanswer=? "
					+ " where task_no = ? and student_id=?; ";

			pstmt = connection.prepareStatement(sql);

			pstmt.setInt(1, selectedDto.getTaskScore());
			pstmt.setString(2, selectedDto.getAnswer());
			pstmt.setInt(3, selectedDto.getTaskNum());
			pstmt.setString(4, selectedDto.getStudentId());

			pstmt.executeUpdate();
			System.out.println("**과제 채점 성공");

		} catch (Exception e) {
			System.out.println("**과제 채점 실패");
			e.printStackTrace();
		} finally {
			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}

	public TaskDetailDto selectTaskDetail(int selectedTaskNo, String selectedStudentId) {
		TaskDetailDto tdDto = new TaskDetailDto();

		// DB연결
		connectionJDBC();

		try {
			String sql = "select * from class_task_status where task_no = ? and student_id=?;";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, selectedTaskNo);
			pstmt.setString(2, selectedStudentId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				tdDto.setTaskNum(rs.getInt("task_no"));
				tdDto.setTaskTitle(rs.getString("task_name"));
				tdDto.setStudentId(rs.getString("student_id"));
				tdDto.setStudentName(rs.getString("student_name"));
				tdDto.setSubmitStatus(rs.getString("tasksubmit"));
				tdDto.setSubmitDate(rs.getTimestamp("tasksubmit_date"));
				tdDto.setTaskScore(rs.getInt("taskscore"));
				tdDto.setQuestion(rs.getString("taskquestion"));
				tdDto.setAnswer(rs.getString("taskanswer"));
				tdDto.setFile(rs.getBytes("taskfile"));
				tdDto.setFilename(rs.getString("taskfile_name"));
				tdDto.setFullscore(rs.getInt("perfect_score"));

			}
			System.out.println("**데이터  조회 성공");

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("**데이터 조회 실패");
		} finally {

			// 접속종료
			ju.disconnect(connection, pstmt, rs);
		}
		return tdDto;

	}

}