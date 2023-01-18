package student.classes;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import main.Main_Master_Controller;
import util.Navigator;

public class Submenu1_Controller implements Initializable{
	
	@FXML Button btn1_1;
	@FXML Button btn1_2;
	@FXML Button btn1_3;

	public static Main_Master_Controller mmc;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		//현재강의 버튼
		this.btn1_1.setOnAction(e ->{
			
			Navigator.loadPages(Navigator.STUDENT_CURRENT_CLASS_LIST);
		});
		//지난강의 버튼
		this.btn1_2.setOnAction(e -> {
			
			Navigator.loadPages(Navigator.STUDENT_PAST_CLASS_LIST);
		});
		//수강신청 버튼
		this.btn1_3.setOnAction(e -> {
			
			Navigator.loadPages(Navigator.STUDENT_FULL_CLASS_LIST);
		});
	}
}