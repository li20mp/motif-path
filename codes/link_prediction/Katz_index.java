package link_prediction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import algo.enhanced_motif_path;
import algo.path_count;
import tool.BFS;
import tool.Datasets;
import tool.graphReady;

public class Katz_index {
	
	
	public void run(Datasets dts, String[]spset, String noe, int queryNums, boolean ifEnhanced, int defragID, enhanced_motif_path emp) throws IOException {
		
		int[]pset = new int[spset.length];
		for(int i=0;i<pset.length;i++)pset[i]=Integer.parseInt(spset[i]);
		
		double beta = 0.01;
		
		graphReady g = dts.g;
		
		//int gd = dts.g.diameterAprox();
		//System.out.println("Nodes: "+g.nodeNum+"\nEdges: "+g.edgeNum+"\nDegree: "+g.getDegree()+"\nDiameter: "+gd);
		//BFS bfs = new BFS();
		
		int lmax=4;
		System.out.println("L_max: "+lmax);
		String pst = "";
		for(int i=0;i<spset.length;i++) {
			pst += (spset[i]+"_");
		}
		//BufferedWriter b1 = new BufferedWriter(new FileWriter(dts.d.dataPath+"_path_counts_nega"+"_p"+pst+"l"+lmax+"_delta"+noe.split(",")[0]+"_noe"+noe.split(",")[1]));
		//BufferedWriter b2 = new BufferedWriter(new FileWriter(dts.d.dataPath+"_path_counts_posi"+"_p"+pst+"l"+lmax+"_delta"+noe.split(",")[0]+"_noe"+noe.split(",")[1]));
		BufferedWriter b1 = new BufferedWriter(new FileWriter(dts.d.dataPath+"_path_counts_nega"+"_m"+spset[0]+"_d"+defragID));
		BufferedWriter b2 = new BufferedWriter(new FileWriter(dts.d.dataPath+"_path_counts_posi"+"_m"+spset[0]+"_d"+defragID));
				
		ArrayList<int[]>arrs = new QueryGen().LinkPreQRead(dts.d, queryNums);//queryNums);
		for(int i=0;i<queryNums;i++){
			if (i % (queryNums / 10) == 0)System.out.print((int) (i * 10 / queryNums) + "->");
			//int[]st = new QueryGen().LinkPreQ(null, dts, Integer.MAX_VALUE);
			//int[]st = arrs.get(i);
			int qid = (int)(Math.random()*arrs.size());
			int[]st = arrs.get(qid);
			int ns = st[0], nt = st[1];
			double nscores = katz(ns,nt,g,lmax, b1, pset,noe,ifEnhanced, emp);//(ns,nt,g,lmax, beta, pset);
			int ps = st[2], pt = st[3];
			g.graph[ps].remove(g.graph[ps].indexOf(pt));
			g.graph[pt].remove(g.graph[pt].indexOf(ps));
			double pscores = katz(ps,pt,g,lmax, b2, pset,noe,ifEnhanced, emp);//(ps,pt,g,lmax, beta, pset);
			g.graph[ps].add(pt);
			g.graph[pt].add(ps);
			//generateMotifGraphThenKatz(dts, pset, ns, nt, ps, pt);
			
			}
		//System.out.println(nst+"\n"+pst);
		
		b1.flush();
		b1.close();
		b2.flush();
		b2.close();
		
	}

	public void run(String mainDir, int id, String[]spset, String noe, int queryNum, boolean ifEnhanced, boolean ifEdMot) throws IOException {
		Datasets dts = new Datasets();
		dts.Initialize(id);
		
		enhanced_motif_path emp = new enhanced_motif_path();
		if(ifEnhanced&!ifEdMot)
			emp.readInjectedEdges(dts, Integer.parseInt(spset[0]));
		run(dts, spset, noe, queryNum, ifEnhanced, 4, emp);
	}
	
	public void run(Datasets dts, int pid, int queryNum, int defragID, enhanced_motif_path emp) throws IOException {
		
		String[]spset = new String[1];
		spset[0] = pid+"";
		
		boolean ifEnhanced = false;
		if(defragID==1)
			ifEnhanced = true;
		
		run(dts, spset, "", queryNum, ifEnhanced, defragID, emp);
	}

