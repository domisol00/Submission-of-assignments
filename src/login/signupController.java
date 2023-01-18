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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import util.Mail;
import util.Util;

public class signupController implements Initializable {
	
	@FXML private TextField idfield;
	@FXML private TextField namefield;
	@FXML private PasswordField passfield;
	@FXML private PasswordField repassfield;
	@FXML private TextField emailfield1;
	@FXML private TextField emailfield2;
	@FXML private TextField certificationfield;
	@FXML private ComboBox<String> combobox;
	@FXML private ComboBox<String> email2;
	@FXML private TextField phonefield1;
	@FXML private TextField phonefield2;
	@FXML private Button done;
	@FXML private Button cancle; 
	@FXML private ToggleButton student;
	@FXML private ToggleButton teacher;
	@FXML private ToggleGroup tg_group;
	@FXML private Button check;
	@FXML private Button btnCertification;
	@FXML private Label idlabel;
	@FXML private Label namelabel;
	@FXML private Label emaillabel;
	@FXML private Label lblCertification;
	@FXML private Label lblCertification2;
	@FXML private Label passlabel;
	@FXML private Label repasslabel;
	@FXML private Label numberlabel;
	@FXML private Label passchecklabel;
	@FXML private Label label;
	@FXML private Label lblTimer;
	@FXML private Label idinputlabel;
	@FXML private Label passinputlabel;
	@FXML private Label repassinputlabel;
	@FXML private Label emailinputlabel;
	
	private Stage pop;
	private int recheck;
	private boolean identityType;
	private boolean checkMail;						//인증번호 발송여부 체크
	private boolean checkCertification;				//인증번호 일치여부 체크
	private boolean isTimerRunning = false;			//타이머 플래그
	private Timer timer;
	private int timeLimit;							//sec
	private String certificationNum;
	private Mail mail;
	BasicDao bDao = new BasicDao();
	Alert alert =new Alert(AlertType.INFORMATION);
	
	public void initialize(URL location, ResourceBundle resources) {
		
		//신분 버튼 그룹화
		tg_group = new ToggleGroup();
		student.setToggleGroup(tg_group);
		teacher.setToggleGroup(tg_group);
		
		emailfield2.setEditable(false);
		passchecklabel.setVisible(false);
		idinputlabel.setVisible(false);
		passinputlabel.setVisible(false);
		repassinputlabel.setVisible(false);
		emailinputlabel.setVisible(false);
		
		//이메일 인증
		this.certificationfield.setVisible(false);
		this.lblCertification.setVisible(false);
		this.btnCertification.setOnAction(e -> {handleEmailCheck();});
		
		done.setOnAction(e->handledone(e));
		cancle.setOnAction(e->handlecancle(e));
		student.setOnAction(e->handlestudent(e));
		teacher.setOnAction(e->handleteacher(e));
		check.setOnAction(e->handlecheck(e));
		email2.setOnAction(e -> handleCombobox());
		
		//입력양식 제한
		idfield.textProperty().addListener(Util.alphabetListener(idfield,idinputlabel));
		passfield.textProperty().addListener(Util.pwListener(passfield,passinputlabel));
		repassfield.textProperty().addListener(Util.pwListener(repassfield,repassinputlabel));
		emailfield1.textProperty().addListener(Util.pwListener(emailfield1,emailinputlabel));
		emailfield2.textProperty().addListener(Util.pwListener(emailfield2,emailinputlabel));
		
		//숫자만 입력
		phonefield1.textProperty().addListener(Util.numberOnlyListener(phonefield1));
		phonefield2.textProperty().addListener(Util.numberOnlyListener(phonefield2));
		
		//글자수 제한
		phonefield1.textProperty().addListener(Util.textCountLimit(phonefield1, 4));
		phonefield2.textProperty().addListener(Util.textCountLimit(phonefield2, 4));
		idfield.textProperty().addListener(Util.textCountLimit(idfield, 20));
		namefield.textProperty().addListener(Util.textCountLimit(namefield, 20));
		emailfield1.textProperty().addListener(Util.textCountLimit(emailfield1, 30));
		emailfield2.textProperty().addListener(Util.textCountLimit(emailfield2, 18));
		passfield.textProperty().addListener(Util.textCountLimit(passfield, 20));
		repassfield.textProperty().addListener(Util.textCountLimit(repassfield, 20));
		
		// 미입력 필드 존재 시 label보이기
		Util.showWarningLabel(idfield, idlabel);
		Util.showWarningLabel(namefield, namelabel);
		Util.showWarningLabel(passfield, passlabel);
		Util.showWarningLabel(repassfield, repasslabel);

		// 패스워드 확인 다를 시 라벨 -- 비번 미입력 라벨과 겹침문제
		BooleanBinding isPasswordEqual = Bindings.createBooleanBinding(
				() -> ((repassfield.getText().length()!=0) &&(!passfield.getText().equals(repassfield.getText())) ), passfield.textProperty(),
				repassfield.textProperty());
		
//		BooleanBinding bb = Bindings.createBooleanBinding(()->(isPasswordEqual && passlabel.isVisible()), isPasswordEqual, passlabel.textProperty());
		
		// pw2 미입력 시 불일치하면 겹침
		
		passchecklabel.visibleProperty().bind(isPasswordEqual);

		// 이메일 텍스트필드, 콤보박스 모두 입력 안될 시 라벨
		BooleanBinding isEmailEmpty = Bindings.createBooleanBinding(
				// 이메일 텍스트필드1,2 비었으면 true 리턴
				() -> (emailfield1.getText().length() == 0 || emailfield2.getText().length() ==0 ), emailfield1.textProperty(),
				emailfield2.textProperty());
		emaillabel.visibleProperty().bind(isEmailEmpty); // true면 보이고 false면 lblWarning 안보이게

		// 핸드폰 콤보, 텍스트필드 1,2 모두 입력 안될 시 라벨
		BooleanBinding isPhoneEmpty = Bindings.createBooleanBinding(
				() -> (combobox.getValue() == null || phonefield1.getText().length() == 0
						|| phonefield2.getText().length() == 0),
				combobox.valueProperty(), phonefield1.textProperty(), phonefield2.textProperty());
		numberlabel.visibleProperty().bind(isPhoneEmpty);
		
	}

