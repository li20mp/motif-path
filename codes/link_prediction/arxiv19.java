package link_prediction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import algo.networkGen;
import tool.BFS;
import tool.Dataset;
import tool.Datasets;
import tool.graphReady;

public class arxiv19 {

	public void writeVec(int queryNums, Datasets dts) throws IOException {

		graphReady g = dts.g;
		
		BufferedWriter b1 = new BufferedWriter(new FileWriter(dts.d.dataPath+"_nega_vec"));
		BufferedWriter b2 = new BufferedWriter(new FileWriter(dts.d.dataPath+"_posi_vec"));

		// int gd = dts.g.diameter();
		// System.out.println("Nodes: "+g.nodeNum+"\nEdges: "+g.edgeNum+"\nDegree:"+g.getDegree()+"\nDiameter: "+gd);

		networkGen a = new networkGen(dts.g);
		
		ArrayList<int[]> arrs = new QueryGen().LinkPreQRead(dts.d, queryNums);
		for (int i = 0; i < queryNums; i++) {
			//if (i % (queryNums / 10) == 0)System.out.print((int) (i * 10 / queryNums) + "->");	
			int[] st = arrs.get(i);
			int ns = st[0], nt = st[1];
			
			int[] nvec = countMotifs (g,ns,nt,a);
			String str = "";for(int j=0;j<nvec.length;j++)str+=(nvec[j]+"\t");str+="\n";b1.write(str);b1.flush();
			System.out.println(str);
			
			int ps = st[2], pt = st[3];
			int psid = g.graph[ps].indexOf(pt), ptid = g.graph[pt].indexOf(ps);
			g.graph[ps].remove(psid);
			g.graph[pt].remove(ptid);
			int[] pvec = countMotifs (g,ps,pt,a);
			g.graph[ps].add(pt);
			g.graph[pt].add(ps);
			str = "";for(int j=0;j<pvec.length;j++)str+=(pvec[j]+"\t");str+="\n";b2.write(str);b2.flush();
			System.out.println(str);

		}

		b1.flush();b1.close();
		b2.flush();b2.close();

	}
	
	public void writeVec(int id, int queryNums, String mainDir) throws IOException {
		//int id = Integer.parseInt(args[1]);

		Datasets dts = new Datasets();
		dts.Initialize(id);

		writeVec(queryNums, dts);

	}

	public int[] countMotifs(graphReady g, int s, int t, networkGen a) {
		int[]vec = new int [30];
		int[] pset = new int[30];
		for (int i = 0; i < pset.length; i++)
			pset[i] = 30-i;
		
		ArrayList<ArrayList<Integer>> tem = a.motifInstances(pset, s, new boolean[g.nodeNum+1], Integer.MAX_VALUE);
		for(int j=0;j<tem.size();j++) {
			ArrayList<Integer> cmotif = tem.get(j);
			int motifId = 31 - a.solMotifTypes.get(j);
			if(cmotif.contains(t)) {
				vec[motifId-1]++;
				//System.out.println(cmotif.toString());
			}
		}
		
		/*
		for(int i=0;i<pset.length;i++) {
			int[]apset =new int[1];
			apset[0] = pset[i];
			System.out.println("Find motifs "+apset[0]+" around node "+s+" - degree = "+g.graph[s].size());
			ArrayList<ArrayList<Integer>> tem = a.motifInstances(apset, s, new boolean[g.nodeNum+1], Integer.MAX_VALUE);
			for(int j=0;j<tem.size();j++) {
				ArrayList<Integer> cmotif = tem.get(j);
				if(cmotif.contains(t)) {
					vec[i]++;
					//System.out.println(cmotif.toString());
				}
			}
			
		}*/

		return vec;
	}

}
