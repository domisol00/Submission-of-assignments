package student.tasks;
/*
 * 작성자 : 도현호
 */
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import DAO.AssignmentDao;
import DTO.AssignmentDto;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Callback;
import launch.AppMain;
import student.tasks.popup.AssignmentPopupController_no;
import student.tasks.popup.AssignmentPopupController_yes;

public class Current_Task_Controller implements Initializable{

	@FXML private AnchorPane assignPane;
	@FXML private TableView<AssignmentDto> tableView;
	@FXML private TableColumn<AssignmentDto, Integer> colTaskNo;
	@FXML private TableColumn<AssignmentDto, String> colClassName;
	@FXML private TableColumn<AssignmentDto, String> colTaskName;
    @FXML private TableColumn<AssignmentDto, String> colTaskSubmit;
    @FXML private TableColumn<AssignmentDto, Date> colTaskSubmissionDate;
    @FXML private TableColumn<AssignmentDto, Date> colTaskDeadlineDate;
    @FXML private TableColumn<AssignmentDto, Integer> colTaskScore;	
    @FXML private TableColumn<AssignmentDto, Void> colTaskAssign;
    @FXML private ComboBox<String> combo;
    @FXML private ProgressBar pgbSubmitCnt;
    @FXML private Label lblSubmitTask;
    @FXML private Label lblTotalTask;

