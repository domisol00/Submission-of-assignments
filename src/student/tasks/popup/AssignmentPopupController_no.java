package student.tasks.popup;
/*
 * 작성자 : 도현호
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import DAO.AssignmentDao;
import DTO.AssignmentDto;
import DTO.AssignmentPopupDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import launch.AppMain;
import util.Util;

public class AssignmentPopupController_no implements Initializable {

	@FXML private Label lblTaskName;
	@FXML private TextArea txtTaskDesc;
	@FXML private Label lblMyScore;
	@FXML private Label lblPerfectScore;
	@FXML private Label lblExpireDate;
	@FXML private Label lblAttachedFilename;
	@FXML private TextArea txtQuestion;
	@FXML private Label lblDescCount;
	@FXML private TextArea txtAnswer;
	@FXML private Label lblTaskFilename;
	@FXML private Button btnDownload;
	@FXML private Button btnSubmitFile;
	@FXML private Button btnDeleteFile;
	@FXML private Button btnSubmit;
	@FXML private Button btnCancle;
	@FXML private Label lblFileSize;

	private AssignmentDao aDao = new AssignmentDao();
	private AssignmentDto assign = new AssignmentDto();
	private String student_id = AppMain.app.getBasic().getId();			//학생ID
	private int class_no = AppMain.app.getBasic().getClass_no();		//강의번호
	private int task_no;												//과제번호				
	private Stage popupStage;
	private Window window;
	
	public AssignmentPopupController_no(Window window) {
		
		//과제제출 상세화면 설정 
		this.popupStage = new Stage(StageStyle.UTILITY);
		this.window = window;

		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../fxml/student/tasks/assignment_popup.fxml"));
			FXMLLoader loader = new FXMLLoader(Class.forName("student.tasks.popup.AssignmentPopupController_no").getResource("/fxml/student/tasks/assignment_popup.fxml"));
			loader.setController(this);
			this.popupStage.setScene(new Scene(loader.load()));
			this.popupStage.setResizable(false);
			this.popupStage.setTitle("과제 제출");
			this.popupStage.initModality(Modality.WINDOW_MODAL);
			this.popupStage.initOwner(this.window);
		} catch (Exception e) {
			System.out.println("과제제출(처음) 생성자 생성 실패");
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		this.btnDownload.setOnAction(e -> {handleBtnAttachFile();});
		this.btnSubmitFile.setOnAction(e -> {handleBtnSubmitFile();});
		this.btnDeleteFile.setOnAction(e -> {handleBtnDeleteFile();});
		this.btnSubmit.setOnAction(e -> { handleBtnSubmit(); });
		this.btnCancle.setOnAction(e -> { handleBtnCancle(); });
		//강좌설명 자동줄바꿈
		this.txtTaskDesc.setWrapText(true);
		//강좌설명 입력제한
		this.txtTaskDesc.setEditable(false);
		//답변 자동줄바꿈
		this.txtAnswer.setWrapText(true);
		//답변 입력제한
		this.txtAnswer.setEditable(false);
		//글자수 제한
		Util.textLengthLimit(txtQuestion, lblDescCount, 5000);
		//파일크기 출력
		this.lblFileSize.setVisible(true);
	}

	//과제제출 상세화면 띄우기
	public void showStage() {
		
		//강의, 과제번호는 받기 전 인스턴스가 생성되기에 0으로 초기화되어 있음, 별도로 받아야 함
		this.class_no = AppMain.app.getBasic().getClass_no();
		this.task_no = AppMain.app.getBasic().getTask_no();
		assignView(this.class_no, this.task_no);
		try {
			
			popupStage.showAndWait();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//화면에 정보 뿌리기
	private void assignView(int c_no, int t_no) {

		this.assign = this.aDao.assignment_selectOne(c_no, t_no);
		this.lblTaskName.setText(this.assign.getTask_name());								//과제명
		this.txtTaskDesc.setText(this.assign.getTask_desc());								//과제설명
		this.lblMyScore.setText(Integer.toString(this.assign.getMyScore()));				//나의 과제 점수
		this.lblPerfectScore.setText(Integer.toString(this.assign.getPerfect_score()));	//과제 만점 점수
		this.lblExpireDate.setText(this.assign.getExpire_date().toString());				//과제 마김일
		this.lblAttachedFilename.setText(assign.getAttachedFile_name());				//첨부파일명
		this.txtQuestion.setText("");												//문의사항
		this.txtAnswer.setText("");													//답변
		this.lblTaskFilename.setText(this.assign.getTaskFile_name());						//과제제출
		//과제설명 줄바꿈
		this.txtTaskDesc.setWrapText(true);
		//과제설명 입력 제한
		this.txtTaskDesc.setEditable(false);
		//질문사항 줄바꿈
		this.txtQuestion.setWrapText(true);
		//다운로드할 파일 없을 경우 버튼 비활성화
		if(this.lblAttachedFilename.getText() == null) {
			this.btnDownload.setDisable(true);
		}
		//강의기간 아닐 때 제출버튼활성화
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date currentTime = new Date();
		String curretnDate = sdf.format(currentTime);
		Date today = null;
		try {
			today = sdf.parse(curretnDate);
		} catch(Exception e) {}
		
		Date start_date = assign.getStart_date();
		Date end_date = assign.getEnd_date();
		System.out.println(today);
		System.out.println(start_date);
		System.out.println(end_date);
		int sdDiff = today.compareTo(start_date);
		int edDiff = today.compareTo(end_date);
		System.out.println(sdDiff + " " + edDiff);
		if((sdDiff ==1 && edDiff ==1) || (sdDiff ==-1 && edDiff ==-1) ) {
			
			this.btnSubmit.setDisable(true);
		} else {
			this.btnSubmit.setDisable(false);
		}
		//점수가 책정되어있으면 제출 버튼 비활성화
//		if(this.assign.getCheckTask()) {
//			this.btnSubmit.setDisable(true);
//		} else {
//			this.btnSubmit.setDisable(false);
//		}
	}
	
	//참고파일 다운로드 버튼
	private void handleBtnAttachFile() {
		
		//파일 확장자 찾기
		String fileName = assign.getAttachedFile_name();
		int extension_idx = fileName.lastIndexOf(".")+1;			//마지막 위치의 앞 인덱스 리턴하기에 +1(또한 찾지못하면 -1을 리턴하기에 0으로 만들어줌)
		String fileExtension = fileName.substring(extension_idx, fileName.length());
		
		//파일 저장DIALOG 띄우기 - 기존 파일 확장자로 바로 저장하기
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName(fileName);
		fileChooser.getExtensionFilters().addAll(
			new ExtensionFilter(fileExtension,"*."+fileExtension)
		);

		try {
			//저장할 파일명 지정
			File file = fileChooser.showSaveDialog(this.popupStage);
			if(file != null) {
				
				InputStream is = assign.getAttachedFile();
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				byte[] readData = new byte[10000];
				int data = 0;
				while((data = is.read(readData)) != -1) {
					
					bos.write(readData,0,data);
				}
				bos.flush();
				bos.close();
				fos.close();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//첨부파일 업로드 버튼
	private void handleBtnSubmitFile() {
		
		//파일 열기DIALOG 띄우기
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("AllFiles","*.*")
		);
		
		try {
			
			//첨부할 파일 지정
			File file = fileChooser.showOpenDialog(this.popupStage);
		
			if(file != null ) {//취소단추 누를 때 예외방지
				
				String sizeResult = Util.fileSizeCheck(file);			//파일용량체크
				if(sizeResult != null) {
					this.lblFileSize.setText("파일크기 : "+sizeResult);
					FileInputStream fis = new FileInputStream(file);
					BufferedInputStream bis = new BufferedInputStream(fis);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try {
						
						int data = 0;
						byte[] readData = new byte[10000];
						while((data = bis.read(readData))!= -1 ) {
							
							baos.write(readData,0,data);
						}
						System.out.println("파일 읽기 성공");
					} catch (IOException e) {
						
						e.printStackTrace();
						System.out.println("파일 읽기 실패");
					}
					this.assign.setTaskFile(baos.toByteArray());		//property에 넣기
					String file_name = file.getName();					//파일명 출력
					this.assign.setTaskFile_name(file_name);
					this.lblTaskFilename.setText(file_name);
					System.out.println("파일명 : " + file_name);
				} else {
					
					Util.showAlert("", "10MB 이하만 등록 가능합니다.", AlertType.ERROR);
				}
			} 
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
			System.out.println("파일 선택 오류");
		}
	}
	
	//첨부파일 삭제 버튼
	private void handleBtnDeleteFile() {
		
		this.assign.setTaskFile(null);
		this.assign.setTaskFile_name(null);
		this.lblTaskFilename.setText(null);
		this.lblFileSize.setText(null);
	}
	
	//제출 버튼
	private void handleBtnSubmit() {
		
		try {
		
			Alert alert = Util.showAlert("과제제출 확인", "제출하시겠습니까?", AlertType.CONFIRMATION);
			Optional<ButtonType> result = alert.showAndWait();
            // OK누르면 신청 후 알림창 출력
            if (result.get().equals(ButtonType.OK)) {
          
            	String taskQuestion = this.txtQuestion.getText();
            	byte[] taskFile = this.assign.getTaskFile();
            	String taskFile_name = this.assign.getTaskFile_name();
            	
            	if(taskQuestion.equals("") && taskFile == null) {
            	
					Util.showAlert("과제 제출 실패", "제출할 내용이 없습니다.", AlertType.ERROR);
            	} else {
            	
					//submitTask DTO
					AssignmentPopupDto sTask = new AssignmentPopupDto(this.class_no, this.task_no, this.student_id, taskQuestion, taskFile, taskFile_name);
					this.aDao.submit_Assignment(sTask);
					Alert alert2 = Util.showAlert("", "제출되었습니다.", AlertType.INFORMATION);
					Optional<ButtonType> result2 = alert2.showAndWait();
					// OK누르면 창 종료
					if (result2.get().equals(ButtonType.OK)) {
						this.popupStage.close();
					}
            	}
            }
		} catch (SQLException e) {
			
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("과제 제출 실패");
			Util.showAlert("", "이미 제출했습니다.", AlertType.ERROR);
		}
	}
	
	// 취소 버튼
	private void handleBtnCancle() {

		this.popupStage.close();
		System.out.println("취소 버튼 클릭");
	}
}