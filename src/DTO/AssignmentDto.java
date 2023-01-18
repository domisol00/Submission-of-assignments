package DTO;
/*
 * 작성자 : 도현호
 */
import java.io.InputStream;
import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AssignmentDto {
	
	private final IntegerProperty taskList_no = new SimpleIntegerProperty();				//리스트상 출력할 번호
	private final StringProperty submitornot = new SimpleStringProperty();					//제출여부
	private final IntegerProperty MyScore = new SimpleIntegerProperty();					//내점수 
	private final BooleanProperty CheckTask = new SimpleBooleanProperty();					//내 점수가 -1이면 False, 0이상이면 True
	private final StringProperty Score = new SimpleStringProperty();						//내 점수 / 만점형태로 담을 값
	private final IntegerProperty task_no = new SimpleIntegerProperty();					//과제번호
	private final StringProperty task_name = new SimpleStringProperty();					//과제명
	private final StringProperty task_desc = new SimpleStringProperty();					//과제설명
	private final ObjectProperty<Date> reg_date = new SimpleObjectProperty<Date>();			//과제 제출일
	private final ObjectProperty<Date> expire_date = new SimpleObjectProperty<Date>();		//과제 만료일
	private final ObjectProperty<InputStream> attachedFile = new SimpleObjectProperty<InputStream>();		//첨부파일
	private final StringProperty attachedFile_name = new SimpleStringProperty();			//첨부파일명
	private final IntegerProperty class_no = new SimpleIntegerProperty();					//강의번호
	private final StringProperty class_name = new SimpleStringProperty();					//강의명
	private final ObjectProperty<Date> start_date = new SimpleObjectProperty<Date>();		//강의 시작일
	private final ObjectProperty<Date> end_date = new SimpleObjectProperty<Date>();			//강의 종료일
	private final IntegerProperty perfect_score = new SimpleIntegerProperty();				//만점
	private final StringProperty taskQuestion = new SimpleStringProperty();					//문의사항
	private final StringProperty taskAnswer = new SimpleStringProperty();					//답변
	private final ObjectProperty<byte[]> taskFile = new SimpleObjectProperty<byte[]>();		//과제 제출 파일
	private final StringProperty taskFile_name = new SimpleStringProperty();				//과제 제출 파일명
	private final IntegerProperty sum_myScore =  new SimpleIntegerProperty();				//과제 획득 총 점수
	private final IntegerProperty sum_perfeectScore = new SimpleIntegerProperty();			//과제 총 점수
	private final IntegerProperty cnt_myAssign =  new SimpleIntegerProperty();				//과제 획득 총 점수
	private final IntegerProperty cnt_totalAssign =  new SimpleIntegerProperty();			//과제 획득 총 점수
	

	//리스트상 번호 출력
	public void setTaskList_no(int value) {
		
		this.taskList_no.set(value);
	}
	
	public int getTaskList_no() {
		
		return this.taskList_no.get();
	}
	
	public IntegerProperty taskList_no_Property() {
		
		return this.taskList_no;
	}

	//리스트상 과제 제출여부 표시
	public void setSubmitornot(String value) {
		
		this.submitornot.set(value);
	}
	
	public String getSubmitornot() {
		
		return this.submitornot.get();
	}
	
	public StringProperty submitornot_Property() {
		
		return this.submitornot;
	}

	//리스트상 점수/총점 순으로 표시
	public void setScore(String value) {
		
		this.Score.set(value);
	}
	
	public String getScore() {
		
		return this.Score.get();
	}
	
	public StringProperty Score_Property() {
		
		return this.Score;
	}
	
	//내 점수
	public void setMyScore(int value) {
		
		this.MyScore.set(value);
	}
	
	public int getMyScore() {
		
		return this.MyScore.get();
	}
	
	public IntegerProperty MyScore_Property() {
		
		return this.MyScore;
	}

	
	//과제번호
	public void setTask_no(int value) {
		
		this.task_no.set(value);
	}
	
	public int getTask_no() {
		
		return this.task_no.get();
	}
	
	public IntegerProperty task_no_Property() {
		
		return this.class_no;
	}

	//과제명
	public void setTask_name(String value) {
		
		this.task_name.set(value);
	}
	
	public String getTask_name() {
		
		return this.task_name.get();
	}
	
	public StringProperty task_name_Property() {
		
		return this.task_desc;
	}

	//과제설명
	public void setTask_desc(String value) {
		
		this.task_desc.set(value);
	}
	
	public String getTask_desc() {
		
		return this.task_desc.get();
	}
	
	public StringProperty task_desc_Property() {
		
		return this.task_desc_Property();
	}

	//과제제출일
	public void setReg_date(Date value) {
		
		this.reg_date.set(value);
	}
	
	public Date getReg_date() {
		
		return this.reg_date.get();
	}
	
	public ObjectProperty<Date> reg_date_Property(){
		
		return this.reg_date_Property();
	}

	//과제제출 마감일
	public void setExpire_date(Date value) {
		
		this.expire_date.set(value);
	}
	
	public Date getExpire_date() {
		
		return this.expire_date.get();
	}
	
	public ObjectProperty<Date> expire_date_Property(){
		
		return this.expire_date;
	}
	
	//과제 참고파일
	public void setAttachedFile(InputStream value) {
		
		this.attachedFile.set(value);
	}
	
	public InputStream getAttachedFile() {
	
		return this.attachedFile.get();
	}
	
	public ObjectProperty<InputStream>attachedFile_Property() {
		
		return this.attachedFile;
	}

	//과제 참고파일명
	public void setAttachedFile_name(String value) {
		
		this.attachedFile_name.set(value);
	}
	
	public String getAttachedFile_name() {
		
		return this.attachedFile_name.get();
	}
	
	public StringProperty attachedFile_name_Property() {
		
		return this.attachedFile_name;
	}

	//강의번호
	public void setClass_no(int value) {
		
		this.class_no.set(value);
	}
	
	public int getClass_no() {
		
		return this.class_no.get();
	}
	
	public IntegerProperty class_no_Property() {
		
		return this.class_no;
	}

	//강의명
	public void setClass_name(String value) {
		
		this.class_name.set(value);
	}
	
	public String getClass_name() {
		
		return this.class_name.get();
	}
	
	public StringProperty class_name_Property() {
		
		return this.class_name;
	}

	//과제 만점
	public void setPerfect_score(int value) {
		
		this.perfect_score.set(value); 
	}
	
	public int getPerfect_score() {
		
		return this.perfect_score.get();
	}
	
	public IntegerProperty perfect_score_Property() {
		
		return this.perfect_score;
	}
	
	//과제 질문
	public void setTaskQuestion(String value) {
		
		this.taskQuestion.set(value);
	}

	public String getTaskQuestion() {
		
		return this.taskQuestion.get();
	}
	
	public StringProperty taskQuestion_Property() {
		
		return this.taskQuestion;
	}
	
	//과제 질문 답변
	public void setTaskAnswer(String value) {
		
		this.taskAnswer.set(value);
	}

	public String getTaskAnswer() {
		
		return this.taskAnswer.get();
	}
	
	public StringProperty taskAnswer_Property() {
		
		return this.taskAnswer;
	}

	//과제 제출 파일
	public void setTaskFile(byte[] value) {
		
		this.taskFile.set(value);
	}
	
	public byte[] getTaskFile() {
	
		return this.taskFile.get();
	}
	
	public ObjectProperty<byte[]>taskFile_Property() {
		
		return this.taskFile;
	}
	
	//과제 제출 파일명
	public void setTaskFile_name(String value) {
		
		this.taskFile_name.set(value);
	}

	public String getTaskFile_name() {
		
		return this.taskFile_name.get();
	}
	
	public StringProperty taskFile_Name_Property() {
		
		return this.taskFile_name;
	}

	//과제 획득 총 점수
	public void setSumMyScore(int value) {
		
		this.sum_myScore.set(value);
	}
	
	public int getSumMyScore() {
		
		return this.sum_myScore.get();
	}
	
	public IntegerProperty sumMyScore_Property() {
		
		return this.sum_myScore;
	}
	
	//과제 총 점수
	public void setSumPerfectScore(int value) {
		
		this.sum_perfeectScore.set(value);
	}
	
	public int getSumPerfectScore() {
		
		return this.sum_perfeectScore.get();
	}
	
	public IntegerProperty sumPerfectScore_Property() {
	
		return this.sum_perfeectScore;
	}
	
	//제출 과제 수
	public void setCntMyAssign(int value) {
		
		this.cnt_myAssign.set(value);
	}
	
	public int getCntMyAssign() {
		
		return this.cnt_myAssign.get();
	}
	
	public IntegerProperty cntMyAssign_Property() {
		
		return this.cnt_myAssign;
	}
	
	//총 과제 수
	public void setCntTotalAssign(int value) {
		
		this.cnt_totalAssign.set(value);
	}
	
	public int getCntTotalAssign() {
		
		return this.cnt_totalAssign.get();
	}
	
	public IntegerProperty cntTotalAssign_Property() {
		
		return this.cnt_totalAssign;
	}
	
	//선생님이 점수 부여 여부
	public void setCheckTask(Boolean value) {
		
		this.CheckTask.set(value);
	}
	
	public boolean getCheckTask() {
		
		return this.CheckTask.get();
	}
	
	public BooleanProperty checkTaskProperty() {
		
		return this.CheckTask;
	}
	
	//강의 시작일
	public void setStart_date(Date value) {
		
		this.start_date.set(value);
	}
	
	public Date getStart_date() {
		
		return this.start_date.get();
	}
	
	public ObjectProperty<Date> start_date_Property(){
		
		return this.start_date;
	}
	
	//강의 종료일
	public void setEnd_date(Date value) {
		
		this.end_date.set(value);
	}
	
	public Date getEnd_date() {
		
		return this.end_date.get();
	}
	
	public ObjectProperty<Date> end_date_Property(){
		
		return this.end_date;
	}

}