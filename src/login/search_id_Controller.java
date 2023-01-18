package login;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import DAO.BasicDao;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import util.Mail;
import util.Util;

public class search_id_Controller implements Initializable {

	@FXML private TextField namefield;
	@FXML private TextField emailfield1;
	@FXML private TextField emailfield2;
	@FXML private TextField certificationfield;
	@FXML private TextField phonefield;
	@FXML private Button done;
	@FXML private Button cancle;
	@FXML private ToggleButton student;
	@FXML private ToggleButton teacher;
	@FXML private Button btnSendMail;
	@FXML private ComboBox<String> email2;
	@FXML private Label lblNameWarning;
	@FXML private Label lblMailWarning;
	@FXML private Label lblPhoneWarning;
	@FXML private Label lblCertificationWarning; 
	@FXML private Label lblTimer;
	@FXML private ToggleGroup tg_group;
	
	private boolean type;
	private Stage pop;
	private Mail mail;
	private boolean checkMail;
	private boolean checkCertification;
	private String certificationNum;
	private BasicDao bDao = new BasicDao();
	private boolean isTimerRunning = false;			//타이머 플래그
	private Timer timer;
	private int timeLimit;							//sec

	public void initialize(URL location, ResourceBundle resources) {
		
		//신분 버튼 그룹화
		tg_group = new ToggleGroup();
		student.setToggleGroup(tg_group);
		teacher.setToggleGroup(tg_group);
		
		done.setOnAction(e -> handledone(e));
		cancle.setOnAction(e -> handlecancle(e));
		student.setOnAction(e -> handlestudent(e));
		teacher.setOnAction(e -> handleteacher(e));
		email2.setOnAction(e -> handleCombobox());
		emailfield2.setEditable(false);
		certificationfield.setVisible(false);
		lblCertificationWarning.setVisible(false);
		phonefield.textProperty().addListener(Util.textCountLimit(phonefield, 11));
		
		// textfield warning label
		Util.showWarningLabel(namefield, lblNameWarning);
		Util.showWarningLabel(phonefield, lblPhoneWarning);

		// 이메일 텍스트필드, 콤보박스 모두 입력 안될 시 라벨
		BooleanBinding isEmailEmpty = Bindings.createBooleanBinding(
				// 이메일 텍스트필드1,2 비었으면 true 리턴
				() -> (emailfield1.getText().length() == 0 || emailfield2.getText().length() ==0 ), emailfield1.textProperty(),
				emailfield2.textProperty());
		lblMailWarning.visibleProperty().bind(isEmailEmpty); // true면 보이고 false면 lblWarning 안보이게

		// 전화번호 필드에 숫자만 오도록 제한
		phonefield.textProperty().addListener(Util.numberOnlyListener(phonefield));
	
		//이메일 인증
		btnSendMail.setOnAction(e->{handleEmailCheck();});

		//전화번호 필드에 숫자만 오도록 제한
		phonefield.textProperty().addListener(Util.numberOnlyListener(phonefield));

	}
	
	//이메일 콤보박스 선택
	public void handleCombobox() {
		if(email2.getValue().equals("직접입력")) {
			emailfield2.setEditable(true);
			emailfield2.setText("");
		} else {
			emailfield2.setEditable(false);
			String emailField2 = email2.getValue();	
			emailfield2.setText(emailField2);
		}
	}
	
