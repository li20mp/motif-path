package algo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import tool.Combination;
import tool.graphReady;

public class networkGen {
	motifNet[]mn = null;
	graphReady g;
	public networkGen(graphReady g) {
		// TODO Auto-generated method stub
		this.g=g;
		mn = init();
		//touched = new boolean[g.nodeNum];
		/*
		for(int i=1;i<mn.length;i++){
			if(i!=10&&i!=17)continue;
			System.out.println("Motif "+i);
			String ss = mn[i].printIsotms();
			System.out.println(ss+"\n");
			
		}*/
	}
	ArrayList<ArrayList<Integer>>sol;
	/*
	public ArrayList<ArrayList<Integer>> motifInstances_old(int[]query, graphReady g, int s){	
		sol=new ArrayList();
		ArrayList<isotopeMotifNet>motifsJump1 = buildMotifNet(mn, query);
		//now motif-network is built as motifsJump1.
		
		for(int i=0;i<motifsJump1.size();i++){
			isotopeMotifNet m = motifsJump1.get(i);
			ArrayList<Integer>nei = g.graph[s]; 
			//find motif-instances that are isomorphic to m;
			int k = m.getMotifSize()-1;
			//find k nodes from nei
			if(nei.size()<k)continue;
			String[]sig = new Combination().getStr(nei.size(), k);
			ArrayList<Integer>sset = new ArrayList();sset.add(s);
			ArrayList<Integer>degset = new ArrayList();degset.add(0);
			ArrayList<ArrayList<Integer>>candidates=canGen_old(sig,nei,g, k, sset, degset, m.motif.signature, s, 1);
			//get candidates that isomorphic to m.motif.signature (jump 1)
			//String[] sig, ArrayList<Integer> nei, graphReady g, int k, ArrayList<Integer>s, ArrayList<Integer>deg, String signature
			
			if(m.next==null||m.next.size()==0){
				sol.addAll(candidates);
				continue;
			}
			if(m.outputSig){
				sol.addAll(candidates);
			}
			//candidates, g[s]
				
			for(int j=0;j<candidates.size();j++){	
				
				ArrayList<Integer> cnv = candidates.get(j);
				for(int p=0;p<m.next.size();p++){
					
					isotopeMotifNet cnext = m.next.get(p);
					ArrayList<ArrayList<Integer>>candidates2=candidateGen_old(cnext, cnv, g, s, m);
					if(candidates2==null||candidates2.size()==0)continue;
					//get candidates2 that is isomorphic to cnext.motif.signature
					
					if(cnext.next==null||cnext.next.size()==0){
						sol.addAll(candidates2);
						continue;
					}
					if(cnext.outputSig)sol.addAll(candidates2);
					
					for(int j2=0;j2<candidates2.size();j2++){
						ArrayList<Integer> cnv2 = candidates2.get(j2);
						for(int p2=0;p2<cnext.next.size();p2++){
							isotopeMotifNet cnext2 = cnext.next.get(p2);
							ArrayList<ArrayList<Integer>>candidates3=candidateGen_old(cnext2, cnv2, g, s, cnext);
							if(candidates3==null||candidates3.size()==0)continue;
							//get candidates2 that is isomorphic to cnext2.motif.signature
							
							if(cnext2.next==null||cnext2.next.size()==0){
								sol.addAll(candidates3);
								continue;
							}
							if(cnext2.outputSig)sol.addAll(candidates3);
							
							for(int j3=0;j3<candidates3.size();j3++){
								ArrayList<Integer> cnv3 = candidates3.get(j3);
								for(int p3=0;p3<cnext2.next.size();p3++){
									isotopeMotifNet cnext3 = cnext2.next.get(p3);
									ArrayList<ArrayList<Integer>>candidates4=candidateGen_old(cnext3, cnv3, g, s, cnext2);
									if(candidates4==null||candidates4.size()==0)continue;
									//get candidates2 that is isomorphic to cnext2.motif.signature
									if(cnext3.next==null||cnext3.next.size()==0){
										sol.addAll(candidates4);
										continue;
									}
									if(cnext3.outputSig)sol.addAll(candidates4);
								}
							}
							}
						}
					
					}
					
					
				}
				
				
			
		}
		
		return sol;
			
		
		
	}*/
	public boolean overflowSig;
	//public boolean[]touched;
	