	//교사신분 선택
	public void handleteacher(ActionEvent e) {
		System.out.println("교사로 전환");
		identityType=true;
	}

	//학생신분 선택
	public void handlestudent(ActionEvent e) {
		System.out.println("학생으로 전환");
		identityType=false;
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

			String address = emailfield1.getText() + "@" + emailfield2.getText();
			//이메일 중복 체크
			if(bDao.checkDuplicateMail(address)) {
//					waitAlert.close();
					Alert alert = Util.showAlert("", "가입된 이메일 주소가 있습니다.", AlertType.INFORMATION);
					alert.showAndWait();
				
			} else {
				String txt = "인증메일 보내는 중";
				Alert waitAlert = Util.showAlert("", txt, AlertType.NONE);
				waitAlert.show();
				
				//인증번호 입력칸 생성
				this.certificationfield.setVisible(true);
				this.lblCertification.setText("인증번호를 입력해주세요.");
				
				//메일 값 넣기
				this.mail = new Mail(this.namefield.getText(), address);
				this.mail.mailSend();
				this.checkMail = this.mail.getCheckMail();
				if(this.checkMail) {
//					waitAlert.close();
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
				//타이머
				lblTimerSet();
				
				//인증번호 입력 안될 시 라벨
				BooleanBinding isCertification = Bindings.createBooleanBinding(
						() -> (this.certificationfield.isVisible() && this.certificationfield.getText().length() ==0 && !this.checkCertification),
						this.certificationfield.textProperty());
				this.lblCertification.visibleProperty().bind(isCertification);
				this.certificationNum = this.mail.getCertificationNum();
			
				//버튼 이름 및 기능 변경
				this.btnCertification.setText("확인");
				this.btnCertification.setOnAction(e->{
			
					if(!this.isTimerRunning) {
						
						Alert alert = Util.showAlert("", "인증시간이 초과되었습니다.\n 다시 인증해주세요.", AlertType.INFORMATION);
						alert.showAndWait();
						this.btnCertification.setText("메일인증발송");
						this.btnCertification.setOnAction(e1->{handleEmailCheck();});
						this.lblTimer.setVisible(false);
						
					} else {
						try {
							if(this.checkMail && (this.certificationfield.getText().length()==0)) {
							
								this.lblCertification.setVisible(true);
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
	}

	//완료버튼 선택
	public void handledone(ActionEvent e) {
		try {
			//아이디 입력 확인
			if(idfield.getText().length()==0) {
				idlabel.setVisible(true);
			}
			//이름 입력 확인
			if(namefield.getText().length()==0) {
				namelabel.setVisible(true);
			}
			//비밀번호 입력 확인
			if(passfield.getText().length()==0) {
				passlabel.setVisible(true);
			}
			//비밀번호 확인 입력 확인
			if(repassfield.getText().length()==0) {
				repasslabel.setVisible(true);
			}
			//이메일ID 입력 확인
			if(email2.getValue()==null) {
				emaillabel.setVisible(true);
			}
			//이메일 주소 입력 확인 
			if(emailfield1.getText().length()==0) {
				emaillabel.setVisible(true);
			}
			if(emailfield2.getText().length()==0) {
				emaillabel.setVisible(true);
			}
			//휴대폰번호 가운데 4자리 입력 확인
			if(phonefield1.getText().length()==0) {
				numberlabel.setVisible(true);
			}
			//휴대폰번호 마지막 4자리 입력 확인
			if(phonefield2.getText().length()==0) {
				numberlabel.setVisible(true);
			}
			//콤보박스 선택 확인
			if(combobox.getValue()==null) {
				numberlabel.setVisible(true);
			}
		
			String id = idfield.getText();
			String name = namefield.getText();
			String pass1 = passfield.getText();
			String pass2 = repassfield.getText();
			String email = emailfield1.getText()+"@"+emailfield2.getText();
			String pass = passfield.getText();
			String phone1 = phonefield1.getText();
			String phone2 = phonefield2.getText();
			String combo = (String) combobox.getValue();
			String phone = combo+phone1+phone2;
			
			if(!(pass1.equals(pass2))) { 
				repasslabel.setVisible(false);
				passchecklabel.setVisible(true);
				throw new Exception();
			}
			
			if(id.length()==0 || name.length()==0 || pass1.length()==0 
				|| pass2.length()==0 || email.length()==0 || phone1.length()==0 
				  || phone2.length()==0 || combobox.getValue()==null || email2.getValue()==null){
				
			 System.out.println("데이터가 없습니다.");
			 throw new Exception();
			}
			
			if(!(phonefield1.getText().length()==4)) {
				System.out.println("핸드폰 번호 4자리를 입력해 주세요.");
				throw new Exception();
			}
			else if(!(phonefield2.getText().length()==4)) {
				System.out.println("핸드폰 번호 4자리를 입력해 주세요.");
				throw new Exception();
			}
			if(recheck==0) { //중복체크를 안 했을시
				alert.setContentText("중복체크를 해 주세요.");
			    alert.show();
			    throw new Exception();
			}
		
			//인증메일발송여부
			if(!this.checkMail) {
				
				alert.setContentText("인증메일발송버튼을 눌러 주세요.");
				alert.show();
				throw new Exception();
			} 
			//메일 인증 여부
			if(!this.checkCertification) {
				
				alert.setContentText("메일을 인증해주세요.");
				alert.show();
				throw new Exception();
			}

			bDao.insertBoard(id, pass, name, email, phone ,identityType);	
	
			alert.setContentText("회원가입완료");
			alert.show();
			
			pop = (Stage)cancle.getScene().getWindow(); 
	        pop.close();
		} catch (Exception ex){
			System.out.println("회원가입 실패");
//			ex.printStackTrace();
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


	//취소버튼 
	public void handlecancle(ActionEvent e) {
		pop = (Stage)cancle.getScene().getWindow(); // 버튼을 통해서 현재 스테이지를 알아냄
        pop.close();
	}
    public void handlecheck(ActionEvent e) {
		
	    try {
		String id = idfield.getText();
		if(id.length()==0) {
			 alert.setContentText("아이디를 입력해주세요.");
			 alert.show();
			 throw new Exception();
		}
		bDao.check(id);
		
		recheck=1;
	   }catch(Exception ex) {System.out.println("회원가입 중복체크 에러");}
     }
}