	private void generateMotifGraphThenKatz(Datasets dts, int[] pset, int ns, int nt, int ps, int pt) throws IOException {
		// TODO Auto-generated method stub
		//Motif_graph_gen mgg = new Motif_graph_gen(dts, pset, ns, nt, dts.d.dataPath+"_"+pset[0]+"_Nega_"+ns+"_"+nt+"_");
		//mgg = new Motif_graph_gen(dts, pset, ps, pt, dts.d.dataPath+"_"+pset[0]+"_Posi_"+ps+"_"+pt+"_");
		System.out.print(ns+" "+nt+" "+ps+" "+pt+" ");
		for(int j=1;j<=4;j++){
			graphReady g =new graphReady();
			g.normalize(dts.d.dataPath+"_"+pset[0]+"_Nega_"+ns+"_"+nt+"_"+j, " ", 0, 1, 2, 0, false);
			graphwrite(g,dts.d.dataPath+"Norm_"+pset[0]+"_Nega_"+ns+"_"+nt+"_"+j);
			System.out.print(g.nodeNum+" ");
		}
		for(int j=1;j<=4;j++){
			graphReady g =new graphReady();
			g.normalize(dts.d.dataPath+"_"+pset[0]+"_Posi_"+ps+"_"+pt+"_"+j, " ", 0, 1, 2, 0, false);
			graphwrite(g,dts.d.dataPath+"Norm_"+pset[0]+"_Posi_"+ps+"_"+pt+"_"+j);
			System.out.print(g.nodeNum+" ");
		}
		System.out.println();
	}

	private void graphwrite(graphReady g, String s) throws IOException {
		BufferedWriter b = new BufferedWriter(new FileWriter(s));
		for(int i=1;i<=g.nodeNum;i++){
			for(int j=0;j<g.graph[i].size();j++){
				b.write(i+" "+g.graph[i].get(j)+"\n");
				b.write(g.graph[i].get(j)+" "+i+"\n");
			}
		}
		b.flush();
		b.close();
	}
	
	public int[][] samplingS(BFS bfs, Datasets dts, int spd_min, int layer_minsize) {
		
		int[][]st = new int[4][layer_minsize];
		int s = -1, t = -1;// spd = Integer.MAX_VALUE;
		
		int spd = spd_min;
		
		//positive sampling
		int psc = 0;
		while(psc<layer_minsize){
			s = (int)(Math.random()*dts.g.nodeNum+1);
			
			
			int tid = (int)(Math.random()*dts.g.graph[s].size());
			t = dts.g.graph[s].get(tid);
			if(!dts.g.graph[s].contains(t))continue;
			if(s==t)continue;

			int spd_posi = bfs.searchWioutST(s, t, dts.g.graph);
			int max = Integer.MAX_VALUE;
			if(spd_posi<spd_min||spd_posi==max)continue;
			System.out.println("Posi: d("+s+","+t+")="+spd_posi);
				
			st[2][psc] = s;
			st[3][psc] = t;
			
			psc++;
		}		
		
		
		s = -1;	t = -1;
		//negtive sampling
		int nsc = 0;
		while(s==t||dts.g.graph[s].contains(t)){
			s = (int)(Math.random()*dts.g.nodeNum+1);
			if(bfs!=null){
				
				bfs.search(s, Integer.MAX_VALUE, dts.g.graph);
				ArrayList<Integer>layer = new ArrayList();
				for(int i=1;i<bfs.jump.length;i++)
					if(bfs.jump[i]==spd&&i!=s){
						layer.add(i);
					}
				if(layer.size()<layer_minsize)
					continue;
				
				for(int i=0;i<layer_minsize;i++){
					int tid = (int)(Math.random()*layer.size());
					t = layer.get(tid);
					st[0][i]=s;
					st[1][i]=t;
					System.out.println("Nega: d("+s+","+t+")="+spd);
				}
				
				}
			nsc++;
		}

		
		
		return st;
		
	}


	private double katz(int s, int t, graphReady g, int lmax, double beta, int[]pset, boolean ifEnhanced, enhanced_motif_path emp) {

		int[]path_counts=new path_count(g).count(s,t,lmax, pset, null, ifEnhanced, emp);
		double katzscore = 0;
		for(int i=1;i<=lmax;i++){
			katzscore += (Math.pow(beta, i)*path_counts[i]);
			System.out.print(path_counts[i]+" ");
		}
		System.out.println();
		return katzscore;
	}
	
	private double katz(int s, int t, graphReady g, int lmax, BufferedWriter b, int[]pset, String link_type, boolean ifEnhanced, enhanced_motif_path emp) throws IOException {

		int[]path_counts=null;
		path_count pc = new path_count(g);
		
		path_counts = pc.count(s,t,lmax, pset, link_type, ifEnhanced, emp);
		//now pset is the paper format
			
		String str = "";
		for(int i=1;i<=lmax;i++){
			if(path_counts[i]<0)
				str+=Integer.MAX_VALUE;
			else
				str+=(path_counts[i]+" ");
		}
		str+=("\n");
		
		b.write(str);
		b.flush();
		//System.out.print(str);
		return 0;
	}

}
