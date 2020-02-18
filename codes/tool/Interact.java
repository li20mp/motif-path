package tool;

public class Interact {
	public int intwarn(String s, int defult, char c) {
		int si = 0;
		try {
		si = Integer.parseInt(s);
		}catch(Exception e) {
			System.out.println("Please input an integer for "+c+"! Now use defult value "+c+" = "+defult+".");
			return defult;
		}
		return si;
	}
}
