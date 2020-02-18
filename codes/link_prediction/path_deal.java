package link_prediction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class path_deal {
	
	public path_deal(int n){
		barCMin = n;
	}

	DecimalFormat fmt = new DecimalFormat("##0.0000");
	
	void calcMGDMultiP(int noe) throws IOException {
		int[]pset = {5,22,30,10,12}; 
		int[]dset = {32};//{30,31,32,34,35};
		
		//for single p

		//for single p
		for(int did=0;did<dset.length;did++){
		int d=dset[did];
		System.out.print("d"+d+"\t");
		for(int pid=0;pid<pset.length;pid++){
			int p = pset[pid];
			int[]pt=new int[1];
			pt[0] = p;
		double[]res = calcMGD(d,pt,1,noe);
		double auc=res[0];
		double acc=res[1];
		System.out.print(auc+"\t"+acc+"\t");
		}
		System.out.println();
		}

		
		for(int did=0;did<dset.length;did++) {
			int d = dset[did];
			System.out.print("d"+d+"\t");
			for(int i=0;i<pset.length-1;i++) {
				for(int j=i+1;j<pset.length;j++) {
					//String s = "p"+pset[i]+","+pset[j];
					int[]psets = new int[2];
					psets[0] = pset[i];
					psets[1] = pset[j];
					double[]res = calcMGD(d,psets,1,noe);
					System.out.print(res[0]+"\t"+res[1]+"\t");
				}
			}
			System.out.println();
		}
		
		for(int did=0;did<dset.length;did++) {
			int d = dset[did];
			System.out.print("d"+d+"\t");	
			for(int i=0;i<pset.length-2;i++) {
				for(int j=i+1;j<pset.length-1;j++) {
					for(int k=j+1;k<pset.length;k++) {
						//String s = "p"+pset[i]+","+pset[j]+","+pset[k];
						int[]psets = new int[3];
						psets[0] = pset[i];
						psets[1] = pset[j];
						psets[2] = pset[k];
						double[]res = calcMGD(d,psets,1,noe);
						System.out.print(res[0]+"\t"+res[1]+"\t");
					}
				}
			}
			System.out.println();
			}
		for(int did=0;did<dset.length;did++) {
			int d = dset[did];
			System.out.print("d"+d+"\t");		
			for(int i=0;i<pset.length-3;i++) {
				for(int j=i+1;j<pset.length-2;j++) {
					for(int k=j+1;k<pset.length-1;k++) {
						for(int p=k+1;p<pset.length;p++) {
							//String s = "p"+pset[i]+","+pset[j]+","+pset[k]+","+pset[p];
							int[]psets = new int[4];
							psets[0] = pset[i];
							psets[1] = pset[j];
							psets[2] = pset[k];
							psets[3] = pset[p];
							double[]res = calcMGD(d,psets,1,noe);
							System.out.print(res[0]+"\t"+res[1]+"\t");
						}
					}
				}
			}
			System.out.println();
			}
		for(int did=0;did<dset.length;did++) {
			int d = dset[did];
			System.out.print("d"+d+"\t");		
			//String s = "p"+pset[0]+","+pset[1]+","+pset[2]+","+pset[3]+","+pset[4];
			double[]res = calcMGD(d,pset,1,noe);
			System.out.println(res[0]+"\t"+res[1]+"\t");
		}
	}
	
	double[] calcMGD(int d, int pset[], int delta, int noe) throws IOException{
		double re[] = null;
		//int pset[] = {5,22,30,10,12};
			if(pset.length==1) {
				int p = pset[0];
				String mainDir = "C:/Users/Sheldon/Downloads/New folder/link_pre/mgd/";
				re = runmgd(mainDir + "mlog_d"+d+"_p"+p+"_l4_delta"+delta+"_noe"+noe,d+"",p+"");
				//System.out.println("d"+d+"p"+p+"\t"+re[0]+"\t"+re[1]);
			}
			if(pset.length>1) {
				String mainDir = "C:/Users/Sheldon/Downloads/New folder/link_pre/mgd/";
				String str = "";
				for(int j=0;j<pset.length;j++) {
					str += pset[j]+"_";
				}
				String s = mainDir + "mlog_d"+d+"_p"+str+"l4_delta"+delta+"_noe"+noe;
				
				re = runmgd(s,d+"",str);
				//System.out.println("d"+d+"p"+str+"\t"+re[0]+"\t"+re[1]);
			}
			
			return re;
	}
	
	void calcMCN(int[]pset) throws IOException{
		int[]dset = {29,30,31,32,28,34,35};
		//int pset[] = {5};
		for(int i=0;i<dset.length;i++) {
			if(pset.length==1) {
				int p = pset[0];
				String mainDir = "C:/Users/Sheldon/Downloads/New folder/link_pre/motif-data2/";
				double[]re = run(mainDir + "path_counts_posi_d"+dset[i]+"_p"+p+"_l4_delta1_noe1", mainDir + "path_counts_nega_d"+dset[i]+"_p"+p+"_l4_delta1_noe1", 1, 2,dset[i]+"",p+"");
				System.out.println("d"+dset[i]+"p"+p+"\t"+re[0]+"\t"+re[1]);
			}
			if(pset.length>1) {
				String mainDir = "C:/Users/Sheldon/Downloads/New folder/link_pre/multi_p_noe1_2/";
				String str = "";
				for(int j=0;j<pset.length;j++) {
					str += pset[j]+"_";
				}
				String sPosi = mainDir + "path_counts_posi_d"+dset[i]+"_p"+str+"l4_delta1_noe1";
				String sNega = mainDir + "path_counts_nega_d"+dset[i]+"_p"+str+"l4_delta1_noe1";
				
				double[]re = run(sPosi,sNega, 1, 2,dset[i]+"",str);
				System.out.println("d"+dset[i]+"p"+str+"\t"+re[0]+"\t"+re[1]);
			}
		}
	}

	int barCMin = 0;
	private void calcMultiP(int noe, String mainDir) throws IOException {
		int[]pset = {5,22,30,10,12}; 
		int[]dset = {34};
		barCMin = 30;
		
		//for single p
		for(int did=0;did<dset.length;did++){
		int d=dset[did];
		System.out.print("d"+d+"\t");
		for(int pid=0;pid<pset.length;pid++){
			int p = pset[pid];
		double[]res = run(d,p,0.1,4,2, mainDir,"");
		double auc=res[0];
		double acc=res[1];
		System.out.print(auc+"\t"+acc+"\t");
		}
		System.out.println();
		}
		
		for(int did=0;did<dset.length;did++) {
			int d = dset[did];
			System.out.print("d"+d+"\t");
			for(int i=0;i<pset.length-1;i++) {
				for(int j=i+1;j<pset.length;j++) {
					//String s = "p"+pset[i]+","+pset[j];
					int[]psets = new int[2];
					psets[0] = pset[i];
					psets[1] = pset[j];
					double[]res = run(d,psets,noe, mainDir);
					System.out.print(res[0]+"\t"+res[1]+"\t");
				}
			}
			System.out.println();
		}
		
		for(int did=0;did<dset.length;did++) {
			int d = dset[did];
			System.out.print("d"+d+"\t");	
			for(int i=0;i<pset.length-2;i++) {
				for(int j=i+1;j<pset.length-1;j++) {
					for(int k=j+1;k<pset.length;k++) {
						//String s = "p"+pset[i]+","+pset[j]+","+pset[k];
						int[]psets = new int[3];
						psets[0] = pset[i];
						psets[1] = pset[j];
						psets[2] = pset[k];
						double[]res = run(d,psets,noe, mainDir);
						System.out.print(res[0]+"\t"+res[1]+"\t");
					}
				}
			}
			System.out.println();
			}
		for(int did=0;did<dset.length;did++) {
			int d = dset[did];
			System.out.print("d"+d+"\t");		
			for(int i=0;i<pset.length-3;i++) {
				for(int j=i+1;j<pset.length-2;j++) {
					for(int k=j+1;k<pset.length-1;k++) {
						for(int p=k+1;p<pset.length;p++) {
							//String s = "p"+pset[i]+","+pset[j]+","+pset[k]+","+pset[p];
							int[]psets = new int[4];
							psets[0] = pset[i];
							psets[1] = pset[j];
							psets[2] = pset[k];
							psets[3] = pset[p];
							double[]res = run(d,psets,noe, mainDir);
							System.out.print(res[0]+"\t"+res[1]+"\t");
						}
					}
				}
			}
			System.out.println();
			}
		for(int did=0;did<dset.length;did++) {
			int d = dset[did];
			System.out.print("d"+d+"\t");		
			//String s = "p"+pset[0]+","+pset[1]+","+pset[2]+","+pset[3]+","+pset[4];
			double[]res = run(d,pset,noe, mainDir);
			System.out.println(res[0]+"\t"+res[1]+"\t");
		}
		
	}
	private void calcMultiL(int[] dset, int p) throws IOException {
		// TODO Auto-generated method stub
		for(int i=0;i<dset.length;i++) {
			System.out.println("d"+dset[i]);
			String mainDir = "C:/Users/Sheldon/Downloads/New folder/link_pre/motif-data2/";
			double[]re = run(mainDir + "path_counts_posi_d"+dset[i]+"_p"+p+"_l4_delta1_noe1", mainDir + "path_counts_nega_d"+dset[i]+"_p"+p+"_l4_delta1_noe1", 0.1, 4, dset[i]+"",p+"");
			System.out.println("delta1\tnoe1\t"+re[0]+"\t"+re[1]);
			mainDir = "C:/Users/Sheldon/Downloads/New folder/link_pre/motif-data2/";
			for(int noe = 1;noe<3;noe++) {
				for(int delta = 1;delta<5;delta++) {
					if(delta==1&&noe==1)continue;
						String sPosi = mainDir + "path_counts_posi_d"+dset[i]+"_p"+p+"_l4_delta"+delta+"_noe"+noe;
						String sNega = mainDir + "path_counts_nega_d"+dset[i]+"_p"+p+"_l4_delta"+delta+"_noe"+noe;
						double[]res = run(sPosi,sNega, 0.1, 4, dset[i]+"",p+"");
						System.out.println("delta"+delta+"\tnoe"+noe+"\t"+res[0]+"\t"+res[1]);
						}
				}
			System.out.println();
		}
	}
	
	private void calcMGDMultiL(int[] dset, int p) throws IOException {
		// TODO Auto-generated method stub
		for(int i=0;i<dset.length;i++) {
			System.out.println("d"+dset[i]);
			String mainDir = "C:/Users/Sheldon/Downloads/New folder/link_pre/mgd/";
			double[]re = runmgd(mainDir + "mlog_d"+dset[i]+"_p"+p+"_l4_delta1_noe1",dset[i]+"",p+"");
			System.out.println("delta1\tnoe1\t"+re[0]+"\t"+re[1]);
			for(int noe = 1;noe<3;noe++) {
				for(int delta = 1;delta<5;delta++) {
					if(delta==1&&noe==1)continue;
						String s = mainDir + "mlog_d"+dset[i]+"_p"+p+"_l4_delta"+delta+"_noe"+noe;
						double[]res = runmgd(s, dset[i]+"",p+"");
						System.out.println("delta"+delta+"\tnoe"+noe+"\t"+res[0]+"\t"+res[1]);
						}
				}
			System.out.println();
		}
	}

	private void calcEpsilon(int d, int p, double epsilon, int l, int noe, int[] pset, int[] dset) throws IOException {
		// TODO Auto-generated method stub
		noe = 2;//1;
		for(int i=0;i<dset.length;i++) {
			d = dset[i];
			System.out.print("d"+d+"noe"+noe+"=[");
			for(int j=0;j<pset.length;j++) {
				p = pset[j];
				//calcEpsilon(d,p,epsilon,4,noe);
			}
			System.out.println("];");
		}
		
		//2nd step
		for(noe=1;noe<3;noe++) 
		for(int i=0;i<dset.length;i++) {
			String s = "data("+noe+",";
			d = dset[i];
			s+=(i+1)+",:,:)=d"+d+"noe"+noe+";";
			System.out.println(s);
		}
		
	}
	
	private void runLP(int d, double epsilon, String mainDir) throws IOException {
		//String mainDir = "C:/Users/Sheldon/Downloads/New folder/link_pre/motif-data2/";
		String sOriginalTriangle_posi = mainDir + "exp_results/original/path_counts_posi_d"+d+"_p"+5+"_l4_delta1_noe1";
		//String sOriginalTriangle_nega = mainDir + mainDir + "exp_results/original/path_counts_nega_d"+d+"_p"+5+"_l4_delta1_noe1";
		
		String sOriginalEdge_posi = mainDir + "exp_results/original/path_counts_posi_d"+d+"_p"+1+"_l4_delta1_noe1";
		//String sOriginalEdge_nega = mainDir + "exp_results/original/path_counts_naga_d"+d+"_p"+1+"_l4_delta1_noe1";
		
		String sEnhancedTriangle_posi = mainDir + "exp_results/enhanced/path_counts_posi_d"+d+"_p"+5+"_l4_delta1_noe1";
		//String sEnhancedTriangle_nega = mainDir + "exp_results/enhanced/path_counts_nega_d"+d+"_p"+5+"_l4_delta1_noe1";
		
		String[]files = new String [3];
		files[0] = sOriginalEdge_posi;
		files[1] = sOriginalTriangle_posi;
		files[2] = sEnhancedTriangle_posi;
		
		DecimalFormat df = new DecimalFormat("0.00");
		String auc = "", acc = "";
		for(int i=0;i<3;i++) {
			double[]res = run(files[i],files[i].replaceAll("posi", "nega"), epsilon, 4, d+"", "");
			auc += df.format(res[0])+" ";
			acc += df.format(res[1])+" ";
		}
		System.out.println(auc+" - "+acc);
		
	}

	private void calcEpsilon(int d, int p, double epsilon, int l, int noe, String mainDir) throws IOException {
		// TODO Auto-generated method stub
		String auc="", acc="";
		DecimalFormat df = new DecimalFormat("0.00");
		//for(epsilon=0.00001;epsilon<=1;epsilon*=10) {
			double[]res = run(d,p,0.1,l,noe, mainDir,"");
			auc+=df.format(res[0])+" ";
			//acc+=res[1]+" ";
		//}

		System.out.println(auc);//+"\n"+acc);
	}

	private void calcTopK(int d, int p, double epsilon, int l, int noe, int[] pset, int[] dset, String mainDir) throws IOException {
		for(int did=0;did<dset.length;did++) {
			
			d = dset[did];
			
			int k = 3;
			ArrayList<double[]>max=new ArrayList(), max2=new ArrayList();
			//int dmax=0, pmax=0, lmax=0, noemax=0; double epmax=0, res0max=0, res1max=0;
			for(int i=0;i<k;i++) {max.add(new double[7]);max2.add(new double[7]);}
		
		for(noe=1;noe<3;noe++) {
			
		for(epsilon=0.00001;epsilon<=1;epsilon*=10) {
			
		for(l=4;l<=4;l++) {	
			
		for(int pid=0;pid<pset.length;pid++) {
			p = pset[pid];
			double[]res = run(d,p,epsilon,l,noe, mainDir,"");
			//System.out.println("d"+d+"_p"+p+"_eps"+epsilon+"_l"+l+"_noe"+noe+"\tauc: "+res[0]+"\tacc: "+res[1]);
			
			if(p==1&&noe==1) {
				System.out.println(res[0]+" "+res[1]);
				//System.out.println("d"+add(d)+"_p"+add(p)+"_eps"+fmt.format(epsilon)+"_l"+l+"_noe"+noe+"\tauc: "+fmt.format(res[0])+"\tacc: "+fmt.format(res[1]));
				}
			else {
				if(res[0]>max.get(k-1)[5])
					//update knn
					for(int i=0;i<k;i++) {
						if(res[0]>max.get(i)[5]) {
							double[]plug=new double[7];
							plug[0]=d;plug[1]=p;plug[4]=epsilon;plug[2]=l;plug[3]=noe;plug[5]=res[0];plug[6]=res[1];
							max.add(i, plug); max.remove(k);
							break;
						}
					}
				if(res[1]>max2.get(k-1)[6])
					//update knn
					for(int i=0;i<k;i++) {
						if(res[1]>max2.get(i)[6]) {
							double[]plug=new double[7];
							plug[0]=d;plug[1]=p;plug[4]=epsilon;plug[2]=l;plug[3]=noe;plug[5]=res[0];plug[6]=res[1];
							max2.add(i, plug); max2.remove(k);
							break;
						}
					}
			}
		}
		
		}
		}
		
		for(int i=0;i<k;i++) 
			knnout(max.get(i));
		for(int i=0;i<k;i++) 
			knnout(max2.get(i));
		

		System.out.println();
		
		}
		
		}
	}

	private void knnout(double[]arr) {
		// TODO Auto-generated method stub
		int dmax = (int)arr[0], pmax = (int)arr[1], lmax= (int)arr[2], noemax= (int)arr[3]; 
		double epmax=arr[4], res0max=arr[5], res1max=arr[6];
		
		System.out.println("d"+add(dmax)+"_p"+add(pmax)+"_eps"+fmt.format(epmax)+"_l"+lmax+"_noe"+noemax+"\tauc: "+fmt.format(res0max)+"\tacc: "+fmt.format(res1max));
		
	}

	private String add(int x) {
		// TODO Auto-generated method stub
		if(x<10)
			return "0"+x;
		return x+"";
	}

	public double[] run(int d, int p, double epsilon, int l, int noe, String mainDir, String suffix) throws IOException {
		//String mainDir = "C:/Users/Sheldon/Downloads/New folder/link_pre/motif-data2/";
		String sPosi = mainDir + "path_counts_posi_d"+d+"_p"+p+"_l4_delta1_noe"+noe+suffix;
		String sNega = mainDir + "path_counts_nega_d"+d+"_p"+p+"_l4_delta1_noe"+noe+suffix;
		
		return run(sPosi,sNega, epsilon, l, d+"",p+"");
	}
	
	private void runLGC_k(int d, int gt, String mainDir, int k) throws IOException {
		// TODO Auto-generated method stub
		String sOriginalEdge = mainDir + "exp_results/original/log_d"+d+"_p1_delta1_noe1_gt"+gt;
		String sOriginalTriangle = mainDir + "exp_results/original/log_d"+d+"_p5_delta1_noe1_gt"+gt;
		String sEnhancedTriangle = mainDir + "exp_results/enhanced/log_d"+d+"_p5_delta1_noe1_gt"+gt;
		
		String[]files = new String [3];
		files[0] = sOriginalEdge;
		files[1] = sOriginalTriangle;
		files[2] = sEnhancedTriangle;
		
		
		for(int i=0;i<3;i++) {
			BufferedReader a = new BufferedReader(new FileReader(files[i]));
			a.readLine();a.readLine();a.readLine();
			for(int j=0;j<k-1;j++) 
				a.readLine();
			
			String[]tem = a.readLine().split("	");
			double pre = Double.parseDouble(tem[0]);
			double rec = Double.parseDouble(tem[1]);
			double f1 = pre*rec*2/(pre+rec);	
			System.out.println(f1);
		}

	}
	
	private void runLGC(int d, int gt, String mainDir) throws IOException {
		//String mainDir = "C:/Users/Sheldon/Downloads/New folder/link_pre/motif-data2/";
		String sOriginalTriangle = mainDir + "exp_results/original/log_d"+d+"_p5_delta1_noe1_gt"+gt;
		String sOriginalEdge = mainDir + "exp_results/original/log_d"+d+"_p1_delta1_noe1_gt"+gt;
		String sEnhancedTriangle = mainDir + "exp_results/enhanced/log_d"+d+"_p5_delta1_noe1_gt"+gt;
		
		String[]files = new String [3];
		files[0] = sOriginalEdge;
		files[1] = sOriginalTriangle;
		files[2] = sEnhancedTriangle;
		
		String preLine = "", recLine = "", f1Line = "";
		for(int i=0;i<3;i++) {
			BufferedReader a = new BufferedReader(new FileReader(files[i]));
			a.readLine();a.readLine();a.readLine();
			for(int j=0;j<100;j++) {
				String[]tem = a.readLine().split("	");
				double pre = Double.parseDouble(tem[0]);
				double rec = Double.parseDouble(tem[1]);
				double f1 = pre*rec*2/(pre+rec);
				preLine += pre+" ";
				recLine += rec+" ";
				f1Line += f1+" ";
			}
			preLine += "\n";
			recLine += "\n";
			f1Line += "\n";
		}
		System.out.println(preLine+""+recLine+""+f1Line);
		
	}
	
	private double[] run(int d, int[]psets, int noe, String mainDir) throws IOException {
		//String mainDir = "C:/Users/Sheldon/Downloads/New folder/link_pre/multi_p_noe"+noe+"_2/";
		String str = "";
		for(int i=0;i<psets.length;i++) {
			str += psets[i]+"_";
		}
		String sPosi = mainDir + "path_counts_posi_d"+d+"_p"+str+"l4_delta1_noe"+noe;
		String sNega = mainDir + "path_counts_nega_d"+d+"_p"+str+"l4_delta1_noe"+noe;
		
		return run(sPosi,sNega, 0.1, 4, d+"",str);
	}
	
	public double[] run(String sPosi, String sNega, double epsilon, int l, String dname, String pname) throws IOException {
		BufferedReader a = null;
		
		ArrayList<int[]>resp = getData(a,sPosi);
		ArrayList<int[]>resn = getData(a,sNega);
		
		int cmin=Math.min(resp.size(), resn.size());
		if(cmin<barCMin) {
			//String dname = sNega.split("nega_d")[1].split("_p")[0];
			//String pname = sNega.split("_pre")[1];
			//pname = pname.split("_p")[1];
			//pname = pname.split("_l")[0];
			System.out.println("_MORE-d"+dname+"p"+pname+"-num"+cmin+"_");
		}
		
		//double[]cset = getC (resp, resn, epsilon, l);
		double[]cset = getC2 (resp, resn, epsilon, l);
		
		double auc = 0, acc = 0;
		double[]res = new double[2];
		
		double c = cset[0], c1 = cset[1], c2 = cset[2];
		if(c==0) return res;
		
		auc = ((double)c1*2+c2)/2/c;
		acc = (double)c1/c;
		res[0] = auc;
		res[1] = acc;
		
		return res;
	}
	
	private double[] runmgd(String s, String dname, String pname) throws IOException {
		s+=".out";
		BufferedReader a = null;
		try {a = new BufferedReader(new FileReader(s));
		}catch(Exception e) {
			double[]res = new double[2];
			return res;
		}
		double auc = 0, acc = 0;
		int counter = 0;
		String str = a.readLine();
		for(int i=0;i<101;i++) {
			if(str==null||str.equals(""))break;
			if(i==0) {str = a.readLine();continue;}
			try {
			String tem[] = str.split("\t");
			auc = Double.parseDouble(tem[1]);
			acc = Double.parseDouble(tem[2]);
			}catch(Exception e) {
				str = a.readLine();
				continue;
			}
			counter = i+1;
			str = a.readLine();
		}

		if(counter<barCMin) {
			System.out.print("_MORE-d"+dname+"p"+pname+"-num"+counter+"_");
		}
		
		double[]res = new double[2];
		res[0] = auc;
		res[1] = acc;
		
		return res;
	}

	private double[] getC(ArrayList<int[]> resp, ArrayList<int[]> resn, double epsilon, int l) {
		int c=resp.size()*resn.size(), c1=0, c2=0;
		
		for(int i=0;i<resp.size();i++) {
			int[]aresp = resp.get(i);
			double scorep = getKatz(aresp, epsilon, l);

			for(int j=0;j<resn.size();j++){
				int[]aresn = resn.get(j);
				double scoren = getKatz(aresn, epsilon, l);
				if(scorep==scoren) {
					c2++;
				}else if(scorep>scoren){
					c1++;
				}
			}
		}
		
		double[]res= new double[3];
		res[0] = c;
		res[1] = c1;
		res[2] = c2;
		
		return res;
	}
	
	private double[] getC2(ArrayList<int[]> resp, ArrayList<int[]> resn, double epsilon, int l) {
		int c=Math.min(resp.size(), resn.size()), c1=0, c2=0;
		
		for(int i=0;i<c;i++) {
			int[]aresp = resp.get(i);
			double scorep = getKatz(aresp, epsilon, l);
			
			int[]aresn = resn.get(i);
			double scoren = getKatz(aresn, epsilon, l);
			
			if(scorep==scoren) {
				c2++;
			}else if(scorep>scoren){
				c1++;
			}
			
		}
		
		double[]res= new double[3];
		res[0] = c;
		res[1] = c1;
		res[2] = c2;
		
		return res;
	}

	private double getKatz(int[] aresp, double epsilon, int l) {
		double scorep = 0;
		for(int t=0;t<l;t++) 
			scorep += (Math.pow(epsilon, t))*aresp[t];
		return scorep;
	}

	private ArrayList<int[]> getData (BufferedReader a, String s) throws IOException {
		String str = "";
		ArrayList<int[]>res = new ArrayList();
		try {
			a= new BufferedReader(new FileReader(s));
			str = a.readLine();
			}catch(Exception e) {
				//System.out.println("File not found: "+s);
				//a.close();
				return res;
			}
		
		while(str!=null&&!str.equals("")) {
			String[]tem = str.split(" ");
			int[]line = new int[tem.length];
			for(int i=0;i<tem.length;i++) {
				line[i]=Integer.parseInt(tem[i]);
			}
			res.add(line);
			str = a.readLine();
		}
		a.close();
		return res;
		
	}

}

