import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import algo.enhanced_motif_path;
import link_prediction.Graph_distance;
import link_prediction.Katz_index;
import link_prediction.Motif_common_neighbor;
import link_prediction.QueryGen;
import link_prediction.Traditional_lp;
import link_prediction.arxiv19;
import link_prediction.path_deal;
import tool.Dataset;
import tool.Datasets;
import tool.Interact;
import tool.graphReady;

public class Linkp {

	public static void main(String[] args) throws IOException {
		
		String mainDir = "data/ppi/gavin";
		int pid = 5, defragID = 0, lid = 0, cid = 0, sid = 0, tid = 0, queryNum = 5000;
		int tradition_metric_id = 0, tmetricID = 0;//cn, jc, pa, aa, fm
		char metricID = 't';
		Interact nt = new Interact();
		for(int i=0;i<args.length;i++) {
			String[]tem=args[i].split(":");
			char temc = tem[0].charAt(1);
			switch(temc) {
			case 'g': mainDir = args[i].substring(3); break;
			case 'm': pid = nt.intwarn(tem[1], pid, 'm'); break;
			case 'd': defragID = nt.intwarn(tem[1], defragID, 'd'); break;
			case 'l': lid = 1; break; //motif feature vector metric
			case 'n': queryNum = nt.intwarn(tem[1], queryNum, 'n'); break;
			case 'c': cid = 1; break; //motif common neighbor metric 
			case 'q': sid = 1; break;
			case 't': tid = nt.intwarn(tem[1], tid, 't'); metricID = 't'; break; //path-based metric
			case 'r': tid = 0; metricID = 'r'; break; //rooted pagerank metric
			case 'h': tid = 0; metricID = 'h'; break; //hitting time metric
			case 'o': tradition_metric_id = 1; tmetricID = 0; break; // cn
			case 'j': tradition_metric_id = 1; tmetricID = 1; break; // jc
			case 'a': tradition_metric_id = 1; tmetricID = 2; break; // aa
			case 'p': tradition_metric_id = 1; tmetricID = 3; break; // pa
			case 'f': tradition_metric_id = 1; tmetricID = 4; break; // fm
			}
		}
		
		graphReady g = new graphReady();
		Datasets dts = new Datasets();
		
		enhanced_motif_path emp = new enhanced_motif_path();
		if(defragID==0||defragID==1) {
			g.readGraph(mainDir, ",", 0, 1, 2, 1, false);
			Dataset d = new Dataset (mainDir, ",", 1);
			d.g = g;
			dts.d = d;
			dts.g = g;
			if(defragID==1)
				emp.readInjectedEdges(dts, pid);
		}
		if(defragID==2) {
			//emp.readInjectedEdgesEdmot(dts);
			g.readGraph(mainDir+".edmot", ",", 0, 1, 2, 1, false);
			Dataset d = new Dataset (mainDir, ",", 1);
			d.g = g;
			dts.d = d;
			dts.g = g;
			if(pid!=5) {
				System.out.println("Edmot only supports triangles (-m:5)!");			
				return;
			}	
		}
		
		if(tradition_metric_id==1) {
			new Traditional_lp().run(dts, queryNum, tmetricID);;
			return;
		}
		
		
		if(sid==1) {
			//generate the queries for link predication: x.linkp
			if(pid!=1) {
				System.out.println("Please use -m:1 to generate the queries!");
				return;
			}
			QueryGen qg = new QueryGen();
			qg.LinkPreQWrite_sameDistribution(queryNum, dts);
			System.out.println("Queries generated to "+dts.d.dataPath+".linkp");
			return;
		}
		if(lid==1) {
			//calculate feature vectors based on arxiv19: x.mlp
			arxiv19 a = new arxiv19();
			a.writeVec(queryNum, dts);
			return;
			}
		if(cid==1) {
			//calculate MCN
			new Motif_common_neighbor().MCN(dts,pid,queryNum);
			return;
			}
		if(tid==0) {
			//Graph Distance: integrate hitting time, rooted pagerank
			new Graph_distance().run(dts, pid, queryNum, defragID, emp, metricID);
			return;
		}else {
			//Katz Index
			new Katz_index().run(dts, pid, queryNum, defragID, emp);
			System.out.println("Logging finished, start running AUC analysis.");
			String[]tem = mainDir.split("/");
			double[]re = new path_deal(30).run(mainDir + "_path_counts_posi_m"+pid+"_d"+defragID, mainDir + "_path_counts_nega_m"+pid+"_d"+defragID, 0.01, 4, tem[tem.length-1], pid+"");
			System.out.println("AUC score: "+re[0]);
			return;
		}
		
		
		
		
	}

	

}
