package node_ranking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import algo.enhanced_motif_path;
import algo.motif_path;
import tool.Datasets;
import tool.motif;

public class Node_ranking {
	
	public void run(Datasets dts, int[] pset, enhanced_motif_path emp, int defragID) throws IOException {
		//calculate motif-path components and betweenness centrality
		int[]components=new int[dts.g.nodeNum+1];
		int cComponentID = 0;
		
		int[]betweenness = new int[dts.g.nodeNum+1];
		int[]betweennessID = new int[dts.g.nodeNum+1];
		int max = Integer.MAX_VALUE;
		
		//get gt ranking list
		BufferedReader a = new BufferedReader(new FileReader(dts.d.dataPath+".nodes"));

		int[]hIndex = new int[dts.g.nodeNum+1], hIndexID = new int[dts.g.nodeNum+1];
		
		int diameter = 0;
		
		for(int i=1;i<=dts.g.nodeNum;i++) {
			if(dts.d.id>=60&&dts.d.id<70)
				hIndex[i]=Integer.parseInt(a.readLine().split(" ")[2]);
			hIndexID[i] = i;
			betweennessID[i] = i;
			if(i%(dts.g.nodeNum/100)==0)
				System.out.print(i/(dts.g.nodeNum/100)+"->");
			if(components[i]==0) {
				cComponentID++;
				components[i] = cComponentID;
			}
			
			
			motif[]touched = null;
			int[]jump = null;
			emp.motif_path_enhanced(dts.g, pset, "", i, Integer.MAX_VALUE);
			touched = emp.touched;
			jump = emp.jump;
			
			for(int j=1;j<=dts.g.nodeNum;j++) {
				if(i==j) {
					continue;
				}
				int smpd_ij = jump[j];
				if(smpd_ij>=0&&smpd_ij<max) {
					if(smpd_ij>diameter)
						diameter = smpd_ij;
					if(components[j]==0)
						components[j] = components[i];
					ArrayList<Integer>coveredNodes = new ArrayList();
					motif cmotif = touched[j];
					for(int t=0;t<cmotif.nodes.size();t++)
						if(cmotif.nodes.get(t)!=i&&cmotif.nodes.get(t)!=j&&!coveredNodes.contains(cmotif.nodes.get(t)))
							coveredNodes.add(cmotif.nodes.get(t));
					while(cmotif.last!=null) {
						cmotif = cmotif.last;
						for(int t=0;t<cmotif.nodes.size();t++)
							if(cmotif.nodes.get(t)!=i&&cmotif.nodes.get(t)!=j&&!coveredNodes.contains(cmotif.nodes.get(t)))
								coveredNodes.add(cmotif.nodes.get(t));
					}
					
					for(int t=0;t<coveredNodes.size();t++) {
						betweenness[coveredNodes.get(t)]++;
					}
				}
				//System.out.println();
			}
		}
		
		System.out.println("Diameter of d"+dts.d.id+": "+diameter);
		
		for(int i=1;i<dts.g.nodeNum;i++) {
			for(int j=i+1;j<=dts.g.nodeNum;j++) {
				if(betweenness[i]<betweenness[j]) {
					int tem = betweenness[i];
					betweenness[i] = betweenness[j];
					betweenness[j] = tem;
					tem = betweennessID[i];
					betweennessID[i] = betweennessID[j];
					betweennessID[j] = tem;
				}
				if(hIndex[i]<hIndex[j]) {
					int tem2 = hIndex[i];
					hIndex[i] = hIndex[j];
					hIndex[j] = tem2;
					int tem = hIndexID[i];
					hIndexID[i] = hIndexID[j];
					hIndexID[j] = tem;
				}
			}
		}
		
		String compo = "", betwe = "", betweID = "", hin="", hinID="";
		compo+=("Components("+cComponentID)+"):";
		//betwe+="Betweenness:\t";
		betweID+="BetweennessID:";
		hin+="hIndex:";
		hinID+="hIndexID:";
		String writeDir = dts.d.dataPath;
		if(pset[0]==1)
			writeDir += ".rank";
		else {
			writeDir += "_m"+pset[0];
			if(defragID==0)
				writeDir += ".rank";
			else
				writeDir += "_d"+defragID+".rank";
			
		}
		BufferedWriter b = new BufferedWriter(new FileWriter(writeDir));
		betwe += ("Betweenness:\t");
		for(int i=1;i<=dts.g.nodeNum;i++) {
			compo+=(components[i]+",");
			//betwe += betweenness[i]+",";
			if(hIndex[i]!=-1) {
				hin += hIndex[i]+",";
				hinID += hIndexID[i]+",";
				}
			if(hIndex[betweennessID[i]]!=-1) {
				betwe += (betweenness[i]+",");
				betweID += betweennessID[i]+",";
				}
		}
		compo+="\n";
		betweID+="\n";
		//betwe+="\n";
		betwe+=("\n");
		hinID+="\n";
		hin+="\n";
		
		b.write(compo+hinID+hin+betweID+betwe);
		b.flush();
		b.close();
	}

}
