package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Util {

	// 팝업창
	public static Alert showAlert(String title, String msg, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		if (type == AlertType.ERROR) {
			alert.showAndWait();
		}
		return alert;
	}

	// TextInputControl에 숫자만 오도록
	public static ChangeListener<String> numberOnlyListener(TextInputControl textInputControl) {
		return new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					textInputControl.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		};
	}

	// TextInputControl에 알파벳과 숫자만 오도록
		public static ChangeListener<String> alphabetListener(TextInputControl textInputControl, Label lbl) {
			return new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!(newValue.matches("[\\d||a-z||A-Z]*"))) {
						textInputControl.setText(newValue.replaceAll("[^\\d||a-z||A-Z]", ""));
						lbl.setVisible(true);
					} else {
						lbl.setVisible(false);
					}
				}
			};
		}

		// TextInputControl에 알파벳,숫자,특수문자만 오도록
		public static ChangeListener<String> pwListener(TextInputControl textInputControl, Label lbl) {
			return new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("[!-z]*")) {
						textInputControl.setText(newValue.replaceAll("[^!-z]", ""));
						lbl.setVisible(true);
					} else {
						lbl.setVisible(false);
					}
				}

			};
		}

	// TextInputControl에 글자 입력수 제한
	public static ChangeListener<String> textCountLimit(TextInputControl textInputControl, int count) {
		return new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > oldValue.length()) {
					if (textInputControl.getText().length() > count) {
						textInputControl.setText(textInputControl.getText().substring(0, count));
					}
				}
			}
		};
	}

	// txtInputControl에 입력되는 글자 수가 label에 표시되고, 글자수 제한은 length만큼
	public static void textLengthLimit(TextInputControl txtInputControl, Label lbl, int length) {
		lbl.setText("( 0 / "+length+" )");
		
		txtInputControl.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				lbl.setText("( " + newValue.length() + " / " + length + " )");
				if (newValue.length() > length) {
					txtInputControl.setText(oldValue);
				}
			}
		});
	}
	
	public static String fileSizeCheck(File file) {
	
		long restrictSize = 1024 * 1024 * 10;				//10MB
		long fileSize = file.length();
		double size = 0.0;
		
		String sizeUnit = "";								//단위표기
		String strSize = "";								//나고 난 뒤 값
	
		if(fileSize <= restrictSize ) {
			
			if(fileSize >= 1024*1024) {
				sizeUnit = "MB";
				size = ((double)fileSize/(double)(1024*1024));
				strSize = String.format("%,.3f", size) + sizeUnit;
				System.out.println(strSize);
				
			} else {
				sizeUnit = "KB";
				size = ((double)fileSize/(double)(1024));
				strSize = String.format("%,.3f", size) + sizeUnit;
				System.out.println(strSize);
			}
		} else {
			
			strSize = null;
			Util.showAlert("", "10MB 이하만 등록 가능합니다.", AlertType.INFORMATION);
		}
		
		return strSize;
	}

	// textfield 미입력 시 warning label 보이기
	public static void showWarningLabel(TextField txtfield, Label lbl) {

		txtfield.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() == 0) {
					lbl.setVisible(true);
				} else {
					lbl.setVisible(false);
				}
			}
		});
	}

	//excel로 export하는 기능
	public static void excelExport(String fileName, String sheetName, TableView<?> tblView) {

		Workbook workbook = new HSSFWorkbook();
		Sheet spreadsheet = workbook.createSheet(sheetName);

		Row row = spreadsheet.createRow(0);

		//0행에는 컬럼네임으로
		for (int j = 0; j < tblView.getColumns().size(); j++) {
			row.createCell(j).setCellValue(tblView.getColumns().get(j).getText());
		}

		//tableViewReport의 item갯수만큼 row에 저장
		for (int i = 0; i < tblView.getItems().size(); i++) {
			row = spreadsheet.createRow(i + 1);
			for (int j = 0; j < tblView.getColumns().size(); j++) {
				if (tblView.getColumns().get(j).getCellData(i) != null) {
					row.createCell(j).setCellValue(tblView.getColumns().get(j).getCellData(i).toString());
				} else {
					row.createCell(j).setCellValue("");
				}
			}
		}
		try {
			//filechooser로 저장할 위치 지정
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("excel(*.xls)", "*.xls")); //확장자 지정
			fileChooser.setInitialFileName(fileName); //파일명 지정

			File file = fileChooser.showSaveDialog(tblView.getScene().getWindow()); //저장 다이얼로그 띄우기

			if (file == null) { //취소
				return;
			}

			String filePath = file.getPath();
			File xlsFile = new File(filePath);

			FileOutputStream fileOut = new FileOutputStream(xlsFile);
			workbook.write(fileOut);
			fileOut.close();

			Util.showAlert("엑셀로 내보내기 성공", "엑셀파일이 저장되었습니다. ", AlertType.INFORMATION).show();

		} catch (IOException e) {

			Util.showAlert("엑셀로 내보내기 실패", "엑셀파일 저장 실패하였습니다. ", AlertType.WARNING).show();
			e.printStackTrace();
		}
	
	}

}