package DTO;

public class SignupDto {

	private String id;
	private String password;
	private String name;
	private String email;

	private String phone;

	private boolean type;

	public SignupDto() {

	}

	public SignupDto(String id, String password, String name, String email, String phone1, String phone2, String combo,
			boolean type) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phone = combo + phone1 + phone2;
		this.type = type;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone1) {
		this.phone = phone1;
	}

	public boolean gettype() {
		return type;
	}

	public void settype(boolean type) {
		this.type = type;
	}

}