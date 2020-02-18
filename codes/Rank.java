

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import algo.enhanced_motif_path;
import node_ranking.Node_ranking;
import tool.Dataset;
import tool.Datasets;
import tool.Interact;
import tool.graphReady;

public class Rank {

	public static void main(String[] args) throws IOException {
		
		String mainDir = "data/social/dblp1";
		int pid = 5, defragID = 0, k = 15, degsig = 0, ranksig = 0;
		Interact nt = new Interact();
		
		for(int i=0;i<args.length;i++) {
			String[]tem=args[i].split(":");
			char temc = tem[0].charAt(1);
			switch(temc) {
			case 'g': mainDir = args[i].substring(3); break;
			case 'm': pid = nt.intwarn(tem[1], pid, 'm'); break;
			case 'd': defragID = nt.intwarn(tem[1], defragID, 'd'); break;
			case 'k': k = nt.intwarn(tem[1], k, 'k'); break;
			case 'n': degsig = 1; break;
			case 'b': ranksig = 1; break;
			}
		}
		
		graphReady g = new graphReady();
		Datasets dts = new Datasets();
		int[]pset = new int[1];
		pset[0] = pid;
		
		enhanced_motif_path emp = new enhanced_motif_path();
		if(defragID==0||defragID==1) {
			g.readGraph(mainDir, ",", 0, 1, 2, 1, false);
			Dataset d = new Dataset (mainDir, ",", 1);
			d.g = g;
			dts.d = d;
			dts.g = g;
			if(defragID==1)
				emp.readInjectedEdges(dts, pid);
		}
		if(defragID==2) {
			//emp.readInjectedEdgesEdmot(dts);
			g.readGraph(mainDir+".edmot", ",", 0, 1, 2, 1, false);
			Dataset d = new Dataset (mainDir, ",", 1);
			d.g = g;
			dts.d = d;
			dts.g = g;
			if(pid!=5) {
				System.out.println("Edmot only supports triangles (-m:5)!");			
				return;
			}	
		}
		
		if(ranksig==1) {
			new Node_ranking().run(dts, pset, emp, defragID);
			System.out.println("Node ranking finished. Results are written into "+dts.d.dataPath+"_m"+pid+"_d"+defragID+".rank");
			return;
		}

		double[]degree = new double[dts.g.nodeNum+1];
		int[]id = new int[dts.g.nodeNum+1];
		for(int i=1;i<=dts.g.nodeNum;i++) {
			id[i] = i;
		}
		
		if(degsig==1) 
			degree = getDEG(dts);//DEG
		else {
			if(pid==1)
				degree = getMBET(dts.d.dataPath+".rank",dts.g.nodeNum,2);//BET
			if(pid==5) {
				if(defragID==0)
					degree = getMBET(dts.d.dataPath+"_m"+pid+".rank",dts.g.nodeNum,2);//MBET
				if(defragID==1)
					degree = getMBET(dts.d.dataPath+"_m"+pid+"_d"+defragID+".rank",dts.g.nodeNum,2);//MBET-b
				if(defragID==2)
					degree = getMBET(dts.d.dataPath+"_m"+pid+"_d"+defragID+".rank",dts.g.nodeNum,2);//MBET-c
			}
			//degree = getBPR(dts.ds[did].dataPath+"-1.aaaidata",dts.g.nodeNum);//BPR
			//degree = getBPR(dts.ds[did].dataPath+"-w1.aaaidata",dts.g.nodeNum);//WPR
			//degree = getBPR(dts.ds[did].dataPath+"-5.aaaidata",dts.g.nodeNum);//MPR
			//degree = getMBET(dts.ds[did].dataPath+"-1.betclo",dts.g.nodeNum,3);//BET
			//degree = getMBET(dts.ds[did].dataPath+"-5.betclo",dts.g.nodeNum,3);//MBET
			//degree = getMBET(dts.ds[did].dataPath+"-5-emp.betclo",dts.g.nodeNum,3);//MBET-e
			//degree = getMBET(dts.ds[did].dataPath+"-1.betclo",dts.g.nodeNum,5);//CLO
			//degree = getMBET(dts.ds[did].dataPath+"-5.betclo",dts.g.nodeNum,5);//MCLO
			//degree = getMBET(dts.ds[did].dataPath+"-5-emp.betclo",dts.g.nodeNum,5);//MCLO-e
			//degree = getMBET(dts.ds[did].dataPath+"-1.betclo",dts.g.nodeNum,7);//GRA
		}
		
		for(int i=1;i<degree.length;i++) {
			for(int j=i+1;j<degree.length;j++) {
				if(degree[i]<degree[j]) {
					double tem = degree[i];
					degree[i] = degree[j];
					degree[j] = tem;
					int item = id[i];
					id[i] = id[j];
					id[j] = item;
				}
			}
		}
		
		int[]kset = new int[1];//{10,30,50};
		kset[0] = k;
		DecimalFormat   df   =new   java.text.DecimalFormat("0.00");
		
		for(int ks = 0;ks<kset.length;ks++) {		
			k = kset[ks];
			String dir = dts.d.dataPath;
			if(pset[0]==1)
				dir += ".rank";
			else {
				dir += "_m"+pset[0];
				if(defragID==0)
					dir += ".rank";
				else
					dir += "_d"+defragID+".rank";	
			}
			int[]hIndex = gethIndex(dir, dts, k);
			System.out.println("Current ranking:");
				//output the ndcg of top-k nodes
				double dcg = 0;
				for(int i=1;i<=k;i++) {
					dcg += (double)hIndex[id[i]]/(Math.log(i+1)/Math.log(2));
					System.out.print(id[i]+" ");
				}
				double ndcg = dcg / idcg;
				
				System.out.println("\nNDCG score: "+df.format(ndcg));
			}
		
	}
	
