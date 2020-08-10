package algo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import tool.Datasets;

public class Cache {

	static boolean cacheSig = false;
	static boolean cacheSig2 = false;
	static boolean cacheSig3 = false;
	static int counter = 0, counter1 = 0, counter2 = 0, counter3 = 0;
	public static void main(String[] args) throws IOException {
		String mainDir = "./";
		Datasets dts = new Datasets(mainDir); 
		dts.Initialize(32);
		// change the dataset here
		long t1 = System.currentTimeMillis();
		
		String str = dts.d.dataPath;
		if(!cacheSig&&!cacheSig2&&!cacheSig3)
			str += ".cache";
		if(!cacheSig&&cacheSig2&&!cacheSig3)
			str += "-2.cache";
		if(!cacheSig&&cacheSig2&&cacheSig3)
			str += "-3.cache";
		BufferedWriter b = new BufferedWriter (new FileWriter(str));
		for(int i=1;i<dts.g.graph.length;i++) {
		//System.out.println(dts.g.graph.length);for(int t=0;t<dts.g.graph.length/1000;t++){int i = (int)(Math.random()*dts.g.graph.length)+1;
			if(i%(dts.g.graph.length/100)==0) System.out.print((int)(i*100/dts.g.graph.length)+"->");
			counter = 0; counter1 = 0; counter2 = 0; counter3 = 0; 
			int cc0 =0, cc1 = 0;
			b.write("0,-1:"+i+"\n"); counter++;
			for(int j=0;j<dts.g.graph[i].size();j++) {
				int nei1 = dts.g.graph[i].get(j); counter1=counter;
				//if(nei1 <= i)continue; // nei1 > i
				//it is an edge
				b.write("1,0:"+nei1+"\n"); cc0 = counter; counter++;
				if(cacheSig2) {
					for(int t=0;t<dts.g.graph[nei1].size();t++) {
						int nei2 = dts.g.graph[nei1].get(t); 
						if(nei2!=i&&!dts.g.graph[nei2].contains(i)) {
							b.write("2,"+cc0+":"+nei2+"\n");  cc1=counter; counter++;
							if(cacheSig3) 
							for(int x=0;x<dts.g.graph[nei2].size();x++) {
								int nei3 = dts.g.graph[nei2].get(x);
								if(nei3==nei1||dts.g.graph[nei3].contains(i)||dts.g.graph[nei3].contains(nei1))continue;
								for(int y=x+1;y<dts.g.graph[nei2].size();y++) {
									int nei4 = dts.g.graph[nei2].get(y);  
									if(nei4==nei1||dts.g.graph[nei4].contains(i)||dts.g.graph[nei4].contains(nei1))continue;
									if(dts.g.graph[nei4].contains(nei3)) {
										b.write("14,"+cc1+":"+nei3+","+nei4+"\n"); counter++;
										}
								}
							}
							}
					}
					for(int x=0;x<dts.g.graph[nei1].size();x++) {
						int nei2 = dts.g.graph[nei1].get(x);  
						if(nei2==i||dts.g.graph[nei2].contains(i))continue;
						for(int y=x+1;y<dts.g.graph[nei1].size();y++) {
							int nei3 = dts.g.graph[nei1].get(y);  
							if(nei3==i||dts.g.graph[nei3].contains(i))continue;
							if(dts.g.graph[nei2].contains(nei3)) {
								b.write("9,"+cc0+":"+nei2+","+nei3+"\n"); cc1 = counter; counter++;
								if(cacheSig3)
									for(int t=0;t<dts.g.graph[nei2].size();t++) {
										int nei4 = dts.g.graph[nei2].get(t);
										if(nei4==nei1||dts.g.graph[nei4].contains(i)||dts.g.graph[nei4].contains(nei1))
											continue;
										if(dts.g.graph[nei4].contains(nei3)) {
											b.write("21,"+cc1+":"+nei4+"\n"); counter++;
											}
									}
							}
						}
					}
					
				}
				for(int k=j+1; k<dts.g.graph[i].size();k++) {
					int nei2 = dts.g.graph[i].get(k); counter2=counter;
					//if(nei2 <= i)continue; // nei2 > i
					if(dts.g.graph[nei1].contains(nei2)) {
						//it is a triangle
						//b.write("5:"+i+","+nei1+","+nei2+"\n");
						b.write("5,"+counter1+":"+nei2+"\n"); cc0 = counter; counter++;
						if(cacheSig2) {
							for(int t=0;t<dts.g.graph[nei1].size();t++) {
								int nei3 = dts.g.graph[nei1].get(t);  
								if(nei3!=i&&nei3!=nei2&&!dts.g.graph[nei3].contains(i)) {
									if(dts.g.graph[nei3].contains(nei2)) {
										b.write("16,"+cc0+":"+nei3+"\n"); cc1 = counter; counter++;
										if(cacheSig3)
											for(int x=0;x<dts.g.graph[nei3].size();x++) {
												int nei4 = dts.g.graph[nei3].get(x);
												if(nei4!=nei1&&nei4!=nei2&&!dts.g.graph[nei4].contains(nei1)&&!dts.g.graph[nei4].contains(nei2)&&!dts.g.graph[nei4].contains(i)) {
													b.write("21,"+cc1+":"+nei4+"\n"); counter++;
													}
											}
										}
									else {
										b.write("9,"+cc0+":"+nei3+"\n"); cc1 = counter; counter++;
										if(cacheSig3)
											for(int x=0;x<dts.g.graph[nei3].size();x++) {
												int nei4 = dts.g.graph[nei3].get(x);
												if(nei4!=nei1&&nei4!=nei2&&!dts.g.graph[nei4].contains(nei1)&&!dts.g.graph[nei4].contains(nei2)&&!dts.g.graph[nei4].contains(i)) {
													b.write("14,"+cc1+":"+nei4+"\n"); counter++;
													}
											}
										}
								} 	
							}
							for(int t=0;t<dts.g.graph[nei2].size();t++) {
								int nei3 = dts.g.graph[nei2].get(t);  
								if(nei3!=i&&nei3!=nei1&&!dts.g.graph[nei3].contains(i)) {
									if(!dts.g.graph[nei3].contains(nei1)) {
										b.write("9,"+cc0+":"+nei3+"\n"); cc1 = counter; counter++;
										if(cacheSig3)
											for(int x=0;x<dts.g.graph[nei3].size();x++) {
												int nei4 = dts.g.graph[nei3].get(x);
												if(nei4!=nei1&&nei4!=nei2&&!dts.g.graph[nei4].contains(nei1)&&!dts.g.graph[nei4].contains(nei2)&&!dts.g.graph[nei4].contains(i)) {
													b.write("14,"+cc1+":"+nei4+"\n"); counter++;
													}
											}
										}
									
								} 	
							}
						}
						for(int p=k+1; p<dts.g.graph[i].size();p++) {
							int nei3 = dts.g.graph[i].get(p);  counter3 = counter;
							//if(nei3 <= i)continue; // nei3 > i
							if(dts.g.graph[nei1].contains(nei3)) {
								if(dts.g.graph[nei2].contains(nei3)) {
									//it is a 4-clique
									//b.write("22:"+i+","+nei1+","+nei2+","+nei3+"\n");
									b.write("22,"+counter2+":"+nei3+"\n"); counter++;
									f22(i,nei1,nei2,nei3,dts.g.graph,b,p);
								}else {
									//it is a v16
									//b.write("16:"+i+","+nei1+","+nei2+","+nei3+"\n");
									b.write("16,"+counter2+":"+nei3+"\n"); cc0 = counter; counter++;
									if(cacheSig2) {
										for(int t=0;t<dts.g.graph[nei2].size();t++) {
											int nei4 = dts.g.graph[nei2].get(t);
											if(nei4!=i&&nei4!=nei3&&nei4!=nei1&&!dts.g.graph[nei4].contains(i)&&!dts.g.graph[nei4].contains(nei1)&&!dts.g.graph[nei4].contains(nei3)) {
												b.write("21,"+cc0+":"+nei4+"\n"); cc1 = counter; counter++;
												}
										}
										for(int t=0;t<dts.g.graph[nei3].size();t++) {
											int nei4 = dts.g.graph[nei3].get(t);
											if(nei4!=i&&nei4!=nei2&&nei4!=nei1&&!dts.g.graph[nei4].contains(i)&&!dts.g.graph[nei4].contains(nei1)&&!dts.g.graph[nei4].contains(nei2)) {
												b.write("21,"+cc0+":"+nei4+"\n"); cc1 = counter; counter++;
												}
										}
									}
									f16(i,nei1,nei2,nei3,dts.g.graph,b,p);
									
								}
							}else {
								if(dts.g.graph[nei2].contains(nei3)) {
									//it is a v16
									//b.write("16:"+i+","+nei1+","+nei2+","+nei3+"\n");
									b.write("16,"+counter2+":"+nei3+"\n"); cc0 = counter; counter++;
									if(cacheSig2) {
										for(int t=0;t<dts.g.graph[nei1].size();t++) {
											int nei4 = dts.g.graph[nei1].get(t);
											if(nei4!=i&&nei4!=nei3&&nei4!=nei2&&!dts.g.graph[nei4].contains(i)&&!dts.g.graph[nei4].contains(nei2)&&!dts.g.graph[nei4].contains(nei3)) {
												b.write("21,"+cc0+":"+nei4+"\n"); counter++;
												}
										}
										for(int t=0;t<dts.g.graph[nei3].size();t++) {
											int nei4 = dts.g.graph[nei3].get(t);
											if(nei4!=i&&nei4!=nei2&&nei4!=nei1&&!dts.g.graph[nei4].contains(i)&&!dts.g.graph[nei4].contains(nei1)&&!dts.g.graph[nei4].contains(nei2)) {
												b.write("21,"+cc0+":"+nei4+"\n"); counter++;
												}
										}
									}
									f16(i,nei1,nei2,nei3,dts.g.graph,b,p);
								}else {
									//it is a tailed-triangle v9
									//b.write("9:"+i+","+nei1+","+nei2+","+nei3+"\n");
									b.write("9,"+counter2+":"+nei3+"\n"); cc0 = counter; counter++;
									if(cacheSig2) {
										for(int t=0;t<dts.g.graph[nei1].size();t++) {
											int nei4 = dts.g.graph[nei1].get(t);
											if(nei4!=i&&nei4!=nei2&&nei4!=nei3&&!dts.g.graph[nei4].contains(i)&&!dts.g.graph[nei4].contains(nei2)&&!dts.g.graph[nei4].contains(nei3)&&dts.g.graph[nei4].contains(nei2)) {
												b.write("21,"+cc0+":"+nei4+"\n"); counter++;
												}
										}
										for(int t=0;t<dts.g.graph[nei3].size();t++) {
											int nei4 = dts.g.graph[nei3].get(t);
											if(nei4!=i&&nei4!=nei2&&nei4!=nei1&&!dts.g.graph[nei4].contains(i)&&!dts.g.graph[nei4].contains(nei2)&&!dts.g.graph[nei4].contains(nei1)) {
												b.write("14,"+cc0+":"+nei4+"\n"); counter++;
												}
										}
									}
									f9(i,nei1,nei2,nei3,dts.g.graph,b,p);
								}
							}
						}
					}else {
						//it is a 3-path
						//b.write("2:"+i+","+nei1+","+nei2+"\n");
						b.write("2,"+counter1+":"+nei2+"\n"); cc0 = counter; counter++;
						if(cacheSig2) {
							for(int p=0; p<dts.g.graph[nei1].size();p++) {
								int nei3 = dts.g.graph[nei1].get(p); 
								if(nei3==i||nei3==nei2||dts.g.graph[nei3].contains(i))
									continue;
								if(dts.g.graph[nei3].contains(nei2)) {
									b.write("10,"+cc0+":"+nei3+"\n"); counter++;
								}else {
									for(int q=p+1;q<dts.g.graph[nei1].size();q++) {
										int nei4 = dts.g.graph[nei1].get(q); 
										if(nei4==i||nei4==nei2||dts.g.graph[nei4].contains(i)||dts.g.graph[nei4].contains(nei2))
											continue;
										if(dts.g.graph[nei4].contains(nei3)) {
											b.write("14,"+cc0+":"+nei3+","+nei4+"\n"); counter++;
										}
									}
									for(int q=0;q<dts.g.graph[nei2].size();q++) {
										int nei4 = dts.g.graph[nei2].get(q);
										if(nei4==i||nei4==nei1||dts.g.graph[nei4].contains(i)||dts.g.graph[nei4].contains(nei1))
											continue;
										if(dts.g.graph[nei4].contains(nei3)) {
											b.write("12,"+cc0+":"+nei3+","+nei4+"\n"); counter++;
										}
										}
								}
									
								}
							
							for(int p=0; p<dts.g.graph[nei2].size();p++) {
								int nei3 = dts.g.graph[nei2].get(p); 
								if(nei3==i||nei3==nei1||dts.g.graph[nei3].contains(i))
									continue;
								if(!dts.g.graph[nei3].contains(nei1)) {
									for(int q=p+1;q<dts.g.graph[nei2].size();q++) {
										int nei4 = dts.g.graph[nei2].get(q); 
										if(nei4==i||nei4==nei1||dts.g.graph[nei4].contains(i)||dts.g.graph[nei4].contains(nei1))
											continue;
										if(dts.g.graph[nei4].contains(nei3)) {
											b.write("14,"+cc0+":"+nei3+","+nei4+"\n"); counter++;
										}
									}
								}
									
								}
						}
						for(int p=k+1; p<dts.g.graph[i].size();p++) {
							int nei3 = dts.g.graph[i].get(p); counter3 = counter;
							//if(nei3 <= i)continue; // nei3 > i
							if(dts.g.graph[nei1].contains(nei3)) {
								if(dts.g.graph[nei2].contains(nei3)) {
									//it is a v16
									//b.write("16:"+i+","+nei1+","+nei2+","+nei3+"\n");
									b.write("16,"+counter2+":"+nei3+"\n"); cc0 = counter; counter++;
									if(cacheSig2) {
										for(int t=0;t<dts.g.graph[nei1].size();t++) {
											int nei4 = dts.g.graph[nei1].get(t);
											if(nei4!=i&&nei4!=nei3&&nei4!=nei2&&!dts.g.graph[nei4].contains(i)&&!dts.g.graph[nei4].contains(nei2)&&!dts.g.graph[nei4].contains(nei3)) {
												b.write("21,"+cc0+":"+nei4+"\n"); counter++;
												}
										}
										for(int t=0;t<dts.g.graph[nei2].size();t++) {
											int nei4 = dts.g.graph[nei2].get(t);
											if(nei4!=i&&nei4!=nei3&&nei4!=nei1&&!dts.g.graph[nei4].contains(i)&&!dts.g.graph[nei4].contains(nei1)&&!dts.g.graph[nei4].contains(nei3)) {
												b.write("21,"+cc0+":"+nei4+"\n"); counter++;
												}
										}
									}
									f16(i,nei1,nei2,nei3,dts.g.graph,b,p);
								}else {
									//it is a v9
									//b.write("9:"+i+","+nei1+","+nei2+","+nei3+"\n");
									b.write("9,"+counter2+":"+nei3+"\n"); cc0 = counter; counter++;
									if(cacheSig2) {
										for(int t=0;t<dts.g.graph[nei1].size();t++) {
											int nei4 = dts.g.graph[nei1].get(t);
											if(nei4!=i&&nei4!=nei2&&nei4!=nei3&&!dts.g.graph[nei4].contains(i)&&!dts.g.graph[nei4].contains(nei2)&&dts.g.graph[nei4].contains(nei3)) {
												b.write("21,"+cc0+":"+nei4+"\n"); counter++;
												}
										}
										for(int t=0;t<dts.g.graph[nei2].size();t++) {
											int nei4 = dts.g.graph[nei2].get(t);
											if(nei4!=i&&nei4!=nei3&&nei4!=nei1&&!dts.g.graph[nei4].contains(i)&&!dts.g.graph[nei4].contains(nei3)&&!dts.g.graph[nei4].contains(nei1)) {
												b.write("14,"+cc0+":"+nei4+"\n"); counter++;
												}
										}
									}
									f9(i,nei1,nei2,nei3,dts.g.graph,b,p);
								}
							}else {
								if(dts.g.graph[nei2].contains(nei3)) {
									//it is a v9
									//b.write("9:"+i+","+nei1+","+nei2+","+nei3+"\n");
									b.write("9,"+counter2+":"+nei3+"\n"); cc0 = counter; counter++;
									if(cacheSig2) {
										for(int t=0;t<dts.g.graph[nei2].size();t++) {
											int nei4 = dts.g.graph[nei2].get(t);
											if(nei4!=i&&nei4!=nei1&&nei4!=nei3&&!dts.g.graph[nei4].contains(i)&&!dts.g.graph[nei4].contains(nei1)&&dts.g.graph[nei4].contains(nei3)) {
												b.write("21,"+cc0+":"+nei4+"\n"); counter++;
												}
										}
										for(int t=0;t<dts.g.graph[nei1].size();t++) {
											int nei4 = dts.g.graph[nei1].get(t);
											if(nei4!=i&&nei4!=nei3&&nei4!=nei2&&!dts.g.graph[nei4].contains(i)&&!dts.g.graph[nei4].contains(nei3)&&!dts.g.graph[nei4].contains(nei2)) {
												b.write("14,"+cc0+":"+nei4+"\n"); counter++;
												}
										}
									}
									f9(i,nei1,nei2,nei3,dts.g.graph,b,p);
								}else {
									//it is a 4-star
									//b.write("4:"+i+","+nei1+","+nei2+","+nei3+"\n");
									if(cacheSig) {
										b.write("4,"+counter2+":"+nei3+"\n"); counter++;
										}
									f4(i,nei1,nei2,nei3,dts.g.graph,b,p);
								}
							}
						}
						
					}
				
				}
			}
		}
		

		long t2 = System.currentTimeMillis();
		System.out.println("\ntime:"+(t2-t1));
		
		b.flush();
		b.close();
	}
	
