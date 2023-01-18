package teacher.tasks;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import util.Navigator;

public class TaskSubMenuController implements Initializable {

	@FXML
	Button btn1_1;
	@FXML
	Button btn1_2;
	@FXML
	Button btn1_3;

	public static int btnNo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// 현재과제-1
		btn1_1.setOnAction(e -> {
			System.out.println("과제-현재과제");
			btnNo = 1;
			Navigator.loadPages(Navigator.TEACHER_TASK_LIST);
		});

		// 지난과제-2
		btn1_2.setOnAction(e -> {
			System.out.println("과제-지난과제");
			btnNo = 2;
			Navigator.loadPages(Navigator.TEACHER_TASK_LIST);
		});

		// 전체과제-0
		btn1_3.setOnAction(e -> {
			System.out.println("과제-전체과제");
			btnNo = 0;
			Navigator.loadPages(Navigator.TEACHER_TASK_LIST);
		});
	}
}