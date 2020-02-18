package algo;
import java.util.ArrayList;

public class isotopeMotifNet{
	public int jumpID;
	public isotopeMotifNet last;
	public ArrayList<isotopeMotifNet>next;
	public boolean outputSig = false;
	public int expandDegree = -1;
	public int singular = 0; // 0 for extend in turn, 1 for singular extension, 2 for parallel extension
	//public int sDegree = -1;
	public motifNet motif;
	public isotopeMotifNet(int i, isotopeMotifNet m, motifNet mo) {
		jumpID=i;
		last=m;
		motif=mo;
		next = new ArrayList<isotopeMotifNet>();
		//expandDegree = new ArrayList<Integer>();
	}
	public String backtrack(){
		String s = "";
		isotopeMotifNet p = last;
		while(p!=null){
			s += p.motif.signature+"-";
			//s += p.motif.printIsotms();
			p = p.last;
		}
		return s;
	}
	public int getMotifSize(){
		String s = motif.signature;
		int counter = s.length();
		for(int i=s.length()-1;i>=0;i--){
			if(s.charAt(i)=='0'){
				counter--;
			}
		}
		return counter;
	}

}