	public ArrayList<ArrayList<Integer>> motifInstances(int[]query, int s, ArrayList<Integer>cms, int delta, int noe, boolean[]motif_sig, int t){
		//from seed s[] to t with query
		
		overflowSig = false;
		sol=new ArrayList();
		solMotifTypes=new ArrayList();
		
		ArrayList<isotopeMotifNet>motifsJump1 = buildMotifNet(mn, query);
		//now motif-network is built as motifsJump1.
		if(motif_sig[s])return sol;
		for(int i=0;i<motifsJump1.size();i++){
			isotopeMotifNet m = motifsJump1.get(i);
			ArrayList<Integer>nei = g.graph[s]; 
			//find motif-instances that are isomorphic to m;
			int k = m.getMotifSize()-1;
			//find k nodes from nei
			if(nei.size()<k)continue;
			Combination cb = new Combination();
			String[]sig = cb.getStr(nei.size(), k);
			if(sig==null){
				overflowSig=true; return sol;
				}
			ArrayList<Integer>sset = new ArrayList();sset.add(s);
			ArrayList<Integer>degset = new ArrayList();degset.add(0);
			ArrayList<ArrayList<Integer>>candidates=canGen(sig,nei, k, sset, degset, m, s, 1, motif_sig, t);
			//get candidates that isomorphic to m.motif.signature (jump 1)
			//String[] sig, ArrayList<Integer> nei, graphReady g, int k, ArrayList<Integer>s, ArrayList<Integer>deg, String signature
			
			if(m.next==null||m.next.size()==0||m.outputSig){
				addcan(candidates, cms, delta, noe, m);
				//sol.addAll(candidates);
				if(m.next==null||m.next.size()==0)
					continue;
			}
			//if(m.outputSig){
			//	sol.addAll(candidates);
			//}
			if(FindTSig){
				return sol;
			}
			//candidates, g[s]
				
			for(int j=0;j<candidates.size();j++){	
				
				ArrayList<Integer> cnv = candidates.get(j);
				for(int p=0;p<m.next.size();p++){
					
					isotopeMotifNet cnext = m.next.get(p);
					ArrayList<ArrayList<Integer>>candidates2=candidateGen(cnext, cnv, g, s, m, motif_sig, t);
					if(candidates2==null||candidates2.size()==0)continue;
					//get candidates2 that is isomorphic to cnext.motif.signature
					
					if(cnext.next==null||cnext.next.size()==0){
						addcan(candidates2, cms, delta, noe, cnext);
						//sol.addAll(candidates2);
						continue;
					}
					if(cnext.outputSig)
						addcan(candidates2, cms, delta, noe,cnext);
						//sol.addAll(candidates2);
					if(FindTSig||overflowSig){
						return sol;
					}
					
					for(int j2=0;j2<candidates2.size();j2++){
						ArrayList<Integer> cnv2 = candidates2.get(j2);
						for(int p2=0;p2<cnext.next.size();p2++){
							isotopeMotifNet cnext2 = cnext.next.get(p2);
							ArrayList<ArrayList<Integer>>candidates3=candidateGen(cnext2, cnv2, g, s, cnext, motif_sig, t);
							if(candidates3==null||candidates3.size()==0)continue;
							//get candidates2 that is isomorphic to cnext2.motif.signature
							
							if(cnext2.next==null||cnext2.next.size()==0){
								addcan(candidates3, cms, delta, noe, cnext2);
								//sol.addAll(candidates3);
								continue;
							}
							if(cnext2.outputSig)
								addcan(candidates3, cms, delta, noe,cnext2);
								//sol.addAll(candidates3);
							if(FindTSig||overflowSig){
								return sol;
							}
							
							for(int j3=0;j3<candidates3.size();j3++){
								ArrayList<Integer> cnv3 = candidates3.get(j3);
								for(int p3=0;p3<cnext2.next.size();p3++){
									isotopeMotifNet cnext3 = cnext2.next.get(p3);
									ArrayList<ArrayList<Integer>>candidates4=candidateGen(cnext3, cnv3, g, s, cnext2, motif_sig, t);
									if(candidates4==null||candidates4.size()==0)continue;
									//get candidates2 that is isomorphic to cnext2.motif.signature
									if(cnext3.next==null||cnext3.next.size()==0){
										addcan(candidates4, cms, delta, noe, cnext3);
										//sol.addAll(candidates4);
										continue;
									}
									if(cnext3.outputSig)
										addcan(candidates4, cms, delta, noe, cnext3);
										//sol.addAll(candidates4);
									if(FindTSig||overflowSig){
										return sol;
									}
								}
							}
							}
						}
					
					}
					
					
				}
				
				
			
		}
		
		return sol;
			
		
		
	}

	private void addcan(ArrayList<ArrayList<Integer>> candidates, ArrayList<Integer> cms, int seedSize, int noe, isotopeMotifNet cnext) {
		// TODO Auto-generated method stub
		for(int pi = 0;pi<candidates.size();pi++){
			ArrayList<Integer>ca = candidates.get(pi);
			//calculate the intersection of ca and cms and check if seed is contained
			int snn = 0, sen = 0;
			ArrayList<Integer>sg = new ArrayList<Integer>();
			for(int pj=0;pj<ca.size();pj++){
				int can = ca.get(pj);
				if(cms.contains(can)){
					snn++;
					for(int pt=0;pt<sg.size();pt++){
						if(g.graph[can].contains(pt))
							sen++;
						}
					sg.add(can);
					}	
			}
			sen/=2;
			if(noe==1){//check node
				if(snn>=seedSize) {
					sol.add(ca);
					solMotifTypes.add(cnext.motif.id);
					}
			}
			if(noe==2){//check edge
				if(sen>=seedSize) {
					sol.add(ca);
					solMotifTypes.add(cnext.motif.id);
					}
			}
		}
	}

	public ArrayList<ArrayList<Integer>> motifInstances(int[]query, int s, boolean[]motif_sig, int t, String link_type, ArrayList<Integer>nodes){
		
		boolean linksig = false;
		int delta = -1, noe = -1;
		if(link_type==null||link_type.length()==0)
			linksig = false;
		else{
			String[]link=link_type.split(",");
			//int delta = -1, noe = -1; // noe=1/2: two motifs should share delta nodes/edges
			if(link_type.length()!=0&&link.length==2){
				delta = Integer.parseInt(link[0]);
				noe = Integer.parseInt(link[1]);
				linksig = true; // start using seeds.
			}
		}
		networkGen a = new networkGen(g);
		ArrayList<ArrayList<Integer>> sols = null;
		
		if(linksig)
			sols=a.motifInstances(query, s, nodes, delta, noe, motif_sig, Integer.MAX_VALUE);
		else
			sols=a.motifInstances(query, s, motif_sig, Integer.MAX_VALUE);
		
		return sols;
	}
	
