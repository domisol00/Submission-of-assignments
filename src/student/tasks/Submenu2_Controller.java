package student.tasks;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import launch.AppMain;
import main.Main_Master_Controller;
import util.Navigator;

public class Submenu2_Controller implements Initializable{
	
	@FXML private Button btn2_1;
	@FXML private Button btn2_2;

	public static Main_Master_Controller mmc;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		//현재과제 버튼
		this.btn2_1.setOnAction(e ->{
			AppMain.app.getBasic().setClass_no(0);
			Navigator.loadPages(Navigator.STUDENT_CURRENT_TASK_LIST);
		});
		//전체과제 버튼
		this.btn2_2.setOnAction(e -> {
			AppMain.app.getBasic().setClass_no(0);
			Navigator.loadPages(Navigator.STUDENT_FULL_TASK_LIST);
		});
	}
}