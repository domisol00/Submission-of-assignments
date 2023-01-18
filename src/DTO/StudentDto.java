package DTO;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author 김지현
 *
 */
public class StudentDto {

	private SimpleIntegerProperty stNo;
	private SimpleStringProperty stTitle;
	private SimpleStringProperty stDesc;
	private SimpleObjectProperty<LocalDate> stRegdate;
	private SimpleObjectProperty<LocalDate> stExpireDate;
	private ObjectProperty<byte[]> attachedFile;
	private SimpleStringProperty stFile;
	private SimpleObjectProperty<Integer> perfectScore;
	private SimpleBooleanProperty dateCheck;
	private SimpleIntegerProperty classNo;

	public StudentDto() {
		this.stNo = new SimpleIntegerProperty();
		this.stTitle = new SimpleStringProperty();
		this.stDesc = new SimpleStringProperty();
		this.stRegdate = new SimpleObjectProperty<LocalDate>();
		this.stExpireDate = new SimpleObjectProperty<LocalDate>();
		this.attachedFile = new SimpleObjectProperty<byte[]>();
		this.stFile = new SimpleStringProperty();
		this.perfectScore = new SimpleObjectProperty<Integer>();
		this.dateCheck = new SimpleBooleanProperty();
		this.classNo = new SimpleIntegerProperty();
	}

	public StudentDto(Integer tcNo, String tcTitle, String tcDesc, LocalDate tcRegdate, LocalDate tcExpireDate,
			byte[] attachedFile, String tcFile, Integer perfectScore) {
		this.stNo = new SimpleIntegerProperty(tcNo);
		this.stTitle = new SimpleStringProperty(tcTitle);
		this.stDesc = new SimpleStringProperty(tcDesc);
		this.stRegdate = new SimpleObjectProperty<LocalDate>(tcRegdate);
		this.stExpireDate = new SimpleObjectProperty<LocalDate>(tcExpireDate);
		this.attachedFile = new SimpleObjectProperty<byte[]>(attachedFile);
		this.stFile = new SimpleStringProperty(tcFile);
		this.perfectScore = new SimpleObjectProperty<Integer>(perfectScore);
	}

	public StudentDto(Integer tcNo, String tcTitle, String tcDesc, LocalDate tcRegdate, LocalDate tcExpireDate,
			String tcFile, Integer perfectScore) {
		super();
		this.stNo = new SimpleIntegerProperty(tcNo);
		this.stTitle = new SimpleStringProperty(tcTitle);
		this.stDesc = new SimpleStringProperty(tcDesc);
		this.stRegdate = new SimpleObjectProperty<LocalDate>(tcRegdate);
		this.stExpireDate = new SimpleObjectProperty<LocalDate>(tcExpireDate);
		this.stFile = new SimpleStringProperty(tcFile);
		this.perfectScore = new SimpleObjectProperty<Integer>(perfectScore);
	}

	public StudentDto(String tcTitle, String tcDesc, LocalDate tcRegdate, LocalDate tcExpireDate,
			byte[] attachedFile, String tcFile, Integer perfectScore) {
		super();
		this.stTitle = new SimpleStringProperty(tcTitle);
		this.stDesc = new SimpleStringProperty(tcDesc);
		this.stRegdate = new SimpleObjectProperty<LocalDate>(tcRegdate);
		this.stExpireDate = new SimpleObjectProperty<LocalDate>(tcExpireDate);
		this.attachedFile = new SimpleObjectProperty<byte[]>(attachedFile);
		this.stFile = new SimpleStringProperty(tcFile);
		this.perfectScore = new SimpleObjectProperty<Integer>(perfectScore);
	}

	

	public String getStTitle() {
		return stTitle.get();
	}
	public void setStTitle(String stTitle) {
		this.stTitle.set(stTitle);
	}
	public Integer getStNo() {
		return stNo.get();
	}
	public void setStNo(Integer stNo) {
		this.stNo.set(stNo);
	}
	public String getStDesc() {
		return stDesc.get();
	}
	public void setStDesc(String stDesc) {
		this.stDesc.set(stDesc);
	}
	
	public String getStFile() {
		return stDesc.get();
	}
	public void setStFile(String stFile) {
		this.stFile.set(stFile);
	}
	
	

	@Override
	public String toString() {
		String str = "task : " + stNo + " / " + stTitle + " / " + stDesc + " / " + stRegdate + " / " + stExpireDate
				+ " / " + stFile + " / " + perfectScore + " / " + dateCheck;
		return str;
	}
}