package algo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import tool.Dataset;
import tool.Datasets;
import tool.graphReady;
import tool.motif;

public class enhanced_motif_path extends motif_path{
	
	public ArrayList<Integer>[]injectedEdges = null;//most elements are null!
	
	public void readInjectedEdges(Datasets dts, int pid) throws IOException{
		injectedEdges = new ArrayList[dts.g.nodeNum+1];
		BufferedReader a = new BufferedReader(new FileReader(dts.d.dataPath+"-m"+pid+".mcom"));
		int[]comps = new int[dts.g.nodeNum+1];
		String[]tem = a.readLine().split(":")[1].split(",");
		for(int i=1;i<comps.length;i++) {
			comps[i] = Integer.parseInt(tem[i-1]);
		}
		int counter = 0;
		for(int i=1;i<dts.g.graph.length;i++) {
			for(int j=0;j<dts.g.graph[i].size();j++) {
				int nei = dts.g.graph[i].get(j);
				if(comps[i]!=comps[nei]) {
					counter++;
					if(injectedEdges[i]==null) 
						injectedEdges[i] = new ArrayList();
					if(injectedEdges[nei]==null) 
						injectedEdges[nei] = new ArrayList();
					if(!injectedEdges[i].contains(nei))
						injectedEdges[i].add(nei);
					if(!injectedEdges[nei].contains(i))
						injectedEdges[nei].add(i);
					}
			}
		}
		
		System.out.println("Finish reading "+counter +" edges to be injected.");
		
	}
	
	public void readInjectedEdgesEdmot(Datasets dts) throws IOException{
		injectedEdges = new ArrayList[dts.g.nodeNum+1];
		BufferedReader a = new BufferedReader(new FileReader(dts.d.dataPath+"-edmot-new.rwdata"));
		
		String s = a.readLine();
		int counter = 1;
		while(s!=null&&!s.equals("")) {
			String[]tem = s.split("\t");
			int n1 = Integer.parseInt(tem[0]);
			int n2 = Integer.parseInt(tem[1]);
			if(injectedEdges[n1]==null) 
				injectedEdges[n1] = new ArrayList();
			if(injectedEdges[n2]==null) 
				injectedEdges[n2] = new ArrayList();
			if(!injectedEdges[n1].contains(n2))
				injectedEdges[n1].add(n2);
			if(!injectedEdges[n2].contains(n1))
				injectedEdges[n2].add(n1);
			s=a.readLine();
			counter ++;
		}
		
		System.out.println("Finish reading "+counter +" edges to be injected.");
		
	}
	
