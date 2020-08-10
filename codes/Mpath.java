import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import algo.Cache;
import algo.CachedMotifPath;
import algo.InsCached;
import algo.enhanced_motif_path;
import algo.motif_path;
import tool.BFS;
import tool.Dataset;
import tool.Datasets;
import tool.Interact;
import tool.graphReady;

public class Mpath {

	public static void main(String[] args) throws IOException {
		
		String mainDir = "data/ppi/gavin";
		int s = 1, t = 2, pid = 5, defragID = 0, bid = 0, cid=0, cachedLevel = 0, oid=0;
		Interact nt = new Interact();
		for(int i=0;i<args.length;i++) {
			String[]tem=args[i].split(":");
			char temc = tem[0].charAt(1);
			switch(temc) {
			case 'g': mainDir = args[i].substring(3); break;
			case 's': s = nt.intwarn(tem[1], s, 's'); break;
			case 't': t = nt.intwarn(tem[1], t, 't'); break;
			case 'm': pid = nt.intwarn(tem[1], pid, 'm'); break;
			case 'd': defragID = nt.intwarn(tem[1], defragID, 'd'); break;
			case 'b': bid = 1; break;
			case 'c': cid = 1; cachedLevel = nt.intwarn(tem[1], cachedLevel, 'c'); break;
			case 'o': oid = 1; cachedLevel = nt.intwarn(tem[1], cachedLevel, 'o'); break;
			}
		}
		
		if(cid==1&&cachedLevel==0)
			cid = 0;
		
		if(defragID==2) {
			//emp.readInjectedEdgesEdmot(dts);
			graphReady g = new graphReady();
			g.readGraph(mainDir+".edmot", ",", 0, 1, 2, 1, false);
			
			Dataset d = new Dataset (mainDir+".edmot", ",", 1);
			d.g = g;
			Datasets dts = new Datasets();
			dts.d = d;
			dts.g = g;
			
			if(pid==5) {
				outputEmp(pid, new enhanced_motif_path(), g, s, t, defragID);
			}else {
				System.out.println("Edmot only supports triangles (-m:5)!");
			}
			
			return;
			}
		
		graphReady g = new graphReady();
		g.readGraph(mainDir, ",", 0, 1, 2, 1, false);
		
		Dataset d = new Dataset (mainDir, ",", 1);
		d.g = g;
		Datasets dts = new Datasets();
		dts.d = d;
		dts.g = g;
		
		if(bid == 1) {
			//run codes for generating motif-components
			motif_components(dts, pid);
			System.out.println("Motif components calculation finished!");
			return;
		}
		
		if(oid==1) {
			if(cachedLevel==0) {
				System.out.println("Please use bigger caching level!");
				return;
			}
			Cache ce = new Cache();
			ce.buildMOD(dts, cachedLevel);
			System.out.println("MOD-Index construction finished!");
			return;
		}
		
		if(pid==1) {
			BFS bfs = new BFS();
			ArrayList<Integer> sp = bfs.searchPath(s, t, g.graph);
			System.out.println("Shortest path:");
			for(int i=sp.size()-1;i>=0;i--) {
				if(i>0)
					System.out.print(sp.get(i)+"->");
				else
					System.out.println(sp.get(i));
			}
			System.out.println("\nShortest path distance (motif m"+pid+") between "+s+" and "+t+" is "+(sp.size()-1)+".");
			return;
		}
		
		if(cid == 1) {
			//find motif-path with caching
			CachedMotifPath cmp = new CachedMotifPath();
			InsCached ic = new InsCached();
			ic.loadCached(dts, cachedLevel);
			System.out.println("Index loaded.");

			enhanced_motif_path emp = new enhanced_motif_path();
			ArrayList<Integer>[]injectedEdges = null;
			
			if(defragID==1)
				injectedEdges = emp.readInjectedEdges(dts, pid);
			if(defragID==2)
				injectedEdges = emp.readInjectedEdgesEdmot(dts);
			
			int enhanceSig = 0;
			if(defragID>0)
				enhanceSig = 1;
			cmp.smpOResmpST(dts, pid, enhanceSig, cachedLevel, ic, emp, injectedEdges, s, t);
			
			return;
		}
		
		enhanced_motif_path emp = new enhanced_motif_path();
		if(defragID==1)
			emp.readInjectedEdges(dts, pid);
		if(emp.comps[s] == emp.comps[t]) {
			int[]pset = new int[1];
			pset[0] = pid;
			motif_path mp = new motif_path(g,pset,"",s,t);
			return;
		}
		
		outputEmp(pid, emp, g, s, t, defragID);
	}
	


