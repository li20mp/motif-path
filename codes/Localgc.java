

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import algo.enhanced_motif_path;
import algo.motif_path;
import graph_clustering.Ground_truth;
import tool.BFS;
import tool.Dataset;
import tool.Datasets;
import tool.Interact;
import tool.graphReady;

public class Localgc {
	
	public static void main(String[]args) throws IOException{

		String mainDir = "data/ppi/gavin";
		int pid = 5, defragID = 0, k = 15, queryNum = 500;
		Interact nt = new Interact();
		
		for(int i=0;i<args.length;i++) {
			String[]tem=args[i].split(":");
			char temc = tem[0].charAt(1);
			switch(temc) {
			case 'g': mainDir = args[i].substring(3); break;
			case 'm': pid = nt.intwarn(tem[1], pid, 'm'); break;
			case 'd': defragID = nt.intwarn(tem[1], defragID, 'd'); break;
			case 'n': queryNum = nt.intwarn(tem[1], queryNum, 'n'); break;
			case 'k': k = nt.intwarn(tem[1], k, 'k'); break;
			}
		}
		
		graphReady g = new graphReady();
		Datasets dts = new Datasets();
		int[]pset = new int[1];
		pset[0] = pid;
		int kid = k;
		
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
		
		BFS bfs = new BFS();
		Ground_truth ppi = new Ground_truth(dts.d.dataPath+".gt", ",");
		
		double[]pre = new double[queryNum], rec = new double[queryNum], f1 = new double[queryNum];
		
		for(int i=0;i<queryNum;i++) {
			int sid = (int)(Math.random()*ppi.nodes.size());
			int s = ppi.nodes.get(sid);
			
			ArrayList<Integer>knn = null;
			if(pid==1) {
				knn = bfs.searchKNN(s,k,dts.g.graph);
			}
			if(pid==5) {
				knn = emp.motif_path_early_stop(dts.g, pset, "", s, k);
			}
			
			//double [] res = ppi.run(knn, s);
			for(k=1;k<=kid;k++) {
				//if(pid==5&&!enhance_sig)
					if(knn.size()<k)
						knn.add(0);
				ArrayList<Integer>tem = new ArrayList();
				for(int t=1;t<=k;t++)
					tem.add(knn.get(t-1));
				double[]res = ppi.run(tem, s);
				pre[k-1]+=res[0];
				rec[k-1]+=res[1];
			}
			k=kid;

			
		}
		
		
		String pr ="", re="", f="";
		for(k=1;k<=kid;k++) {
			pre[k-1]/=queryNum;
			rec[k-1]/=queryNum;
			f1[k-1] = pre[k-1]*rec[k-1]*2/(pre[k-1]+rec[k-1]);
			pr+=pre[k-1]+" ";
			re+=rec[k-1]+" ";
			f+=f1[k-1]+" ";
		}
		k=kid;
		
		//System.out.println(pr+"\n"+re+"\n"+f);
		System.out.println("Precision\t"+pre[kid-1]);
		System.out.println("Recall\t\t"+rec[kid-1]);
		System.out.println("F1-score\t"+f1[kid-1]);
		
	}
	
	static void run2 (int ii, String mainDir, Datasets dts) throws IOException{
		graphReady g = dts.g;
		Dataset ds = dts.d;
		
		//Initialize(ds, mainDir, g);
		Ground_truth ppi = new Ground_truth(ds.ground_truth_dir, ds.ground_truth_sp);//ds.ground_truth;
		ArrayList<ArrayList<String>>mc = new ArrayList();
		ArrayList<String>mc_nodes = new ArrayList();
		ArrayList<Integer>mc_cid = new ArrayList();
		
		BufferedReader a = new BufferedReader(new FileReader(mainDir+"PPI/motifclustering-result/"+ii+"_clusters.txt"));
		String str = a.readLine();
		int minCluster = 2;
		while(str!=null){
			String[]tem = str.split(" ");
			if(tem.length>minCluster-1){
				ArrayList<String> amc = new ArrayList<String>();
				for(int j=0;j<tem.length;j++){
					int id = Integer.parseInt(tem[j]);
					String aknn =  g.match[id+1];
					if(!mc_nodes.contains(aknn)&&ppi.nodes.contains(aknn)) {
						mc_nodes.add(aknn);
						mc_cid.add(mc.size());
					}
					amc.add(aknn);
				}
				mc.add(amc);
			}
			str = a.readLine();
		}
		System.out.println("Query nodes size: "+mc_nodes.size());
		int queryNum = 100;
		double acc = 0, rec = 0;
		for(int i = 0;i<queryNum;i++){
			int qid = (int)(Math.random()*mc_nodes.size());
			String q = mc_nodes.get(qid);
			int qcid = mc_cid.get(qid);
			ArrayList<String>cc = mc.get(qcid);
			double counter = 0;
			for(int j=0;j<cc.size();j++){
				String aknn = cc.get(j);
				if(aknn.equals(q))continue;
				boolean sig = false;//ppi.runEach(aknn, q, g);
				if(sig)
					counter++;
			}
			acc+=counter/(cc.size()-1);
			rec+=counter/(ppi.sc.size());
		}
		System.out.println(acc/queryNum+" "+rec/queryNum);
	}
	
	

}
