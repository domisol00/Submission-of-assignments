package DTO;

import java.sql.Timestamp;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class TaskDetailDto {

	private SimpleIntegerProperty colNum;
	private SimpleStringProperty colScore;
	private SimpleStringProperty colMarkStatus;
	private SimpleIntegerProperty taskNum;
	private SimpleStringProperty taskTitle;
	private SimpleStringProperty studentId;
	private SimpleStringProperty studentName;
	private SimpleStringProperty submitStatus;
	private SimpleObjectProperty<Timestamp> submitDate;
	private SimpleIntegerProperty taskScore;
	private SimpleStringProperty question;
	private SimpleStringProperty answer;
	private SimpleObjectProperty<byte[]> file;
	private SimpleStringProperty filename;
	private SimpleIntegerProperty fullscore;
	private SimpleBooleanProperty btnDetail;

	public TaskDetailDto() {
		super();
		this.colNum = new SimpleIntegerProperty();
		this.colScore = new SimpleStringProperty();
		this.colMarkStatus = new SimpleStringProperty();
		this.taskNum = new SimpleIntegerProperty();
		this.taskTitle = new SimpleStringProperty();
		this.studentId = new SimpleStringProperty();
		this.studentName = new SimpleStringProperty();
		this.submitStatus = new SimpleStringProperty();
		this.submitDate = new SimpleObjectProperty<Timestamp>();
		this.taskScore = new SimpleIntegerProperty();
		this.question = new SimpleStringProperty();
		this.answer = new SimpleStringProperty();
		this.file = new SimpleObjectProperty<byte[]>();
		this.filename = new SimpleStringProperty();
		this.fullscore = new SimpleIntegerProperty();
		this.btnDetail = new SimpleBooleanProperty();
	}

	public TaskDetailDto(Integer colNum, String colScore, String colMarkStatus, Integer taskNum, String taskTitle,
			String studentId, String studentName, String submitStatus, Timestamp submitDate, Integer taskScore,
			String question, String answer, byte[] file, String filename, Integer fullscore, Boolean btnDetail) {
		super();
		this.colNum = new SimpleIntegerProperty(colNum);
		this.colScore = new SimpleStringProperty(colScore);
		this.colMarkStatus = new SimpleStringProperty(colMarkStatus);
		this.taskNum = new SimpleIntegerProperty(taskNum);
		this.taskTitle = new SimpleStringProperty(taskTitle);
		this.studentId = new SimpleStringProperty(studentId);
		this.studentName = new SimpleStringProperty(studentName);
		this.submitStatus = new SimpleStringProperty(submitStatus);
		this.submitDate = new SimpleObjectProperty<Timestamp>(submitDate);
		this.taskScore = new SimpleIntegerProperty(taskScore);
		this.question = new SimpleStringProperty(question);
		this.answer = new SimpleStringProperty(answer);
		this.file = new SimpleObjectProperty<byte[]>(file);
		this.filename = new SimpleStringProperty(filename);
		this.fullscore = new SimpleIntegerProperty(fullscore);
		this.btnDetail = new SimpleBooleanProperty(btnDetail);
	}

	// getters
	public Integer getColNum() {
		return colNum.get();
	}

	public String getColScore() {
		return colScore.get();
	}

	public String getColMarkStatus() {
		return colMarkStatus.get();
	}

	public Integer getTaskNum() {
		return taskNum.get();
	}

	public String getTaskTitle() {
		return taskTitle.get();
	}

	public String getStudentId() {
		return studentId.get();
	}

	public String getStudentName() {
		return studentName.get();
	}

	public String getSubmitStatus() {
		return submitStatus.get();
	}

	public Timestamp getSubmitDate() {
		return submitDate.get();
	}

	public Integer getTaskScore() {
		return taskScore.get();
	}

	public String getQuestion() {
		return question.get();
	}

	public String getAnswer() {
		return answer.get();
	}

	public byte[] getFile() {
		return file.get();
	}

	public String getFilename() {
		return filename.get();
	}

	public Integer getFullscore() {
		return fullscore.get();
	}

	public Boolean getBtnDetail() {
		return btnDetail.get();
	}
	
	// setters
	public void setColNum(Integer colNum) {
		this.colNum.set(colNum);
	}

	public void setColScore(String colScore) {
		this.colScore.set(colScore);
	}

	public void setColMarkStatus(String colMark) {
		this.colMarkStatus.set(colMark);
	}

	public void setTaskNum(Integer taskNum) {
		this.taskNum.set(taskNum);
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle.set(taskTitle);
	}

	public void setStudentId(String studentId) {
		this.studentId.set(studentId);
	}

	public void setStudentName(String studentName) {
		this.studentName.set(studentName);
	}

	public void setSubmitStatus(String submitStatus) {
		this.submitStatus.set(submitStatus);
	}

	public void setSubmitDate(Timestamp submitDate) {
		this.submitDate.set(submitDate);
	}

	public void setTaskScore(Integer taskScore) {
		this.taskScore.set(taskScore);
	}

	public void setQuestion(String question) {
		this.question.set(question);
	}

	public void setAnswer(String answer) {
		this.answer.set(answer);
	}

	public void setFile(byte[] file) {
		this.file.set(file);
	}

	public void setFilename(String filename) {
		this.filename.set(filename);
	}

	public void setFullscore(Integer fullscore) {
		this.fullscore.set(fullscore);
	}

	public void setBtnDetail(Boolean btnDetail) {
		this.btnDetail.set(btnDetail);
	}
}