	public int[] motif_counts_st(int[]query, int s, int t){
		//count motif_instanses around s, and the counts containing t
		int []count = new int[2];		
		
		overflowSig = false;
		//sol=new ArrayList();
		ArrayList<isotopeMotifNet>motifsJump1 = buildMotifNet(mn, query);
		//now motif-network is built as motifsJump1.
		for(int i=0;i<motifsJump1.size();i++){
			isotopeMotifNet m = motifsJump1.get(i);
			ArrayList<Integer>nei = g.graph[s]; 
			//find motif-instances that are isomorphic to m;
			int k = m.getMotifSize()-1;
			//find k nodes from nei
			if(nei.size()<k)continue;
			String[]sig = new Combination().getStr(nei.size(), k);
			if(sig==null){
				overflowSig=true; return count;
				}
			ArrayList<Integer>sset = new ArrayList();sset.add(s);
			ArrayList<Integer>degset = new ArrayList();degset.add(0);
			boolean[]motif_sig = new boolean[g.nodeNum+1];
			ArrayList<ArrayList<Integer>>candidates=canGen(sig,nei, k, sset, degset, m, s, 1, motif_sig, Integer.MAX_VALUE);
			//get candidates that isomorphic to m.motif.signature (jump 1)
			//String[] sig, ArrayList<Integer> nei, graphReady g, int k, ArrayList<Integer>s, ArrayList<Integer>deg, String signature
			
			if(m.next==null||m.next.size()==0){
				//sol.addAll(candidates);
				count[0]+=candidates.size();
				count[1]+=motif_counts_t(t,candidates);
				continue;
			}
			if(m.outputSig){
				//sol.addAll(candidates);
				count[0]+=candidates.size();
				count[1]+=motif_counts_t(t,candidates);
			}
			if(FindTSig){
				return count;
			}
			//candidates, g[s]
				
			for(int j=0;j<candidates.size();j++){	
				
				ArrayList<Integer> cnv = candidates.get(j);
				for(int p=0;p<m.next.size();p++){
					
					isotopeMotifNet cnext = m.next.get(p);
					ArrayList<ArrayList<Integer>>candidates2=candidateGen(cnext, cnv, g, s, m, motif_sig, t);
					if(candidates2==null||candidates2.size()==0)continue;
					//get candidates2 that is isomorphic to cnext.motif.signature
					
					if(cnext.next==null||cnext.next.size()==0){
						count[0]+=candidates2.size();
						count[1]+=motif_counts_t(t,candidates2);
						continue;
					}
					if(cnext.outputSig){//sol.addAll(candidates2);
						count[0]+=candidates2.size();
						count[1]+=motif_counts_t(t,candidates2);
					}
					if(FindTSig||overflowSig){
						return count;
					}
						
					for(int j2=0;j2<candidates2.size();j2++){
						ArrayList<Integer> cnv2 = candidates2.get(j2);
						for(int p2=0;p2<cnext.next.size();p2++){
							isotopeMotifNet cnext2 = cnext.next.get(p2);
							ArrayList<ArrayList<Integer>>candidates3=candidateGen(cnext2, cnv2, g, s, cnext, motif_sig, t);
							if(candidates3==null||candidates3.size()==0)continue;
							//get candidates2 that is isomorphic to cnext2.motif.signature
							
							if(cnext2.next==null||cnext2.next.size()==0){
								//sol.addAll(candidates3);
								count[0]+=candidates3.size();
								count[1]+=motif_counts_t(t,candidates3);
								continue;
							}
							if(cnext2.outputSig){//sol.addAll(candidates3);
								count[0]+=candidates3.size();
								count[1]+=motif_counts_t(t,candidates3);
							}
							if(FindTSig||overflowSig){
								return count;
							}
							
							for(int j3=0;j3<candidates3.size();j3++){
								ArrayList<Integer> cnv3 = candidates3.get(j3);
								for(int p3=0;p3<cnext2.next.size();p3++){
									isotopeMotifNet cnext3 = cnext2.next.get(p3);
									ArrayList<ArrayList<Integer>>candidates4=candidateGen(cnext3, cnv3, g, s, cnext2, motif_sig, t);
									if(candidates4==null||candidates4.size()==0)continue;
									//get candidates2 that is isomorphic to cnext2.motif.signature
									if(cnext3.next==null||cnext3.next.size()==0){
										//sol.addAll(candidates4);
										count[0]+=candidates4.size();
										count[1]+=motif_counts_t(t,candidates4);
										continue;
									}
									if(cnext3.outputSig){//sol.addAll(candidates4);
										count[0]+=candidates4.size();
										count[1]+=motif_counts_t(t,candidates4);
									}
									if(FindTSig||overflowSig){
										return count;
									}
								}
								}
								}
							}
						
						}
						
						
					}
					
					
				
			}
			return count;
	}

	public int motif_counts_t(int t, ArrayList<ArrayList <Integer>>m){
		int count = 0;
		for(int i=0;i<m.size();i++){
			if(m.get(i).contains(t))
				count++;
		}
		return count;
	}
	
