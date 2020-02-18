package link_prediction;

import java.io.IOException;
import java.util.ArrayList;

import tool.BFS;
import tool.Datasets;
import tool.graphReady;

public class Traditional_lp {

	BFS bfs = new BFS();
	public void run (Datasets dts, int queryNums, int j) throws IOException {
		graphReady g = dts.g;
		double[]c1=new double[5], c2 = new double[5]; // cn,jc,pa,aa,fm
		ArrayList<int[]>arrs = new QueryGen().LinkPreQRead(dts.d, queryNums);
		for(int i=0;i<queryNums;i++){
			//if (i % (queryNums / 10) == 0)System.out.print((int) (i * 10 / queryNums) + "->");
			//int[]st = new QueryGen().LinkPreQ(null, dts, Integer.MAX_VALUE);
			int[]st = arrs.get(i);
			//int t = (int)(Math.random()*(arrs.size()));
			//int[]st = arrs.get(t);
			int ns = st[0], nt = st[1];
			double nscores[] = calculateCN(ns,nt,g.graph);
			int ps = st[2], pt = st[3];
			g.graph[ps].remove(g.graph[ps].indexOf(pt));
			g.graph[pt].remove(g.graph[pt].indexOf(ps));
			double pscores[] = calculateCN(ps,pt,g.graph);
			g.graph[ps].add(pt);
			g.graph[pt].add(ps);
			if(i==queryNums-1)
				System.out.print("after "+(i+1)+" queries:\t");
			//for(int j=0;j<nscores.length;j++) {
				double nsc = nscores[j];
				double psc = pscores[j];
				if(psc>nsc)
					c1[j]++;
				if(psc==nsc)
					c2[j]++;
				double auc = (c1[j]+c2[j]/2)/(i+1);
				double acc = (c1[j])/(i+1);
				if(i==queryNums-1)
					System.out.print(auc+"\t");
			//	}
			if(i==queryNums-1)
				System.out.println();
				
			//generateMotifGraphThenKatz(dts, pset, ns, nt, ps, pt);				
			}
	}
	private double[] calculateCN(int s, int t, ArrayList<Integer>[] graph) {
		int sn = graph[s].size();
		int tn = graph[t].size();
		ArrayList<Integer>union = new ArrayList(), intersection=new ArrayList();
		for(int i=0;i<sn;i++) {
			union.add(graph[s].get(i));
		}
		for(int i=0;i<tn;i++) {
			int nei = graph[t].get(i);
			if(!union.contains(nei))
				union.add(nei);
			else
				intersection.add(nei);
		}
		double unionNum = union.size();
		double intersectionNum = intersection.size();
		
		double cn = intersectionNum;
		double jc = intersectionNum/unionNum;
		double pa = sn*tn;
		double aa = 0;
		double fm = intersectionNum;
		for(int i=0;i<sn;i++) {
			int neis = graph[s].get(i);
			for(int j=0;j<graph[t].size();j++) {
				int neit = graph[t].get(j);
				if(graph[neis].contains(neit))
					fm++;
			}
		}
		for(int i=0;i<intersection.size();i++) {
			int nei = intersection.get(i);
			double neis = graph[nei].size();
			if(neis!=0) {
				if(neis==1) {
					System.out.println("Only 1 neibour of "+nei);
					aa += Double.MAX_VALUE;
					break;
				}else
					aa += ((double)1/(Math.log(neis)));
				
			}
				
		}
		
		double[]res = new double[5];
		res[0] = cn;
		res[1] = jc;
		res[2] = aa;
		res[3] = pa;
		res[4] = fm;
		
		return res;
	}

}