class test {
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		path_deal pd = new path_deal(30);
		
		int[]pset = {5};//{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
		int[]dset = {30,31,32,34,35};//{29,30,34,35};//29,30,31,32,34,35};//,36,28,1,37};
		
		int d = -1;
		int p = -1;
		double epsilon = 0.1;
		int l =   4;
		int noe = 1;
		//calcTopK(d,p,epsilon,l,noe,pset,dset);
		
		String mainDir = "C:/Users/Sheldon/Google ÔÆ¶ËÓ²ÅÌ£¨xdli@connect.hku.hk£©/DESK/documents/researchWorks/motif-path/motif-data/";
		int gt = 1;
		for(int i=0;i<dset.length;i++) {
			//calcEpsilon(dset[i], 5, 0.01, 4, 1, "C:/Users/Sheldon/Google ÔÆ¶ËÓ²ÅÌ£¨xdli@connect.hku.hk£©/DESK/documents/researchWorks/motif-path/motif-data/exp_results/enhanced/");
			//runLGC(dset[i],gt,mainDir);
			//runLGC_k(dset[i],gt,mainDir,20);
			//runLP(dset[i],0.01,mainDir);
		}
		
			//calcEpsilon(d,p,epsilon,l,noe,pset,dset);

		//calcMultiP(1);
		//calcMultiL(dset,21);
		
