package algo;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import tool.graphReady;
import tool.motif;

public class HBSMP extends motif_path{
	
	public int SPD = Integer.MAX_VALUE;
	public int uSPD = Integer.MAX_VALUE;
	boolean[]motif_sig;
	networkGen a = null;
	int[]query = null;
	
	public HBSMP(graphReady g, int[]query, String link_type, int s, int t){
		String[]link=link_type.split(",");
		boolean linksig = false;
		int delta = -1, noe = -1; // noe=1/2: two motifs should share delta nodes/edges
		if(link_type.length()!=0&&link.length==2){
			delta = Integer.parseInt(link[0]);
			noe = Integer.parseInt(link[1]);
			linksig = true; // start using seeds.
		}
		
		this.query = query;
		motif_sig = new boolean[g.nodeNum+1];
		
		motif[]touchedS = new motif [g.nodeNum+1];
		int[]jumpS = new int[g.nodeNum+1];
		motif[]touchedT = new motif [g.nodeNum+1];
		int[]jumpT = new int[g.nodeNum+1];
		
		a = new networkGen(g);
		if(a.FindTSig||a.overflowSig){
			SPD = 1;
			return;
			}
		
		//search motif-instances around node s.
		ArrayList<ArrayList<Integer>>solsS=a.motifInstances(query, s, motif_sig, t);
		//search motif-instances around node t.
		ArrayList<ArrayList<Integer>>solsT=a.motifInstances(query, t, motif_sig, s);
		
		Queue<motif>qs=new LinkedList<motif>();
		Queue<motif>qt=new LinkedList<motif>();
		
		int topS = 1, topT = 1;
		boolean firstSigS = firstJump(solsS, touchedS, jumpS, touchedT, jumpT, qs, topS, topT, s, t);
		if(firstSigS)return;
		boolean firstSigT = firstJump(solsT, touchedT, jumpT, touchedS, jumpS, qt, topT, topS, t, s);
		if(firstSigT)return;
		
		//int[]qset={300,298,304,42}; ArrayList<Integer>qseta=new ArrayList();
		//for(int ti=0;ti<qset.length;ti++)qseta.add(qset[ti]);
		
		//while(!q.isEmpty()&&!motif_sig[t]){
		while(!qs.isEmpty()&&!qt.isEmpty()){
			motif crS = qs.poll(), crT = qt.poll();

			/*
			for(int qid=0;qid<qset.length;qid++){
				if(crS.nodes.contains(qset[qid])){
					int sid = crS.nodes.indexOf(qset[qid]);
					for(int q2id=0;q2id<crS.nodes.size()&&q2id!=sid;q2id++){
						if(qseta.contains(crS.nodes.get(q2id))){
							System.out.println(qset[qid]+" "+crS.nodes.get(q2id));
						}
					}
				}
				if(crT.nodes.contains(qset[qid])){
					int sid = crT.nodes.indexOf(qset[qid]);
					for(int q2id=0;q2id<crT.nodes.size()&&q2id!=sid;q2id++){
						if(qseta.contains(crT.nodes.get(q2id))){
							System.out.println(qset[qid]+" "+crT.nodes.get(q2id));
						}
					}
				}
			}*/
			
			boolean nextSigS = false;
			if(linksig)
				nextJump(solsS, touchedS, jumpS, touchedT, jumpT, qs, topS, topT, t, crS, delta, noe);
			else
				nextJump(solsS, touchedS, jumpS, touchedT, jumpT, qs, topS, topT, t, crS);
			if(nextSigS) return;
			
			boolean nextSigT = false;
			if(linksig)
				nextJump(solsT, touchedT, jumpT, touchedS, jumpS, qt, topT, topS, s, crT, delta, noe);
			else
				nextJump(solsT, touchedT, jumpT, touchedS, jumpS, qt, topT, topS, s, crT);
			if(nextSigT) return;
		}
		
	}

