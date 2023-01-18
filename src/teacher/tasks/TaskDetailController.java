package teacher.tasks;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

import DAO.TaskDao;
import DAO.TaskDetailDao;
import DTO.TaskDetailDto;
import DTO.TaskDto;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import launch.AppMain;
import main.Main_Master_Controller;
import util.Util;

/**
 * @author 김지현
 *
 */
public class TaskDetailController extends Main_Master_Controller implements Initializable {

	@FXML
	private AnchorPane taskDetailListPane;
	@FXML
	private Label lblTaskTitle;
	@FXML
	private ComboBox<String> comboTaskList;

	@FXML
	private TableView<TaskDetailDto> tblView;
	@FXML
	private TableColumn<TaskDetailDto, ?> tcNo;
	@FXML
	private TableColumn<TaskDetailDto, ?> tcName;
	@FXML
	private TableColumn<TaskDetailDto, String> tcSubmitStatus;
	@FXML
	private TableColumn<TaskDetailDto, ?> tcSubmitDate;
	@FXML
	private TableColumn<TaskDetailDto, ?> tcScore;
	@FXML
	private TableColumn<TaskDetailDto, String> tcMark;
	@FXML
	private TableColumn<TaskDetailDto, Boolean> tcBtn;

	@FXML
	private Label lblSubmitRate;
	@FXML
	private ProgressBar pbarSubmitRate;

	@FXML
	private Label lblAvg;
	@FXML
	private ProgressBar pbarAvg;

	@FXML
	private Button btnExport;

	int taskNo = AppMain.app.getBasic().getTask_no();

	TaskDao tDao = new TaskDao();
	TaskDto currentTask = tDao.selectTask(taskNo);

	ObservableList<TaskDetailDto> tdList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// 과제명 표시 라벨링
		setLabel();

		// 콤보박스에 과제목록 표시
		setCombo();

		// 테이블에 과제별 학생 제출 정보 표시
		setTableColumns();