	public motif[]touched = null;
	public boolean[] motif_sig = null;
	public int motif_path_enhanced_incremental(graphReady g, int[]query, String link_type, int s, int t){
		motif_path mp = new motif_path(g,query,link_type,s,t); 
		int jumpAll[] = new int[g.nodeNum+1];
		int max = Integer.MAX_VALUE;
		if(mp.SPD>=1&&mp.SPD<max)
			return SPD;
		
		jumpAll[s] = 1;
		ArrayList<Integer>q = new ArrayList();
		for(int i=1;i<=g.nodeNum;i++) {
			if(i!=s&&mp.jump[i]>0&&mp.jump[i]<max) {
				jumpAll[i] = mp.jump[i];
				for(int j=0;j<g.graph[i].size();j++) {
					int nei = g.graph[i].get(j);
					if(mp.jump[nei]<=0||mp.jump[nei]==max) {
						//bridging edge detected
						q.add(nei);
						//System.out.println(i+"-"+nei);
						jumpAll[nei] = jumpAll[i]+1;
					}
				}
			}
		}
		
		while(q.size()>0) {
			int nei = q.remove(0);
			motif_path mp2 = new motif_path(g,query,link_type,nei,t);
			if(mp2.SPD>=1&&mp2.SPD<max)
				return jumpAll[nei]+mp2.SPD;
			for(int i=1;i<=g.nodeNum;i++) {
				if(mp2.jump[i]>0&&i!=nei&&mp2.jump[i]<max) {
					jumpAll[i] = jumpAll[nei]+mp2.jump[i];
					for(int j=0;j<g.graph[i].size();j++) {
						int nei2 = g.graph[i].get(j);
						if(mp2.jump[nei2]<=0||mp2.jump[nei2]==max) {
							//bridging edge detected
							q.add(nei2);
							//System.out.println(i+"-"+nei2);
							jumpAll[nei2] = jumpAll[i]+1;
						}
					}
				}
			}
				
		}
		
		
		return SPD;
		
		
		//System.out.println("No motif-path exists!");
		
	}
	
	
	public int motif_path_enhanced(graphReady g, int[]query, String link_type, int s, int t){
		String[]link=link_type.split(",");
		int max = Integer.MAX_VALUE;
		boolean linksig = false;
		int delta = -1, noe = -1; // noe=1/2: two motif-instances should share delta nodes/edges
		if(link_type.length()!=0&&link.length==2){
			delta = Integer.parseInt(link[0]);
			noe = Integer.parseInt(link[1]);
			linksig = true; // start using seeds.
		}
			
		motif_sig= new boolean[g.nodeNum+1];
		touched = new motif [g.nodeNum+1];
		jump = new int[g.nodeNum+1];
		for(int i=1;i<jump.length;i++)
			jump[i] = Integer.MAX_VALUE;
		networkGen a = new networkGen(g);
		ArrayList<ArrayList<Integer>>sols=a.motifInstances(query, s, motif_sig, t);
		enhance(sols, s);
		
		Queue<motif>q=new LinkedList<motif>();
		
		motif_sequense = new ArrayList();
		if(a.FindTSig||a.overflowSig){
			if(sols!=null&&sols.size()>0) {
				motif_sequense.add(sols.get(sols.size()-1));
				//System.out.print(sols.get(sols.size()-1).toString());
				}
			SPD = 1;
			return SPD;
			}
		
		ArrayList<Integer>cans;
		for(int i=0;i<sols.size();i++){
			cans = sols.get(i);
			if(cans.contains(t)){
				//System.out.print(cans.toString());
				motif_sequense.add(cans);
				SPD = 1;
				return SPD;
			}
			motif nm = null;
			if(cans.size()>2)
				nm = new motif(cans,a.solMotifTypes.get(i));
			else
				nm = new motif(cans,30);
			//for each motif-instance, check if it is touched.
			boolean touchSig = true;
			for (int p = 0; p<cans.size(); p++){
				int ned = cans.get(p);
				if(touched[ned]==null){
					touched[ned] = nm;
					jump[ned]=1;
					touchSig = false;
				}
			}
			if(!touchSig)
				q.add(nm);	
		}
		motif_sig[s]=true;
		
		while(!q.isEmpty()&&(t==max||!motif_sig[t])){
			motif cr = q.poll();
			for(int ii=0;ii<cr.nodes.size();ii++){
				int nei0 = cr.nodes.get(ii);
				if(motif_sig[nei0])continue;
				
				//System.out.println("Start finding motif-instances around node: "+nei0);
				if(linksig)
					sols=a.motifInstances(query, nei0, cr.nodes, delta, noe, motif_sig, t);
				else
					sols=a.motifInstances(query, nei0, motif_sig, t);
				
				enhance(sols, nei0);
				
				for(int i=0;i<sols.size();i++){
					cans = sols.get(i);
					motif m1 = null;
					if(cans.size()>2)
						m1 = new motif(cans,a.solMotifTypes.get(i));
					else
						m1 = new motif(cans,30);
					m1.last = cr;
					if(cans.contains(t)){
						SPD = backtrack(m1, g);
						//System.out.println("Motif-path found with length = "+SPD);
						return SPD;
					}
					//for each motif-instance, check if it is touched.
					boolean touchSig = true;
					for (int p = 0; p<cans.size(); p++){
						int ned = cans.get(p);
						if(touched[ned]==null){
							touched[ned] = m1;
							jump[ned]=jump[nei0]+1;
							touchSig = false;
						}
					}
					if(!touchSig)
						q.add(m1);	
				}
				if(a.FindTSig||a.overflowSig){
					if(a.FindTSig)System.out.println("Motif-path found with length = 0");
					if(a.overflowSig){SPD=-1;System.out.println("Motif-path overflow");}
					SPD = 1;
					return SPD;
					}
				motif_sig[nei0]=true;
				
			}			
		}
		
		
		
		return SPD;
		
		
		//System.out.println("No motif-path exists!");
		
	}
	public ArrayList<ArrayList<Integer>> enhance(ArrayList<ArrayList<Integer>> sols, int s) {
		if(injectedEdges!=null&&injectedEdges[s]!=null) {
			for(int i=0;i<injectedEdges[s].size();i++) {
				int nei = injectedEdges[s].get(i);
				ArrayList<Integer>tem = new ArrayList();
				tem.add(s);
				tem.add(nei);
				sols.add(tem);
			}
		}
		return sols;
		
	}
	