	//이메일 인증
	private void handleEmailCheck() {
		
		if(this.emailfield1.getText().length() != 0 && this.emailfield2.getText().length() != 0) {
			
			String txt = "인증메일 보내는 중";
			Alert waitAlert = Util.showAlert("", txt, AlertType.NONE);
			waitAlert.show();

			String address = emailfield1.getText() + "@" + emailfield2.getText();
				
			//인증번호 입력칸 생성
			this.certificationfield.setVisible(true);
			
			//메일 값 넣기
			this.mail = new Mail(this.namefield.getText(), address);
			this.mail.mailSend();
			this.checkMail = this.mail.getCheckMail();
			if(this.checkMail) {
				Alert alert = Util.showAlert("", "인증메일이 발송되었습니다.", AlertType.INFORMATION);
				alert.showAndWait();
			}
			new Thread(()-> {
				
				try {
			        Thread.sleep(50);
			    } catch (InterruptedException ex) {
			    }
			    Platform.runLater(() -> {
			        waitAlert.setResult(ButtonType.CANCEL);
			        waitAlert.close();
			    });
				
			}).start();
			this.certificationNum = this.mail.getCertificationNum();
			
			//인증번호 입력 안될 시 라벨
			BooleanBinding isCertification = Bindings.createBooleanBinding(
					() -> (this.certificationfield.isVisible() && this.certificationfield.getText().length() ==0 && !this.checkCertification),
					this.certificationfield.textProperty());
			this.lblCertificationWarning.visibleProperty().bind(isCertification);
			this.certificationNum = this.mail.getCertificationNum();
			
			//타이머
			lblTimerSet();
			
			//버튼 이름 및 기능 변경
			this.btnSendMail.setText("확인");
			this.btnSendMail.setOnAction(e->{
			
				if(!this.isTimerRunning) {
					
					Alert alert = Util.showAlert("", "인증시간이 초과되었습니다.\n 다시 인증해주세요.", AlertType.INFORMATION);
					alert.showAndWait();
					this.btnSendMail.setText("메일인증발송");
					this.btnSendMail.setOnAction(e1->{handleEmailCheck();});
					this.lblTimer.setVisible(false);
				}else {
					try {
						if(this.checkMail && (this.certificationfield.getText().length()==0)) {
						
							this.btnSendMail.setVisible(true);
							throw new Exception();
						} 
						if(this.certificationfield.getText().length() != 0 &&
								(!(this.certificationNum.equals(certificationfield.getText().toLowerCase())))){
					
							Util.showAlert("", "인증번호가 일치하지 않습니다.", AlertType.ERROR);
							throw new Exception();
						} 
						this.checkCertification = true;
						emailfield1.setEditable(false);
						this.timer.cancel();
						Alert alert = Util.showAlert("", "인증되었습니다.", AlertType.INFORMATION);
						alert.showAndWait();
					} catch (Exception e1) { }
				}
			});
		}
	}

	public void handleteacher(ActionEvent e) {
		System.out.println("교사로 전환");
		type = true;
	}

	public void handlestudent(ActionEvent e) {
		System.out.println("학생으로 전환");
		type = false;
	}
	
	public void handledone(ActionEvent e) {
		try {
			String name = namefield.getText().trim();
			String phone = phonefield.getText().trim();
			String email = emailfield1.getText().trim() + "@" + emailfield2.getText().trim();
			
			if(name.length() == 0) {
				Util.showAlert("입력오류", "이름을 입력해 주세요", AlertType.ERROR);
				throw new Exception();
			}
			if(emailfield1.getText().length() == 0 || emailfield2.getText().length() == 0) {
				Util.showAlert("입력오류", "이메일을 입력해 주세요", AlertType.ERROR);
				throw new Exception();
			}
			if (phone.length() == 0 ) {
				Util.showAlert("입력오류", "전화번호를 입력해 주세요", AlertType.ERROR);
				throw new Exception();
			}
			
			
			//인증메일
			if(!checkMail) {
				Alert alert = Util.showAlert("", "인증메일발송버튼을 눌러 주세요.", AlertType.INFORMATION);
				alert.showAndWait();
			    throw new Exception();
			}
			
			//메일 인증 여부
			if(!this.checkCertification) {
				
				Alert alert = Util.showAlert("", "메일을 인증해주세요.", AlertType.INFORMATION);
				alert.showAndWait();
				throw new Exception();
			}

			String success = bDao.search_id(name, email, phone, type);
			System.out.println("완료");
			if (success.equals("fail")) {
				Alert alert = Util.showAlert("", "일치하는 회원 정보가 없습니다.", AlertType.INFORMATION);
				alert.showAndWait();
				return;
			}

			pop = (Stage) cancle.getScene().getWindow(); // 버튼을 통해서 현재 스테이지를 알아냄
			pop.close();

		} catch (Exception ex) {
			System.out.println("아이디 찾기 실패");
			
//         ex.printStackTrace();
		}
	}
	// 인증시간 제한 타이머 라벨링
	private void lblTimerSet() {
		lblTimer.setVisible(true);

		// 실행중인 타이머가 있으면 취소시키고 새로실행
		if (isTimerRunning) {
			timer.cancel();
//			System.out.println("타이머중지");
		}

		// 시간제한 (sec) -- 현재 3분30초 설정
		timeLimit = 210;

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				isTimerRunning = true;				// 실행중에는 플래그 true

				Platform.runLater(() -> {

					timeLimit--;

					String time = String.format("%02d", (timeLimit / 60)) + " : "
							+ String.format("%02d", (timeLimit % 60));
					lblTimer.setText(time);

					if (timeLimit == 0) { 			// timeout되면
						isTimerRunning = false; 	// 플래그 false
						certificationNum = null; 	// 인증번호 리셋
						timer.cancel();				// 타이머 취소
//						System.out.println("timeout 후 인증번호 :" + certificationNum);
					}
				});
			}
		};

		timer = new Timer();
		timer.schedule(task, 1000, 1000);
	}

	// 취소버튼
	public void handlecancle(ActionEvent e) {
		pop = (Stage) cancle.getScene().getWindow(); // 버튼을 통해서 현재 스테이지를 알아냄
		pop.close();
	}
}