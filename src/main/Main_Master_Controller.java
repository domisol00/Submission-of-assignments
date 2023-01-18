package main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import DTO.BasicDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import launch.AppMain;

/**
 * @author 김지현
 *
 */
public class Main_Master_Controller implements Initializable {

	public static Main_Master_Controller mmc;
	@FXML private BorderPane borderPane;
	@FXML private StackPane innerPane;
	@FXML private AnchorPane subMenu;
	@FXML private Label lbl;
	@FXML protected Button btn1;
	@FXML protected Button btn2;
	@FXML protected Button btn3;
	@FXML protected Button btn4;
	@FXML protected Button btn1_1;
	@FXML protected Button btn1_2;
	@FXML protected Button btn1_3;
	@FXML private ImageView imgView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		mmc = this;
		// 현재 로그인한 유저 정보 받아오기
		BasicDto user = AppMain.app.getBasic();
		// 현재 로그인 사용자 정보
		setUserInfoLabel(user);
		// 로그아웃
		btn4.setOnAction(e -> handleBtn4(e));
	
	}

	// 로그인 시 사용자 정보 라벨링
	   private void setUserInfoLabel(BasicDto u) {
	      // 신분 식별 - true:강사, false:학생
	      boolean type = u.gettype2();
	      String position = "";
	      if (type) {
	         position = "[선생님] ";
	      } else {
	         position = "[학생] ";
	      }

	      // 라벨링 ( 신분 + 이름 )
	      lbl.setText(position + u.getName() + " 님");
	   }

	// 로그아웃 버튼
	private void handleBtn4(ActionEvent e) {
		try {
			// db연결 끊기
//			JdbcUtil.disconnect();

			// 로그인페이지 보여주고
			Stage stage = new Stage();
			AppMain.app.showLogin(stage);

			// 현재창은 닫음
			((Node) (e.getSource())).getScene().getWindow().hide();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	// subMenu에 fxml 넣기
	public void setSubMenu(Node node) {
		subMenu.getChildren().add(node);
	}

	// innerpane에 fxml 넣기
	public void setPage(Node node) {
		innerPane.getChildren().add(node);
	}
}