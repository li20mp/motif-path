package link_prediction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import algo.networkGen;
import tool.BFS;
import tool.Datasets;
import tool.graphReady;

public class Motif_common_neighbor {

	public double[] MCN(Datasets dts, int pid, int queryNums) throws IOException {
		

	
		int[]pset=new int[1];
		pset[0] = pid;
		
		graphReady g = dts.g;
		double c1 = 0, c2 = 0; 
		ArrayList<int[]>arrs = new QueryGen().LinkPreQRead(dts.d, queryNums);
		for(int i=0;i<queryNums;i++){
			if(i%(queryNums/10)==0) System.out.print((int)(i*10/queryNums)+"->");
			//int[]st = new QueryGen().LinkPreQ(null, dts, Integer.MAX_VALUE);
			int[]st = arrs.get(i);

			//int[]st = lp.samplingOnce(queryNums, null, dts, Integer.MAX_VALUE);
			int ns = st[0], nt = st[1];
			double nscores = runMCN(g,pset,ns,nt);//(ns,nt,g,lmax, beta, pset);
			int ps = st[2], pt = st[3];
			g.graph[ps].remove(g.graph[ps].indexOf(pt));
			g.graph[pt].remove(g.graph[pt].indexOf(ps));
			double pscores = runMCN(g,pset,ps,pt);//(ps,pt,g,lmax, beta, pset);
			g.graph[ps].add(pt);
			g.graph[pt].add(ps);
			//generateMotifGraphThenKatz(dts, pset, ns, nt, ps, pt);
			
			//b1.write(nscores+" "+pscores+"\n");
			//b1.flush();
			//System.out.print(i+": "+ nscores+" "+pscores+"\n");
			if(pscores>nscores)
				c1++;
			if(pscores==nscores)
				c2++;
			}
		
		double acc = c1/queryNums, auc = (2*c1+c2)/(2*queryNums);
		//System.out.println(acc+"\t"+auc);
		
		//b1.flush();
		//b1.close();
		double[]res = new double[2];
		res[0] = auc;
		res[1] = acc;
		
		System.out.println("AUC score: "+res[0]);
		return res;

	}
	
	
	public double[] MJI(String[] args, String mainDir) throws IOException {
		

		int id = Integer.parseInt(args[0]);
		String[]spset = args[1].split(",");
		int[]pset = new int[spset.length];
		for(int i=0;i<pset.length;i++)pset[i]=Integer.parseInt(spset[i]);
		
		Datasets dts = new Datasets();
		dts.Initialize(id);
		
		graphReady g = dts.g;
		
		//int gd = dts.g.diameter();
		//System.out.println("Nodes: "+g.nodeNum+"\nEdges: "+g.edgeNum+"\nDegree: "+g.getDegree()+"\nDiameter: "+gd);
		//BFS bfs = new BFS();
		
		double c1 = 0, c2 = 0; 
		int queryNums = Integer.parseInt(args[2]), lmax = 1;//Integer.parseInt(args[2]);//(int) ((double)gd/2.2);
		//System.out.println("L_max: "+lmax);
		String nst = "", pst="";
		//BufferedWriter b1 = new BufferedWriter(new FileWriter(mainDir+"pi_d"+id+"_p"+pset[0]));
		ArrayList<int[]>arrs = new QueryGen().LinkPreQRead(dts.d, queryNums);
		for(int i=0;i<queryNums;i++){
			if(i%(queryNums/10)==0) System.out.print((int)(i*10/queryNums)+"->");
			//int[]st = new QueryGen().LinkPreQ(null, dts, Integer.MAX_VALUE);
			int[]st = arrs.get(i);

			//int[]st = lp.samplingOnce(queryNums, null, dts, Integer.MAX_VALUE);
			int ns = st[0], nt = st[1];
			double nscores = run(g,pset,ns,nt);//(ns,nt,g,lmax, beta, pset);
			int ps = st[2], pt = st[3];
			g.graph[ps].remove(g.graph[ps].indexOf(pt));
			g.graph[pt].remove(g.graph[pt].indexOf(ps));
			double pscores = run(g,pset,ps,pt);//(ps,pt,g,lmax, beta, pset);
			g.graph[ps].add(pt);
			g.graph[pt].add(ps);
			//generateMotifGraphThenKatz(dts, pset, ns, nt, ps, pt);
			
			//b1.write(nscores+" "+pscores+"\n");
			//b1.flush();
			//System.out.print(i+": "+ nscores+" "+pscores+"\n");
			if(pscores>nscores)
				c1++;
			if(pscores==nscores)
				c2++;
			}
		
		double acc = c1/queryNums, auc = (2*c1+c2)/(2*queryNums);
		//System.out.println(acc+"\t"+auc);
		
		//b1.flush();
		//b1.close();
		double[]res = new double[2];
		res[0] = auc;
		res[1] = acc;
		
		return res;

	}
	
	public static double run(graphReady g, int[]pset, int s, int t){
		//String r = "";
		networkGen a = new networkGen (g);
		boolean[]motif_sig = new boolean[g.nodeNum+1];
		int res[]=null;
		
		
		res=a.motif_counts_st(pset, s, t);
		//motif_counts, motif_counts_containing_t
		double neis = 0, neit =0, cn = 0, c = 0;
		if(res!=null){
			neis=res[0];
			cn+=res[1];
			c++;
		}
		
		res=a.motif_counts_st(pset, t, s);
		if(res!=null){
			neit=res[0];
			cn+=res[1];
			c++;
		}
		cn=cn/c;
		double ji = 0;
		if((neis+neit)!=0)
			ji=cn/(neis+neit);
		
		return ji;
	}
	
	public static double runMCN(graphReady g, int[]pset, int s, int t){
		//String r = "";
		networkGen a = new networkGen (g);
		boolean[]motif_sig = new boolean[g.nodeNum+1];
		int res[]=null;
		
		
		res=a.motif_counts_st(pset, s, t);
		//motif_counts, motif_counts_containing_t
		double neis = 0, neit =0, cn = 0, c = 0;
		if(res!=null){
			neis=res[0];
			cn+=res[1];
			c++;
		}
		
		res=a.motif_counts_st(pset, t, s);
		if(res!=null){
			neit=res[0];
			cn+=res[1];
			c++;
		}
		cn=cn/c;
		
		return cn;
	}

}