		//calcMGDMultiP(1);
		//System.out.println();
		//calcMGDMultiL(dset,21);
		
		int[]psets= {5};
		pd.calcMCN(psets);
		DecimalFormat df = new DecimalFormat("0.00");

		for(int i=0;i<dset.length;i++) {
			for(int j=0;j<pset.length;j++) {
			//System.out.print("d"+dset[i]+"p"+pset[j]+"\t");
			mainDir = "C:/Users/Sheldon/Downloads/New folder/link_pre/motif-data2/";
			//double[]re = run(mainDir + "path_counts_posi_d"+dset[i]+"_p"+pset[j]+"_l4_delta1_noe1", mainDir + "path_counts_nega_d"+dset[i]+"_p"+pset[j]+"_l4_delta1_noe1", 0.1, 4, dset[i]+"",pset[j]+"");
			//System.out.println("delta1\tnoe1\t"+df.format(re[0])+"\t"+df.format(re[1]));
			if(pset[j]==1)continue;
			//System.out.print("d"+dset[i]+"p"+pset[j]+"\t");
			//re = run(mainDir + "path_counts_posi_d"+dset[i]+"_p"+pset[j]+"_l4_delta1_noe2", mainDir + "path_counts_nega_d"+dset[i]+"_p"+pset[j]+"_l4_delta1_noe2", 0.1, 4, dset[i]+"",pset[j]+"");
			//System.out.println("delta1\tnoe2\t"+df.format(re[0])+"\t"+df.format(re[1]));
		}}

	}
}
