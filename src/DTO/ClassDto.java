/* 작성자 : 이성원
 * 설명 : 강의클래스(클래스명으로 Class가 사용불가라 1을붙임)
 */

package DTO;

import java.time.LocalDate;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class ClassDto {

	private SimpleIntegerProperty classNo;
	private SimpleStringProperty className;
	private SimpleStringProperty teacherName;
	private SimpleStringProperty teacherId;
	private SimpleStringProperty classDescription;
	private SimpleObjectProperty<LocalDate> startDate;
	private SimpleObjectProperty<LocalDate> endDate;
	private SimpleIntegerProperty limitStudent;
	private SimpleIntegerProperty currentStudent;
	private SimpleBooleanProperty dateCheck;
	private SimpleStringProperty str;
	
	public ClassDto() {
		this.classNo = new SimpleIntegerProperty();
		this.className = new SimpleStringProperty();
		this.teacherName = new SimpleStringProperty();
		this.teacherId = new SimpleStringProperty();
		this.classDescription = new SimpleStringProperty();
		this.startDate = new SimpleObjectProperty<LocalDate>();
		this.endDate = new SimpleObjectProperty<LocalDate>();
		this.limitStudent = new SimpleIntegerProperty();
		this.currentStudent = new SimpleIntegerProperty();
		this.dateCheck = new SimpleBooleanProperty();
		this.str = new SimpleStringProperty();
	}

//	public ClassDto(int ClassNo, String className, String teacherName, String teacherId, String classDescription,
//			LocalDate startDate, LocalDate endDate, int limitStudent) {
//		this.classNo = new SimpleIntegerProperty(ClassNo);
//		this.className = new SimpleStringProperty(className);
//		this.teacherName = new SimpleStringProperty(teacherName);
//		this.teacherId = new SimpleStringProperty(teacherId);
//		this.classDescription = new SimpleStringProperty(classDescription);
//		this.startDate = new SimpleObjectProperty<LocalDate>(startDate);
//		this.endDate = new SimpleObjectProperty<LocalDate>(endDate);
//		this.limitStudent = new SimpleIntegerProperty(limitStudent);
//	}

	public ClassDto(String className, String teacherId, String classDescription, LocalDate startDate,
			LocalDate endDate, int limitStudent) {
		this.className = new SimpleStringProperty(className);
		this.teacherId = new SimpleStringProperty(teacherId);
		this.classDescription = new SimpleStringProperty(classDescription);
		this.startDate = new SimpleObjectProperty<LocalDate>(startDate);
		this.endDate = new SimpleObjectProperty<LocalDate>(endDate);
		this.limitStudent = new SimpleIntegerProperty(limitStudent);
	}

	public int getClassNo() {
		return classNo.getValue();
	}

	public void setClassNo(int classNo) {
		this.classNo.set(classNo);
	}

	public String getClassName() {
		return className.get();
	}

	public void setClassName(String className) {
		this.className.set(className);
	}

	public String getTeacherName() {
		return teacherName.get();
	}

	public void setTeacherName(String teacherName) {
		this.teacherName.set(teacherName);
	}

	public String getTeacherId() {
		return teacherId.get();
	}

	public void setTeacherId(String teacherId) {
		this.teacherId.set(teacherId);
	}

	public String getClassDescription() {
		return classDescription.get();
	}

	public void setClassDescription(String classDescription) {
		this.classDescription.set(classDescription);
	}

	public LocalDate getStartDate() {
		return startDate.get();
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate.set(startDate);
	}

	public LocalDate getEndDate() {
		return endDate.get();
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate.set(endDate);
	}

	public int getLimitStudent() {
		return limitStudent.getValue();
	}

	public void setLimitStudent(int limitStudent) {
		this.limitStudent.set(limitStudent);
	}
	
	public int getCurrentStudent() {
		return currentStudent.getValue();
	}

	public void setCurrentStudent(int currentStudent) {
		this.currentStudent.set(currentStudent);
	}

	public boolean getDateCheck() {
		return dateCheck.get();
	}

	public void setDateCheck(boolean dateCheck) {
		this.dateCheck.set(dateCheck);
	}
	
	public String getStr() {
		return str.get();
	}

	public void setStr(String str) {
		this.str.set(str);
	}

	public SimpleIntegerProperty getClassNoProperty() {
		return classNo;
	}

	public SimpleStringProperty getClassNameProperty() {
		return className;
	}

	public SimpleStringProperty getTeacherNameProperty() {
		return teacherName;
	}

	public SimpleStringProperty getTeacherIdProperty() {
		return teacherId;
	}

	public SimpleStringProperty getClassDescriptionProperty() {
		return classDescription;
	}

	public SimpleObjectProperty<LocalDate> getStartDateProperty() {
		return startDate;
	}

	public SimpleObjectProperty<LocalDate> getEndDateProperty() {
		return endDate;
	}

	public SimpleIntegerProperty getLimitStudentProperty() {
		return limitStudent;
	}

	public SimpleBooleanProperty dateCheckProperty() {
		return dateCheck;
	}

}
