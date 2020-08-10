package algo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import tool.Datasets;

public class InsCached {

	public ArrayList<ArrayList<Integer>>[][]mod = null;
	
	public void loadCached(Datasets dts, int cacheLevel) throws IOException {
		mod = getIns(dts, cacheLevel);
	}
	
	public void loadCached(Datasets dts, int cacheLevel, int[]pset) throws IOException {
		ArrayList<Integer>pset2 = new ArrayList();
		for(int i=0;i<pset.length;i++)
			pset2.add(pset[i]);
		mod = getIns(dts, cacheLevel, pset2);
	}
	
	public ArrayList<ArrayList<Integer>> getInsCached(Datasets dts, int cacheLevel, int pid, int nid) throws IOException{
		ArrayList<ArrayList<Integer>>res = null;
		//mod = getIns(dts, cacheLevel);
		if(cacheLevel == 3) {// directly access
			res = mod[pid][nid];
			}
		if(cacheLevel == 2) {// directly access
			if(pid==21||pid==14) {
				//need to expand
				if(pid==21) {
					res = mod[21][nid];
					ArrayList<ArrayList<Integer>>ins = null;
					ins = level_2t3_v16t21(nid, mod[16][nid], dts.g.graph);
					res.addAll(ins);
					ins = level_2t3_v9t21(nid, mod[9][nid], dts.g.graph);
					res.addAll(ins);
				}else {
					res = mod[14][nid];
					ArrayList<ArrayList<Integer>>ins = null;
					ins = level_2t3_v16t21(nid, mod[9][nid], dts.g.graph);//level_2t3_v9t14
					res.addAll(ins);
					ins = level_2t3_v2t14(nid, mod[2][nid], dts.g.graph);
					res.addAll(ins);
				}
			}else {
				res = mod[pid][nid];
			}
			}
		if(cacheLevel == 1) {
			if(pid==1||pid==5) {//direct acess
				res = mod[pid][nid];
			}else {
				if(pid==21||pid==14) {//expand two-times
					if(pid==14) {
						res = mod[14][nid];
						ArrayList<ArrayList<Integer>>ins = null;
						ins = level_1t2_v9t14(nid, mod[9][nid], dts.g.graph);
						res.addAll(ins);
						ins = level_1t2_v2t14(nid, mod[2][nid], dts.g.graph);
						res.addAll(ins);
						
						mod[9][nid].addAll(level_1t2_v5t9(nid, mod[5][nid], dts.g.graph));
						ins = level_2t3_v16t21(nid, mod[9][nid], dts.g.graph);//level_2t3_v9t14
						res.addAll(ins);
						mod[2][nid].addAll(level_1t2_v1t2(nid, mod[1][nid], dts.g.graph));
						ins = level_2t3_v2t14(nid, mod[2][nid], dts.g.graph);
						res.addAll(ins);
						
					}else {
						res = mod[21][nid];
						ArrayList<ArrayList<Integer>>ins = null;
						ins = level_1t2_v9t21(nid, mod[9][nid], dts.g.graph);
						res.addAll(ins);
						ins = level_1t2_v16t21(nid, mod[16][nid], dts.g.graph);
						res.addAll(ins);
						
						mod[16][nid].addAll(level_1t2_v5t16(nid, mod[5][nid], dts.g.graph));
						ins = level_2t3_v16t21(nid, mod[16][nid], dts.g.graph);
						res.addAll(ins);
						mod[9][nid].addAll(level_1t2_v1t9(nid, mod[1][nid], dts.g.graph));
						ins = level_2t3_v9t21(nid, mod[9][nid], dts.g.graph);
						res.addAll(ins);
					}
				}else {//expand one time
					if(pid==2) {
						res = mod[2][nid];
						ArrayList<ArrayList<Integer>>ins = null;
						ins = level_1t2_v1t2(nid, mod[1][nid], dts.g.graph);
						res.addAll(ins);
						
					}
					if(pid==9) {
						res = mod[9][nid];
						ArrayList<ArrayList<Integer>>ins = null;
						ins = level_1t2_v1t9(nid, mod[1][nid], dts.g.graph);
						res.addAll(ins);
						ins = level_1t2_v5t9(nid, mod[5][nid], dts.g.graph);
						res.addAll(ins);
					}
					if(pid==16) {
						res = mod[16][nid];
						ArrayList<ArrayList<Integer>>ins = null;
						ins = level_1t2_v5t16(nid, mod[5][nid], dts.g.graph);
						res.addAll(ins);
					}
					
				}
			}
		}
		
		return res;
		
	}
	
