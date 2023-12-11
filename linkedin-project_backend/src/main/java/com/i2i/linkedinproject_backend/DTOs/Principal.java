package com.i2i.linkedinproject_backend.DTOs;

/*
 * Description: This class is used to represent credentials recieved from the frontend which claim access to a certain resource. The crentials in this 
 * stored in this class have not been authenticated.
 */
public class Principal {
	private String username;
	private String password;
	
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
	
	public Principal(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public Principal() {
	}
	
	/*
	 * Instantiates Principal class and returns a new Principal object from a Basic Authentication header in the format "Basic <username>:<password>"
	 * 
	 * @author Arhian Albis Ramos
	 */
	public static Principal buildFromBasicAuth(String basicAuthHeader) {
		String usernameAndPassword = basicAuthHeader.substring(6);
		String[] principalArr = usernameAndPassword.split(":");
		return new Principal(principalArr[0], principalArr[1]);
	}
}
