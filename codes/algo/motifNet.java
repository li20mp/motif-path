package algo;
import java.util.ArrayList;

public class motifNet {
	public int id;
	public String signature;
	public ArrayList<isotopeMotifNet>[]isotopes;
	public motifNet(int i, String sig){
		id=i;
		signature=sig;
		isotopes=new ArrayList[5];
	}
	public void addIsotm(int jumpID, isotopeMotifNet last){
		if(isotopes[jumpID]==null)
			isotopes[jumpID]=new ArrayList<isotopeMotifNet>();
		isotopes[jumpID].add(new isotopeMotifNet(jumpID,last, this));	
	}
	public boolean checkSig(String sig){
		if(sig.equals(signature)){
			return true;
		}
		return false;
	}
	public String printIsotms(){
		String s="";
		for(int i=1;i<4;i++){
			s+=("Jump "+i+": ");
			if(isotopes[i]!=null){
				for(int j=0;j<isotopes[i].size();j++){
					s+=(signature+"-"+isotopes[i].get(j).backtrack()+", ");
				}
				s+=";\n";
			}else
				s+=";\n";
		}
		return s;
	}
}