	static void f4(int i, int nei1, int nei2, int nei3, ArrayList<Integer>[]graph, BufferedWriter b, int p) throws IOException {
		if(!cacheSig) return;
		for(int q=p+1; q<graph[i].size();q++) {
			int nei4 = graph[i].get(q);
			//if(nei4 <= i)continue; // nei4 > i
			if(graph[nei1].contains(nei4)) {
				if(graph[nei2].contains(nei4)) {
					if(graph[nei3].contains(nei4)) {
						//it is a v25
						b.write("25,0:"+nei4+"\n"); counter++;
					}else {
						//it is a v20
						b.write("20,0:"+nei4+"\n"); counter++;
					}
				}else {
					if(graph[nei3].contains(nei4)) {
						//it is a v20
						b.write("20,0:"+nei4+"\n"); counter++;
					}else {
						//it is a v13
						b.write("13,0:"+nei4+"\n"); counter++;
					}
				}
			}else {
				if(graph[nei2].contains(nei4)) {
					if(graph[nei3].contains(nei4)) {
						//it is a v20
						b.write("20,0:"+nei4+"\n"); counter++;
					}else {
						//it is a v13
						b.write("13,0:"+nei4+"\n"); counter++;
					}
				}else {
					if(graph[nei3].contains(nei4)) {
						//it is a v13
						b.write("13,0:"+nei4+"\n"); counter++;
					}else {
						//it is a v6
						b.write("6,0:"+nei4+"\n"); counter++;
					}
				}
			}
			}
	}
	
