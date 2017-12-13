package com.exmaple.model;

public class Register {
	private int id;
	private String username;
	private String password;
	
	public Register(String user,String pwd){
		username = user;
		password = pwd;
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
	
	
}