	public ArrayList<Integer> motif_path_early_stop(graphReady g, int[]query, String link_type, int s, int k){
		//single-sourced SMP to reach each node in the graph, until k-nodes are marked
		
		ArrayList<Integer>res = new ArrayList();
		
		String[]link=link_type.split(",");
		boolean linksig = false;
		int delta = -1, noe = -1; // noe=1/2: two motifs should share delta nodes/edges
		if(link_type.length()!=0&&link.length==2){
			delta = Integer.parseInt(link[0]);
			noe = Integer.parseInt(link[1]);
			linksig = true; // start using seeds.
		}
		boolean[]motif_sig= new boolean[g.nodeNum+1];
		motif[]touched = new motif [g.nodeNum+1];
		jump = new int[g.nodeNum+1];
		for(int i=0;i<jump.length;i++)jump[i]=Integer.MAX_VALUE;
		networkGen a = new networkGen(g);
		ArrayList<ArrayList<Integer>>sols=a.motifInstances(query, s, motif_sig, Integer.MAX_VALUE);
		//ArrayList<ArrayList<Integer>>solt=a.motifInstances(query, g, t);
		sols = enhance(sols, s);
		
		Queue<motif>q=new LinkedList<motif>();
		
		if(a.FindTSig||a.overflowSig){
			//if(a.FindTSig)System.out.println("Motif-path found with length = 0");
			//if(a.overflowSig)System.out.println("Motif-path overflow");
			SPD = 1;
			return res;
			}
		ArrayList<Integer>cans;
		for(int i=0;i<sols.size();i++){
			cans = sols.get(i);
			motif nm = null;
			if(cans.size()>2)
				nm = new motif(cans,a.solMotifTypes.get(i));
			else
				nm = new motif(cans, 30);
			//for each motif-instance, check if it is touched.
			boolean touchSig = true;
			for (int p = 0; p<cans.size(); p++){
				int ned = cans.get(p);
				if(touched[ned]==null){
					touched[ned] = nm;
					jump[ned]=1;
					touchSig = false;
					if(ned!=s)
						res.add(ned);
					if(res.size()==k) {
						return res;
					}
				}
			}
			if(!touchSig)
				q.add(nm);	
		}
		motif_sig[s]=true;
		
		//while(!q.isEmpty()&&untouched(touched)){
		while(!q.isEmpty()){
			motif cr = q.poll();
			for(int ii=0;ii<cr.nodes.size();ii++){
				int nei0 = cr.nodes.get(ii);
				if(motif_sig[nei0])continue;
				
				//System.out.println("Start finding motif-instances around node: "+nei0);
				if(linksig)
					sols=a.motifInstances(query, nei0, cr.nodes, delta, noe, motif_sig, Integer.MAX_VALUE);
				else
					sols=a.motifInstances(query, nei0, motif_sig, Integer.MAX_VALUE);
				sols = enhance(sols, nei0);
				for(int i=0;i<sols.size();i++){
					cans = sols.get(i);
					motif m1 = null;
					if(cans.size()>2)
						m1 = new motif(cans,a.solMotifTypes.get(i));
					else
						m1 = new motif(cans,30);
					m1.last = cr;
					//for each motif-instance, check if it is touched.
					boolean touchSig = true;
					for (int p = 0; p<cans.size(); p++){
						int ned = cans.get(p);
						if(touched[ned]==null){
							touched[ned] = m1;
							jump[ned]=jump[nei0]+1;
							touchSig = false;
							if(ned!=s)
								res.add(ned);
							if(res.size()==k) {
								return res;
							}
						}
					}
					if(!touchSig)
						q.add(m1);	
				}
				if(a.FindTSig||a.overflowSig){
					if(a.FindTSig)System.out.println("Motif-path found with length = 0");
					if(a.overflowSig){SPD=-1;System.out.println("Motif-path overflow");}
					SPD = 1;
					return res;
					}
				motif_sig[nei0]=true;
				
			}			
		}
		
		return res;
		//System.out.println("No motif-path exists!");
		
	}
	
}
