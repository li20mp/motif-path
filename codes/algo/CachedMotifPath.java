package algo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import tool.Dataset;
import tool.Datasets;
import tool.FileOps;
import tool.graphReady;
import tool.motif;

public class CachedMotifPath {

	public static void main(String[] args) throws IOException {
		String mainDir = "";
		boolean localSig = false;
		if(localSig) {
			mainDir = "./";
			args = new String[6];
			args[0] = ""+	12; //pid
			args[1] = ""+	1; //cachedlevel
			args[2] = ""+	1000; //queryNum
			args[3] = ""+	99; //dataset
			args[4] = ""+	0; //enhanceSig
			args[5] = ""+	0; //motif_component_detection_sig
		}else {
			mainDir = "./";
		}
		
		Datasets dts = new Datasets (mainDir);	
		
		int pid = Integer.parseInt(args[0]);
		int cachedLevel = Integer.parseInt(args[1]);
		int queryNum = Integer.parseInt(args[2]);
		int did = Integer.parseInt(args[3]);
		int enhanceSig = Integer.parseInt(args[4]);
		int motif_component_detection_sig = Integer.parseInt(args[5]);
		dts.Initialize(did);
		
		
		InsCached ic = new InsCached();
		int[]pset = {5,10,12,22,30};
		//ic.loadCached(dts, cachedLevel, pset);
		ic.loadCached(dts, cachedLevel);
		System.out.println("Index loaded.");
		
		if(motif_component_detection_sig==1) {
			cached_motif_component_detection (dts, pid, cachedLevel, ic);
			return;
		}
		
		enhanced_motif_path emp = new enhanced_motif_path();
		ArrayList<Integer>[]injectedEdges = emp.readInjectedEdges(dts, pid);
		
		//smpOResmp(dts, queryNum, pid, enhanceSig, cachedLevel, ic, emp, injectedEdges);
		smpANDesmp(dts, queryNum, pid, enhanceSig, cachedLevel, ic, emp, injectedEdges);
		
	}
	
	private static void smpANDesmp(Datasets dts, int queryNum, int pid, int enhanceSig, int cachedLevel, InsCached ic,
			enhanced_motif_path emp, ArrayList<Integer>[] injectedEdges) throws IOException {
		long timeSMP = 0, timeESMP = 0;
		for(int j=0;j<queryNum;j++){
			int s =0, t =0;
			while(s==t||s==0||t==0){
				s = (int)(Math.random()*dts.g.nodeNum)+1;
				t = (int)(Math.random()*dts.g.nodeNum)+1;
			}
			//System.out.println(s+" "+t);
			long t1 = System.currentTimeMillis();
			int spd = cached_motif_path(dts, s, t, pid, cachedLevel, ic);
			long t2 = System.currentTimeMillis();			
			timeSMP += t2 - t1;
			System.out.println(j+"-thESMP:\t"+(double)timeSMP/(j+1)+"\t"+s+"\t"+t+"\t"+spd);
			
			t1 = System.currentTimeMillis();
			spd = cached_enhaced_motif_path(dts, s, t, pid, cachedLevel, ic, emp, injectedEdges);
			t2 = System.currentTimeMillis();
			timeESMP += t2-t1;
			System.out.println(j+"-thSMP:\t"+(double)timeESMP/(j+1)+"\t"+s+"\t"+t+"\t"+spd);
			
		}
		double t1f1 = (double)timeSMP/queryNum, t1f2 = (double)timeESMP/queryNum;
		System.out.println("time:\t"+t1f1+"\t"+t1f2);
	}

	private static void smpOResmp(Datasets dts, int queryNum, int pid, int enhanceSig, int cachedLevel, InsCached ic,
			enhanced_motif_path emp, ArrayList<Integer>[] injectedEdges) throws IOException {
		long time = 0;
		for(int j=0;j<queryNum;j++){
			int s =0, t =0;
			while(s==t||s==0||t==0){
				s = (int)(Math.random()*dts.g.nodeNum)+1;
				t = (int)(Math.random()*dts.g.nodeNum)+1;
			}
			//System.out.println(s+" "+t);
			long t1 = System.currentTimeMillis();
			int spd = -2;
			if(enhanceSig==1)
				spd = cached_enhaced_motif_path(dts, s, t, pid, cachedLevel, ic, emp, injectedEdges);
			else
				spd = cached_motif_path(dts, s, t, pid, cachedLevel, ic);
			long t2 = System.currentTimeMillis();
			time += t2 - t1;
			
			System.out.println(j+"-th:\t"+(double)time/(j+1)+"\t"+s+"\t"+t+"\t"+spd);
		}
		double t1f = (double)time/queryNum;
		System.out.println("time:\t"+t1f);
	}

