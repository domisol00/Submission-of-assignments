package DTO;
/*
 * 작성자 : 도현호
 */
import java.util.Date;

public class AssignmentPopupDto {

	private int class_no;
	private int task_no;				//과제 번호
	private String student_id;			//학생 ID
	private String taskSubmit;			//제출여부
	private Date taskSubmit_date;		//제출날짜
	private String taskQuestion;		//문의사항
	private byte[] taskFile;			//첨부파일
	private String taskFile_name;
	
	public AssignmentPopupDto(int class_no, int task_no, String student_id,  String taskQuestion,
			byte[] taskFile, String taskFile_name) {
		this.class_no = class_no;
		this.student_id = student_id;
		this.task_no = task_no;
		this.taskQuestion = taskQuestion;
		this.taskFile = taskFile;
		this.taskFile_name = taskFile_name;
	}

	public int getClass_no() {
	
		return this.class_no;
	}

	public void setClass_no(int class_no) {
	
		this.class_no = class_no;
	}

	public int getTask_no() {
	
		return this.task_no;
	}

	public void setTask_no(int task_no) {
	
		this.task_no = task_no;
	}

	public String getStudent_id() {
	
		return this.student_id;
	}

	public void setStudent_id(String student_id) {
	
		this.student_id = student_id;
	}

	public String getTaskSubmit() {
	
		return this.taskSubmit;
	}

	public void setTaskSubmit(String taskSubmit) {
	
		this.taskSubmit = taskSubmit;
	}

	public Date getTaskSubmit_date() {
	
		return this.taskSubmit_date;
	}

	public void setTaskSubmit_date(Date taskSubmit_date) {
	
		this.taskSubmit_date = taskSubmit_date;
	}

	public String getTaskQuestion() {
	
		return this.taskQuestion;
	}

	public void setTaskQuestion(String taskQuestion) {
	
		this.taskQuestion = taskQuestion;
	}

	public byte[] getTaskFile() {
	
		return this.taskFile;
	}

	public void setTaskFile(byte[] taskFile) {
	
		this.taskFile = taskFile;
	}

	public String getTaskFile_name() {
	
		return this.taskFile_name;
	}

	public void setTaskFile_name(String taskFile_name) {
	
		this.taskFile_name = taskFile_name;
	}
}