	private ArrayList<ArrayList<Integer>> level_1t2_v1t9(int seed, ArrayList<ArrayList<Integer>> ins,
			ArrayList<Integer>[] graph) {
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		for(int i=0;i<ins.size();i++) {
			ArrayList<Integer>ains = ins.get(i);
			int nei1 = ains.get(0);
			
			for(int j=0;j<graph[nei1].size();j++) {
				int nei2 = graph[nei1].get(j);
				if(nei2==seed||graph[nei2].contains(seed))
					continue;
				
				for(int k=j+1;k<graph[nei1].size();k++) {
					int nei3 = graph[nei1].get(k);
					if(nei3==seed||graph[nei3].contains(seed)||!graph[nei3].contains(nei2))
						continue;
					ArrayList<Integer>ares = new ArrayList();
					ares.add(nei3);ares.add(nei2);ares.add(nei1);ares.add(seed);
					if(ares.contains(-1))
						continue;
					res.add(ares);
				}
				
				
			}
		}
		return res;
	}
	
	private ArrayList<ArrayList<Integer>> level_1t2_v1t2(int seed, ArrayList<ArrayList<Integer>> ins,
			ArrayList<Integer>[] graph) {
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		for(int i=0;i<ins.size();i++) {
			ArrayList<Integer>ains = ins.get(i);
			int nei1 = ains.get(0);
			
			for(int j=0;j<graph[nei1].size();j++) {
				int nei2 = graph[nei1].get(j);
				if(nei2==seed||graph[nei2].contains(seed))
					continue;
				ArrayList<Integer>ares = new ArrayList();
				ares.add(nei2);ares.add(nei1);ares.add(seed);
				if(ares.contains(-1))
					continue;
				res.add(ares);
				
			}
		}
		return res;
	}
	
	private ArrayList<ArrayList<Integer>> level_1t2_v2t14(int seed, ArrayList<ArrayList<Integer>> ins,
			ArrayList<Integer>[] graph) {
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		for(int i=0;i<ins.size();i++) {
			ArrayList<Integer>ains = ins.get(i);
			int nei1 = ains.get(0), nei2 = ains.get(1);
			
			for(int j=0;j<graph[nei1].size();j++) {
				int nei3 = graph[nei1].get(j);
				if(nei3==seed||nei3==nei2||graph[nei3].contains(seed)||graph[nei3].contains(nei2))
					continue;
				for(int k=j+1;k<graph[nei1].size();k++) {
					int nei4 = graph[nei1].get(j);
					if(nei4==seed||nei4==nei2||graph[nei4].contains(seed)||graph[nei4].contains(nei2)||!graph[nei4].contains(nei3))
						continue;
					ArrayList<Integer>ares = new ArrayList();
					ares.add(nei4);ares.add(nei3);ares.add(nei2);ares.add(nei1);ares.add(seed);
					if(ares.contains(-1))
						continue;
					res.add(ares);
				}
			}
			
			for(int j=0;j<graph[nei2].size();j++) {
				int nei3 = graph[nei2].get(j);
				if(nei3==seed||nei3==nei1||graph[nei3].contains(seed)||graph[nei3].contains(nei1))
					continue;
				for(int k=j+1;k<graph[nei2].size();k++) {
					int nei4 = graph[nei2].get(j);
					if(nei4==seed||nei4==nei1||graph[nei4].contains(seed)||graph[nei4].contains(nei1)||!graph[nei4].contains(nei3))
						continue;
					ArrayList<Integer>ares = new ArrayList();
					ares.add(nei4);ares.add(nei3);ares.add(nei2);ares.add(nei1);ares.add(seed);
					if(ares.contains(-1))
						continue;
					res.add(ares);
				}
			}
		}
		return res;
	}
	
