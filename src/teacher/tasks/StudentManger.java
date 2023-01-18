package teacher.tasks;

import java.net.URL;
import java.util.ResourceBundle;

import DAO.ClassDao;
import DAO.StudentDao;
import DTO.BasicDto;
import DTO.ClassDto;
import DTO.StudentDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import launch.AppMain;
import main.Main_Master_Controller;
import util.Navigator;

/**
 * @author 김지현
 */
public class StudentManger extends Main_Master_Controller implements Initializable {
	@FXML
	private StackPane stackPane; @FXML
	protected AnchorPane reportListPane;
	@FXML private TableView<StudentDto> tblViewReport;
	@FXML private TableColumn<StudentDto, ?> stNo;
	@FXML private TableColumn<StudentDto, ?> stDesc;
	@FXML private TableColumn<StudentDto, ?> stTitle;
	@FXML private TableColumn<StudentDto, ?> stFile;
	@FXML private Button btnReportCreate;

	BasicDto user = AppMain.app.getBasic();
	String userid = user.getId();
	int classno = AppMain.app.getBasic().getClass_no();
	ClassDao cDao = new ClassDao();
	ClassDto currentClass = cDao.selectClassOne(classno);
	StudentDao sDao = new StudentDao();
	StudentDto sDto = new StudentDto();

	ObservableList<StudentDto> list = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		setTableColumns();
		
		//refreshTable();

	}

	// 테이블 컬럼 설정
	private void setTableColumns() {
		this.list=sDao.selectUserStudentList(classno);
		
		stNo.setCellValueFactory(new PropertyValueFactory<>("stNo"));
		stTitle.setCellValueFactory(new PropertyValueFactory<>("stTitle"));
		stDesc.setCellValueFactory(new PropertyValueFactory<>("stDesc"));
//		stFile.setCellValueFactory(new PropertyValueFactory<>("stFile"));
		
		this.tblViewReport.setItems(this.list);
		// 테이블 채우기
		//refreshTable();
	} 
	// db에서 저장된 데이터 불러와서 테이블에 넣기
	public void refreshTable() {
		sDao.selectUserStudentList(classno);
	}
}