	public ArrayList<Integer>solMotifTypes = null;
	public ArrayList<ArrayList<Integer>> motifInstances(int[]query, int s, boolean[]motif_sig, int t){
		//from s to t with query
		
		overflowSig = false;
		sol=new ArrayList();
		solMotifTypes = new ArrayList();
		mn = init();
		//touched = new boolean[g.nodeNum];
		
		
		ArrayList<isotopeMotifNet>motifsJump1 = buildMotifNet(mn, query);
		//now motif-network is built as motifsJump1.
		if(motif_sig[s])return sol;
		for(int i=0;i<motifsJump1.size();i++){
			isotopeMotifNet m = motifsJump1.get(i);
			ArrayList<Integer>nei = g.graph[s]; 
			//find motif-instances that are isomorphic to m;
			int k = m.getMotifSize()-1;
			//find k nodes from nei
			if(nei.size()<k)continue;
			
			String[]sig = null;
			sig = new Combination().getStr(nei.size(), k);
			
			if(sig==null){
				overflowSig=true; return sol;
				}
			ArrayList<Integer>sset = new ArrayList();sset.add(s);
			ArrayList<Integer>degset = new ArrayList();degset.add(0);
			
			//System.out.println("sub1: find motif-"+(31-m.motif.id)+" around "+s+" from "+sig.length+" candidates");
			ArrayList<ArrayList<Integer>>candidates=canGen(sig,nei, k, sset, degset, m, s, 1, motif_sig, t);
			//get candidates that isomorphic to m.motif.signature (jump 1)
			//String[] sig, ArrayList<Integer> nei, graphReady g, int k, ArrayList<Integer>s, ArrayList<Integer>deg, String signature
			
			if(m.next==null||m.next.size()==0){
				sol.addAll(candidates);
				for(int j=0;j<candidates.size();j++)
					solMotifTypes.add(m.motif.id);
				continue;
			}
			if(m.outputSig){
				sol.addAll(candidates);
				for(int j=0;j<candidates.size();j++)
					solMotifTypes.add(m.motif.id);
			}
			if(FindTSig){
				return sol;
			}   
			//candidates, g[s]
				
			for(int j=0;j<candidates.size();j++){	
				
				ArrayList<Integer> cnv = candidates.get(j);
				for(int p=0;p<m.next.size();p++){
					
					isotopeMotifNet cnext = m.next.get(p);
					//System.out.println("sub2: find motif-"+(31-cnext.motif.id)+" around "+s+ " from "+candidates.size()+" candidates");
					ArrayList<ArrayList<Integer>>candidates2=candidateGen(cnext, cnv, g, s, m, motif_sig, t);
					if(candidates2==null||candidates2.size()==0)continue;
					//get candidates2 that is isomorphic to cnext.motif.signature
					
					if(cnext.next==null||cnext.next.size()==0){
						sol.addAll(candidates2);
						for(int r=0;r<candidates2.size();r++)
							solMotifTypes.add(cnext.motif.id);
						continue;
					}
					if(cnext.outputSig) {
						sol.addAll(candidates2);
						for(int r=0;r<candidates2.size();r++)
							solMotifTypes.add(cnext.motif.id);
					}
					if(FindTSig||overflowSig){
						return sol;
					}
					
					for(int j2=0;j2<candidates2.size();j2++){
						ArrayList<Integer> cnv2 = candidates2.get(j2);
						for(int p2=0;p2<cnext.next.size();p2++){
							isotopeMotifNet cnext2 = cnext.next.get(p2);
							//System.out.println("sub3: find motif-"+(31-cnext2.motif.id)+" around "+s+" from "+candidates2.size()+" candidates");
							ArrayList<ArrayList<Integer>>candidates3=candidateGen(cnext2, cnv2, g, s, cnext, motif_sig, t);
							if(candidates3==null||candidates3.size()==0)continue;
							//get candidates2 that is isomorphic to cnext2.motif.signature
							
							if(cnext2.next==null||cnext2.next.size()==0){
								sol.addAll(candidates3);
								for(int r=0;r<candidates3.size();r++)
									solMotifTypes.add(cnext2.motif.id);
								continue;
							}
							if(cnext2.outputSig) {
								sol.addAll(candidates3);
								for(int r=0;r<candidates3.size();r++)
									solMotifTypes.add(cnext2.motif.id);
							}
							if(FindTSig||overflowSig){
								return sol;
							}
							
							for(int j3=0;j3<candidates3.size();j3++){
								ArrayList<Integer> cnv3 = candidates3.get(j3);
								for(int p3=0;p3<cnext2.next.size();p3++){
									isotopeMotifNet cnext3 = cnext2.next.get(p3);
									//System.out.println("sub4: find motif-"+(31-cnext3.motif.id)+" around "+s+" from "+candidates3.size()+" candidates");
									ArrayList<ArrayList<Integer>>candidates4=candidateGen(cnext3, cnv3, g, s, cnext2, motif_sig, t);
									if(candidates4==null||candidates4.size()==0)continue;
									//get candidates2 that is isomorphic to cnext2.motif.signature
									if(cnext3.next==null||cnext3.next.size()==0){
										sol.addAll(candidates4);
										for(int r=0;r<candidates4.size();r++)
											solMotifTypes.add(cnext3.motif.id);
										continue;
									}
									if(cnext3.outputSig) {
										sol.addAll(candidates4);
										for(int r=0;r<candidates4.size();r++)
											solMotifTypes.add(cnext3.motif.id);
									}
									if(FindTSig||overflowSig){
										return sol;
									}
								}
							}
							}
						}
					
					}
					
					
				}
				
				
			
		}
		
		return sol;
			
		
		
	}
	private ArrayList<ArrayList<Integer>> candidateGen(isotopeMotifNet cnext, ArrayList<Integer> cnv, graphReady g,
			int s, isotopeMotifNet m, boolean[]motif_sig, int tt) {
		int bar = 500;
		int singularSig = cnext.singular;
		int extendDegree = cnext.expandDegree;
		String nsignature = cnext.motif.signature;
		int extendNum = nsignature.length()-cnv.size();
		for(int i=nsignature.length()-1;i>=0;i--)if(nsignature.charAt(i)==(char)(0+48))extendNum--;else break;
		ArrayList<Integer>degreeSet = new ArrayList();
		for(int t=0;t<nsignature.length();t++){
			int dbit = Character.getNumericValue((int)m.motif.signature.charAt(t));
			if(dbit==0)break;
			degreeSet.add(dbit);
		}
		
		ArrayList<ArrayList<Integer>>candidates2 = new ArrayList();
		if(singularSig==2){//extend by two extendDegree nodes, each contribute a node
			int cnode1 = 0, cnode2 = 0;
			for(int d=0;d<m.motif.signature.length();d++){
				if(m.motif.signature.charAt(d)==(char)(extendDegree+48)){
					cnode1 = cnv.get(d);
					cnode2 = cnv.get(d+1);
					if(cnode1==s||cnode2==s)continue;
					if(motif_sig[cnode1]||motif_sig[cnode2])continue;
					break;
				}
			}
			//extend from g.graph[cnode1],g.graph[cnode2] and compare signature
			ArrayList<Integer>nei11 = new ArrayList();
			ArrayList<Integer>nei12 = new ArrayList();
			for(int i1=0;i1<g.graph[cnode1].size();i1++){
				int nei = g.graph[cnode1].get(i1);
				if(motif_sig[nei])continue;
				if(!cnv.contains(nei))
					nei11.add(nei);
			
			}
			for(int i1=0;i1<g.graph[cnode2].size();i1++){
				int nei = g.graph[cnode2].get(i1);
				if(motif_sig[nei])continue;
				if(!cnv.contains(nei))
					nei12.add(nei);
			}
			if(nei11.size()<1||nei12.size()<1)return null;
			//select one from g.graph[cnode1] and one from g.graph[cnode2]
			String[]sig2 = new String[nei11.size()*nei12.size()];
			String[]sig21 = null;
			sig21 = new Combination().getStr(nei11.size(), 1);
			
			String[]sig22 = null;
			sig22=new Combination().getStr(nei12.size(), 1);
			
			if(sig21==null||sig22==null){overflowSig=true; return candidates2;}
			int counter = 0;
			for(int t=0;t<sig21.length;t++){
				for(int q=0;q<sig22.length;q++){
					sig2[counter]=sig21[t]+sig22[q];
					counter++;
				}
			}
			nei11.addAll(nei12);
			candidates2.addAll(canGen(sig2,nei11, extendNum, cnv, degreeSet, cnext, s, 2, motif_sig, tt));
			candidates2 = sampleCan(candidates2, bar);
			if(FindTSig)
				return candidates2;
		}
		else 
		for(int d=0;d<m.motif.signature.length();d++){
			if(m.motif.signature.charAt(d)==(char)(extendDegree+48)){
				int cnode = cnv.get(d);
				if(cnode==s)continue;
				//first node that matches the extendDegree: cnode
				//extend from g.graph[cnode] and compare signature
				//combination: extendNum nodes from g.graph[cnode];
				ArrayList<Integer>nei1 = new ArrayList();
				for(int i1=0;i1<g.graph[cnode].size();i1++){
					int nei = g.graph[cnode].get(i1);
					if(motif_sig[nei])continue;
					if(!cnv.contains(nei))
						nei1.add(nei);
				}
				if(nei1.size()<extendNum)continue;
				String[]sig2 = null;
				sig2 = new Combination().getStr(nei1.size(), extendNum);
				
				if(sig2==null){overflowSig=true; return candidates2;}
				candidates2.addAll(canGen(sig2,nei1, extendNum, cnv, degreeSet, cnext, s, 2, motif_sig, tt));
				candidates2 = sampleCan(candidates2, bar);
				if(singularSig==1)break;//extend by extendDegree for the first node only
				//extend by extendDegree in turn, if singularSig==0, so do noting to keep the loop run
				if(FindTSig)
					return candidates2;
			}
		}
		candidates2 = sampleCan(candidates2, bar);
		return candidates2;
	}
	private ArrayList<ArrayList<Integer>> sampleCan(ArrayList<ArrayList<Integer>> can, int bar) {
		while(can.size()>bar) {
			double id = Math.random()*can.size();
			can.remove((int)id);
		}
		return can;
	}


