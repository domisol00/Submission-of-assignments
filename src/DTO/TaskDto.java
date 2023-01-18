package DTO;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author 김지현
 *
 */
public class TaskDto {

	private SimpleIntegerProperty tcNo;
	private SimpleStringProperty tcTitle;
	private SimpleStringProperty tcDesc;
	private SimpleObjectProperty<LocalDate> tcRegdate;
	private SimpleObjectProperty<LocalDate> tcExpireDate;
	private ObjectProperty<byte[]> attachedFile;
	private SimpleStringProperty tcFile;
	private SimpleObjectProperty<Integer> perfectScore;
	private SimpleBooleanProperty dateCheck;
	private SimpleIntegerProperty classNo;

	public TaskDto() {
		this.tcNo = new SimpleIntegerProperty();
		this.tcTitle = new SimpleStringProperty();
		this.tcDesc = new SimpleStringProperty();
		this.tcRegdate = new SimpleObjectProperty<LocalDate>();
		this.tcExpireDate = new SimpleObjectProperty<LocalDate>();
		this.attachedFile = new SimpleObjectProperty<byte[]>();
		this.tcFile = new SimpleStringProperty();
		this.perfectScore = new SimpleObjectProperty<Integer>();
		this.dateCheck = new SimpleBooleanProperty();
		this.classNo = new SimpleIntegerProperty();
	}

	public TaskDto(Integer tcNo, String tcTitle, String tcDesc, LocalDate tcRegdate, LocalDate tcExpireDate,
			byte[] attachedFile, String tcFile, Integer perfectScore) {
		this.tcNo = new SimpleIntegerProperty(tcNo);
		this.tcTitle = new SimpleStringProperty(tcTitle);
		this.tcDesc = new SimpleStringProperty(tcDesc);
		this.tcRegdate = new SimpleObjectProperty<LocalDate>(tcRegdate);
		this.tcExpireDate = new SimpleObjectProperty<LocalDate>(tcExpireDate);
		this.attachedFile = new SimpleObjectProperty<byte[]>(attachedFile);
		this.tcFile = new SimpleStringProperty(tcFile);
		this.perfectScore = new SimpleObjectProperty<Integer>(perfectScore);
	}

	public TaskDto(Integer tcNo, String tcTitle, String tcDesc, LocalDate tcRegdate, LocalDate tcExpireDate,
			String tcFile, Integer perfectScore) {
		super();
		this.tcNo = new SimpleIntegerProperty(tcNo);
		this.tcTitle = new SimpleStringProperty(tcTitle);
		this.tcDesc = new SimpleStringProperty(tcDesc);
		this.tcRegdate = new SimpleObjectProperty<LocalDate>(tcRegdate);
		this.tcExpireDate = new SimpleObjectProperty<LocalDate>(tcExpireDate);
		this.tcFile = new SimpleStringProperty(tcFile);
		this.perfectScore = new SimpleObjectProperty<Integer>(perfectScore);
	}

	public TaskDto(String tcTitle, String tcDesc, LocalDate tcRegdate, LocalDate tcExpireDate,
			byte[] attachedFile, String tcFile, Integer perfectScore) {
		super();
		this.tcTitle = new SimpleStringProperty(tcTitle);
		this.tcDesc = new SimpleStringProperty(tcDesc);
		this.tcRegdate = new SimpleObjectProperty<LocalDate>(tcRegdate);
		this.tcExpireDate = new SimpleObjectProperty<LocalDate>(tcExpireDate);
		this.attachedFile = new SimpleObjectProperty<byte[]>(attachedFile);
		this.tcFile = new SimpleStringProperty(tcFile);
		this.perfectScore = new SimpleObjectProperty<Integer>(perfectScore);
	}

	// getter
	public Integer getTcNo() {
		return tcNo.get();
	}

	public String getTcTitle() {
		return tcTitle.get();
	}

	public String getTcDesc() {
		return tcDesc.get();
	}

	public LocalDate getTcRegdate() {
		return tcRegdate.get();
	}

	public LocalDate getTcExpireDate() {
		return tcExpireDate.get();
	}

	public String getTcFile() {
		return tcFile.get();
	}

	public Integer getPerfectScore() {
		return perfectScore.get();
	}

	public boolean getDateCheck() {
		return dateCheck.get();
	}

	public byte[] getAttachedFile() {
		return attachedFile.get();
	}
	
	public Integer getClassNo() {
		return classNo.get();
	}

	// setter
	public void setTcNo(Integer tcNo) {
		this.tcNo.set(tcNo);
	}

	public void setTcTitle(String tcTitle) {
		this.tcTitle.set(tcTitle);
	}

	public void setTcDesc(String tcDesc) {
		this.tcDesc.set(tcDesc);
	}

	public void setTcRegdate(LocalDate tcRegdate) {
		this.tcRegdate.set(tcRegdate);
	}

	public void setTcExpireDate(LocalDate tcExpireDate) {
		this.tcExpireDate.set(tcExpireDate);
	}

	public void setTcFile(String tcFile) {
		this.tcFile.set(tcFile);
	}

	public void setPerfectScore(Integer perfectScore) {
		this.perfectScore.set(perfectScore);
	}

	public void setDateCheck(boolean dateCheck) {
		this.dateCheck.set(dateCheck);
	}

	public void setAttachedFile(byte[] attachedFile) {
		this.attachedFile.set(attachedFile);
	}
	
	public void setClassNo(Integer classNo) {
		this.classNo.set(classNo);
	}

	// get property
	public SimpleIntegerProperty getTcNoProperty() {
		return tcNo;
	}

	public SimpleStringProperty tcTitleProperty() {
		return tcTitle;
	}

	public SimpleStringProperty tcDescProperty() {
		return tcDesc;
	}

	public SimpleObjectProperty<LocalDate> tcRegdateProperty() {
		return tcRegdate;
	}

	public SimpleObjectProperty<LocalDate> tcExpireDateProperty() {
		return tcExpireDate;
	}

	public SimpleStringProperty tcFileProperty() {
		return tcFile;
	}

	public SimpleObjectProperty<Integer> perfectScoreProperty() {
		return perfectScore;
	}

	public SimpleBooleanProperty dateCheckProperty() {
		return dateCheck;
	}

	public ObjectProperty<byte[]> attachedFileProperty() {
		return attachedFile;
	}

	@Override
	public String toString() {
		String str = "task : " + tcNo + " / " + tcTitle + " / " + tcDesc + " / " + tcRegdate + " / " + tcExpireDate
				+ " / " + tcFile + " / " + perfectScore + " / " + dateCheck;
		return str;
	}
}