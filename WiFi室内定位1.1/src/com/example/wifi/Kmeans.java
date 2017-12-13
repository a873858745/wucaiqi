package com.example.wifi;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

/**
 * K��ֵ�����㷨
 */
public class Kmeans {
	private int k;// �ֳɶ��ٴ�
	private int m;// ��������
	private int dataSetLength;// ���ݼ�Ԫ�ظ����������ݼ��ĳ���
	private ArrayList<double[]> dataSet;// ���ݼ�����
	private ArrayList<double[]> center;// ��������
	private ArrayList<ArrayList<double[]>> cluster; // ��
	private ArrayList<Double> jc;// ���ƽ���ͣ�kԽ�ӽ�dataSetLength�����ԽС
	private Random random;

	/**
	 * ����������ԭʼ���ݼ�
	 * 
	 * @param dataSet
	 */

	public void setDataSet(ArrayList<double[]> dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return �����
	 */

	public ArrayList<ArrayList<double[]>> getCluster() {
		return cluster;
	}
	
	/**
	 * ��ȡ���
	 * 
	 * @return �����ļ�
	 */

	public ArrayList<double[]> getCenter() {
		return center;
	}
	
	/**
	 * ���캯����������Ҫ�ֳɵĴ�����
	 * 
	 * @param k
	 *            ������,��k<=0ʱ������Ϊ1����k��������Դ�ĳ���ʱ����Ϊ����Դ�ĳ���
	 */
	public Kmeans(int k) {
		if (k <= 0) {
			k = 1;
		}
		this.k = k;
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		m = 0;
		random = new Random();
		if (dataSet == null || dataSet.size() == 0) {
			initDataSet();
		}
		dataSetLength = dataSet.size();
		if (k > dataSetLength) {
			k = dataSetLength;
		}
		center = initCenters();
		cluster = initCluster();
		jc = new ArrayList<Double>();
	}

	/**
	 * ���������δ��ʼ�����ݼ���������ڲ��������ݼ�
	 */
	private void initDataSet() {
		dataSet = new ArrayList<double[]>();
		// ����{6,3}��һ���ģ����Գ���Ϊ15�����ݼ��ֳ�14�غ�15�ص���Ϊ0

		double[][] dataSetArray = new double[][] { { 8, 2 }, { 3, 4 },
				{ 2, 5 }, { 4, 2 }, { 7, 3 }, { 6, 2 }, { 4, 7 }, { 6, 3 },
				{ 5, 3 }, { 6, 3 }, { 6, 9 }, { 1, 6 }, { 3, 9 }, { 4, 1 },
				{ 8, 6 } };

		for (int i = 0; i < dataSetArray.length; i++) {
			dataSet.add(dataSetArray[i]);
		}
	}

	/**
	 * ��ʼ���������������ֳɶ��ٴؾ��ж��ٸ����ĵ�
	 * 
	 * @return ���ĵ㼯
	 */
	private ArrayList<double[]> initCenters() {
		ArrayList<double[]> center = new ArrayList<double[]>();
		int[] randoms = new int[k];
		boolean flag;
		int temp = random.nextInt(dataSetLength);
		randoms[0] = temp;
		for (int i = 1; i < k; i++) {
			flag = true;
			while (flag) {
				temp = random.nextInt(dataSetLength);
				int j = 0;
				// �����forѭ������j�޷���1
				// for(j=0;j<i;++j)
				// {
				// if(temp==randoms[j]);
				// {
				// break;
				// }
				// }
				while (j < i) {
					if (temp == randoms[j]) {
						break;
					}
					j++;
				}
				if (j == i) {
					flag = false;
				}
			}
			randoms[i] = temp;
		}

		// ����������������
		// for(int i=0;i<k;i++)
		// {
		// System.out.println("test1:randoms["+i+"]="+randoms[i]);
		// }

		// System.out.println();
		for (int i = 0; i < k; i++) {
			center.add(dataSet.get(randoms[i]));// ���ɳ�ʼ����������
		}
		return center;
	}

	/**
	 * ��ʼ���ؼ���
	 * 
	 * @return һ����Ϊk�صĿ����ݵĴؼ���
	 */
	private ArrayList<ArrayList<double[]>> initCluster() {
		ArrayList<ArrayList<double[]>> cluster = new ArrayList<ArrayList<double[]>>();
		for (int i = 0; i < k; i++) {
			cluster.add(new ArrayList<double[]>());
		}

		return cluster;
	}

	/**
	 * ����������֮��ľ���
	 * 
	 * @param ds
	 *            ��1
	 * @param center
	 *            ��2
	 * @return ����
	 */
	private double distance(double[] ds, double[] center) {
		double distance = 0.0f;
		double d1 = ds[0] - center[0];
		double d2 = ds[1] - center[1];
		double d3 = ds[2] - center[2];
		double d4 = ds[3] - center[3];
		double d5 = ds[4] - center[4];
		double d = d1 * d1 + d2 * d2 + d3 * d3 + d4 * d4 + d5 * d5;
		distance = (double) Math.sqrt(d);

		return distance;
	}

	/**
	 * ��ȡ���뼯������С�����λ��
	 * 
	 * @param distance
	 *            ��������
	 * @return ��С�����ھ��������е�λ��
	 */
	private int minDistance(double[] distance) {
		double minDistance = distance[0];
		int minLocation = 0;
		for (int i = 1; i < distance.length; i++) {
			if (distance[i] < minDistance) {
				minDistance = distance[i];
				minLocation = i;
			} else if (distance[i] == minDistance) // �����ȣ��������һ��λ��
			{
				if (random.nextInt(10) < 5) {
					minLocation = i;
				}
			}
		}

		return minLocation;
	}

	/**
	 * ���ģ�����ǰԪ�طŵ���С����������صĴ���
	 */
	private void clusterSet() {
		double[] distance = new double[k];
		for (int i = 0; i < dataSetLength; i++) {
			for (int j = 0; j < k; j++) {
				distance[j] = distance(dataSet.get(i), center.get(j));
				// System.out.println("test2:"+"dataSet["+i+"],center["+j+"],distance="+distance[j]);

			}
			int minLocation = minDistance(distance);
			// System.out.println("test3:"+"dataSet["+i+"],minLocation="+minLocation);
			// System.out.println();

			cluster.get(minLocation).add(dataSet.get(i));// ���ģ�����ǰԪ�طŵ���С����������صĴ���

		}
	}

	/**
	 * ���������ƽ���ķ���
	 * 
	 * @param element
	 *            ��1
	 * @param center
	 *            ��2
	 * @return ���ƽ��
	 */
	private double errorSquare(double[] element, double[] center) {
		// double x = element[0] - center[0];
		// double y = element[1] - center[1];

		double d1 = element[0] - center[0];
		double d2 = element[1] - center[1];
		double d3 = element[2] - center[2];
		double d4 = element[3] - center[3];
		double d5 = element[4] - center[4];
		double errSquare = d1 * d1 + d2 * d2 + d3 * d3 + d4 * d4 + d5 * d5;
		return errSquare;
	}

	/**
	 * �������ƽ����׼��������
	 */
	private void countRule() {
		double jcF = (double) 0;
		for (int i = 0; i < cluster.size(); i++) {
			for (int j = 0; j < cluster.get(i).size(); j++) {
				jcF = jcF + errorSquare(cluster.get(i).get(j), center.get(i));

			}
		}
		jc.add(jcF);
	}

	/**
	 * �����µĴ����ķ���
	 */
	private void setNewCenter() {
		for (int i = 0; i < k; i++) {
			int n = cluster.get(i).size();
			if (n != 0) {
				double[] newCenter = { 0, 0, 0, 0, 0 };
				for (int j = 0; j < n; j++) {
					newCenter[0] += cluster.get(i).get(j)[0];
					newCenter[1] += cluster.get(i).get(j)[1];
					newCenter[2] += cluster.get(i).get(j)[2];
					newCenter[3] += cluster.get(i).get(j)[3];
					newCenter[4] += cluster.get(i).get(j)[4];
				}
				// ����һ��ƽ��ֵ
				newCenter[0] = newCenter[0] / n;
				newCenter[1] = newCenter[1] / n;
				newCenter[2] = newCenter[2] / n;
				newCenter[3] = newCenter[3] / n;
				newCenter[4] = newCenter[4] / n;
				center.set(i, newCenter);
			}
		}
	}

	/**
	 * ��ӡ���ݣ�������
	 * 
	 * @param dataArray
	 *            ���ݼ�
	 * @param dataArrayName
	 *            ���ݼ�����
	 */
	public void printDataArray(ArrayList<double[]> dataArray,
			String dataArrayName) {
		for (int i = 0; i < dataArray.size(); i++) {
			Log.e("print:", dataArrayName + "[" + i + "]={"
					+ dataArray.get(i)[0] + "," + dataArray.get(i)[1] + ","
					+ dataArray.get(i)[2] + "," + dataArray.get(i)[3] + ","
					+ dataArray.get(i)[4] + "}");
			// System.out.println("print:" + dataArrayName + "[" + i + "]={"
			// + dataArray.get(i)[0] + "," + dataArray.get(i)[1] + "}");
		}
		System.out.println("===================================");
	}

	/**
	 * ��ӡ���ݣ�������
	 * 
	 * @param dataArray
	 *            ���ݼ�
	 * @param dataArrayName
	 *            ���ݼ�����
	 */
	public void printCenterData(ArrayList<double[]> center) {
		for (int i = 0; i < center.size(); i++) {
			Log.e("print:", "center: " + (i + 1) + "={"
					+ center.get(i)[0] + "," + center.get(i)[1] + ","
					+ center.get(i)[2] + "," + center.get(i)[3] + ","
					+ center.get(i)[4] + "}");
			// System.out.println("print:" + dataArrayName + "[" + i + "]={"
			// + dataArray.get(i)[0] + "," + dataArray.get(i)[1] + "}");
		}
		System.out.println("===================================");
	}
	
	/**
	 * Kmeans�㷨���Ĺ��̷���
	 */
	private void kmeans() {
		init();
		// printDataArray(dataSet,"initDataSet");
		// printDataArray(center,"initCenter");

		// ѭ�����飬ֱ������Ϊֹ
		while (true) {
			clusterSet();
			// for(int i=0;i<cluster.size();i++)
			// {
			// printDataArray(cluster.get(i),"cluster["+i+"]");
			// }

			countRule();

			// System.out.println("count:"+"jc["+m+"]="+jc.get(m));

			// System.out.println();
			// �����ˣ��������
			if (m != 0) {
				if (jc.get(m) - jc.get(m - 1) == 0) {
					break;
				}
			}

			setNewCenter();
			// printDataArray(center,"newCenter");
			m++;
			cluster.clear();
			cluster = initCluster();
		}

		// System.out.println("note:the times of repeat:m="+m);//�����������
	}

	/**
	 * ִ���㷨
	 */
	public void execute() {
		long startTime = System.currentTimeMillis();
		System.out.println("kmeans begins");
		kmeans();
		long endTime = System.currentTimeMillis();
		System.out.println("kmeans running time=" + (endTime - startTime)
				+ "ms");
		System.out.println("kmeans ends");
		System.out.println();
	}
}