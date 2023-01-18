package student.classes.popup;
/*
 * 작성자 : 도현호
 */

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import DAO.EnrollmentDao;
import DTO.EnrollmentDto;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import launch.AppMain;
import util.Util;

public class EnrollmentPopupController implements Initializable {

	@FXML private AnchorPane anchorPane;
	@FXML private Label lblClassname;
	@FXML private Label lblTeachername;
	@FXML private TextArea txtClassdescription;
	@FXML private Label lblClassdate;
	@FXML private Label lblLimitclass;
	@FXML private Button btnSubmit;
	@FXML private Button btnCancle;

	private Stage popupStage;
	private EnrollmentDto enroll = new EnrollmentDto();
	private EnrollmentDao eDao = new EnrollmentDao();
	private	String stu_id = AppMain.app.getBasic().getId();					//학생ID
	private int c_no;														//강좌번호
	private Window window;
	
	public EnrollmentPopupController(Window window) {
		
		//수강신청 상세화면 설정 
		this.popupStage = new Stage(StageStyle.UTILITY);
		this.window = window;
		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../fxml/student/classes/enrollment_popup.fxml"));
			FXMLLoader loader = new FXMLLoader(Class.forName("student.classes.popup.EnrollmentPopupController").getResource("/fxml/student/classes/enrollment_popup.fxml"));
			loader.setController(this);
			this.anchorPane = (AnchorPane) loader.load();
			this.popupStage.setScene(new Scene(anchorPane));
			this.popupStage.setTitle("수강 신청");
			this.popupStage.setResizable(false);
			this.popupStage.initModality(Modality.WINDOW_MODAL);
			this.popupStage.initOwner(this.window);
			
			System.out.println("수강신청 화면 열기 성공");
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("수강신청 화면 열기 실패");
		}
	}

	//수강신청 상세화면 띄우기
	public void showStage() {
	
		this.c_no = AppMain.app.getBasic().getClass_no();
		enrollView(this.c_no);
		this.popupStage.showAndWait();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		btnSubmit.setOnAction(e -> {
			this.enroll = eDao.check_requestClass(this.c_no, this.stu_id);
			System.out.println(enroll.getCheck());
			if(this.enroll.getCheck()){
				handleBtnDelete();						//수강취소
			} else {
				handleBtnSubmit();						//수강신청
			}
		});
		this.btnCancle.setOnAction(e -> { handleBtnCancle(); });
	}

	
	// 화면에 정보 뿌리기
	private void enrollView(int c_no) {
		
		this.enroll = this.eDao.enrollment_selectOne(c_no);
		this.lblClassname.setText(this.enroll.getClassname());	
		this.lblTeachername.setText(this.enroll.getTeachername());
		this.txtClassdescription.setText(this.enroll.getClassdescription());
		this.lblClassdate.setText(this.enroll.getStartdate().toString() + " ~ " + this.enroll.getEnddate().toString());
		int cnt = (this.enroll.getLimitstudent() - this.enroll.getCurrentstudent());		//수강신청 가능인원수
		this.lblLimitclass.setText(cnt+"명");
		
		//강좌설명 자동줄바꿈
		this.txtClassdescription.setWrapText(true);
		//강좌설명 입력제한
		this.txtClassdescription.setEditable(false);
		//수강신청 가능 인원이 0이면 버튼 비활성화
		this.enroll = this.eDao.check_requestClass(c_no, this.stu_id);
		if(cnt == 0 && !this.enroll.getCheck()) {
			
			this.btnSubmit.setDisable(true);
		}
		//버튼 글자바꾸기
		if(this.enroll.getCheck()) {
			this.btnSubmit.setText("수강취소");
		} else {
			this.btnSubmit.setText("등록");
		}
	}

	// 신청 버튼
	private void handleBtnSubmit() {
		
		try {
			Alert alert = Util.showAlert("수강신청확인","신청하시겠습니까?", AlertType.CONFIRMATION);
			Optional<ButtonType> result = alert.showAndWait();
            // OK누르면 신청 후 알림창 출력
            if (result.get().equals(ButtonType.OK)) {
            	
            	this.eDao.submit_Enrollment(this.stu_id, this.c_no);
            	Alert alert2 = Util.showAlert("", "신청되었습니다.", AlertType.INFORMATION);
				Optional<ButtonType> result2 = alert2.showAndWait();
            	// OK누르면 창 종료
				if (result2.get().equals(ButtonType.OK)) {
					this.popupStage.close();
				}
            }
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("강의 신청 실패");
			Util.showAlert("", "수강 잔여 좌석이 없습니다.", AlertType.ERROR);
		}
	}
	
	//수강취소 버튼
	private void handleBtnDelete() {
		
		try {
			Alert alert = Util.showAlert("수강취소확인","취소하시겠습니까?", AlertType.CONFIRMATION);
			Optional<ButtonType> result = alert.showAndWait();
            // OK누르면 신청 후 알림창 출력
            if (result.get().equals(ButtonType.OK)) {
            	
            	this.eDao.delete_Enrollment(this.stu_id, this.c_no);
            	Alert alert2 = Util.showAlert("", "취소되었습니다.", AlertType.INFORMATION);
				Optional<ButtonType> result2 = alert2.showAndWait();
            	// OK누르면 창 종료
				if (result2.get().equals(ButtonType.OK)) {
					this.popupStage.close();
				}
            }
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("수강취소 신청 실패");
			Util.showAlert("", "수강취소가 되지 않았습니다.", AlertType.ERROR);
		}
	}

	// 취소 버튼
	private void handleBtnCancle() {

		this.popupStage.close();
		System.out.println("취소 버튼 클릭");
	}
}