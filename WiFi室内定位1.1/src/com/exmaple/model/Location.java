package com.exmaple.model;

public class Location {
	private long id;
	private double x;
	private double y;
	private String location;
	public Location(long l,double x,double y,String location){
		this.id = l;
		this.x = x;
		this.y = y;
		this.location = location;
	}
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
}
