package com.exmaple.model;

public class ClusterLocInfo {
	public int i_center;
	public int cluster_j;
	public ClusterLocInfo(int i,int j){
		i_center = i;
		cluster_j = j;
	}
	public int getI_center() {
		return i_center;
	}
	public void setI_center(int i_center) {
		this.i_center = i_center;
	}
	public int getCluster_j() {
		return cluster_j;
	}
	public void setCluster_j(int cluster_j) {
		this.cluster_j = cluster_j;
	}
	
}