	private ArrayList<ArrayList<Integer>> level_1t2_v16t21(int seed, ArrayList<ArrayList<Integer>> ins,
			ArrayList<Integer>[] graph) {
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		for(int i=0;i<ins.size();i++) {
			ArrayList<Integer>ains = ins.get(i);
			ArrayList<Integer>deg2nodes = getDegreeNodes(ains,graph,2);
			int nei1 = deg2nodes.get(0), nei2=deg2nodes.get(1), nei3 = -1;
			for(int j=0;j<ains.size();j++) {
				if(ains.get(j)!=nei1&&ains.get(j)!=seed&&ains.get(j)!=nei2) {
					nei3 = ains.get(j);
				}
			}
			for(int j=0;j<graph[nei1].size();j++) {
				int nei4 = graph[nei1].get(j);
				if(nei4==seed||nei4==nei2||nei4==nei3||graph[nei4].contains(seed)||graph[nei4].contains(nei2)||graph[nei4].contains(nei3))
					continue;
				ArrayList<Integer>ares = new ArrayList();
				ares.add(nei4);ares.add(nei3);ares.add(nei2);ares.add(nei1);ares.add(seed);
				if(ares.contains(-1))
					continue;
				res.add(ares);
			}
			
			for(int j=0;j<graph[nei2].size();j++) {
				int nei4 = graph[nei2].get(j);
				if(nei4==seed||nei4==nei1||nei4==nei3||graph[nei4].contains(seed)||graph[nei4].contains(nei1)||graph[nei4].contains(nei3))
					continue;
				ArrayList<Integer>ares = new ArrayList();
				ares.add(nei4);ares.add(nei3);ares.add(nei1);ares.add(nei2);ares.add(seed);
				if(ares.contains(-1))
					continue;
				res.add(ares);
			}
		}
		return res;
	}
	
	private ArrayList<ArrayList<Integer>> level_1t2_v5t9(int seed, ArrayList<ArrayList<Integer>> ins,
			ArrayList<Integer>[] graph) {
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		for(int i=0;i<ins.size();i++) {
			ArrayList<Integer>ains = ins.get(i);
			int nei1 = ains.get(0), nei2 = ains.get(1);
			
			for(int j=0;j<graph[nei1].size();j++) {
				int nei3 = graph[nei1].get(j);
				if(nei3==seed||nei3==nei2||graph[nei3].contains(seed)||graph[nei3].contains(nei2))
					continue;
				ArrayList<Integer>ares = new ArrayList();
				ares.add(nei3);ares.add(nei2);ares.add(nei1);ares.add(seed);
				if(ares.contains(-1))
					continue;
				res.add(ares);
			}
			
			for(int j=0;j<graph[nei2].size();j++) {
				int nei3 = graph[nei2].get(j);
				if(nei3==seed||nei3==nei1||graph[nei3].contains(seed)||graph[nei3].contains(nei1))
					continue;
				ArrayList<Integer>ares = new ArrayList();
				ares.add(nei3);ares.add(nei1);ares.add(nei2);ares.add(seed);
				if(ares.contains(-1))
					continue;
				res.add(ares);
			}
		}
		return res;
	}
	
	private ArrayList<ArrayList<Integer>> level_1t2_v5t16(int seed, ArrayList<ArrayList<Integer>> ins,
			ArrayList<Integer>[] graph) {
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		for(int i=0;i<ins.size();i++) {
			ArrayList<Integer>ains = ins.get(i);
			int nei1 = ains.get(0), nei2 = ains.get(1);
			
			for(int j=0;j<graph[nei1].size();j++) {
				int nei3 = graph[nei1].get(j);
				if(nei3==seed||nei3==nei2||graph[nei3].contains(seed)||!graph[nei3].contains(nei2))
					continue;
				ArrayList<Integer>ares = new ArrayList();
				ares.add(nei3);ares.add(nei2);ares.add(nei1);ares.add(seed);
				if(ares.contains(-1))
					continue;
				res.add(ares);
				
			}
		}
		return res;
	}
	
	private ArrayList<ArrayList<Integer>> level_1t2_v9t14(int seed, ArrayList<ArrayList<Integer>> ins,
			ArrayList<Integer>[] graph) {
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		for(int i=0;i<ins.size();i++) {
			ArrayList<Integer>ains = ins.get(i);
			ArrayList<Integer>deg1nodes = getDegreeNodes(ains,graph,1);
			int nei1 = deg1nodes.get(0), nei2=-1, nei3=-1;
			for(int j=0;j<ains.size();j++) {
				if(ains.get(j)!=nei1&&ains.get(j)!=seed&&nei2==-1) {
					nei2 = ains.get(j);
				}
				if(ains.get(j)!=nei1&&ains.get(j)!=seed&&nei2!=-1&&nei3==-1) {
					nei3 = ains.get(j);
				}
			}
			
			for(int j=0;j<graph[nei1].size();j++) {
				int nei4 = graph[nei1].get(j);
				if(nei4==seed||nei4==nei2||nei4==nei3||graph[nei4].contains(seed)||graph[nei4].contains(nei2)||graph[nei4].contains(nei3))
					continue;
				ArrayList<Integer>ares = new ArrayList();
				ares.add(nei4);ares.add(nei3);ares.add(nei2);ares.add(nei1);ares.add(seed);
				if(ares.contains(-1))
					continue;
				res.add(ares);
				
			}
		}
		return res;
	}
	
