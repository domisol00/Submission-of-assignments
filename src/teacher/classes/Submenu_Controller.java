package teacher.classes;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import main.Main_Master_Controller;
import util.Navigator;

public class Submenu_Controller implements Initializable{
	
	@FXML Button btn1_1;
	@FXML Button btn1_2;
	@FXML Button btn1_3;
	
	public static Main_Master_Controller mmc;
	public static int btnNo;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		//현재강의 버튼
		btn1_1.setOnAction(e ->{
			btnNo = 0;
			Navigator.loadPages(Navigator.TEACHER_CLASS_LIST);
		});
		
		//지난강의 버튼
		btn1_2.setOnAction(e -> {
			btnNo = 1;
			Navigator.loadPages(Navigator.TEACHER_CLASS_LIST);
		});
		
		//전체강의보기 버튼
		btn1_3.setOnAction(e -> {
			btnNo = 2;
			Navigator.loadPages(Navigator.TEACHER_CLASS_LIST);
		});
	}
}