	private ArrayList<ArrayList<Integer>> canGen_old(String[] sig, ArrayList<Integer> nei, graphReady g, int k, ArrayList<Integer>last, ArrayList<Integer>deg1, String signature, int s, int jumpID) {
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		HashSet<Character>hs = new HashSet();
		for(int i=0;i<signature.length();i++){
			if(!hs.contains(signature.charAt(i)))
				hs.add(signature.charAt(i));
		}
		
		for(int j=0;j<sig.length;j++){
			String asig = sig[j];
			ArrayList<Integer>degreeVec = new ArrayList<Integer>();
			ArrayList<Integer>nodeVec = new ArrayList<Integer>();
			
			boolean breaksig = false;
			for(int p=0;p<asig.length();p++){
				if(asig.charAt(p)=='1'){
					int neip = nei.get(p);
					int degree = 0;
					for(int i=0;i<g.graph[neip].size();i++){
						int nei1 = g.graph[neip].get(i);
						if(last.contains(nei1)||nei.contains(nei1)&&asig.charAt(nei.indexOf(nei1))=='1'){
							degree++;
						}
					}
					if(!hs.contains((char)(degree+48))){
						breaksig = true;
						break;
					}
					degreeVec.add(degree);
					nodeVec.add(neip);
				}
			}
			if(breaksig)continue;
			
			if(jumpID!=1)
			for(int p=0;p<nodeVec.size();p++){
				if(g.graph[s].contains(nodeVec.get(p))){
					breaksig = true;
					break;
				}
				}
			if(breaksig)continue;
			
			ArrayList<Integer>deg = (ArrayList<Integer>) deg1.clone();
			for(int i=0;i<last.size();i++){
				int cn = last.get(i);
				if(jumpID>1&&cn==s)continue;
				for(int p=0;p<nodeVec.size();p++){
					if(g.graph[cn].contains(nodeVec.get(p))){
						deg.set(i, deg.get(i)+1);
						}
				}
			}
			for(int i=0;i<last.size();i++){
				degreeVec.add(deg.get(i));
				nodeVec.add(last.get(i));
			}
			
			for(int i=0;i<degreeVec.size()-1;i++){
				for(int p=i+1;p<degreeVec.size();p++){
					if(degreeVec.get(i)<degreeVec.get(p)){
						int tdeg = degreeVec.get(i);
						int tnod = nodeVec.get(i);
						degreeVec.set(i, degreeVec.get(p));
						nodeVec.set(i, nodeVec.get(p));
						degreeVec.set(p, tdeg);
						nodeVec.set(p, tnod);
					}
				}
				
			}
			if(equCheck(degreeVec, signature)){
				res.add(nodeVec);
			}
		}
		return res;
	}
	public boolean FindTSig;
	private ArrayList<ArrayList<Integer>> canGen(String[] sig, ArrayList<Integer> nei, int k, ArrayList<Integer>last, ArrayList<Integer>deg1, isotopeMotifNet m, int s, int jumpID, boolean[]motif_sig, int t) {
		FindTSig = false;
		ArrayList<ArrayList<Integer>>res = new ArrayList();
		HashSet<Character>hs = new HashSet();
		for(int i=0;i<m.motif.signature.length();i++){
			if(!hs.contains(m.motif.signature.charAt(i)))
				hs.add(m.motif.signature.charAt(i));
		} 
		
		for(int j=0;j<sig.length;j++){
			String asig = sig[j];
			ArrayList<Integer>degreeVec = new ArrayList<Integer>();
			ArrayList<Integer>nodeVec = new ArrayList<Integer>();
			
			boolean breaksig = false;
			for(int p=0;p<asig.length();p++){
				if(asig.charAt(p)=='1'){
					int neip = nei.get(p);
					if(motif_sig[neip]){
						breaksig = true;
						break;
					}
					int degree = 0;
					for(int i=0;i<g.graph[neip].size();i++){
						int nei1 = g.graph[neip].get(i);
						if(last.contains(nei1)||nei.contains(nei1)&&asig.charAt(nei.indexOf(nei1))=='1'){
							degree++;
						}
					}
					if(!hs.contains((char)(degree+48))){
						breaksig = true;
						break;
					}
					degreeVec.add(degree);
					nodeVec.add(neip);
				}
			}
			if(breaksig)continue;
			
			if(jumpID!=1)
			for(int p=0;p<nodeVec.size();p++){
				if(g.graph[s].contains(nodeVec.get(p))){
					breaksig = true;
					break;
				}
				}
			if(breaksig)continue;
			
			ArrayList<Integer>deg = (ArrayList<Integer>) deg1.clone();
			for(int i=0;i<last.size();i++){
				int cn = last.get(i);
				if(jumpID>1&&cn==s)continue;
				for(int p=0;p<nodeVec.size();p++){
					if(g.graph[cn].contains(nodeVec.get(p))){
						deg.set(i, deg.get(i)+1);
						}
				}
			}
			for(int i=0;i<last.size();i++){
				degreeVec.add(deg.get(i));
				nodeVec.add(last.get(i));
			}
			
			for(int i=0;i<degreeVec.size()-1;i++){
				for(int p=i+1;p<degreeVec.size();p++){
					if(degreeVec.get(i)<degreeVec.get(p)){
						int tdeg = degreeVec.get(i);
						int tnod = nodeVec.get(i);
						degreeVec.set(i, degreeVec.get(p));
						nodeVec.set(i, nodeVec.get(p));
						degreeVec.set(p, tdeg);
						nodeVec.set(p, tnod);
					}
				}
				
			}
			if(equCheck(degreeVec, m.motif.signature)){
				res.add(nodeVec);
				if(nodeVec.contains(t)&&m.outputSig){
					//System.out.print(nodeVec.toString());
					FindTSig = true;
					return res;
				}
			}
		}
		return res;
	}
	