	private ArrayList<ArrayList<Integer>> level_1t2_v9t21(int seed, ArrayList<ArrayList<Integer>> ins,
			ArrayList<Integer>[] graph) {
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		for(int i=0;i<ins.size();i++) {
			ArrayList<Integer>ains = ins.get(i);
			ArrayList<Integer>deg1nodes = getDegreeNodes(ains,graph,1);
			int nei1 = deg1nodes.get(0), nei2=-1, nei3=-1;
			for(int j=0;j<ains.size();j++) {
				if(ains.get(j)!=nei1&&ains.get(j)!=seed&&nei2==-1) {
					nei2 = ains.get(j);
				}
				if(ains.get(j)!=nei1&&ains.get(j)!=seed&&nei2!=-1&&nei3==-1) {
					nei3 = ains.get(j);
				}
			}
			
			for(int j=0;j<graph[nei2].size();j++) {
				int nei4 = graph[nei2].get(j);
				if(nei4==seed||nei4==nei1||nei4==nei3||graph[nei4].contains(seed)||graph[nei4].contains(nei1)||!graph[nei4].contains(nei3))
					continue;
				ArrayList<Integer>ares = new ArrayList();
				ares.add(nei4);ares.add(nei3);ares.add(nei2);ares.add(nei1);ares.add(seed);
				if(ares.contains(-1))
					continue;
				res.add(ares);		
			}
		}
		return res;
	}

