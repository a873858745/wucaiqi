package com.exmaple.model;

public class RssiInfo {
	private double rss1;
	private double rss2;
	private double rss3;
	private double rss4;
	private double rss5;
	
	public RssiInfo(double rss1,double rss2,double rss3,double rss4,double rss5){
		this.rss1 = rss1;
		this.rss2 = rss2;
		this.rss3 = rss3;
		this.rss4 = rss4;
		this.rss5 = rss5;
	}
	public double getRss1() {
		return rss1;
	}
	public void setRss1(double rss1) {
		this.rss1 = rss1;
	}
	public double getRss2() {
		return rss2;
	}
	public void setRss2(double rss2) {
		this.rss2 = rss2;
	}
	public double getRss3() {
		return rss3;
	}
	public void setRss3(double rss3) {
		this.rss3 = rss3;
	}
	public double getRss4() {
		return rss4;
	}
	public void setRss4(double rss4) {
		this.rss4 = rss4;
	}
	public double getRss5() {
		return rss5;
	}
	public void setRss5(double rss5) {
		this.rss5 = rss5;
	}
	
}
