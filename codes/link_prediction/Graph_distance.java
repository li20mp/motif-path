package link_prediction;

import java.io.IOException;
import java.util.ArrayList;

import algo.HBSMP;
import algo.enhanced_motif_path;
import algo.motif_path;
import tool.BFS;
import tool.Datasets;
import tool.graphReady;

public class Graph_distance {

	int pset[] = null;
	String noe = "";
	enhanced_motif_path mpe = null;

	public void run(Datasets dts, int pid, int queryNums, int defragID, enhanced_motif_path emp, char metricID) throws IOException {

		int[]pset = new int[1];
		pset[0] =pid;
		String pstr = "";
		for (int i = 0; i < pset.length; i++) {
			pstr+=(pset[i]+"_");
			}
		noe = "1,1";

		graphReady g = dts.g;
		mpe = new enhanced_motif_path();
		if(defragID==1)
			mpe.readInjectedEdges(dts, pset[0]);

		boolean ifEnhanced = false;
		if(defragID>0)
			ifEnhanced = true;
		
		int methodsNum = 1;
		double[] c1 = new double[5], c2 = new double[5]; // cn,jc,pa,aa,fm
		ArrayList<int[]> arrs = new QueryGen().LinkPreQRead(dts.d, queryNums);
		for (int i = 0; i < queryNums; ) {
			//if (i % (queryNums / 10) == 0)
			//	System.out.print((int) (i * 10 / queryNums) + "->");
			// int[]st = new QueryGen().LinkPreQ(null, dts, Integer.MAX_VALUE);
			int queryId = (int)(Math.random()*arrs.size());
			int[] st = arrs.get(queryId);
			int ns = st[0], nt = st[1];
			double []nscores = calculateGraphDistance(ns, nt, dts, defragID, metricID, pid, queryNums, emp);
			int ps = st[2], pt = st[3];
			if(g.graph[ps].indexOf(pt)==-1||g.graph[pt].indexOf(ps)==-1) {
				continue;
			}
			i++;
			g.graph[ps].remove(g.graph[ps].indexOf(pt));
			g.graph[pt].remove(g.graph[pt].indexOf(ps));
			double pscores[] = calculateGraphDistance(ps, pt, dts, defragID, metricID, pid, queryNums, emp);
			g.graph[ps].add(pt);
			g.graph[pt].add(ps);
			
			//if (i == queryNums - 1)
				System.out.print("m"+pstr+"after " + (i) + " iterations:\t");
			for (int j = 0; j < methodsNum; j++) {
				double nsc = nscores[j];
				double psc = pscores[j];
				if (psc > nsc)
					c1[j]++;
				if (psc == nsc)
					c2[j]++;
				double auc = (c1[j] + c2[j] / 2) / (i + 1);
				double acc = (c1[j]) / (i + 1);
				//if (i == queryNums - 1)
					System.out.print(auc + "\t");
			}
			//if (i == queryNums - 1)
				System.out.println();

			// generateMotifGraphThenKatz(dts, pset, ns, nt, ps, pt);
		}

	}

	private static double[] calculateGraphDistance(int s, int t, Datasets dts, int defragID, char metricID, int pid, int queryNum, enhanced_motif_path emp) throws IOException {

		
		double graphDist = -1;
		
		switch(metricID) {
		case 'r': graphDist = rootedPageRank(s, t, dts.g.graph, 1000, 0.85); break;
		case 'h': graphDist = hittingTime(s, t, dts.g.graph, 1000); break;
		case 't': graphDist = motif_path_graph_distance(s, t, dts, queryNum, defragID, pid, emp); break;
				
		}


		double[] res = new double[5];
		res[0] = graphDist;

		return res;
	}

	private static double motif_path_graph_distance(int s, int t, Datasets dts, int queryNum, int defragID, int pid, enhanced_motif_path emp) throws IOException {
		// input: pid in paper format
		
		if(pid==1) {
			BFS bfs = new BFS ();
			double spd = bfs.search(s, t, dts.g.graph);
			if(spd>0)
				spd = (double)1/spd;
			
			return spd;
		}
		
		int[]pset = new int[1];
		pset[0] = pid;
		int sd = 5;
		
		if(defragID>0) {
		emp.motif_path_enhanced(dts.g, pset, "", s, t);
		
		double res = 0;
		if (emp.SPD > 0) {
			res = (double) 1 / emp.SPD;
			return res;
			}
		
		return res;
		}
		
		motif_path mpf = new motif_path(dts.g, pset, "", s, t);
		double res = 0;
		if (mpf.SPD > 0 && mpf.SPD < sd)
			res = (double) 1 / mpf.SPD;
		return res;
	}

	static double rootedPageRank(int s, int t, ArrayList<Integer>[] graph, int walkNum, double alpha) {
		int hittingNum = 0;
		int cn = s;
		for (int i = 0; i < walkNum; i++) {
			if (cn == t)
				hittingNum++;
			double samp = Math.random();
			if (samp < alpha) {
				cn = s;
				continue;
			}
			int cns = graph[cn].size();
			int pickID = (int) (Math.random() * cns);
			cn = graph[cn].get(pickID);
		}
		return (double) 0 - (double) walkNum / hittingNum;
	}

	static double hittingTime(int s, int t, ArrayList<Integer>[] graph, int walkNum) {
		int hittingNum = 0;
		int cn = s;
		for (int i = 0; i < walkNum; i++) {
			if (cn == t)
				hittingNum++;
			int cns = graph[cn].size();
			int pickID = (int) (Math.random() * cns);
			cn = graph[cn].get(pickID);
		}
		return (double) 0 - (double) walkNum / hittingNum;
	}

}
