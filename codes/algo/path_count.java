package algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import tool.graphReady;

public class path_count {
	
	graphReady g = null;
	public path_count(graphReady g){
		this.g = g;
	}

	
	ArrayList<Integer>touched = null, touchedTimes = null;
	ArrayList<Integer>newTouched = null, newTouchedTimes = null;
	public int[] count(int s, int t, int lmax, int[]pset, String link_type, boolean ifEnhanced, enhanced_motif_path emp) {
		//input pset: format as the paper
		
		networkGen a = new networkGen(g);
		int[]res=new int[lmax+1];
		int c=1, d=1;
		String[]cd=link_type.split(",");
		if(cd.length>1) {
			c = Integer.parseInt(cd[1]);
			d = Integer.parseInt(cd[0]);
		}
		
		touched = new ArrayList();
		touchedTimes = new ArrayList();
		newTouched = new ArrayList();
		newTouchedTimes = new ArrayList();
		
		touched.add(s);
		touchedTimes.add(1);
		
		for(int l=0;l<lmax;l++) {
			for(int i=0;i<touched.size();i++) {
				ArrayList<ArrayList<Integer>> tem = a.motifInstances(pset, touched.get(i), new boolean[g.nodeNum+1], Integer.MAX_VALUE);
				//use tem to update touched and touchedTimes
				if(ifEnhanced)
					tem = emp.enhance(tem,touched.get(i));
				
				//consider c-delta
				for(int j=0;j<tem.size();j++) {
					ArrayList<Integer>cmi = tem.get(j);
					//int node_cmi = cmi.size();
					//int edge_cmi = 
					//countEdges(cmi,g.graph);
					
					if(c==1)
						updateNewTouchedNodeBased(cmi, i);
					else
						updateNewTouchedEdgeBased(cmi, i, g.graph);
				}
				//updating finished: get newTouched from cn
				
			}
			touched = newTouched;
			touchedTimes = newTouchedTimes;
			newTouched = new ArrayList();
			newTouchedTimes = new ArrayList();
			if(touched.contains(t)) {
				int id = touched.indexOf(t);
				res[l+1]= touchedTimes.get(id);
				if(res[l+1]<0) {
					for(int j=l+1;j<res.length;j++) {
						res[j] = Integer.MAX_VALUE;
					}
					return res;
				}
				//System.out.println("Node "+t+" is touched "+touchedTimes.get(id)+" times with "+(l+1) +" hops.");
			}
			
		}
		return res;
	}
	
	private void updateNewTouchedEdgeBased(ArrayList<Integer> cmi, int i, ArrayList<Integer>[] graph) {
		for(int p=0;p<cmi.size();p++) {
			int cmin = cmi.get(p);
			if(cmin==touched.get(i))continue;
			//only touch the smaller end-node of each edge
			
			for(int j=0;j<cmi.size();j++) {
				if(p==j||cmin>=cmi.get(j))continue;
				if(graph[cmin].contains(cmi.get(j))) {
					//there is an edge between cn and cmi.get(j) and cn is the smaller end-node
					if(newTouched.contains(cmin)) {
						int id = newTouched.indexOf(cmin);
						newTouchedTimes.set(id, newTouchedTimes.get(id)+touchedTimes.get(i));
					}else {
						newTouched.add(cmin);
						newTouchedTimes.add(touchedTimes.get(i));
					}
				}
					
			}
			
			
			
		}
	}
	
	private void updateNewTouchedNodeBased(ArrayList<Integer> cmi, int i) {
		for(int p=0;p<cmi.size();p++) {
			int cmin = cmi.get(p);
			if(cmin==touched.get(i))continue;
			if(newTouched.contains(cmin)) {
				int id = newTouched.indexOf(cmin);
				newTouchedTimes.set(id, newTouchedTimes.get(id)+touchedTimes.get(i));
			}else {
				newTouched.add(cmin);
				newTouchedTimes.add(touchedTimes.get(i));
			}
		}
	}


	private int countEdges(ArrayList<Integer> cmi, ArrayList<Integer>[] graph) {
		int en = 0;
		for(int i=0;i<cmi.size();i++) {
			int cn = cmi.get(i);
			for(int j=0;j<cmi.size();j++) {
				if(i==j)continue;
				if(graph[cn].contains(cmi.get(j)))
					en++;
			}
		}
		en/=2;
		
		return en;
	}