	static void f22(int i, int nei1, int nei2, int nei3, ArrayList<Integer>[]graph, BufferedWriter b, int p) throws IOException {
		
		if(!cacheSig) {
			for(int q=p+1; q<graph[i].size();q++) {
				int nei4 = graph[i].get(q);
				//if(nei4 <= i)continue; // nei4 > i
				if(graph[nei1].contains(nei4)) {
					if(graph[nei2].contains(nei4)) {
						if(graph[nei3].contains(nei4)) {
							//it is a v30
							b.write("30,0:"+nei4+"\n"); counter++;
						}
					}
				}
			}
			return;
		}
		for(int q=p+1; q<graph[i].size();q++) {
			int nei4 = graph[i].get(q);
			//if(nei4 <= i)continue; // nei4 > i
			if(graph[nei1].contains(nei4)) {
				if(graph[nei2].contains(nei4)) {
					if(graph[nei3].contains(nei4)) {
						//it is a v30
						b.write("30,0:"+nei4+"\n"); counter++;
					}else {
						//it is a v20
						b.write("29,0:"+nei4+"\n"); counter++;
					}
				}else {
					if(graph[nei3].contains(nei4)) {
						//it is a v29
						b.write("29,0:"+nei4+"\n"); counter++;
					}else {
						//it is a v28
						b.write("28,0:"+nei4+"\n"); counter++;
					}
				}
			}else {
				if(graph[nei2].contains(nei4)) {
					if(graph[nei3].contains(nei4)) {
						//it is a v29
						b.write("29,0:"+nei4+"\n"); counter++;
					}else {
						//it is a v28
						b.write("28,0:"+nei4+"\n"); counter++;
					}
				}else {
					if(graph[nei3].contains(nei4)) {
						//it is a v28
						b.write("28,0:"+nei4+"\n"); counter++;
					}else {
						//it is a v26
						b.write("26,0:"+nei4+"\n"); counter++;
					}
				}
			}
		}
	}
	
