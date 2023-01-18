package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import main.Main_Master_Controller;

/**
 * @author 김지현
 */
public class Navigator {
	
	//강사
	public static final String TEACHER_CLASS_LIST = "/fxml/teacher/classes/teacher_main.fxml";
	public static final String TEACHER_TASK_LIST = "/fxml/teacher/tasks/TaskList.fxml";
	public static final String TEACHER_TASK_DETAIL_LIST = "/fxml/teacher/tasks/TaskListDetail.fxml";
	public static final String TEACHER_STUDENT_MANAGER = "/fxml/teacher/classes/teacher_main2.fxml";

	//강사-메뉴
	public static final String TEACHER_CLASS_MENU = "/fxml/teacher/classes/class_menu.fxml";
	public static final String TEACHER_TASK_MENU = "/fxml/teacher/tasks/Task_subMenu.fxml";

	//학생-메뉴
	public static final String STUDENT_CLASS_MENU = "/fxml/student/classes/class_menu.fxml";
	public static final String STUDENT_TASK_MENU = "/fxml/student/tasks/task_menu.fxml";
	
	//학생-리스트(강의)
	public static final String STUDENT_CURRENT_CLASS_LIST = "/fxml/student/classes/current_class.fxml";
	public static final String STUDENT_PAST_CLASS_LIST = "/fxml/student/classes/past_class.fxml";
	public static final String STUDENT_FULL_CLASS_LIST = "/fxml/student/classes/enrollment.fxml";
	
	//학생-리스트(과제)
	public static final String STUDENT_FULL_TASK_LIST = "/fxml/student/tasks/full_task.fxml";
	public static final String STUDENT_CURRENT_TASK_LIST = "/fxml/student/tasks/current_task.fxml";

	//하위메뉴
	public static void loadSubMenu(String fxml) {
		try {
			Node node = FXMLLoader.load(Class.forName("util.Navigator").getResource(fxml));
			Main_Master_Controller.mmc.setSubMenu(node);
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("[Sub Menu Error : " + e.getMessage() + "]");
		}
	}
	
	//메인페이지
	public static void loadPages(String fxml) {
		try {
			Node node = FXMLLoader.load(Class.forName("util.Navigator").getResource(fxml));
			Main_Master_Controller.mmc.setPage(node);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[Mainpage Error : " + e.getMessage() + "]");
		}
	}
}