	private ArrayList<Integer> getDegreeNodes(ArrayList<Integer> ains, ArrayList<Integer>[] graph, int degree) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(int i=0;i<ains.size();i++) {
			int nei = ains.get(i);
			if(getSeedDegree(nei,ains,graph)==degree)
				res.add(nei);
		}
		return res;
	}

	private ArrayList<ArrayList<Integer>> level_2t3_v2t14(int seed, ArrayList<ArrayList<Integer>> ins,
			ArrayList<Integer>[] graph) {
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		for(int i=0;i<ins.size();i++) {
			ArrayList<Integer>ains = ins.get(i);
			int seeddeg = getSeedDegree(seed,ains,graph);
			if(seeddeg!=1) continue;
			//order: nei3 - nei2 - nei1 - seed
			int nei2 = ains.get(0), nei1 = ains.get(1);
			//if(seed!=ains.get(0)) System.out.println("seed is not final position: "+ains.toString()+"_"+seed);
			for(int j=0;j<graph[nei2].size();j++) {
				int nei3 = graph[nei2].get(j);
				if(nei3==seed||nei3==nei1||graph[nei3].contains(seed)||graph[nei3].contains(nei1))
					continue;
				for(int k=j+1;k<graph[nei2].size();k++) {
					int nei4 = graph[nei2].get(k);
					if(nei4==seed||nei4==nei1||graph[nei4].contains(seed)||graph[nei4].contains(nei1)||!graph[nei4].contains(nei3))
						continue;
					ArrayList<Integer>ares = new ArrayList();
					ares.add(nei4);ares.add(nei3);ares.add(nei2);ares.add(nei1);ares.add(seed);
					if(ares.contains(-1))
						continue;
					res.add(ares);
				}
				
			}
		}
		return res;
	}

	private ArrayList<ArrayList<Integer>> level_2t3_v9t21(int seed, ArrayList<ArrayList<Integer>> ins,
			ArrayList<Integer>[] graph) {
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		for(int i=0;i<ins.size();i++) {
			ArrayList<Integer>ains = ins.get(i);
			int seeddeg = getSeedDegree(seed,ains,graph);
			if(seeddeg!=1) continue;
			//order: nei3 - nei2 - nei1 - seed
			int nei3 = ains.get(0), nei2 = ains.get(1), nei1 = ains.get(2);
			//if(seed!=ains.get(0)) System.out.println("seed is not final position: "+ains.toString()+"_"+seed);
			for(int j=0;j<graph[nei3].size();j++) {
				int nei4 = graph[nei3].get(j);
				if(nei4==seed||nei4==nei1||nei4==nei2||graph[nei4].contains(seed)||graph[nei4].contains(nei1)||!graph[nei4].contains(nei2))
					continue;
				ArrayList<Integer>ares = new ArrayList();
				ares.add(nei4);ares.add(nei3);ares.add(nei2);ares.add(nei1);ares.add(seed);
				if(ares.contains(-1))
					continue;
				res.add(ares);
			}
		}
		return res;
	}

	private ArrayList<ArrayList<Integer>> level_2t3_v16t21(int seed, ArrayList<ArrayList<Integer>> ins,
			ArrayList<Integer>[] graph) {
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		for(int i=0;i<ins.size();i++) {
			ArrayList<Integer>ains = ins.get(i);
			int seeddeg = getSeedDegree(seed,ains,graph);
			if(seeddeg!=2) continue;
			//order: nei3 - nei2 - nei1 - seed
			int nei3 = ains.get(0), nei2 = ains.get(1), nei1 = ains.get(2);
			//if(seed!=ains.get(0)) System.out.println("seed is not final position: "+ains.toString()+"_"+seed);
			for(int j=0;j<graph[nei3].size();j++) {
				int nei4 = graph[nei3].get(j);
				if(nei4==seed||nei4==nei1||nei4==nei2||graph[nei4].contains(seed)||graph[nei4].contains(nei1)||graph[nei4].contains(nei2))
					continue;
				ArrayList<Integer>ares = new ArrayList();
				ares.add(nei4);ares.add(nei3);ares.add(nei2);ares.add(nei1);ares.add(seed);
				if(ares.contains(-1))
					continue;
				res.add(ares);
			}
		}
		return res;
	}

	private int getSeedDegree(int seed, ArrayList<Integer> ains, ArrayList<Integer>[] graph) {
		int res = 0;
		for(int i=0;i<ains.size();i++) {
			int nei = ains.get(i);
			if(nei == seed)
				continue;
			if(graph[seed].contains(nei))
				res++;
		}
		return res;
	}
	
	ArrayList<ArrayList<Integer>>[][] getIns(Datasets dts, int level, ArrayList<Integer>pset) throws IOException{
		ArrayList<ArrayList<Integer>>[][] mod = new ArrayList[31][dts.g.graph.length];//pid-nodeid: a set of motif-instances
		for(int i=0;i<mod.length;i++) {
			for(int j=0;j<mod[i].length;j++) {
				mod[i][j] = new ArrayList();
			}
		}
		//ArrayList<ArrayList<Integer>>res = null;
		String str = "";
		if(level>1)
			str = dts.d.dataPath+"-"+level+".cache";
		else
			str = dts.d.dataPath+".cache";
		
		BufferedReader a = new BufferedReader(new FileReader(str));
		String s = a.readLine();
		ArrayList<Integer>last = null;
		ArrayList<int[]>tuples =null;;
		int seed = -1;
		int counter = 0;
		//ArrayList<Integer>removedOffset=null;
		while(s!=null && !s.equals("")) {
			String[]tem = s.split(":");
			String[]index = tem[0].split(",");
			int motifType = Integer.parseInt(index[0]);
			//if(!pset.contains(motifType)) {
				//removedOffset.add(counter);
				//s = a.readLine();
				//counter++;
				//continue;
				//}
			int offset = Integer.parseInt(index[1]);
			//offset = dealOffset(offset, removedOffset);
			String[]ress = tem[1].split(",");
			int[]num = new int[ress.length];
			for(int i=0;i<ress.length;i++) {
				num[i] = Integer.parseInt(ress[i]);
			}
			
			if(offset==-1) {
				counter = 0;
				//removedOffset = new ArrayList();
				seed = num[0];System.out.println(seed);
				//if(seed%(dts.g.graph.length/1000)==0) System.out.print((int)(seed*1000/dts.g.graph.length)+"->");
				tuples = new ArrayList();
				tuples.add(num);
				last = new ArrayList();
				last.add(offset);
			}else {
				tuples.add(num);
				last.add(offset);
				
				int coffset = offset;
				int[]cnum = num;
				ArrayList<Integer>ins = new ArrayList();//get a motif-instance of type motifType
				while(coffset!=-1) {
					for(int i=0;i<cnum.length;i++) {
						ins.add(cnum[i]);				
						}
					cnum = tuples.get(coffset);
					coffset = last.get(coffset);
				}
				ins.add(seed);
				//System.out.println(motifType+"\t"+ins.toString());
				mod[motifType][seed].add(ins);
			}
			
			s = a.readLine();
			counter++;
		}
		return mod;
	}

	private int dealOffset(int offset, ArrayList<Integer> removedOffset) {
		if(removedOffset==null)
			return offset;
		for(int i=0;i<removedOffset.size();i++) {
			int aro = removedOffset.get(i);
			if(offset>aro) {
				offset--;
			}
		}
		return offset;
	}

	ArrayList<ArrayList<Integer>>[][] getIns(Datasets dts, int level) throws IOException{
		int[]ps= {1,2,5,9,10,12,14,16,21,22,30};
		ArrayList<Integer> pset = new ArrayList();
		for(int i=0;i<ps.length;i++)
			pset.add(ps[i]);
		
		return getIns(dts, level, pset);
	}

}
