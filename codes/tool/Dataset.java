package tool;

import java.io.IOException;

import graph_clustering.Ground_truth;

public class Dataset {

	public String dataPath = "";
	public String sp = "\t";
	public int edgeNum = -1;
	public int nodeNum = -1;
	public int nodeLine1 = 0;
	public int nodeLine2 = 1;
	public int weightLine = 2;
	public int startLine = 0;//skipped number of lines at the top of the dataset
	public boolean weighted = false;
	public boolean ifNormalized = false;
	public boolean ifAdd1 = false;
	//public String ground_truth_dir = null;
	//public String InjectedEdgesDir = "";
	public graphReady g = null;
	
	public Dataset(String s, String sp, int edgeNum, int nodeNum, int nodeLine1, int nodeLine2, int WeightedLine, int StartLine, boolean weighted){
		this.dataPath = s;
		this.sp = sp; 
		this.edgeNum = edgeNum;
		this.nodeNum = nodeNum;
		this.nodeLine1 = nodeLine1;
		this.nodeLine2 = nodeLine2;
		this.weightLine = WeightedLine;
		this.startLine = StartLine;
		this.weighted = weighted;
	}
	public Dataset(String s, String sp, int edgeNum, int nodeNum, int nodeLine1, int nodeLine2, int WeightedLine, int StartLine, boolean weighted, boolean in, boolean ia){
		this.dataPath = s;
		this.sp = sp; 
		this.edgeNum = edgeNum;
		this.nodeNum = nodeNum;
		this.nodeLine1 = nodeLine1;
		this.nodeLine2 = nodeLine2;
		this.weightLine = WeightedLine;
		this.startLine = StartLine;
		this.weighted = weighted;
		ifNormalized = in;
		ifAdd1 = ia;
	}
	public Dataset(String s, String sp, int StartLine){
		this.dataPath = s;
		this.sp = sp; 
		//this.edgeNum = edgeNum;
		//this.nodeNum = nodeNum;
		this.nodeLine1 = 0;
		this.nodeLine2 = 1;
		//this.weightLine = WeightedLine;
		this.startLine = StartLine;
		this.weighted = false;
	}
	public Dataset(String s, String sp, int StartLine, boolean in, boolean ia){
		this.dataPath = s;
		this.sp = sp; 
		//this.edgeNum = edgeNum;
		//this.nodeNum = nodeNum;
		this.nodeLine1 = 0;
		this.nodeLine2 = 1;
		//this.weightLine = WeightedLine;
		this.startLine = StartLine;
		this.weighted = false;
		ifNormalized = in;
		ifAdd1 = ia;
	}
	
	public String ground_truth_dir = null, ground_truth_sp = null;
	public String gt0 = null, gt1 = null;
	//public Ground_truth ground_truth = null;
	//public void getGroundTruth(String dir, String sp) throws IOException {
	//	ground_truth = new Ground_truth(dir, sp);
	//}
	public int id;
}
