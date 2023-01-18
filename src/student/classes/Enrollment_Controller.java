package student.classes;
/*
 * 작성자 : 도현호
 * 최종 작성일 : 2020-05-15
 */
import java.net.URL;
import java.util.ResourceBundle;

import DAO.EnrollmentDao;
import DTO.EnrollmentDto;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Callback;
import launch.AppMain;
import student.classes.popup.EnrollmentPopupController;

public class Enrollment_Controller implements Initializable{

	@FXML private AnchorPane anchorPane;
	@FXML private Button currentClass;
	@FXML private Button lastClass;
	@FXML private Button allClass;
	@FXML private TableView<EnrollmentDto> tableView;
	@FXML private TableColumn<EnrollmentDto, Integer> colClassNo;
	@FXML private TableColumn<EnrollmentDto, String> colClassName;
	@FXML private TableColumn<EnrollmentDto, String> colClassDate;
	@FXML private TableColumn<EnrollmentDto, String> colClassTeacher;
	@FXML private TableColumn<EnrollmentDto, String> colClassPeople;
	@FXML private TableColumn<EnrollmentDto, Void> colClassEnroll;
	@FXML private TableColumn<EnrollmentDto, Void> colClassButton;
	@FXML private ComboBox<String> choiceField;
	@FXML private TextField txtSearch;
	@FXML private Button btnSearch;

	private ObservableList<EnrollmentDto> list;
	private EnrollmentDao eDao = new EnrollmentDao();
	private EnrollmentPopupController popupController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		enrollTableView();													//DB내용 출력
		this.tableView.setOnMouseClicked(e -> handleDoubleClicked(e));		//더블클릭
		this.btnSearch.setOnAction(e-> handleBtnSearch());					//검색단추 클릭
		//검색 시 엔터 효과
		this.txtSearch.setOnKeyPressed(e ->{ 
			if(e.getCode() == KeyCode.ENTER) {
				
				handleBtnSearch();
			}
		});
	}
	
	//table에 내용 출력하기
	private void enrollTableView() {

		this.list = eDao.enrollment_selectAll();
		this.colClassNo.setCellValueFactory(new PropertyValueFactory<>("classno"));
		this.colClassName.setCellValueFactory(new PropertyValueFactory<>("classname"));
		this.colClassDate.setCellValueFactory(new PropertyValueFactory<>("period"));
		this.colClassTeacher.setCellValueFactory(new PropertyValueFactory<>("teachername"));
		this.colClassPeople.setCellValueFactory(new PropertyValueFactory<>("str"));
		addButton();													//버튼 추가
		this.tableView.setItems(this.list);
	}
	
	//상세정보 버튼 만들기
	private void addButton() {
		
		Callback<TableColumn<EnrollmentDto, Void>, TableCell<EnrollmentDto, Void>> cellFactory
				= new Callback<TableColumn<EnrollmentDto, Void>, TableCell<EnrollmentDto, Void>>(){
	
			@Override
			public TableCell<EnrollmentDto, Void> call(final TableColumn<EnrollmentDto, Void> param){
				
				final TableCell<EnrollmentDto, Void> cell = new TableCell<EnrollmentDto, Void>(){
					
					private final Button btn = new Button("상세정보");
					
					{
						btn.setOnAction((event)->{
							
							EnrollmentDto enroll = getTableView().getItems().get(getIndex());
							//클래스번호 생성
							int c_no = enroll.getClassno();
							AppMain.app.getBasic().setClass_no(c_no);
							openPopupWindow(btn.getScene().getWindow());
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
		this.colClassButton.setCellFactory(cellFactory);
	}

	//버튼 클릭시 수강신청 상세화면 띄우기
	private void openPopupWindow(Window window) {

		this.popupController = new EnrollmentPopupController(window);
		this.popupController.showStage();
		handleBtnSearch();
	}
	
	//더블클릭 핸들러
	private void handleDoubleClicked(MouseEvent event) {

		EnrollmentDto enroll = tableView.getSelectionModel().getSelectedItem();
		if(enroll == null) {
			//빈화면일 때 더블클릭 시 아무것도 안하기
		} else {
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				
				if (event.getClickCount() == 2) {
					
					//클래스번호 생성
					int c_no = enroll.getClassno();
					AppMain.app.getBasic().setClass_no(c_no);
					openPopupWindow(tableView.getScene().getWindow());
				}
			}
		}
	}

	//검색 및 리스트 새로고침
	private void handleBtnSearch() {
	
		String search_word = this.txtSearch.getText();
		this.list = eDao.search_selectAll(search_word);
		this.tableView.setItems(this.list);
	}
}