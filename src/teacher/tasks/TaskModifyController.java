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

import DAO.TaskDao;
import DTO.TaskDto;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import util.Util;

/**
 * @author 김지현
 *
 */
public class TaskModifyController implements Initializable {
	@FXML
	private TextField txtFile;

	@FXML
	private Button btnFileDel;

	@FXML
	private Button btnDateDel;

	@FXML
	private DatePicker dateExpire;

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
	private Label lblDesc;
	@FXML
	private Label lblTitle;
	@FXML
	private Label lblFileSize;

	TaskDao tDao = new TaskDao();
	int selectedTaskNo;
	byte[] arr;

	public TaskModifyController(int selectedTaskNo) {
		this.selectedTaskNo = selectedTaskNo;
		setContents();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//입력길이 제한
		Util.textLengthLimit(txtTitle, lblTitle, 20);
		Util.textLengthLimit(txtDesc, lblDesc, 5000);
		
		// 숫자만 입력할 수 있도록 제한
		txtPerfectScore.textProperty().addListener(Util.numberOnlyListener(txtPerfectScore));
		
		// 점수필드 - 글자수 제한
		txtPerfectScore.textProperty().addListener(Util.textCountLimit(txtPerfectScore, 9));
		// 첨부파일 버튼
		btnFile.setOnAction(e -> handleAttachedFileBtn());

		// 수정버튼
		btnSubmit.setOnAction(e -> handleSubmitBtn());

		// 날짜 삭제버튼
		btnDateDel.setOnAction(e -> dateExpire.setValue(null));

		// 첨부파일 삭제 버튼
		btnFileDel.setOnAction(e -> {
			arr = null;
			txtFile.clear();
			this.lblFileSize.setText(null);
		});
		btnCancle.setOnAction(e ->{
			Stage stage = (Stage)btnCancle.getScene().getWindow();
			stage.close();
		});
	}

	// 첨부파일 버튼
	private void handleAttachedFileBtn() {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new ExtensionFilter("AllFiles", "*.*"));
		fc.setTitle("첨부파일 등록");

		Scene scene = btnSubmit.getScene();
		File file = fc.showOpenDialog(scene.getWindow());

		// 선택된 파일이 있을 경우
		if (file != null) {
			String sizeResult = Util.fileSizeCheck(file);			//파일용량체크
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

	// 과제 수정 버튼
	private void handleSubmitBtn() {
		System.out.println("**과제수정버튼 클릭!");
		TaskDto selectedReport = tDao.selectTask(selectedTaskNo);

		try {
			// 각 필드에서 값 읽기
			String name = txtTitle.getText();
			String desc = txtDesc.getText();
			String strPftScore = txtPerfectScore.getText();
			Integer perfectScore = null;
			LocalDate expireDate = null;
			LocalDate regDate = selectedReport.getTcRegdate();
			String filename = txtFile.getText();

			// 과제명 is not null
			if (name.equals("")) {
				// 팝업
				Util.showAlert("과제명 오류", "과제명이 입력되지 않았습니다.", AlertType.ERROR);
				return;
			}

			// 점수입력
			if (!strPftScore.isEmpty()) {
				perfectScore = Integer.parseInt(strPftScore);
			}

			// 과제 제출 날짜 검사
			if (dateExpire.getValue() != null) {
				expireDate = dateExpire.getValue();

				// 과제 제출 날짜>현재 날짜 제한
				Period period = Period.between(LocalDate.now(), expireDate);
				int diff = period.getDays();
				if (diff < 0) {
					// 팝업
					Util.showAlert("날짜 오류", "오늘 이전 날짜로는 설정할 수 없습니다.", AlertType.ERROR);
					return;
				}
			}

			// DTO에 수정된 값들 저장
			TaskDto modifiedReport = new TaskDto(selectedTaskNo, name, desc, regDate, expireDate, arr, filename,
					perfectScore);

			// db에 데이터 저장
			tDao.updateReport(modifiedReport);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	// 선택된 과제 정보 입력
	private void setContents() {
		System.out.println("selected Task : " + selectedTaskNo);
		TaskDto selectedTask = tDao.selectTask(selectedTaskNo);

		Platform.runLater(() -> {
			txtTitle.setText(selectedTask.getTcTitle());
			txtDesc.setText(selectedTask.getTcDesc());

			if (selectedTask.getPerfectScore() != null) {
				txtPerfectScore.setText(selectedTask.getPerfectScore().toString());
			} else {
				txtPerfectScore.setText("");
			}

			dateExpire.setValue(selectedTask.getTcExpireDate());
			txtFile.setText(selectedTask.getTcFile());
		});
	}
}