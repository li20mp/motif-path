package algo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

//import org.graphstream.graph.Graph;

import tool.Combination;
import tool.graphReady;
import tool.motif;
import visulization.dataVisual;

public class motif_path {
	public int SPD = Integer.MAX_VALUE;
	public motif_path() {
		
	}
	//protected boolean ifConnected;
	public motif_path(graphReady g, int[]query, String link_type, int s, int t){
		//ifConnected = true;
		jump = new int[g.nodeNum+1];
		String[]link=link_type.split(",");
		boolean linksig = false;
		int delta = -1, noe = -1; // noe=1/2: two motifs should share delta nodes/edges
		if(link_type.length()!=0&&link.length==2){
			delta = Integer.parseInt(link[0]);
			noe = Integer.parseInt(link[1]);
			linksig = true; // start using seeds.
		}
		
		
		//int s = 13, t = 0;
		//int[]query={21,15,9};
		boolean[]motif_sig= new boolean[g.nodeNum+1];
		motif[]touched = new motif [g.nodeNum+1];
		
		networkGen a = new networkGen(g);
		ArrayList<ArrayList<Integer>>sols=a.motifInstances(query, s, motif_sig, t);
		//ArrayList<ArrayList<Integer>>solt=a.motifInstances(query, g, t);
		
		Queue<motif>q=new LinkedList<motif>();
		
		if(a.FindTSig||a.overflowSig){
			//if(a.FindTSig)System.out.println("Motif-path found with length = 0");
			//if(a.overflowSig)System.out.println("Motif-path overflow");
			SPD = 1;
			return;
			}
		
		ArrayList<Integer>cans;
		for(int i=0;i<sols.size();i++){
			cans = sols.get(i);
			if(cans.contains(t)){
				//System.out.println("Motif-path found with length = 0: " + cans.toString());
				SPD = 1;
				return;
			}
			motif nm = new motif(cans,a.solMotifTypes.get(i));
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
		jump[s] = 1;
		
		while(!q.isEmpty()&&!motif_sig[t]){
			motif cr = q.poll();
			for(int ii=0;ii<cr.nodes.size();ii++){
				int nei0 = cr.nodes.get(ii);
				if(motif_sig[nei0])continue;
				
				//System.out.println("Start finding motif-instances around node: "+nei0);
				if(linksig)
					sols=a.motifInstances(query, nei0, cr.nodes, delta, noe, motif_sig, t);
				else
					sols=a.motifInstances(query, nei0, motif_sig, t);
				for(int i=0;i<sols.size();i++){
					cans = sols.get(i);
					motif m1 = new motif(cans,a.solMotifTypes.get(i));
					m1.last = cr;
					if(cans.contains(t)){
						jump[t]=jump[nei0]+1;
						SPD = backtrack(m1);
						//System.out.println("Motif-path found with length = "+SPD);
						return;
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
					return;
					}
				motif_sig[nei0]=true;
				
			}			
		}
		
		
		//System.out.println("No motif-path exists!");
		
	}
	
	public int [] jump = null;
	public motif_path(graphReady g, int[]query, String link_type, int s, ArrayList<Integer>tNodes){
		//single-sourced SMP to reach tNodes only
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
		for(int i=1;i<jump.length;i++)
			jump[i] = Integer.MAX_VALUE;
		networkGen a = new networkGen(g);
		ArrayList<ArrayList<Integer>>sols=a.motifInstances(query, s, motif_sig, Integer.MAX_VALUE);
		//ArrayList<ArrayList<Integer>>solt=a.motifInstances(query, g, t);
		
		Queue<motif>q=new LinkedList<motif>();
		
		if(a.FindTSig||a.overflowSig){
			//if(a.FindTSig)System.out.println("Motif-path found with length = 0");
			//if(a.overflowSig)System.out.println("Motif-path overflow");
			SPD = 1;
			return;
			}
		
		ArrayList<Integer>cans;
		for(int i=0;i<sols.size();i++){
			cans = sols.get(i);
			motif nm = new motif(cans,a.solMotifTypes.get(i));
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
		
		while(!q.isEmpty()&&untouched(tNodes,touched)){
			motif cr = q.poll();
			for(int ii=0;ii<cr.nodes.size();ii++){
				int nei0 = cr.nodes.get(ii);
				if(motif_sig[nei0])continue;
				
				//System.out.println("Start finding motif-instances around node: "+nei0);
				if(linksig)
					sols=a.motifInstances(query, nei0, cr.nodes, delta, noe, motif_sig, Integer.MAX_VALUE);
				else
					sols=a.motifInstances(query, nei0, motif_sig, Integer.MAX_VALUE);
				for(int i=0;i<sols.size();i++){
					cans = sols.get(i);
					motif m1 = new motif(cans,a.solMotifTypes.get(i));
					m1.last = cr;
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
					return;
					}
				motif_sig[nei0]=true;
				
			}			
		}
		
		
		//System.out.println("No motif-path exists!");
		
	}
	
	public motif touched[] = null;
	public ArrayList<ArrayList<Integer>>motif_adjs = null;
	public motif_path(graphReady g, int[]query, String link_type, int s){
		//single-sourced SMP to reach each node in the graph
		String[]link=link_type.split(",");
		boolean linksig = false;
		int delta = -1, noe = -1; // noe=1/2: two motifs should share delta nodes/edges
		if(link_type.length()!=0&&link.length==2){
			delta = Integer.parseInt(link[0]);
			noe = Integer.parseInt(link[1]);
			linksig = true; // start using seeds.
		}
		boolean[]motif_sig= new boolean[g.nodeNum+1];
		touched = new motif [g.nodeNum+1];
		jump = new int[g.nodeNum+1];
		for(int i=0;i<jump.length;i++)jump[i]=Integer.MAX_VALUE;
		networkGen a = new networkGen(g);
		ArrayList<ArrayList<Integer>>sols=a.motifInstances(query, s, motif_sig, Integer.MAX_VALUE);
		//ArrayList<ArrayList<Integer>>solt=a.motifInstances(query, g, t);
		motif_adjs = sols;
		//int counter = 1; System.out.println(counter++ + " layer: "+sols.size());
		Queue<motif>q=new LinkedList<motif>();
		
		if(a.FindTSig||a.overflowSig){
			//if(a.FindTSig)System.out.println("Motif-path found with length = 0");
			//if(a.overflowSig)System.out.println("Motif-path overflow");
			SPD = 1;
			return;
			}
		
		ArrayList<Integer>cans;
		for(int i=0;i<sols.size();i++){
			cans = sols.get(i);
			motif nm = new motif(cans,a.solMotifTypes.get(i));
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
		
		//while(!q.isEmpty()&&untouched(touched)){
		while(!q.isEmpty()){
			motif cr = q.poll();
			for(int ii=0;ii<cr.nodes.size();ii++){
				int nei0 = cr.nodes.get(ii);
				if(motif_sig[nei0])continue;
				
				//System.out.println("start searching from node "+nei0+" with degree "+g.graph[nei0].size());
				if(linksig)
					sols=a.motifInstances(query, nei0, cr.nodes, delta, noe, motif_sig, Integer.MAX_VALUE);
				else
					sols=a.motifInstances(query, nei0, motif_sig, Integer.MAX_VALUE);
				//System.out.println(counter++ + " layer: "+sols.size());
				for(int i=0;i<sols.size();i++){
					cans = sols.get(i);
					motif m1 = new motif(cans,a.solMotifTypes.get(i));
					m1.last = cr;
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
					return;
					}
				motif_sig[nei0]=true;
				
			}			
		}
		
		
		//System.out.println("No motif-path exists!");
		
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
			motif nm = new motif(cans,a.solMotifTypes.get(i));
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
				for(int i=0;i<sols.size();i++){
					cans = sols.get(i);
					motif m1 = new motif(cans,a.solMotifTypes.get(i));
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
	
	
	private boolean untouched(ArrayList<Integer> tNodes, motif[] touched) {
		//every node in tNodes should be touched
		for(int i=0;i<tNodes.size();i++){
			int cn = tNodes.get(i);
			if(touched[cn]==null)
				return false;
		}
		
		return true;
	}
	
	private boolean untouched(motif[] touched) {
		//every node should be touched
		for(int i=0;i<touched.length;i++){
			if(touched[i]==null)
				return false;
		}
		return true;
	}

	public ArrayList<ArrayList<Integer>>motif_sequense = null;
	public boolean[] path_sig = null;
	public int backtrack(motif tem, graphReady g) {
		path_sig =new boolean[g.nodeNum+1];
		int SPD = 0;
		int counter = 0;
		while(tem!=null){
			for(int i=0;i<tem.nodes.size();i++){
				path_sig[tem.nodes.get(i)]=true;
				//System.out.print(g.match[tem.nodes.get(i)]+"-");
			}
			//if(counter>0)
				//System.out.print("<-"+tem.nodes.toString());
				motif_sequense.add(tem.nodes);
			counter++;
			//System.out.println();
			tem=tem.last;
			SPD++;
		}
		return SPD;
	}
	public int backtrack(motif tem) {
		//boolean sig[]=new boolean[g.nodeNum+1];
		int SPD = 0;
		while(tem!=null){
			//for(int i=0;i<tem.nodes.size();i++){
				//sig[tem.nodes.get(i)]=true;
				//System.out.print(g.match[tem.nodes.get(i)]+"-");
			//}
			//System.out.println();
			tem=tem.last;
			SPD++;
		}
		return SPD;
	}

}