	private boolean firstJump(ArrayList<ArrayList<Integer>> sols, motif[] touched, int[]jump, motif[] touched2, int[]jump2, Queue<motif> q, int top, int top2, int s, int t) {
		//return true: find t from the first jump of s.
		ArrayList<Integer>cans;
		for(int i=0;i<sols.size();i++){
			cans = sols.get(i);
			if(cans.contains(t)){
				//System.out.println("Motif-path found with length = 0: " + cans.toString());
				SPD = 1;
				return true;
			}
			//System.out.println(i+" "+a.solMotifTypes.size());
			if(a.solMotifTypes.size()<=i)return false;
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
				//if cans contains any nodes touched by another side, than find a uSPD
				if(touched2[ned]!=null){
					int tem = jump[ned]+jump2[ned];
					if(tem<uSPD){
						uSPD = tem;
						//System.out.println("Find uSPD="+uSPD+" via node "+ned);
						if(top+top2>=uSPD){
							SPD = uSPD;
							//System.out.println("Find SPD="+SPD);
							return true;
						}
					}
				}
			}
			if(!touchSig)
				q.add(nm);	
		}
		motif_sig[s]=true;
		return false;
	}
	private boolean nextJump(ArrayList<ArrayList<Integer>> sols, motif[] touched, int[]jump, motif[] touched2, int[]jump2, Queue<motif> q, int top, int top2, int t, motif cr) {
		for(int ii=0;ii<cr.nodes.size();ii++){
			int nei0 = cr.nodes.get(ii);
			if(motif_sig[nei0])continue;
			
			top = jump[nei0]+1;
			sols = a.motifInstances(query, nei0, motif_sig, t);
			for(int i=0;i<sols.size();i++){
				ArrayList<Integer> cans = sols.get(i);
				motif m1 = new motif(cans,a.solMotifTypes.get(i));
				m1.last = cr;
				if(cans.contains(t)){
					//SPD = backtrack(m1);
					SPD = top;
					//System.out.println("Motif-path found with length = "+SPD);
					return true;
				}
				//for each motif-instance, check if it is touched.
				boolean touchSig = true;
				for (int p = 0; p<cans.size(); p++){
					int ned = cans.get(p);
					if(touched[ned]==null){
						touched[ned] = m1;
						jump[ned]=top;
						touchSig = false;
					}
					//if cans contains any nodes touched by another side, than find a uSPD
					if(touched2[ned]!=null){
						int tem = jump[ned]+jump2[ned];
						if(tem<uSPD){
							uSPD = tem;
							//System.out.println("Find uSPD="+uSPD+" via node "+ned);
							//if(top+top2>=uSPD){
								SPD = uSPD;
								//System.out.println("Find SPD="+SPD);
								return true;
							//}
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
				return true;
				}
			motif_sig[nei0]=true;
		}
		return false;
	}
	
	private boolean nextJump(ArrayList<ArrayList<Integer>> sols, motif[] touched, int[]jump, motif[] touched2, int[]jump2, Queue<motif> q, int top, int top2, int t, motif cr, int delta, int noe) {
		for(int ii=0;ii<cr.nodes.size();ii++){
			int nei0 = cr.nodes.get(ii);
			if(motif_sig[nei0])continue;
			
			top = jump[nei0]+1;
			sols = a.motifInstances(query, nei0, cr.nodes,delta, noe, motif_sig, t);
			for(int i=0;i<sols.size();i++){
				ArrayList<Integer> cans = sols.get(i);
				motif m1 = new motif(cans,a.solMotifTypes.get(i));
				m1.last = cr;
				if(cans.contains(t)){
					//SPD = backtrack(m1);
					SPD = top;
					//System.out.println("Motif-path found with length = "+SPD);
					return true;
				}
				//for each motif-instance, check if it is touched.
				boolean touchSig = true;
				for (int p = 0; p<cans.size(); p++){
					int ned = cans.get(p);
					if(touched[ned]==null){
						touched[ned] = m1;
						jump[ned]=top;
						touchSig = false;
					}
					//if cans contains any nodes touched by another side, than find a uSPD
					if(touched2[ned]!=null){
						int tem = jump[ned]+jump2[ned];
						if(tem<uSPD){
							uSPD = tem;
							//System.out.println("Find uSPD="+uSPD+" via node "+ned);
							//if(top+top2>=uSPD){
								SPD = uSPD;
								//System.out.println("Find SPD="+SPD);
								return true;
							//}
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
				return true;
				}
			motif_sig[nei0]=true;
		}
		return false;
	}
		
}
