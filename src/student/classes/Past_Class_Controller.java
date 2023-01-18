package student.classes;
/*
 * 작성자 : 도현호
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import launch.AppMain;
import util.Navigator;

public class Past_Class_Controller implements Initializable{
	
	@FXML private TableView<EnrollmentDto> tableView;
	@FXML private TableColumn<EnrollmentDto, Integer> colClassNo;
	@FXML private TableColumn<EnrollmentDto, String> colClassName;
	@FXML private TableColumn<EnrollmentDto, String> colClassDate;
	@FXML private TableColumn<EnrollmentDto, String> colClassTeacher;
	@FXML private TableColumn<EnrollmentDto, Integer> colClassTask;
	@FXML private TableColumn<EnrollmentDto, String> colClassStatus;	
    @FXML private ComboBox<String> choiceField;
    @FXML private TextField txtSearch;
    @FXML private Button btnSearch;
   
    private ObservableList<EnrollmentDto> list;
    private EnrollmentDao eDao = new EnrollmentDao();
    private String stu_id = AppMain.app.getBasic().getId();
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		pastTableView();								//리스트 출력
		this.btnSearch.setOnAction(e-> handleBtnSearch());	//검색단추 클릭
		//검색 시 엔터 효과
		this.txtSearch.setOnKeyPressed(e ->{ 
			if(e.getCode() == KeyCode.ENTER) {
				
				handleBtnSearch();
			}
		});
		//더블클릭시 효과
		this.tableView.setOnMouseClicked(e->{handleClicked(e);});
	}
	
	//table에 내용 출력하기
	private void pastTableView() {

		this.list = eDao.past_selectAll(this.stu_id);
		this.colClassNo.setCellValueFactory(new PropertyValueFactory<>("classno"));
		this.colClassName.setCellValueFactory(new PropertyValueFactory<>("classname"));
		this.colClassDate.setCellValueFactory(new PropertyValueFactory<>("period"));
		this.colClassTeacher.setCellValueFactory(new PropertyValueFactory<>("teachername"));
		this.colClassTask.setCellValueFactory(new PropertyValueFactory<>("taskCount"));
		this.colClassStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		this.tableView.setItems(this.list);
	}

	//검색단추
	private void handleBtnSearch() {
	
		String search_word = txtSearch.getText();
		this.list = eDao.search_past_selectAll(this.stu_id, search_word);
		this.tableView.setItems(this.list);
	}
	
	//더블클릭 시 이벤트->과제리스트로 넘어가기
	private void handleClicked(MouseEvent e) {
	
		if(e.getClickCount()==2) {
			
			EnrollmentDto selectedRowClass = this.tableView.getSelectionModel().getSelectedItem();
			if(selectedRowClass != null) {
			
				//AppMain에 강의번호 입력
				AppMain.app.getBasic().setClass_no(selectedRowClass.getClassno());
				Navigator.loadPages(Navigator.STUDENT_FULL_TASK_LIST);
				Navigator.loadSubMenu(Navigator.STUDENT_TASK_MENU);
			}
		}
	}
}