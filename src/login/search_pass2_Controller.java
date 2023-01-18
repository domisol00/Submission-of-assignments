package login;

import java.net.URL;
import java.util.ResourceBundle;

import DAO.BasicDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import util.Util;


public class search_pass2_Controller implements Initializable {
	
	@FXML private PasswordField passfield;
	@FXML private PasswordField repassfield;
	@FXML private Button done;
	@FXML private Button cancle; 
	
	private Stage pop;
	BasicDao bDao = new BasicDao();
	
	public void initialize(URL location, ResourceBundle resources) {

	//입력양식 제한
	passfield.textProperty().addListener(Util.pwListener(passfield,null));
	repassfield.textProperty().addListener(Util.pwListener(repassfield,null));
	//글자수 제한
	passfield.textProperty().addListener(Util.textCountLimit(passfield, 20));
	repassfield.textProperty().addListener(Util.textCountLimit(repassfield, 20));
	
	done.setOnAction(e->handledone(e));
	cancle.setOnAction(e->handlecancle(e));
			
	}

	public void handledone(ActionEvent e) {
		try {
		
			if(passfield.getText().length()==0) {
				System.out.println("비밀번호를 입력해주세요");
				throw new Exception();
			}
			String pass = passfield.getText();
			
			if(repassfield.getText().length()==0) {
				System.out.println("비밀번호를 입력해주세요");
				throw new Exception();
			}
			
			String repass = repassfield.getText();
			
			if(!(pass.equals(repass))) {
				System.out.println("비밀번호를 다시 확인하세요.");
				throw new Exception();
			}
			
			bDao.search_pass2(pass);
			
			pop = (Stage)cancle.getScene().getWindow(); // 버튼을 통해서 현재 스테이지를 알아냄
	        pop.close();
			
		}
		catch (Exception ex){
			System.out.println("비밀번호 찾기 실패");
			System.out.println(ex.getMessage());
		}
	}
	
	public void handlecancle(ActionEvent e) {
		pop = (Stage)cancle.getScene().getWindow(); // 버튼을 통해서 현재 스테이지를 알아냄
        pop.close();
	}
}