    private Task<Void> task;
    private ObservableList<AssignmentDto> list;
    private ObservableList<String> classList;
    private AssignmentDto assign = new AssignmentDto();
	private AssignmentDao aDao = new AssignmentDao();
	private AssignmentPopupController_no popupController_no;
	private AssignmentPopupController_yes popupController_yes; 
	private String student_id = AppMain.app.getBasic().getId();				//학생ID - 추후 연동 필요
	private int class_no = 	AppMain.app.getBasic().getClass_no();			//강의번호 - 추후 연동 필요
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		assignTableView();												//DB값 불러오기
		this.tableView.setOnMouseClicked(e -> handleDoubleClicked(e));		//더블클릭 event 처리
		setCombo();														//콤보박스
	}
	
	//테이블 내용 출력
	private void assignTableView() {
		
		if(this.class_no==0) {								//강의가 지정 안된 경우
			this.list = this.aDao.assignment_selectAll(this.student_id);
		} else {											//강의가 지정 된 경우
			this.list = this.aDao.assignment_selectAll(this.class_no, this.student_id);
		}
		this.colTaskNo.setCellValueFactory(new PropertyValueFactory<>("taskList_no"));
		this.colClassName.setCellValueFactory(new PropertyValueFactory<>("class_name"));
		this.colTaskName.setCellValueFactory(new PropertyValueFactory<>("task_name"));
		this.colTaskSubmit.setCellValueFactory(new PropertyValueFactory<>("submitornot"));
		this.colTaskSubmissionDate.setCellValueFactory(new PropertyValueFactory<>("reg_date"));		//수정필요
		this.colTaskDeadlineDate.setCellValueFactory(new PropertyValueFactory<>("expire_date"));
		this.colTaskScore.setCellValueFactory(new PropertyValueFactory<>("Score"));
		addButton();													//버튼생성
		this.tableView.setItems(this.list);
		viewTaskCount();
	}
	
	
	//상세정보 버튼 만들기
	private void addButton() {
		
		Callback<TableColumn<AssignmentDto, Void>, TableCell<AssignmentDto, Void>> cellFactory
				= new Callback<TableColumn<AssignmentDto, Void>, TableCell<AssignmentDto, Void>>(){
	
			@Override
			public TableCell<AssignmentDto, Void> call(final TableColumn<AssignmentDto, Void> param){
				
				final TableCell<AssignmentDto, Void> cell = new TableCell<AssignmentDto, Void>(){
					
					private final Button btn = new Button("제출하기");
					
					{
						btn.setOnAction((event)->{
							
							assign = getTableView().getItems().get(getIndex());
							System.out.println("selectedData(버튼) : " + assign.getSubmitornot());
						
							//과제번호 생성
							AppMain.app.getBasic().setClass_no(assign.getClass_no());
							AppMain.app.getBasic().setTask_no(assign.getTask_no());
							
							if(assign.getSubmitornot().equals("N")) {
								openPopupWindow_no(btn.getScene().getWindow());							//처음 제출시 상세화면창 열기
								refreshTable(0);									//저장 후 리스트 새로 고침
							} else if(assign.getSubmitornot().equals("Y")) {
								openPopupWindow_yes(btn.getScene().getWindow());							//재 제출시 상세화면창 열기
								refreshTable(0);									//저장 후 리스트 새로 고침
							}
						});
					}
					
					@Override
					public void updateItem(Void item, boolean empty) {
						
						super.updateItem(item, empty);
						if(empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}
				};
				return cell;
			}
		};
		this.colTaskAssign.setCellFactory(cellFactory);
	}
	
	//버튼 클릭시 수강신청 상세화면 띄우기
	private void openPopupWindow_no(Window window) {
	
		this.popupController_no = new AssignmentPopupController_no(window);
		this.popupController_no.showStage();
		refreshTable(0);											//저장 후 리스트 새로 고침
	}
	
	//버튼 클릭시 수강신청 상세화면 띄우기
	private void openPopupWindow_yes(Window window) {
		
		this.popupController_yes = new AssignmentPopupController_yes(window);
		this.popupController_yes.showStage();
		refreshTable(0);											//저장 후 리스트 새로 고침
	}
	
	//강의별 과제 목록
	private void setCombo() {
		
		this.classList = aDao.current_className_selectAll(this.student_id);
		this.classList.add(0,"전체보기");
		this.combo.setItems(this.classList);

		//선택된 강의가 있을 때 콤보박스 설정
		if(this.class_no != 0) {
			int c_no = 0;
			for(int i = 0 ; i < this.classList.size(); i++) {
			
				c_no =Integer.valueOf(this.classList.get(i).substring(1, 5)); 		//문자열에서 강의번호자르기
				if(c_no == this.class_no) {
					
					this.combo.getSelectionModel().select(this.classList.get(i));
					return;
				}
			}
		}
		
		this.combo.setOnAction(e -> {

			if(this.combo.getSelectionModel().getSelectedIndex() == 0) {
				
				this.class_no=0;
				refreshTable(0);
			} else {
				String selectedCombo = combo.getSelectionModel().getSelectedItem();
				int selectedNo = Integer.valueOf(selectedCombo.substring(1, 5));
			
				AppMain.app.getBasic().setClass_no(selectedNo);
				refreshTable(1);
			}
		});
	}
	
	//더블클릭 핸들러
	private void handleDoubleClicked(MouseEvent event) {
	
		AssignmentDto assign = this.tableView.getSelectionModel().getSelectedItem();
		if(assign == null) { 
			//아무것도 없을 때 더블클릭 하면 아무것도 안하기
		} else {
		
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				
				if (event.getClickCount() == 2) {
					
					AppMain.app.getBasic().setClass_no(assign.getClass_no());
					AppMain.app.getBasic().setTask_no(assign.getTask_no());
					
					System.out.println("selectedData(더블클릭) : " + assign.getSubmitornot());
					if(assign.getSubmitornot().equals("N")) {
						
						openPopupWindow_no(this.tableView.getScene().getWindow());							//처음 제출시 상세화면창 열기
						refreshTable(0);									//저장 후 리스트 새로 고침
					} else if(assign.getSubmitornot().equals("Y")){
						
						openPopupWindow_yes(this.tableView.getScene().getWindow());							//재 제출시 상세화면창 열기
						refreshTable(0);									//저장 후 리스트 새로 고침
					}
				}
			}
		}
	}

	//데이터 저장 후 리스트 새로고침
	private void refreshTable(int num) {
		
		if(num==0) {						//강의번호 호출 안함
			
		} else if(num==1) {					//강의번호 호출 함
			this.class_no = AppMain.app.getBasic().getClass_no();
		}
	
		if(this.class_no==0) {				//강의가 지정 안된 경우
			this.list = aDao.assignment_selectAll(this.student_id);
		} else {							//강의가 지정 된 경우
			this.list = aDao.assignment_selectAll(this.class_no, this.student_id);
		}
		this.tableView.setItems(this.list);
		viewTaskCount();
	}

	//과제 제출 현황 보여주기
	private void viewTaskCount() {
	
		if(this.class_no==0) {
			
			this.assign.setCntMyAssign(aDao.myCount_ing_submit_select(this.student_id));			//제출과제수
			this.assign.setCntTotalAssign(aDao.myCount_ing_total_select(this.student_id));			//전체과제수
		} else {
			
			this.assign.setCntMyAssign(aDao.myCount_ing_submit_select(this.student_id, this.class_no));			//제출과제수
			this.assign.setCntTotalAssign(aDao.myCount_ing_class_select(this.student_id, this.class_no));		//전체과제수
		}
		int MyAssign = this.assign.getCntMyAssign();
		int TotalAssign = this.assign.getCntTotalAssign();
	
		//progressBar 지정
		this.task = new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
			
				for(int i = 1; i< 10 ; i++) {
					
					if(isCancelled()) { break; }
					
					updateProgress(MyAssign, TotalAssign);
					
					try { Thread.sleep(100); } catch (Exception e) { }
					
					if(isCancelled()) { break; }
				}
				
				return null;
			}
		};
		this.pgbSubmitCnt.progressProperty().bind(this.task.progressProperty());
		
		Thread thread = new Thread(this.task);
		thread.setDaemon(true);
		thread.start();
		lblSubmitTask.setText(Integer.toString(MyAssign));
		lblTotalTask.setText(Integer.toString(TotalAssign));
	}
}