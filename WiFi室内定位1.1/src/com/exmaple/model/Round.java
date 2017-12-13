package com.exmaple.model;

public class Round {
	double x,y,r;
//	(X-x)*(X-x) + (Y-y)*(Y-y) == r*r;
	public Round(double x,double y,double r){
		this.x = x;
		this.y = y;
		this.r = r;
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
	public double getR() {
		return r;
	}
	public void setR(double r) {
		this.r = r;
	}
	
}
