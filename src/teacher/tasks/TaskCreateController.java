package teacher.tasks;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

import DAO.ClassDao;
import DAO.TaskDao;
import DTO.ClassDto;
import DTO.TaskDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import launch.AppMain;
import util.Util;

/**
 * @author 김지현
 *
 */
public class TaskCreateController implements Initializable {
	@FXML
	private TextField txtFile;

	@FXML
	private Button btnFileDel;

	@FXML
	private Button btnDateDel;

	@FXML
	private DatePicker dateExpire;

	@FXML
	private Label txtClassName;

	@FXML
	private TextArea txtDesc;

	@FXML
	private Button btnFile;

	@FXML
	private Button btnSubmit;

	@FXML
	private Button btnCancle;
	@FXML
	private TextField txtTitle;

	@FXML
	private TextField txtPerfectScore;

	@FXML
	private ComboBox<String> comboClassName;

	@FXML
	private Label lblDescCount;

	@FXML
	private Label lblTitleCount;
	
	@FXML
	private Label lblFileSize;

	ClassDto currentClass;
	byte[] arr;
	TaskListController tlc = new TaskListController();

	ObservableList<String> classList = FXCollections.observableArrayList();

	public TaskCreateController(ClassDto currentClass) {
		this.currentClass = currentClass;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// 강의명 표시
		setClassName();

		// 콤보박스 클릭 시 강의선택
		comboClassName.setOnAction(e -> handleCombobox());

		// 글자 수 표시
		Util.textLengthLimit(txtTitle, lblTitleCount, 20);
		Util.textLengthLimit(txtDesc, lblDescCount, 5000);

		// 점수필드 - 숫자만 입력할 수 있도록 제한
		txtPerfectScore.textProperty().addListener(Util.numberOnlyListener(txtPerfectScore));
		
		// 점수필드 - 글자수 제한
		txtPerfectScore.textProperty().addListener(Util.textCountLimit(txtPerfectScore, 9));
		
		// 첨부파일 버튼
		btnFile.setOnAction(e -> handleAttachedFileBtn());

		// 등록버튼
		btnSubmit.setOnAction(e -> handleSubmitBtn());

		// 날짜 삭제버튼
		btnDateDel.setOnAction(e -> dateExpire.setValue(null));

		// 첨부파일 삭제 버튼
		btnFileDel.setOnAction(e -> {
			arr = null;
			txtFile.clear();
			lblFileSize.setText(null);
		});
		//취소버튼
		btnCancle.setOnAction(e ->{
			Stage stage = (Stage)btnCancle.getScene().getWindow();
			stage.close();
		});

	}

	// 콤보박스 클릭 시 강의선택
	private void handleCombobox() {
		String selectedCombo = comboClassName.getValue();
		String strClassno = selectedCombo.substring(selectedCombo.indexOf("[") + 1, selectedCombo.lastIndexOf("]"));
		int classno = Integer.parseInt(strClassno);
		ClassDao cDao = new ClassDao();
		currentClass = cDao.selectClassOne(classno);
	}

	// 강의명 표시
	private void setClassName() {

		if (currentClass.getClassName() == null) {// 선택된 강의가 없을 경우
			// 콤보박스 보이고 라벨 안보임
			comboClassName.setVisible(true);
			txtClassName.setVisible(false);

			// 강의목록 불러와서 보여줌
			TaskDao tDao = new TaskDao();
			String userid = AppMain.app.getBasic().getId();
			classList = tDao.selectUserCurrentClassList(userid);

			comboClassName.setItems(classList);

		} else {// 선택된 강의가 있을 경우
			// 콤보박스 안보이고 라벨 보임
			comboClassName.setVisible(false);
			txtClassName.setVisible(true);

			// 라벨에 현재 강의명 표시
			txtClassName.setText(currentClass.getClassName());
		}
	}

