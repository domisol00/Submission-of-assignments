package DTO;
/*
 * 작성자 : 도현호
 */
import java.sql.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EnrollmentDto {
	
	private final IntegerProperty classno = new SimpleIntegerProperty();						//강의번호
	private final StringProperty classname = new SimpleStringProperty();						//강의명
	private final StringProperty teachername = new SimpleStringProperty();						//강사명
	private final StringProperty teacherid = new SimpleStringProperty();						//강사id
	private final StringProperty classdescription = new SimpleStringProperty();					//강의설명
	private final ObjectProperty<Date> startdate = new SimpleObjectProperty<Date>();			//강의시작일
	private final ObjectProperty<Date> enddate = new SimpleObjectProperty<Date>();				//강의종료일
	private final IntegerProperty currentstudent = new SimpleIntegerProperty();					//현재수강생수
	private final IntegerProperty limitstudent = new SimpleIntegerProperty();					//수강가능인원
	private final StringProperty status = new SimpleStringProperty(); 							//강의상태
	private final StringProperty period = new SimpleStringProperty();							//강의시작일~강의종요일
	private final StringProperty str = new SimpleStringProperty(); 								//수강신청인원/수강가능인원
	private final IntegerProperty taskCount = new SimpleIntegerProperty();						//과제 수
	private String stu_id;																		//학생아이디
	private boolean check = false;																//수강신청여부
	
	//강의번호
	public int getClassno() {
		
		return this.classno.get();
	}
	
	public void setClassno(int value) {
		
		this.classno.set(value);
	}
	
	public IntegerProperty classnoProperty() {
		
		return this.classno;
	}
	//강의명
	public String getClassname() {
		
		return this.classname.get();
	}
	
	public void setClassname(String value) {
		
		this.classname.set(value);
	}
	
	public StringProperty classnameProperty() {
		
		return this.classname;
	}
	//강사이름
	public String getTeachername() {
		
		return this.teachername.get();
	}
	
	public void setTeachername(String value) {
		
		this.teachername.set(value);
	}
	
	public StringProperty teachernameProperty() {
		
		return this.teachername;
	}
	//강사id
	public String getTeacherid() {
		
		return this.teacherid.get();
	}
	
	public void setTeacherid(String value) {
		
		this.teacherid.set(value);
	}
	
	public StringProperty teacheridProperty() {
		
		return this.teacherid;
	}
	//강의설명
	public String getClassdescription() {
		
		return this.classdescription.get();
	}
	
	public void setClassdescription(String value) {
		
		this.classdescription.set(value);
	}
	
	public StringProperty classdescriptionProperty() {
		
		return this.classdescription;
	}
	
	//강의시작일
	public Date getStartdate () {
		
		return this.startdate.get();
	}
	
	public void setStartdate (Date value) {
		
		this.startdate.set(value);
	}
	
	public ObjectProperty<Date> startdateProperty() {
		
		return this.startdate;
	}
	
	//강의종료일
	public Date getEnddate () {
		
		return this.enddate.get();
	}
	
	public void setEnddate (Date value) {
		
		this.enddate.set(value);
	}
	
	public ObjectProperty<Date> enddateProperty() {
		
		return this.enddate;
	}
	
	//강의기간
	public String getPeriod() {
		return this.period.get();
	}
	
	public void setPeriod(String value) {
		
		this.period.set(value);
	}

	public StringProperty periodProperty() {
		
		return this.period;
	}

	
	//수강신청인원
	public int getCurrentstudent() {
		
		return this.currentstudent.get();
	}
	
	public void setCurrentstudent(int value) {
		
		this.currentstudent.set(value);
	}
	
	public IntegerProperty CurrentProperty() {
		
		return this.currentstudent;
	}

	//수강신청가능 인원
	public int getLimitstudent() {
		
		return this.limitstudent.get();
	}
	
	public void setLimitstudent(int value) {
		
		this.limitstudent.set(value);
	}
	
	public IntegerProperty limitstudentProperty() {
		
		return this.limitstudent;
	}

	//강의상태
	public String getStatus() {
		
		return this.status.get();
	}
	
	public void setStatus(String value) {
		
		this.status.set(value);
	}
	
	public StringProperty statusProperty() {
		
		return this.status;
	}
	
	//강의기간
	public String getStr() {
		return this.str.get();
	}
	
	public void setStr(String value) {
		
		this.str.set(value);
	}

	public StringProperty strProperty() {
		
		return this.str;
	}
	
	//과제수
	public int getTaskCount() {
		
		return this.taskCount.get();
	}
	
	public void setTaskCount(int value) {
		
		this.taskCount.set(value);
	}
	
	public IntegerProperty taskCountProperty() {
		
		return this.taskCount;
	}
	

	//수강신청여부 체크
	public boolean getCheck() {
		
		return check;
	}
	
	public void setCheck(boolean check) {
		
		this.check = check;
	}
	//아이디
	public String getStu_id() {
		return stu_id;
	}

	public void setStu_id(String stu_id) {
		this.stu_id = stu_id;
	}
}