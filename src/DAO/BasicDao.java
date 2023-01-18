package DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DTO.BasicDto;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import util.JdbcUtil;

public class BasicDao {

	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private JdbcUtil ju;
//	private int id_count = 1;
    private static String private_id;
    private boolean private_type;


	//DB연결
	private void connectionJDBC() {
		
		try {
			
			ju = new JdbcUtil();
			connection = ju.getConnection();
			System.out.println("드라이버 로딩 성공 : BasicDao");
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("드라이버 로딩 실패 : BasicDao");
		}
	}

	// DB에 데이터를 저장하는 메서드
    public void insertBoard(String id, String pass, String name, String email, String phone, boolean type) {
    
		//DB연결
		connectionJDBC();
    	
    	String sql = "insert into user values(?,?,?,?,?,curdate(),?);";
    	
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, name); 
            pstmt.setString(3, pass);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            if(type==false) {
            	pstmt.setString(6, "학생");
            }
            else {
            	pstmt.setString(6, "강사");
            }

            pstmt.executeUpdate();
            System.out.println("데이터 삽입 성공");
            
            System.out.println("아이디:"+ id);
            System.out.println("비밀번호:"+pass);
            System.out.println("이름:"+ name);
            System.out.println("이메일:"+email);
            System.out.println("핸드폰번호:"+phone);
//            id_count++;
            
         } catch (SQLException e) {            
            System.out.println("데이터 삽입 실패");
             e.printStackTrace();
         } finally {
        	 //접속종료
        	 ju.disconnect(connection, pstmt, rs);
         }
    }
    
	// 조건에 맞는 행을 DB에서 1개 행만 가져오는 메서드
	public BasicDto selectOne(BasicDto login) {

		//DB연결
		connectionJDBC();

		String sql = "select user_id, user_name, user_password from student where user_id = ? and user_password=?;";
		if (login.gettype2() == true) {
			sql = "select user_id, user_name, user_password from teacher where user_id = ? and user_password = ? ;";
		}
		BasicDto loginuser = new BasicDto();

		try {
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, login.getId());
			pstmt.setString(2, login.getPassword());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				String id = rs.getString("user_id");
				String name = rs.getString("user_name");
				String password = rs.getString("user_password");

				loginuser.setId(id);
				loginuser.setPassword(password);
				loginuser.setName(name);
				loginuser.settype2(login.gettype2());

				if (id.equals(loginuser.getId()) && password.equals(loginuser.getPassword())) {
					System.out.println("로그인 성공");
				}
				return loginuser;
			}
			return null;
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("로그인 실패");
			return null;
		} finally {
	
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}

	//아이디 찾기 BasicDao
    public String search_id(String name, String email, String phone, boolean type) {
       
      //DB연결
      connectionJDBC();
      
      String success = "fail";
       String sql = "select user_id, user_name, email, phoneNum from student where user_name = ? and email=? and phoneNum = ?;";
       if(type==true) {
          sql = "select user_id, user_name, email, phoneNum from teacher where user_name = ? and email=? and phoneNum = ?;";
       }
 
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {  
               String name2 = rs.getString("user_name");
               String email2 = rs.getString("email");
               String phone2 = rs.getString("phoneNum");
               
               if(name.equals(name2)&&email.equals(email2)&&phone.equals(phone2)) {
                  
                  String user_id = rs.getString("user_id");
                   Alert alert =new Alert(AlertType.INFORMATION);
                   alert.setContentText("아이디 찾은 결과:"+user_id);
                   alert.show();
                   success = "success";
               }
          return success;
            }
      } catch (SQLException e) {
//         e.printStackTrace();
         System.out.println("[SQL Error : " + e.getMessage() + "]");
         System.out.println("아이디 조회 실패");
      } finally {
   
         //접속종료
         ju.disconnect(connection, pstmt, rs);
      }
      return success;
    } 
    
    //비밀번호 찾기
    public String search_pass(String id, String name, String email, boolean type) {
        
        //DB연결
        connectionJDBC();
        
        String success = "fail"; //비밀번호 찾기 성공 여부
         String sql = "select user_id, user_name, email from student where user_id =? and user_name = ? and email=?;";
         if(type==true) {
            sql = "select user_id, user_name, email from teacher where user_id = ? and user_name = ? and email=?;";
         }
          
          try {
              pstmt = connection.prepareStatement(sql);
              pstmt.setString(1, id);
              pstmt.setString(2, name);
              pstmt.setString(3, email);
              
              System.out.println(email);
              
              private_id=id;
              this.private_type=type;
              
              rs = pstmt.executeQuery();
              if (rs.next()) {  
                 String name2 = rs.getString("user_name");
                 String email2 = rs.getString("email");
                 String id2 = rs.getString("user_id");
            
                 System.out.println(email2);
                 
              if(name.equals(name2)&&email.equals(email2)&&id.equals(id2)) {
                 success="success";
                 try {
                    Parent parent = FXMLLoader.load(getClass().getResource("/fxml/login/search_password2.fxml"));
                    Scene scene = new Scene(parent);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                 } catch (IOException ex) {              
                    ex.printStackTrace();
                    System.out.println("비밀번호 찾기 실패");
                 }
              }
           }
        } catch (SQLException e) {
//           e.printStackTrace();
           System.out.println("[SQL Error : " + e.getMessage() + "]");
           System.out.println("비밀번호 찾기실패");
        } finally {
           //접속종료
           ju.disconnect(connection, pstmt, rs);
        }
          return success;
      }
      
	//비밀번호 재설정
	public void search_pass2(String pass) {

		//DB연결
		connectionJDBC();
		String sql2 = "UPDATE student SET user_password =? WHERE user_id=?;";
		if(this.private_type==true) {
			
			sql2 = "UPDATE teacher SET user_password =? WHERE user_id=?;";
		}

		try {
			pstmt = connection.prepareStatement(sql2);
			pstmt.setString(1, pass);
			pstmt.setString(2, private_id);
			pstmt.executeUpdate();

			Alert alert =new Alert(AlertType.INFORMATION);
			alert.setContentText("비밀번호 재설정 완료");
			alert.show();
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("[SQL Error : " + e.getMessage() + "]");
			System.out.println("비밀번호 재설정 실패");
		} finally {
			//접속종료
			ju.disconnect(connection, pstmt, rs);
		}
	}
	public void check(String id) { //중복체크

		connectionJDBC(); //DB연결

		int count=0;

		String sql = "select count(*) from user where user_id=?;";

		Alert alert =new Alert(AlertType.INFORMATION);

		try {
			//학생
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, id);				 
			ResultSet rs = pstmt.executeQuery();


			if (rs.next()) {  
				String count2 = rs.getString("count(*)");
				count = Integer.parseInt(count2);
			}

			System.out.println(count);

			if(count>=1) {
				alert.setContentText("이미 사용 중인 아이디 입니다.");
				alert.show();
				throw new Exception();
			}
			else {
				alert.setContentText("사용가능 한 아이디 입니다.");
				alert.show();
			}
		}catch (Exception e) {
			System.out.println("중복체크 에러");
		} 
	}
	
	public boolean checkDuplicateMail(String email) {
		
		connectionJDBC(); //DB연결
		
		String sql = "select count(*)"
				   + "  from user"
				   + " where email = ?";
		int num = 0;
		
		try {
			//학생
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, email);				 
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {  
				num = rs.getInt(1);
			}
			
		}catch (Exception e) {
			System.out.println("이메일 중복체크 에러");
		} 
		if(num >= 1) {
			return true;
		} else {
			
			return false;
		}
	}
}
