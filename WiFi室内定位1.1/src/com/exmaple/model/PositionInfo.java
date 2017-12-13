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
		return "横坐标：" + x + "\n纵坐标：" + y + "\n当前位置：" + locDis;
		
	}
}
