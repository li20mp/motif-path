package link_prediction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import algo.path_count;
import tool.BFS;
import tool.Dataset;
import tool.Datasets;
//import tool.Diameter;
import tool.graphReady;

public class QueryGen {

	public ArrayList<int[]> LinkPreQRead(Dataset d, int queryNum) throws IOException {
		ArrayList<int[]> LinkPreQOffline = new ArrayList();
		BufferedReader a = new BufferedReader(new FileReader(d.dataPath+".linkp"));
		for(int i=0;i<queryNum;i++) {
			String[]tem=a.readLine().split(",");
			int[]arr = new int[4];
			for(int j=0;j<arr.length;j++) {
				arr[j]=Integer.parseInt(tem[j]);	
			}
			LinkPreQOffline.add(arr);
		}
		return LinkPreQOffline;
	}
	
	public void LinkPreQWrite(int spd_max, int queryNum, int id, String mainDir) throws IOException {
		
		Datasets dts = new Datasets();
		dts.Initialize(id);
		BufferedWriter b = new BufferedWriter (new FileWriter(dts.d.dataPath+".linkp"));
		
		BFS bfs = new BFS();
		
		for(int i=0;i<queryNum;) {
			
			int s = (int)(Math.random()*dts.g.graph.length);
			
			ArrayList<Integer>pts = dts.g.graph[s];
			for(int j=0;j<pts.size();j++) {
				int spd = bfs.searchWioutST(s, pts.get(j), dts.g.graph);
				if(spd>spd_max)continue;
				//pts2.add(pts.get(j));
				
				int nt = (int)(Math.random()*dts.g.graph.length);
				while(s==nt||dts.g.graph[s].contains(nt)){
					nt = (int)(Math.random()*dts.g.graph.length);
					}
				
				String str = "";
				str = str + s +","+ nt +","+ s +","+ pts.get(j) +"\n";
				
				System.out.print(str);
				b.write(str);
				i++;
				break;
			}
			
		}
		
		b.flush();
		b.close();
	}
	
	public void LinkPreQWrite_sameDistribution (int queryNum, Datasets dts) throws IOException {
		
		BufferedWriter b = new BufferedWriter (new FileWriter(dts.d.dataPath+".linkp"));
		
		BFS bfs = new BFS();
		
		double posiDists[] = new double[51], negaDists[] = new double[51];
		int[][]posi = new int[queryNum][2], nega = new int[queryNum][2];
		
		for(int i=0;i<queryNum;) {
			int s = (int)(Math.random()*dts.g.graph.length);
			if(s==0)continue;
			ArrayList<Integer>pts = dts.g.graph[s];
			for(int j=0;j<pts.size();j++) {
				int spd = bfs.searchWioutST(s, pts.get(j), dts.g.graph);
				//if(spd>spd_max)continue;
				if(spd>posiDists.length||spd==-1) {continue;}
				posiDists[spd]++;
				
				posi[i][0]=s;
				posi[i][1]=pts.get(j);
				
				i++;
				//System.out.print(i+" ");
				break;
				}
				
		}
		//System.out.println();
		
		for(int i=0;i<posiDists.length;i++) {
			//System.out.print(posiDists[i]+",");
			posiDists[i]/=queryNum;
		}
		//System.out.println();
		
		for(int i=0;i<queryNum;) {
			
			
			int s = (int)(Math.random()*dts.g.graph.length);
			if(s==0)continue;
			int hop = sampleEdge(posiDists);
			negaDists[hop]++;
			ArrayList<Integer>candidates = 	bfs.searchTheHop(s, hop, dts.g.graph);
			if(candidates==null||candidates.size()==0)continue;
			double ptid = Math.random()*candidates.size();	
			
			String str = "";
			str = str + s +","+ candidates.get((int)ptid) +","+posi[i][0] +","+ posi[i][1]+"\n";
			
			//System.out.print(str);
			b.write(str);
			i++;
			//System.out.print(i+" ");
			}
		//System.out.println();
		
		for(int i=0;i<negaDists.length;i++) {
			//System.out.print(negaDists[i]+",");
			negaDists[i]/=queryNum;
		}
		//System.out.println();
		
		b.flush();
		b.close();
	}
	
	public void LinkPreQWrite_sameDistribution (int queryNum, int id, String mainDir) throws IOException {
		
		Datasets dts = new Datasets();
		dts.Initialize(id);
		LinkPreQWrite_sameDistribution(queryNum, dts);
	}

	public int sampleEdge(double[]w) {
		// TODO Auto-generated method stub
		double rand = Math.random();
		int val = 0;
		for(int p=1;p<w.length;p++){
			rand-=w[p];
			if(rand<=0){
				val = p;
				break;
			}
		}
		return val;
	}
	
	public int[] LinkPreQ(BFS bfs, ArrayList<Integer>[]graph, int spd_max) {
		//return a pair of negative nodes (without edge) and a pair of positive nodes (with an edge)
		
		int[]st = new int[4];
		
		//negtive sampling
		int nsc = 0;
		int s = 1, t = 1;// spd = Integer.MAX_VALUE;
		while(s==t||graph[s].contains(t)){//spd>4||
			s = (int)(Math.random()*graph.length);
			t = (int)(Math.random()*graph.length);
			if(s==t)continue;
			if(bfs!=null){
				int spd = bfs.search(s, t, graph);
				if(spd>spd_max)continue;
				}
			nsc++;
			//System.out.println("continue negative");
		}
		//System.out.println(s+" "+t+" "+katz(s,t)+" "+nsc);
		st[0] = s;
		st[1] = t;
		
		//positive sampling
		int psc = 0;
		s = 1;	t = 1; //spd = Integer.MAX_VALUE;
		while(s==t||!graph[s].contains(t)){//||spd>8
			s = (int)(Math.random()*graph.length);
			t = (int)(Math.random()*graph.length);
			if(s==t)continue;
			if(bfs!=null){
				int spd = bfs.searchWioutST(s, t, graph);
				if(spd>spd_max)continue;
			}
			psc++;
			//System.out.println("continue positive");
		}
		//System.out.println(s+" "+t+" "+katz(s,t)+" "+psc);
		
		st[2] = s;
		st[3] = t;
		
		return st;
		
	}
	}
