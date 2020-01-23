package main.resources.moviesEvaluation;

public class User {
	private String id;
	private String name;
	private String username;
	private String password;
	private String country;
	private Boolean admin;
	
	
	public User(String _id, String name, String username, String password, String country, Boolean admin) {
		id = _id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.country = country;
		this.admin = admin;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Boolean getAdmin() {
		return admin;
	}
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
}