		//엑셀 반출 바튼 클릭 시
		btnExport.setOnAction(e -> {
			//파일명과 시트명 초기설정
			String fileName = "["+currentTask.getTcNo()+"]"+currentTask.getTcTitle()+" 제출현황 및 점수";
			String sheetName = "과제 "+currentTask.getTcNo();
			
			Util.excelExport(fileName, sheetName, tblView);
		});

	}

	// 상단 라벨에 과제명 표시
	private void setLabel() {
		String taskTitle = currentTask.getTcTitle();
		lblTaskTitle.setText(taskTitle);
	}

	// 현재 유저-현재 강의-과제목록 표시
	private void setCombo() {
		ObservableList<String> classTaskList = FXCollections.observableArrayList();
		TaskDao tDao = new TaskDao();
		int classNo = currentTask.getClassNo();
		System.out.println(classNo);
		classTaskList = tDao.selectUserTaskComoboList(classNo);

		comboTaskList.setItems(classTaskList);

		// 콤보박스에 현재 과제명을 보여준다.
		if (taskNo != 0) {
			String taskName = currentTask.getTcTitle();
			String taskInfo = "[" + taskNo + "] " + taskName;
			comboTaskList.getSelectionModel().select(taskInfo);
		}

		// 콤보박스에서 과제명 선택 시 이동
		comboTaskList.setOnAction(e -> {
			String selectedCombo = comboTaskList.getSelectionModel().getSelectedItem();
			int selectedNo = Integer
					.valueOf(selectedCombo.substring(selectedCombo.indexOf("[") + 1, selectedCombo.lastIndexOf("]")));
			System.out.println(selectedNo);

			// 선택된 과제로 데이터변경
			taskNo = selectedNo;
			currentTask = tDao.selectTask(taskNo);

			Platform.runLater(() -> {
				setLabel();
			});
			refreshTable();
		});
	}

	// 컬럼 설정
	private void setTableColumns() {
		tcNo.setCellValueFactory(new PropertyValueFactory<>("colNum"));
		tcName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
		tcSubmitStatus.setCellValueFactory(new PropertyValueFactory<>("submitStatus"));
		tcSubmitDate.setCellValueFactory(new PropertyValueFactory<>("submitDate"));
		tcScore.setCellValueFactory(new PropertyValueFactory<>("colScore"));
		tcMark.setCellValueFactory(new PropertyValueFactory<>("colMarkStatus"));
		tcBtn.setCellValueFactory(new PropertyValueFactory<>("btnDetail"));
		tcBtn.setCellFactory(setButton());

		// cell에 색상 넣어서 강조
		setCellColor(tcSubmitStatus, "N", "Y", "highlightRed");
		setCellColor(tcMark, "채점 전", "채점 완료", "highlightYellow");

		// 테이블 채우기
		refreshTable();
	}

	private void setCellColor(TableColumn<TaskDetailDto, String> col, String applyCellValue, String defaultCellValue,
			String styleClass) {

		col.setCellFactory(new Callback<TableColumn<TaskDetailDto, String>, TableCell<TaskDetailDto, String>>() {

			@Override
			public TableCell<TaskDetailDto, String> call(TableColumn<TaskDetailDto, String> param) {
				return new TableCell<TaskDetailDto, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						ObservableList<String> styles = getStyleClass();

						if (item != null && !empty) {
							setText(item);

							if (item.equals(applyCellValue) && !styles.contains(styleClass)) {

								getStyleClass().remove("table-row-cell");
								getStyleClass().add(styleClass);

							} else if (item.equals(defaultCellValue)) {
								styles.removeAll(Collections.singleton(styleClass));
							}
						}

					}
				};
			}

		});
	}

	// 상세보기 컬럼설정
	private Callback<TableColumn<TaskDetailDto, Boolean>, TableCell<TaskDetailDto, Boolean>> setButton() {
		return item -> new TableCell<TaskDetailDto, Boolean>() {

			private final Button btnDetail = new Button("상세보기");

			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null) {
					setGraphic(null);
				} else {
					btnDetail.setPrefWidth(80);

					// 기본값 버튼활성화
					btnDetail.setDisable(false);

					// 과제가 제출되지 않았으면 버튼 비활성화
					TaskDetailDto selectedTdDto = getTableView().getItems().get(getIndex());
					String status = selectedTdDto.getSubmitStatus();

					if (status.equals("N")) {
						btnDetail.setDisable(true);
					}

					setGraphic(btnDetail);

					// 상세버튼 누르면 상세창
					btnDetail.setOnAction(e -> handleBtnDetail(selectedTdDto));
				}
			}

			// 과제상세(채점)창
			public void handleBtnDetail(TaskDetailDto selectedTdDto) {
				try {
					// new stage - 제출된 과제 과제 확인창
					Stage stage = new Stage(StageStyle.UTILITY);
					stage.initOwner(tblView.getScene().getWindow());
					stage.initModality(Modality.WINDOW_MODAL);
					stage.setTitle("과제 확인");

					// fmxl, controller
//					FXMLLoader loader = new FXMLLoader(getClass().getResource("../../fxml/teacher/tasks/TaskMark.fxml"));
					FXMLLoader loader = new FXMLLoader(Class.forName("teacher.tasks.TaskDetailController")
							.getResource("/fxml/teacher/tasks/TaskMark.fxml"));
					loader.setController(new TaskMarkController(selectedTdDto));

					Parent parent = loader.load();
					stage.setScene(new Scene(parent));
					stage.setResizable(false);
					stage.show();

					// 채점완료 버튼 클릭 시
					Button btnSubmit = (Button) parent.lookup("#btnSubmit");
					btnSubmit.setOnMouseClicked(mouseEvent -> {

						// alert
						Alert alert = new Alert(AlertType.INFORMATION, "채점이 완료되었습니다.", ButtonType.CLOSE);
						alert.setTitle("채점 완료");

						// 닫기버튼 누르면 창 닫힘
						Optional<ButtonType> btn = alert.showAndWait();
						if (btn.get().equals(ButtonType.CLOSE)) {
							stage.close();
						}

						// tableView refresh
						refreshTable();
					});

					// 취소버튼 클릭 시 새창 닫기
					Button btnCancel = (Button) parent.lookup("#btnCancel");
					btnCancel.setOnMouseClicked(mouseEvent -> stage.close());

				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		};
	}

	// db에서 저장된 데이터 불러와서 넣기
	private void refreshTable() {
		TaskDetailDao tdDao = new TaskDetailDao();
		tdList = tdDao.selectTaskStudentList(taskNo);
		tblView.setItems(tdList);

		// 제출율, 평균
		setSubmiitedInfo();

	}

	// 제출 정보(제출율, 평균) 표시
	public void setSubmiitedInfo() {
		Platform.runLater(() -> {
			int totalNum = tdList.size(); // 전체학생수
			int count = 0; // 제출한 학생수
			int totalScore = 0;
			int pscore = 0;

			for (int i = 0; i < totalNum; i++) {
				String submit = tdList.get(i).getSubmitStatus();
				Integer getScore = tdList.get(i).getTaskScore();
				pscore = tdList.get(i).getFullscore();

				// 제출한 학생만 계산
				if (submit.equals("Y")) {
					count++;
					if (getScore != null && getScore.intValue() != -1) {
						totalScore += getScore;
					}
				}
			}

			double avg = (double) totalScore / count;

			// 제출율 표시
			lblSubmitRate.setText(count + " 명/ " + totalNum + " 명");
			pbarSubmitRate.setProgress((double) count / totalNum);

			// 과제 평균점수 표시
			if (count == 0) {
				lblAvg.setText("0 점 / " + pscore + "점");
			} else {
				lblAvg.setText(new DecimalFormat("##.##").format(avg) + "점 / " + pscore + "점");
			}
			pbarAvg.setProgress(avg / pscore);
		});
	}
}