	static void f16(int i, int nei1, int nei2, int nei3, ArrayList<Integer>[]graph, BufferedWriter b, int p) throws IOException {
		if(!cacheSig) return;
		
		for(int q=p+1; q<graph[i].size();q++) {
			int nei4 = graph[i].get(q);
			//if(nei4 <= i)continue; // nei4 > i
			if(graph[nei1].contains(nei4)) {
				if(graph[nei2].contains(nei4)) {
					if(graph[nei3].contains(nei4)) {
						//it is a v29
						b.write("29,"+counter3+":"+nei4+"\n"); counter++;
					}else {
						if(graph[nei1].contains(nei2)) {
							//it is a v28
							b.write("28,"+counter3+":"+nei4+"\n"); counter++;
							}
						else {
							//it is a v27
							b.write("27,"+counter3+":"+nei4+"\n"); counter++;
							}
					}
				}else {
					if(graph[nei3].contains(nei4)) {
						//it is a v29
						if(graph[nei1].contains(nei2)) {
							//it is a v27
							b.write("27,"+counter3+":"+nei4+"\n"); counter++;
							}
						else {
							//it is a v28
							b.write("28,"+counter3+":"+nei4+"\n"); counter++;
							}
					}else {
						//it is a v24
						b.write("24,"+counter3+":"+nei4+"\n"); counter++;
					}
				}
			}else {
				if(graph[nei2].contains(nei4)) {
					if(graph[nei3].contains(nei4)) {
						//it is a v28
						b.write("28,"+counter3+":"+nei4+"\n"); counter++;
					}else {
						if(graph[nei1].contains(nei2)) {
							//it is a v25
							b.write("25,"+counter3+":"+nei4+"\n"); counter++;
							}
						else {
							//it is a v24
							b.write("24,"+counter3+":"+nei4+"\n"); counter++;
							}
					}
				}else {
					if(graph[nei3].contains(nei4)) {
						if(graph[nei1].contains(nei2)) {
							//it is a v24
							b.write("24,"+counter3+":"+nei4+"\n"); counter++;
							}
						else {
							//it is a v25
							b.write("25,"+counter3+":"+nei4+"\n"); counter++;
							}
					}else {
						//it is a v20
						b.write("20,"+counter3+":"+nei4+"\n"); counter++;
					}
				}
			}
		}
	}
	