	// 첨부파일 버튼 -> fileChooser dialog
	private void handleAttachedFileBtn() {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new ExtensionFilter("AllFiles", "*.*"));
		fc.setTitle("첨부파일 등록");

		Scene scene = btnSubmit.getScene();
		File file = fc.showOpenDialog(scene.getWindow());

		// 선택된 파일이 있을 경우
		if (file != null) {
			String sizeResult = Util.fileSizeCheck(file);			//파일 용량체크
			if(sizeResult != null) {
				try {
					this.lblFileSize.setText("파일크기 : "+sizeResult);
					FileInputStream fis = new FileInputStream(file);
					BufferedInputStream bis = new BufferedInputStream(fis);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();

					int data = 0;
					byte[] readData = new byte[10000];
					while ((data = bis.read(readData)) != -1) {
						baos.write(readData, 0, data);
					}

					arr = baos.toByteArray();

					String filePath = file.getPath();
					System.out.println("selected file path : " + filePath);

					String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
					System.out.println("selected file name : " + fileName);

					txtFile.setText(fileName);

					baos.close();
					bis.close();
					fis.close();
					System.out.println("file write 성공");

				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.println("파일 선택 오류");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("file write 실패");
				}
			} else {
				
				Util.showAlert("", "10MB 이하만 등록 가능합니다.", AlertType.ERROR);
			}
		}

	}

	// 과제 등록 버튼
	private void handleSubmitBtn() {
		System.out.println("**과제 등록 버튼 클릭");

		try {
			// 각 필드에서 값 읽기

			// 강의명
			if (comboClassName.isVisible()) { // 콤보박스에서 강의명 얻기

			} else if (txtClassName.isVisible()) {
			}

			String name = txtTitle.getText();
			String desc = txtDesc.getText();
			String strPftScore = txtPerfectScore.getText();
			Integer perfectScore = null;
			LocalDate expireDate = dateExpire.getValue();
			LocalDate cDate = LocalDate.now();
			String filename = txtFile.getText();

			// 과제명 is not null
			if (name.equals("")) {
				// 팝업 넣기
				Alert titleAlert = new Alert(Alert.AlertType.ERROR);
				titleAlert.setTitle("과제명 없음");
				titleAlert.setContentText("과제명이 입력되지 않았습니다.");
				titleAlert.showAndWait();
				return;
			}

			// 점수입력되면 Integer로
			if (!strPftScore.isEmpty()) {
				perfectScore = Integer.parseInt(strPftScore);
			}

			// 과제 제출 날짜를 설정하지 않으면 자동으로 클래스의 종료일로 설정
			if (dateExpire.getValue() == null) {
				expireDate = currentClass.getEndDate();
			}

			// 과제 제출 날짜>현재 날짜 제한
			int diff = Period.between(cDate, expireDate).getDays();
			if (diff < 0) {
				// 팝업 넣기
				Util.showAlert("날짜 오류", "오늘 이전 날짜로는 설정할 수 없습니다.", AlertType.ERROR);
				return;
			}

			// 과제 제출 날짜>강의 시작 날짜
			LocalDate classStartDate = currentClass.getStartDate();
			int diff2 = Period.between(classStartDate, expireDate).getDays();
			if (diff2 < 0) {
				Util.showAlert("날짜 오류", "강의 시작 전에 과제 마감을 할 수 없습니다.", AlertType.ERROR);
				return;
			}

			// DTO에 읽은 값들 저장
			TaskDto task = new TaskDto(name, desc, cDate, expireDate, arr, filename, perfectScore);

			// db에 데이터 저장
			TaskDao tDao = new TaskDao();

			int classno = currentClass.getClassNo();

			tDao.insertTask(task, classno);

			// 입력창 클리어
			clear();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	private void clear() {
		txtTitle.clear();
		txtDesc.clear();
		txtPerfectScore.clear();
		dateExpire.setValue(null);
		txtFile.clear();
	}

}