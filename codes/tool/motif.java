package tool;
import java.util.ArrayList;


public class motif {
	//public ArrayList<String>edges=null;
	public ArrayList<Integer>nodes=null;
	public int id;
	public int mid;
	public motif last;
	public motif(int id){
		//edges=new ArrayList();
		nodes = new ArrayList();
		this.id=id;
	}
	public motif(ArrayList<Integer>ns, int mid){
		nodes=ns;
		this.mid = mid;
	}
	public void addTriangle(int i, int j, int k){
		//edges=new ArrayList();
		nodes.add(i);
		nodes.add(j);
		nodes.add(k);
	}
	public void addRectangle(int i, int j, int k, int p){
		//edges=new ArrayList();
		nodes.add(i);
		nodes.add(j);
		nodes.add(k);
		nodes.add(p);
	}
	public String toString() {
		String s = "";
		for(int i=0;i<nodes.size();i++) {
			s+=(nodes.get(i)+"_");
		}
		return s;
	}

}