	static void f9(int i, int nei1, int nei2, int nei3, ArrayList<Integer>[]graph, BufferedWriter b, int p) throws IOException {
		if(!cacheSig) return;
		for(int q=p+1; q<graph[i].size();q++) {
			int nei4 = graph[i].get(q);
			//if(nei4 <= i)continue; // nei4 > i
			if(graph[nei1].contains(nei4)) {
				if(graph[nei2].contains(nei4)) {
					if(graph[nei3].contains(nei4)) {
						//it is a v28
						b.write("28,"+counter3+":"+nei4+"\n"); counter++;
					}else {
						if(graph[nei1].contains(nei2)) {
							//it is a v26
							b.write("26,"+counter3+":"+nei4+"\n"); counter++;
							}
						else {
							//it is a v24
							b.write("24,"+counter3+":"+nei4+"\n"); counter++;
							}
					}
				}else {
					if(graph[nei3].contains(nei4)) {
						if(graph[nei1].contains(nei2)) {
							//it is a v24
							b.write("24,"+counter3+":"+nei4+"\n"); counter++;
							}
						else {
							//it is a v26
							b.write("26,"+counter3+":"+nei4+"\n"); counter++;
							}
					}else {
						if(graph[nei1].contains(nei2)) {
							//it is a v19
							b.write("19,"+counter3+":"+nei4+"\n"); counter++;
							}
						else {
							//it is a v24
							b.write("20,"+counter3+":"+nei4+"\n"); counter++;
							}
					}
				}
			}else {
				if(graph[nei2].contains(nei4)) {
					if(graph[nei3].contains(nei4)) {
						//it is a v24
						b.write("24,"+counter3+":"+nei4+"\n"); counter++;
					}else {
						if(graph[nei1].contains(nei2)) {
							//it is a v20
							b.write("20,"+counter3+":"+nei4+"\n"); counter++;
							}
						else {
							//it is a v19
							b.write("19,"+counter3+":"+nei4+"\n"); counter++;
							}
					}
				}else {
					if(graph[nei3].contains(nei4)) {
						if(graph[nei1].contains(nei2)) {
							//it is a v19
							b.write("19,"+counter3+":"+nei4+"\n"); counter++;
							}
						else {
							//it is a v20
							b.write("20,"+counter3+":"+nei4+"\n"); counter++;
						}
					}else {
						//it is a v13
						b.write("13,"+counter3+":"+nei4+"\n"); counter++;
					}
				}
			}
		}
			}

}
