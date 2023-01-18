package util;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
;

public class Mail implements Initializable {

	@FXML private TextField receive; 
	@FXML private TextField title;
	@FXML private Button btnFile;
	@FXML private Button btnSend;
	@FXML private HTMLEditor contents;
	private String host = "smtp.gmail.com"; 						// 네이버일 경우 네이버 계정, gmail경우 gmail 계정 
	private String user = "projectfexam@gmail.com";  				// 보내는 사람의 메일 계정
	private String username = "과제제출프로그램";					// 보내는 이
	private String password = "rhjhilsijzuyzune";   				// 패스워드 - 지메일은 access용 비밀번호 입력해야됨
	private String user_name;
	private String to_user;											// 받는 사람 메일
	private String certificationNum;
	private boolean checkMail;

	//생성자
	public Mail(String user_name, String to_user) {
	
		this.user_name = user_name;
		this.to_user = to_user;
	}

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
	}
	
	//메일 보내기
	public void mailSend() { 
	
		//mail중복 체크
		
		
		//세션생성
		//SMTP 서버 정보를 설정한다. 
		Properties props = new Properties(); 
		props.put("mail.smtp.host", host); 
		props.put("mail.smtp.port", 587); 
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");			//지메일 사용시 추가 필요
 
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() { 
					return new PasswordAuthentication(user, password); 
				} 
			}); 
		
		try { 
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user, username));										// 보내는 사람 설정
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to_user));				// 받는 사람 설정			
			message.setSubject("메일 인증 번호 발송 - 과제제출프로그램");								// 메일 제목
			
			//랜덤번호 생성
			randomCertification();
			String msg = this.user_name+"님, 안녕하세요. <br/>"
						+ "과제제출 프로그램 인증번호 발송 메일입니다. <br/>"
						+ "인증번호는 [ " + this.certificationNum +" ]입니다.<br/>"
						+ "인증번호 입력칸에 인증번호를 입력해주세요.";
			
			message.setContent(msg, "text/html;charset=utf-8");
		
			//메일 내용 보내기 
			Transport.send(message); 
			System.out.println("메일 보내기 성공");
			this.checkMail = true;
		} catch (Exception e1) { e1.printStackTrace(); } 
	}
	
	//인증번호 생성
	private void randomCertification() {
		
		//a~z(97~122)까지 다섯 글자 무작위 생성
		char[] char_arr = new char[5];
		
		for(int i = 0 ; i < char_arr.length ; i++) {
			
			char_arr[i] = (char)((int)(Math.random()*26)+97);
		}
	
		this.certificationNum = String.valueOf(char_arr);
	}
	
	public String getCertificationNum() {
		
		return this.certificationNum;
	}
	
	public boolean getCheckMail() {
		
		return this.checkMail;
	}
}