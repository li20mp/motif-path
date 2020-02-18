package graph_clustering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader; 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import tool.graphReady;

public class Ground_truth {
	public ArrayList<ArrayList<Integer>>gt = null;
	public ArrayList<Integer>nodes = null;
	public ArrayList<ArrayList<Integer>>nodeCluster = null;
	
	public Ground_truth(String s, String sp) throws IOException{
		//System.out.println("Start MIPS: ");
		mips(s, sp);
		//System.out.println("Start SGD: ");
		//sgd (s);
	}
	public double[] run (ArrayList<Integer>knn, Integer sn){
		if(!nodes.contains(sn)){
			System.out.println("Source node: "+sn+" is not in any cluster!");
			return null;
		}
		
		int sid = nodes.indexOf(sn);
		ArrayList<Integer> scids = nodeCluster.get(sid);
	
		ArrayList<Integer> sc = new ArrayList();
		for(int i=0;i<scids.size();i++)
			for(int j=0;j<gt.get(scids.get(i)).size();j++){
				if(!sc.contains(gt.get(scids.get(i)).get(j)))
					sc.add(gt.get(scids.get(i)).get(j));
		}
		double acc = 0, rec = 0;
		//compare sc and knn
		for(int i=0;i<knn.size();i++){
			int aknn = knn.get(i);
			if(sc.contains(aknn)){
				acc++;
				rec++;
			}
		}
		double sol[] = new double[2];
		sol[0] = acc/knn.size();
		if(knn.size()==0)
			return null;
		sol[1] = rec/sc.size();
		//System.out.println(acc+"\t"+rec);
		
		return sol;
	}
	
	
	public ArrayList<Integer> sc = null;
	public boolean runEach (int aknn, int sn){
		//return if aknn and sn are in the same cluster
		if(!nodes.contains(sn)){
			System.out.println("Source node: "+sn+" is not in any cluster!");
			return false;
		}
		ArrayList<Integer>scids = nodeCluster.get(nodes.indexOf(sn));
		ArrayList<Integer>scall = new ArrayList();
		for(int i=0;i<scids.size();i++){
			int scid = scids.get(i);
			ArrayList<Integer> scs = gt.get(scid);
			for(int j=0;j<scs.size();j++){
				int scsa = scs.get(j);
				if(!scall.contains(scsa)){
					scall.add(scsa);
				}
			}
		}
		sc = scall;//new ArrayList<Integer>();
		//for(int i=0;i<scall.size();i++){
			//if(g.map.containsKey(scall.get(i)))
		//		sc.add(scall.get(i));
		//}
		if(sc.contains(aknn)){
			return true;
		}else
			return false;
	}
	
	public void mips(String s, String sp) throws IOException{
		//A list of clusters
		gt = new ArrayList<ArrayList<Integer>>();
		nodes = new ArrayList<Integer>();
		nodeCluster = new ArrayList<ArrayList<Integer>>();
		//System.out.println(s);
		BufferedReader a = new BufferedReader(new FileReader(s));
		String str = a.readLine();
		int cid = 0;
		while(str!=null){
			String[]tem = str.split(sp);
			ArrayList<Integer>tema = new ArrayList();
			for(int i=0;i<tem.length;i++){
				int id = Integer.parseInt(tem[i]);
				if(id==-1) continue;
				tema.add(id);
				if(!nodes.contains(id)){
					nodes.add(id);
					ArrayList<Integer>t=new ArrayList<Integer>();
					t.add(cid);
					nodeCluster.add(t);
					}else{
						int lcid = nodes.indexOf(Integer.parseInt(tem[i]));
						//System.out.println(tem[i]+"is the "+lcid +"-th element of "+nodes.toString());
						nodeCluster.get(lcid).add(cid);
					}
			}
			gt.add(tema);
			str = a.readLine();
			cid++;
		}
		
		
	}
	/*
	public void sgd(String s) throws IOException {
		gt = new ArrayList();
		nodes = new ArrayList();
		nodeCluster = new ArrayList();
		BufferedReader a = new BufferedReader(new FileReader(s+"sgd.txt"));
		String str = a.readLine();
		while(str!=null){
			String[]tem = str.split("	");
			if(!nodes.contains(tem[0]))nodes.add(Integer.parseInt(tem[0]));
			else System.out.println("Error: duplicates in sgd-gt: "+tem);
			Hashtable<String, Integer>ht = new Hashtable<String, Integer>();
			if(ht.contains(tem[1])){
				int id = ht.get(tem[1]);
				ArrayList<Integer>t = new ArrayList<Integer>();
				t.add(id);
				nodeCluster.add(t);
				if(!gt.get(id).contains(tem[0])){
					gt.get(id).add(Integer.parseInt(tem[0]));
				}
			}else{
				ht.put(tem[1], gt.size());
				ArrayList<Integer>t = new ArrayList<Integer>();
				t.add(gt.size());
				nodeCluster.add(t);
				ArrayList<Integer>tema = new ArrayList();
				tema.add(Integer.parseInt(tem[0]));
				gt.add(tema);
			}
			str = a.readLine();
		}
	}*/

}