	public boolean equCheck(ArrayList<Integer> degreeVec, String signature) {
		//while(signature.charAt(signature.length()-1)=='0')
		//	signature=signature.substring(0, signature.length()-1);
		for(int i=0;i<degreeVec.size();i++){
			//char p = signature.charAt(signature.length()-1-i);
			if((char)(degreeVec.get(i)+48)!=signature.charAt(i)){
				return false;
				}
		}
		return true;
	}
	private ArrayList<isotopeMotifNet> buildMotifNet(motifNet[] mn, int[] query) {
		ArrayList<isotopeMotifNet>motifsJump1 = new ArrayList();
		for(int i=0;i<query.length;i++){
 			int motifID = query[i];
			for(int j=1;j<mn[motifID].isotopes.length;j++){
				ArrayList<isotopeMotifNet>motifChain = mn[motifID].isotopes[j];
				int jumpID = j;
				if(motifChain==null)continue;
				if(jumpID==1){
					for(int p=0;p<motifChain.size();p++){
						isotopeMotifNet motifTemplate = motifChain.get(p);
						motifTemplate.outputSig = true;
						if(!motifsJump1.contains(motifTemplate))
							motifsJump1.add(motifTemplate);
						}
					continue;
				}
				for(int p=0;p<motifChain.size();p++){
					isotopeMotifNet motifTemplate = motifChain.get(p);
					motifTemplate.outputSig = true;
					while(motifTemplate.last!=null){
						isotopeMotifNet motifTemplateLast = motifTemplate;
						motifTemplate=motifTemplate.last;
						if(!motifTemplate.next.contains(motifTemplateLast))
							motifTemplate.next.add(motifTemplateLast);
						if(motifTemplate.jumpID==1){
							if(!motifsJump1.contains(motifTemplate))
								motifsJump1.add(motifTemplate);
							}
						}
					}
				}
			}
		return motifsJump1;
	}
	private motifNet[] init() {
		String[]motifs={"44444","44433","44332","43333","43331","44222","43322","33332","33330","33321","43221","42222","33222","33222","33220","32221","32221","42211","22222","33211","22220","32210","32111","22211","41111","22200","31110","22110","21100","11000"};
		motifNet[]mn=new motifNet[31];
		for(int i=0;i<motifs.length;i++){
			mn[i+1] = new motifNet(i+1,motifs[29-i]);
		}
		
		int[]jump1={9,1,15,27,2,22,3,4,26,6,5,7,29,11,12,18,30,25};
		for(int i=0;i<jump1.length;i++)
			mn[31-jump1[i]].addIsotm(1, null);
		
		int[]jump2_1={2,3,5};
		for(int i=0;i<jump2_1.length;i++){
			mn[31-jump2_1[i]].addIsotm(2, mn[31-9].isotopes[1].get(0));
			mn[31-jump2_1[i]].isotopes[2].get(0).expandDegree=3;
				}
		mn[31-2].isotopes[2].get(0).singular=1;
		
		int[]jump2_2={4,7,8,10,11};
		for(int i=0;i<jump2_2.length;i++){
			mn[31-jump2_2[i]].addIsotm(2, mn[31-15].isotopes[1].get(0));
			if(jump2_2[i]==4||jump2_2[i]%2==1)
				mn[31-jump2_2[i]].isotopes[2].get(0).expandDegree=3;
			else
				mn[31-jump2_2[i]].isotopes[2].get(0).expandDegree=2;
		}
		mn[31-8].isotopes[2].get(0).singular=1;
		
		int[]jump2_3={23,14,16};
		for(int i=0;i<jump2_3.length;i++){
			mn[31-jump2_3[i]].addIsotm(2, mn[31-27].isotopes[1].get(0));
			mn[31-jump2_3[i]].isotopes[2].get(0).expandDegree=1;
			}
		mn[31-14].isotopes[2].get(0).singular=1;
		
		int[]jump2_4={8,10,13,17,20};
		for(int i=0;i<jump2_4.length;i++)
			mn[31-jump2_4[i]].addIsotm(2, mn[31-22].isotopes[1].get(0));
		mn[31-8].isotopes[2].get(1).expandDegree=1;
		mn[31-13].isotopes[2].get(0).expandDegree=1;
		mn[31-17].isotopes[2].get(0).expandDegree=1;
		mn[31-10].isotopes[2].get(1).expandDegree=2;
		mn[31-20].isotopes[2].get(0).expandDegree=2;
		mn[31-10].isotopes[2].get(1).singular=1;
		
		int[]jump2_5={3,6,7,11,12,13,15,18,20,22};
		for(int i=0;i<jump2_5.length;i++)
			mn[31-jump2_5[i]].addIsotm(2, mn[31-26].isotopes[1].get(0));
		mn[31-3].isotopes[2].get(1).expandDegree=2;
		mn[31-3].isotopes[2].get(1).singular=1;
		mn[31-6].isotopes[2].get(0).expandDegree=2;
		mn[31-6].isotopes[2].get(0).singular=1;
		mn[31-7].isotopes[2].get(1).expandDegree=2;
		mn[31-11].isotopes[2].get(1).expandDegree=2;
		mn[31-12].isotopes[2].get(0).expandDegree=2;
		mn[31-13].isotopes[2].get(1).expandDegree=2;
		mn[31-13].isotopes[2].get(1).singular=2;
		mn[31-15].isotopes[2].get(0).expandDegree=2;
		mn[31-15].isotopes[2].get(0).singular=1;
		mn[31-18].isotopes[2].get(0).expandDegree=2;
		mn[31-20].isotopes[2].get(1).expandDegree=2;
		mn[31-20].isotopes[2].get(1).singular=2;
		mn[31-22].isotopes[2].get(0).expandDegree=2;
		
		int[]jump2_6={8,13,14,16,17,19,21,23,24,28};
		for(int i=0;i<jump2_6.length;i++)
			mn[31-jump2_6[i]].addIsotm(2, mn[31-29].isotopes[1].get(0));
		mn[31-8].isotopes[2].get(2).expandDegree=1;
		mn[31-8].isotopes[2].get(2).singular=1;
		mn[31-13].isotopes[2].get(2).expandDegree=1;
		mn[31-14].isotopes[2].get(1).expandDegree=1;
		mn[31-14].isotopes[2].get(1).singular=1;
		mn[31-16].isotopes[2].get(1).expandDegree=1;
		mn[31-17].isotopes[2].get(1).expandDegree=1;
		mn[31-19].isotopes[2].get(0).expandDegree=1;
		mn[31-19].isotopes[2].get(0).singular=2;
		mn[31-21].isotopes[2].get(0).expandDegree=1;
		mn[31-21].isotopes[2].get(0).singular=1;
		mn[31-23].isotopes[2].get(1).expandDegree=1;
		mn[31-24].isotopes[2].get(0).expandDegree=1;
		mn[31-24].isotopes[2].get(0).singular=2;
		mn[31-28].isotopes[2].get(0).expandDegree=1;
		
		int[]jump2_7={5,11,18,22,25,27,29};
		for(int i=0;i<jump2_7.length;i++)
			mn[31-jump2_7[i]].addIsotm(2, mn[31-30].isotopes[1].get(0));
		mn[31-5].isotopes[2].get(1).expandDegree=1;
		mn[31-11].isotopes[2].get(2).expandDegree=1;
		mn[31-18].isotopes[2].get(1).expandDegree=1;
		mn[31-22].isotopes[2].get(1).expandDegree=1;
		mn[31-25].isotopes[2].get(0).expandDegree=1;
		mn[31-27].isotopes[2].get(0).expandDegree=1;
		mn[31-29].isotopes[2].get(0).expandDegree=1;
		
		mn[31-10].addIsotm(3, mn[31-15].isotopes[2].get(0));
		mn[31-17].addIsotm(3, mn[31-22].isotopes[2].get(0));
		mn[31-10].addIsotm(3, mn[31-22].isotopes[2].get(1));//check
		mn[31-20].addIsotm(3, mn[31-22].isotopes[2].get(1));
		mn[31-16].addIsotm(3, mn[31-21].isotopes[2].get(0));
		mn[31-24].addIsotm(3, mn[31-28].isotopes[2].get(0));
		
		
		mn[31-10].isotopes[3].get(0).expandDegree=2;
		mn[31-17].isotopes[3].get(0).expandDegree=1;
		mn[31-10].isotopes[3].get(1).expandDegree=2;
		mn[31-10].isotopes[3].get(1).singular=1;
		mn[31-20].isotopes[3].get(0).expandDegree=2;
		mn[31-16].isotopes[3].get(0).expandDegree=2;
		mn[31-24].isotopes[3].get(0).expandDegree=1;
		
		int[]jump3_1={17,23,28};
		for(int i=0;i<jump3_1.length;i++)
			mn[31-jump3_1[i]].addIsotm(3, mn[31-29].isotopes[2].get(0));
		mn[31-17].isotopes[3].get(1).expandDegree=1;
		mn[31-23].isotopes[3].get(0).expandDegree=1;
		mn[31-28].isotopes[3].get(0).expandDegree=1;
		
		int[]jump3_2={16,23};
		for(int i=0;i<jump3_2.length;i++)
			mn[31-jump3_2[i]].addIsotm(3, mn[31-27].isotopes[2].get(0));
		mn[31-16].isotopes[3].get(1).expandDegree=1;
		mn[31-16].isotopes[3].get(1).singular=1;
		mn[31-23].isotopes[3].get(1).expandDegree=1;
		
		mn[31-24].addIsotm(4, mn[31-28].isotopes[3].get(0));
		mn[31-24].isotopes[4].get(0).expandDegree=1;
		
		/*
		setSDegree(9,1,0,3);
		setSDegree(1,1,0,4);
		setSDegree(15,1,0,3);
		setSDegree(27,1,0,3);
		setSDegree(2,1,0,4);
		setSDegree(22,1,0,3);
		setSDegree(3,1,0,4);
		setSDegree(4,1,0,4);
		setSDegree(26,1,0,2);
		setSDegree(6,1,0,4);
		setSDegree(5,1,0,4);
		setSDegree(7,1,0,4);
		setSDegree(29,1,0,2);
		setSDegree(11,1,0,4);
		setSDegree(12,1,0,4);
		setSDegree(18,1,0,4);
		setSDegree(30,1,0,1);
		setSDegree(25,1,0,4);
		setSDegree(2,2,0,3);
		setSDegree(3,2,0,3);
		setSDegree(5,2,0,3);
		setSDegree(8,2,0,3);
		setSDegree(10,2,0,3);
		setSDegree(11,2,0,3);
		setSDegree(4,2,0,3);
		setSDegree(7,2,0,3);
		setSDegree(23,2,0,3);
		setSDegree(14,2,0,3);
		setSDegree(16,2,0,3);
		setSDegree(8,2,1,3);
		setSDegree(13,2,0,3);
		setSDegree(17,2,0,3);
		setSDegree(10,2,1,3);
		setSDegree(20,2,0,3);
		setSDegree(6,2,0,2);
		setSDegree(13,2,1,2);
		setSDegree(18,2,0,2);
		setSDegree(15,2,0,2);
		setSDegree(11,2,1,2);
		setSDegree(7,2,1,2);
		setSDegree(12,2,0,2);
		setSDegree(22,2,0,2);
		setSDegree(3,2,1,2);
		setSDegree(20,2,1,2);
		setSDegree(21,2,0,2);
		setSDegree(16,2,1,2);
		setSDegree(13,2,2,2);
		setSDegree(19,2,0,2);
		setSDegree(28,2,0,2);
		setSDegree(8,2,2,2);
		setSDegree(14,2,1,2);
		setSDegree(17,2,1,2);
		setSDegree(24,2,0,2);
		setSDegree(23,2,1,2);
		setSDegree(29,2,0,1);
		setSDegree(11,2,2,1);
		setSDegree(18,2,1,1);
		setSDegree(27,2,0,1);
		setSDegree(22,2,1,1);
		setSDegree(25,2,0,1);
		setSDegree(5,2,1,1);
		setSDegree(10,3,0,2);
		setSDegree(17,3,0,2);
		setSDegree(16,3,0,2);
		setSDegree(24,3,0,2);
		setSDegree(28,3,0,1);
		setSDegree(23,3,0,1);
		setSDegree(17,3,1,1);
		setSDegree(16,3,1,1);
		setSDegree(23,3,1,1);
		setSDegree(20,3,0,2);
		setSDegree(10,3,1,1);
		*/
		return mn;
	}
	/*
	void setSDegree(int motifID, int jumpID, int instanceID, int d){
		mn[motifID].isotopes[jumpID].get(instanceID).sDegree=d;
	}*/
}