	public int[] count2(int s, int t, int lmax, int[]pset, String link_type) {
		//return #paths of length i
		//input pset: format as the paper
		
		ArrayList <ArrayList<Integer>> lastLayer = new ArrayList();
		ArrayList<Integer>lastLayerNum = new ArrayList();
		ArrayList <ArrayList<Integer>> cLayer = new ArrayList();
		ArrayList<Integer>cLayerNum = new ArrayList();
		
		boolean[]motif_sig= new boolean[g.nodeNum+1];
		networkGen a = new networkGen(g);
		
		ArrayList<Integer>sms = new ArrayList();
		sms.add(s);
		lastLayer.add(sms);
		lastLayerNum.add(1);
		
		int[]pc = new int[lmax+1];
		
		for(int i=1;i<=lmax;i++){
			pc[i] = onceLayerJumpMotif(lastLayer, lastLayerNum, cLayer, cLayerNum, t, pset, link_type, a);
			lastLayer = cLayer;
			lastLayerNum = cLayerNum;
			cLayer = new ArrayList();
			cLayerNum = new ArrayList();
		}
		
		return pc;
	}
	
	private int onceLayerJumpMotif(ArrayList <ArrayList<Integer>> lastLayer, ArrayList<Integer> lastLayerNum, ArrayList <ArrayList<Integer>> cLayer,
			ArrayList<Integer> cLayerNum, int t, int[]pset, String link, networkGen a) {
		//input pset: format as the paper
		
		Hashtable<String, Integer> ht = new Hashtable();
		ArrayList<Integer>tids = new ArrayList();
		
		while(lastLayer.size()>0){
			
			ArrayList<Integer> cn = lastLayer.get(lastLayer.size()-1);
			int cnn = lastLayerNum.get(lastLayerNum.size()-1);
			lastLayer.remove(lastLayer.size()-1);
			lastLayerNum.remove(lastLayerNum.size()-1);
			
			ArrayList<ArrayList<Integer>>ms;
			for(int i=0;i<cn.size();i++){
				int acn = cn.get(i);
				boolean[]motif_sig = new boolean[g.nodeNum+1];
				
				ms=a.motifInstances(pset, acn, motif_sig, Integer.MAX_VALUE, "", null);
				if(ms==null)return 0;
				for(int j=0;j<ms.size();j++){
					ArrayList<Integer> nei = ms.get(j);
					Collections.sort(nei);
					String neistr = nei.toString();
					//if(neistr.equals("[1, 2]"))
					//	System.out.println();
					if(ht.containsKey(neistr)){
						int clid = ht.get(neistr);
						int cln = cLayerNum.get(clid)+cnn;
						cLayerNum.set(clid, cln);
					}else{
						ht.put(neistr, cLayer.size());
						if(nei.contains(t)){
							tids.add(cLayer.size());
						}
						cLayer.add(nei);
						cLayerNum.add(1);
					}
				}
			}
			
		}
		
		int res = 0;
		for(int i=0;i<tids.size();i++){
			 res += cLayerNum.get(tids.get(i));
		}
		
		return res;
		
	}
	
	private int onceLayerJump(ArrayList<Integer> lastLayer, ArrayList<Integer> lastLayerNum, ArrayList<Integer> cLayer,
			ArrayList<Integer> cLayerNum, int t, int[]pset, String link) {
		
		while(lastLayer.size()>0){
			int cn = lastLayer.get(lastLayer.size()-1);
			int cnn = lastLayerNum.get(lastLayerNum.size()-1);
			lastLayer.remove(lastLayer.size()-1);
			lastLayerNum.remove(lastLayerNum.size()-1);
			
			for(int i=0;i<g.graph[cn].size();i++){
				int nei = g.graph[cn].get(i);
				if(cLayer.contains(nei)){
					int clid = cLayer.indexOf(nei);
					int cln = cLayerNum.get(clid)+cnn;
					cLayerNum.set(clid, cln);
				}else{
					cLayer.add(nei);
					cLayerNum.add(1);
				}
			}
		}
		
		if(cLayer.contains(t)){
			int tid = cLayer.indexOf(t);
			return cLayerNum.get(tid);
		}else
			return 0;
		
	}

}
