package com.exmaple.model;

public class PositionInfo {
	public double x;
	public double y;
	public String locDis;
	
	public PositionInfo(double X,double Y,String dis){
		x = X;
		y = Y;
		locDis = dis;
	}
	
	public String toString(){
		return "�����꣺" + x + "\n�����꣺" + y + "\n��ǰλ�ã�" + locDis;
		
	}
}