	private static void outputEmp(int pid, enhanced_motif_path emp, graphReady g, int s, int t, int defragID) {
		int[]pset = new int[1];
		pset[0] = pid;
		int spd = emp.motif_path_enhanced(g, pset, "", s, t);
		
		System.out.println("Motif-path:");
		ArrayList<Integer>motif_path = new ArrayList();
		motif_path.add(s);
		for(int i=emp.motif_sequense.size()-1;i>=0;i--) {
			if(i>0) {
				System.out.print(emp.motif_sequense.get(i)+"->");
				ArrayList<Integer> cn = emp.motif_sequense.get(i), former = emp.motif_sequense.get(i-1);
				for(int j=0;j<cn.size();j++){
					if(former.contains(cn.get(j))) {
						motif_path.add(cn.get(j));
						break;
					}
				}
				}
			else
				System.out.println(emp.motif_sequense.get(i));
			
		}
		motif_path.add(t);
		//System.out.println("Motif-path:");
		for(int i=0;i<motif_path.size();i++) {
			//if(i<motif_path.size()-1)
			//	System.out.print(motif_path.get(i)+"->");
			//else
				//System.out.println(motif_path.get(i));
		}
		
		System.out.print("\nShortest motif-path distance (motif m"+pid+") between "+s+" and "+t+" is "+spd+", ");
		if(defragID==0)
			System.out.println("without defragmentation.");
		if(defragID==1)
			System.out.println("with bridging-edge based defragmentation.");
		if(defragID==2)
			System.out.println("with defragmentation from EdMot.");
	}



	static void motif_components(Datasets dts, int pid) throws IOException {
		int[]components=new int[dts.g.nodeNum+1];
		ArrayList<Integer>diameters = new ArrayList();
		int cComponentID = 0;
		int max = Integer.MAX_VALUE;

		
		for(int i=1;i<=dts.g.nodeNum;i++) {
			if(i%(dts.g.nodeNum/100)==0)
				System.out.print(i/(dts.g.nodeNum/100)+"->");
			if(components[i]>0)
				continue;
			if(components[i]==0) {
				cComponentID++;
				components[i] = cComponentID;
			}
			//System.out.println("start searching from node "+i+" with degree "+dts.g.graph[i].size());
			int[]pset = new int[1];
			pset[0] = pid;
			motif_path mpf = new motif_path(dts.g, pset, "", i);
			int diameter = 0;
			for(int j=1;j<=dts.g.nodeNum;j++) {
				if(i==j) {
					continue;
				}
				int smpd_ij = mpf.jump[j];
				if(smpd_ij>=0&&smpd_ij<max) {
					if(smpd_ij>diameter)
						diameter = smpd_ij;
					if(components[j]==0)
						components[j] = components[i];
				}
			}
			diameters.add(diameter);
		}

		
		BufferedWriter b = new BufferedWriter(new FileWriter(dts.d.dataPath+"_m"+(pid)+".mcom"));
		b.write("Components("+cComponentID+"):");
		for(int i=1;i<=dts.g.nodeNum;i++) {
			b.write(components[i]+",");
		}
		b.write("\n");
		b.write("Diameters:");
		for(int i =0;i<diameters.size();i++) {
			b.write(diameters.get(i)+",");
		}
		b.flush();
		b.close();
		
	}

}