	public static int cached_enhaced_motif_path(Datasets dts, int s, int t, int pid, int cachedLevel, InsCached ic, enhanced_motif_path emp, ArrayList<Integer>[]injectedEdges) throws IOException{
		
		if(emp.comps[s] == emp.comps[t])
			return cached_motif_path(dts, s, t, pid, cachedLevel, ic);
		
		int [] jump = new int[dts.g.nodeNum+1];
		int SPD =  -1;
	
		boolean[]motif_sig= new boolean[dts.g.nodeNum+1];
		motif[]touched = new motif [dts.g.nodeNum+1];
		
		//ArrayList<ArrayList<Integer>>sols=null;
		//sols = cacheDeal(dts, pid, s, motif_sig, t, cachedLevel);
		
		ArrayList<ArrayList<Integer>>sols = ic.getInsCached(dts, cachedLevel, pid, s);
		if(sols==null)
			sols = new ArrayList();
		if(injectedEdges[s]!=null)
		for(int i=0;i<injectedEdges[s].size();i++) {
			int nei = injectedEdges[s].get(i);
			ArrayList<Integer>ains = new ArrayList();
			ains.add(i);
			ains.add(nei);
			sols.add(ains);
		}
		Queue<motif>q=new LinkedList<motif>();
		
		ArrayList<Integer>cans;
		for(int i=0;i<sols.size();i++){
			cans = sols.get(i);
			
			if(!checkMotifSig(cans,motif_sig)) 
				continue;

			if(cans.contains(t)){
				//System.out.println("Motif-path found with length = 0: " + cans.toString());
				SPD = 1;
				return SPD;
			}
			motif nm = new motif(cans,pid);
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
				sols = ic.getInsCached(dts, cachedLevel, pid, nei0);
				if(sols==null)
					sols = new ArrayList();
				for(int i=0;i<sols.size();i++){
					cans = sols.get(i);
					if(!checkMotifSig(cans,motif_sig)) 
						continue;
					motif m1 = new motif(cans,pid);
					m1.last = cr;
					if(cans.contains(t)){
						jump[t]=jump[nei0]+1;
						SPD = backtrack(m1, dts);
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
				
				motif_sig[nei0]=true;
				
			}			
		}
		
		
		//System.out.println("No motif-path exists!");
		return SPD;
		
	}
	
	public static void cached_motif_component_detection (Datasets dts, int pid, int cachedLevel, InsCached ic) throws IOException{
		//ifConnected = true;
		int [] jump = new int[dts.g.nodeNum+1];
		int SPD =  -1;
		int[]comps = new int[dts.g.graph.length];//starting from 1
		
		boolean[]motif_sig= new boolean[dts.g.nodeNum+1];
		motif[]touched = new motif [dts.g.nodeNum+1];
		
		ArrayList<Integer>unmarked = new ArrayList();
		for(int i=1;i<motif_sig.length;i++) {
			unmarked.add(i);
		}
		
		//ArrayList<ArrayList<Integer>>sols=null;
		//sols = cacheDeal(dts, pid, s, motif_sig, t, cachedLevel);
		
		int mcid = 1;
		
		while(unmarked.size()!=0) {
			int sid = unmarked.remove(0);
			comps[sid] = mcid; 
			
			ArrayList<ArrayList<Integer>>sols = ic.getInsCached(dts, cachedLevel, pid, sid);
			if(sols == null) {
				mcid++;
				continue;
			}
			Queue<motif>q=new LinkedList<motif>();
			
			ArrayList<Integer>cans;
			for(int i=0;i<sols.size();i++){
				cans = sols.get(i);
				
				if(!checkMotifSig(cans,motif_sig)) 
					continue;
				motif nm = new motif(cans,pid);
				//for each motif-instance, check if it is touched.
				boolean touchSig = true;
				for (int p = 0; p<cans.size(); p++){
					int ned = cans.get(p);
					if(touched[ned]==null){
						touched[ned] = nm;
						comps[ned] = mcid;
						if(unmarked.contains(ned)) {
							unmarked.remove(unmarked.indexOf(ned));
						}
						jump[ned]=1;
						touchSig = false;
					}
				}
				if(!touchSig)
					q.add(nm);	
			}
			motif_sig[sid]=true;
			jump[sid] = 1;
			
			while(!q.isEmpty()&&unmarked.size()!=0){
				motif cr = q.poll();
				for(int ii=0;ii<cr.nodes.size();ii++){
					int nei0 = cr.nodes.get(ii);
					if(motif_sig[nei0])continue;
					
					//System.out.println("Start finding motif-instances around node: "+nei0);
					sols = ic.getInsCached(dts, cachedLevel, pid, nei0);
		
					for(int i=0;i<sols.size();i++){
						cans = sols.get(i);
						if(!checkMotifSig(cans,motif_sig)) 
							continue;
						motif m1 = new motif(cans,pid);
						m1.last = cr;
						
						//for each motif-instance, check if it is touched.
						boolean touchSig = true;
						for (int p = 0; p<cans.size(); p++){
							int ned = cans.get(p);
							if(touched[ned]==null){
								touched[ned] = m1;
								jump[ned]=jump[nei0]+1;
								touchSig = false;
								
								comps[ned] = mcid;
								if(unmarked.contains(ned)) {
									unmarked.remove(unmarked.indexOf(ned));
								}
							}
						}
						if(!touchSig)
							q.add(m1);	
					}
					
					motif_sig[nei0]=true;
					
				}			
			}
			
			mcid++;
		}
		
		
		
		
		//System.out.println("No motif-path exists!");
		FileOps fo = new FileOps();
		BufferedWriter b = fo.BWriter(dts.d.dataPath+"_m"+pid+".mcom");
		b.write("Components("+(mcid-1)+"):");
		for(int i=1;i<comps.length;i++) {
			b.write(comps[i]+",");
		}
		b.flush();
		b.close();
	}
	
	private static int unmarked(motif[] touched) {
		// TODO Auto-generated method stub
		for(int i=1;i<touched.length;i++) {
			if(touched[i]==null) {
				return i;
			}
		}
		return -1;
	}

	public static int cached_motif_path(Datasets dts, int s, int t, int pid, int cachedLevel, InsCached ic) throws IOException{
		//ifConnected = true;
		int [] jump = new int[dts.g.nodeNum+1];
		int SPD =  -1;
	
		boolean[]motif_sig= new boolean[dts.g.nodeNum+1];
		motif[]touched = new motif [dts.g.nodeNum+1];
		
		//ArrayList<ArrayList<Integer>>sols=null;
		//sols = cacheDeal(dts, pid, s, motif_sig, t, cachedLevel);
		
		ArrayList<ArrayList<Integer>>sols = ic.getInsCached(dts, cachedLevel, pid, s);
		if(sols==null)
			sols = new ArrayList();
		Queue<motif>q=new LinkedList<motif>();
		
		ArrayList<Integer>cans;
		for(int i=0;i<sols.size();i++){
			cans = sols.get(i);
			
			if(!checkMotifSig(cans,motif_sig)) 
				continue;

			if(cans.contains(t)){
				//System.out.println("Motif-path found with length = 0: " + cans.toString());
				SPD = 1;
				return SPD;
			}
			motif nm = new motif(cans,pid);
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
				sols = ic.getInsCached(dts, cachedLevel, pid, nei0);
	
				for(int i=0;i<sols.size();i++){
					cans = sols.get(i);
					if(!checkMotifSig(cans,motif_sig)) 
						continue;
					motif m1 = new motif(cans,pid);
					m1.last = cr;
					if(cans.contains(t)){
						jump[t]=jump[nei0]+1;
						SPD = backtrack(m1, dts);
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
				
				motif_sig[nei0]=true;
				
			}			
		}
		
		
		//System.out.println("No motif-path exists!");
		return SPD;
	}

	public static int backtrack(motif tem, Datasets dts) {
		boolean sig[]=new boolean[dts.g.nodeNum+1];
		int SPD = 0;
		while(tem!=null){
			for(int i=0;i<tem.nodes.size();i++){
				sig[tem.nodes.get(i)]=true;
				//System.out.print(dts.g.match[tem.nodes.get(i)]+"-");
			}
			//System.out.println();
			tem=tem.last;
			SPD++;
		}
		return SPD;
	}

	private static boolean checkMotifSig(ArrayList<Integer> ins, boolean[] motif_sig) {
		for(int j=0;j<ins.size();j++) {
			if(motif_sig[ins.get(j)])
				return false;
		}
		return true;
	}

	private static ArrayList<ArrayList<Integer>> cacheDeal(Datasets dts, int pid, int s, boolean[] motif_sig, int t, int cacheLevel) throws IOException {
		InsCached ic = new InsCached();
		ArrayList<ArrayList<Integer>>ins = ic.getInsCached(dts, cacheLevel, pid, s);
		ArrayList<ArrayList<Integer>> sols = new ArrayList();
		for(int i=0;i<ins.size();i++) {
			boolean sig = true;
			for(int j=0;j<ins.get(i).size();j++) {
				if(motif_sig[ins.get(i).get(j)])
					sig = false;
			}
			if(sig)
				sols.add(ins.get(i));
		}
		
		return sols;
	}

}