	private static double[] getDEG(Datasets dts) {
		double[]degree = new double[dts.g.nodeNum+1];
		for(int i=1;i<=dts.g.nodeNum;i++) {
			degree [i] = dts.g.graph[i].size();
		}
		return degree;
	}

	private static double[] getMBET(String dir, int nodeNum,int skipedLines) throws IOException {
		BufferedReader a = new BufferedReader(new FileReader(dir));
		double[]scores = new double[nodeNum+1];
		String s = null;
		for(int i=0;i<skipedLines;i++)
			s = a.readLine();
		String[]tem1=a.readLine().split(":")[1].split(",");//id
		String[]tem2=a.readLine().split(":")[1].split(",");//score
		for(int i=0;i<tem1.length;i++){
			scores[Integer.parseInt(tem1[i])] = Double.parseDouble(tem2[i]);
		}
		return scores;
	}

	private static double[] getBPR(String dir, int nodeNum) throws IOException {
		BufferedReader a = new BufferedReader(new FileReader(dir));
		double[]scores = new double[nodeNum+1];
		String s = a.readLine();
		while(s!=null&&!s.equals("")) {
			String[]tem=s.split(" ");
			scores[Integer.parseInt(tem[1])] = Double.parseDouble(tem[0]);
			s=a.readLine();
		}
		return scores;
	}

	static int idcg = 0;
	static int[] gethIndex(String dir, Datasets dts, int k) throws IOException {
		System.out.println("Ideal ranking (by H-index):");
		BufferedReader a = new BufferedReader(new FileReader(dir));
		//a.readLine();
		idcg = 0;
		String[]tem=a.readLine().split(":")[1].split(",");//hindex node id
		int [] hIndexID = new int[tem.length];
		for(int t=0;t<tem.length;t++) {
			hIndexID[t] = Integer.parseInt(tem[t]);
		}
		tem=a.readLine().split(":")[1].split(",");//hindex value
		int[]hIndex = new int[dts.g.nodeNum+1];
		for(int t=0;t<tem.length;t++) {
			int atem = Integer.parseInt(tem[t]);
			if(atem<0)
				atem = 0;
			hIndex[hIndexID[t]] = atem;
			if(t<k) {
				System.out.print(hIndexID[t]+" ");
				idcg += (double)atem/(Math.log(t+2)/Math.log(2));
				}
		}
		System.out.println();
		return hIndex;
	}
	
	static void getRes(String mainDir, int did) throws IOException {
		for(int k = 1;k<=1000;k+=10) 
		{
		
		Datasets dts = new Datasets();
		DecimalFormat df = new DecimalFormat("0.00");
			String readDir = dts.ds[did].dataPath;
			String edgePath = readDir + "-1.betclo";
			String trianglePath = readDir + "-5.betclo";
			String triangleEnhancedPath = readDir + "-5-emp.betclo";
			String[]files = new String[3];
			files[0] = edgePath;
			files[1] = trianglePath;
			files[2] = triangleEnhancedPath;
			
			int[]hIndex = null, hIndexID = null;
			double idcg = 0;
			String dcgbets = "", dcgclos = "";
			for(int j=0;j<3;j++) {
				BufferedReader a = new BufferedReader(new FileReader(files[j]));
				a.readLine();//components
				if(hIndex==null) {
					String[]tem=a.readLine().split("	")[1].split(",");//hindex node id
					hIndexID = new int[tem.length];
					for(int t=0;t<tem.length;t++) {
						hIndexID[t] = Integer.parseInt(tem[t]);
					}
					tem=a.readLine().split("	")[1].split(",");//hindex value
					hIndex = new int[dts.ds[did].nodeNum+1];
					for(int t=0;t<tem.length;t++) {
						int atem = Integer.parseInt(tem[t]);
						if(atem < 0)
							atem = 0;
						hIndex[hIndexID[t]] = atem;
						if(t<k)
							idcg += (double)atem/(Math.log(t+2)/Math.log(2));
					}
				}else {
					a.readLine();
					a.readLine();
				}
				String[]tem=a.readLine().split("	")[1].split(",");//bet node id
				int[]betID = new int[tem.length];
				double dcgbet = 0;
				for(int t=0;t<tem.length;t++) {
					int atem = Integer.parseInt(tem[t]);
					//betID[t] = atem;
					if(t<k&&atem<hIndex.length)
						dcgbet += (double)hIndex[atem]/(Math.log(t+2)/Math.log(2));
				}
				dcgbet /= idcg;
				tem=a.readLine().split("	")[1].split(",");//bet
				int[]bet = new int[tem.length+1];
				for(int t=0;t<tem.length;t++) {
					//bet[betID[t]] = Integer.parseInt(tem[t]);
				}
				tem=a.readLine().split("	")[1].split(",");//col node id
				int[]colID = new int[tem.length];
				double dcgclo = 0;
				for(int t=0;t<tem.length;t++) {
					int atem = Integer.MAX_VALUE;
					try{atem = Integer.parseInt(tem[t]);}catch(Exception e) {}
					//colID[t] = atem;
					if(t<k&&atem<hIndex.length)
						dcgclo += (double)hIndex[atem]/(Math.log(t+2)/Math.log(2));
				}
				dcgclo /= idcg;
				//tem=a.readLine().split("	")[1].split(",");//col
				int[]col = new int[tem.length+1];
				for(int t=0;t<tem.length;t++) {
					//col[betID[t]] = Integer.parseInt(tem[t]);
				}
				dcgbets += dcgbet + " ";
				dcgclos += dcgclo + " ";
				}
			System.out.println(dcgbets+" "+dcgclos);
			
		}
	}

}
