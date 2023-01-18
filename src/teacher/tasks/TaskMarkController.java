package teacher.tasks;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import DAO.TaskDetailDao;
import DTO.TaskDetailDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import util.Util;

/**
 * @author 김지현
 *
 */
public class TaskMarkController implements Initializable {
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Label lblTaskTitle;
	@FXML
	private Label lblStudentName;
	@FXML
	private Label lblSubmittedDate;
	@FXML
	private Label lblAttachedFilename;
	@FXML
	private Button btnDownload;
	@FXML
	private TextArea txtQuestion;
	@FXML
	private TextArea txtAnswer;
	@FXML
	private TextField txtScore;
	@FXML
	private Label lblPerfectScore;
	@FXML
	private Button btnSubmit;
	@FXML
	private Button btnCancel;
	@FXML
	private Label lblAnswer;

	int taskNum;
	String studentId;
	TaskDetailDto selectedDto;
	TaskDetailDao tdDao = new TaskDetailDao();

	public TaskMarkController(TaskDetailDto selectedTdDto) {
		this.selectedDto = selectedTdDto;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 제출된 과제 정보 표시
		setLabels();
		
		//입력제한
		Util.textLengthLimit(txtAnswer,lblAnswer,5000);

		// 다운로드 버튼
		btnDownload.setOnAction(e -> handleBtnDownload());

		// 점수는 숫자만 입력
		txtScore.textProperty().addListener(Util.numberOnlyListener(txtScore));

		// 채점 완료 버튼
		btnSubmit.setOnAction(e -> handleBtnSubmit());
	}

	// 채점 완료 버튼
	private void handleBtnSubmit() {

		String answer = txtAnswer.getText();
		Integer score = Integer.parseInt(txtScore.getText());

		if (score > selectedDto.getFullscore()) {
			Util.showAlert("점수 오류", "과제 최고점보다 점수가 클 수는 없습니다.", AlertType.ERROR);
			return;
		}

		selectedDto.setAnswer(answer);
		selectedDto.setTaskScore(score);

		tdDao.markTask(selectedDto);

	}

	// 첨부파일 다운로드
	private void handleBtnDownload() {
		System.out.println("click");

		// 파일 확장자 찾기
		String fileName = selectedDto.getFilename();
		if (fileName == null) {
			return;
		}
		int extension_idx = fileName.lastIndexOf(".") + 1;
		String fileExtension = fileName.substring(extension_idx, fileName.length());

		// 파일 저장 DIALOG - 기존 파일 확장자로 저장하도록 지정
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter(fileExtension, "*." + fileExtension));

		// 저장할 파일명 지정(과제명 - 학생명 - 학생아이디 - 학생이 제출한 파일명)
		String fileSaveName = selectedDto.getTaskTitle() + "-" + selectedDto.getStudentName() + "("
				+ selectedDto.getStudentId() + ")-" + selectedDto.getFilename();
		fileChooser.setInitialFileName(fileSaveName);

		try {
			File file = fileChooser.showSaveDialog(btnDownload.getScene().getWindow());
//			File file = fileChooser.showSaveDialog(anchorPane.getScene().getWindow());

			if (file != null) {// 취소하면 null

				byte[] fileBytes = selectedDto.getFile();
				ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				byte[] readData = new byte[10000];
				int data = 0;
				while ((data = bis.read(readData)) != -1) {
					bos.write(readData, 0, data);
				}

				bos.flush();
				bos.close();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 학생이 제출한 과제 표시
	private void setLabels() {

		// 과제명, 학생번호
		int selectedTaskNo = selectedDto.getTaskNum();
		String selectedStudentId = selectedDto.getStudentId();

		// 위 정보로 db에서 검색
		TaskDetailDto selected = tdDao.selectTaskDetail(selectedTaskNo, selectedStudentId);

		// 현재창에 정보 표시
		lblTaskTitle.setText(selected.getTaskTitle());
		lblStudentName.setText(selected.getStudentName() + "(" + selected.getStudentId() + ")");

		if (selected.getSubmitDate() == null) {
			lblSubmittedDate.setText("과제 제출 전");
		} else {
			lblSubmittedDate.setText(selected.getSubmitDate().toString());
		}

		lblAttachedFilename.setText(selected.getFilename());

		// 제출된 파일이 없으면 다운로드 버튼 비활성화
		if (selectedDto.getFile() == null) {
			System.out.println("file null");
			btnDownload.setDisable(true);
		}

		txtQuestion.setText(selected.getQuestion());
		txtAnswer.setText(selected.getAnswer());

		if (selected.getTaskScore() == -1) { // -1 = 제출전
			txtScore.clear();
		} else { // 채점한 점수가 있으면 표시
			txtScore.setText(selected.getTaskScore().toString());
		}

		lblPerfectScore.setText(selected.getFullscore().toString());

	}

}