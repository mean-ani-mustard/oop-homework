import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A  run is a series of adjacent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		if (str.isEmpty()) return 0;
		int cur = 0;
		int res = 1;
		char ch =str.charAt(0);
		for (int i = 0; i<str.length(); i++){
			if(str.charAt(i)==ch){
				cur++;
			}else{
				if(cur>res){
					res = cur;
				}
				ch = str.charAt(i);
				cur = 1;
			}
		}
		return Math.max(cur,res);

	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		if(str.isEmpty()) return "";
		int num;
		String result = "";
		for (int i = 0; i < str.length(); i++){
			if(Character.isDigit(str.charAt(i)) ){
				if(i<str.length()-1) {
					num = Character.getNumericValue(str.charAt(i));
					for(int j = 0; j<num; j++){
						result= result + str.charAt(i+1);
					}
				}

			}else{
				result = result + str.charAt(i);
			}
		}
		return result;
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		Set<String> myS = new HashSet<>();
		int al = a.length();
		int bl = b.length();
		for(int i = 0; i<=al-len; i++){
			myS.add(a.substring(i,i+len));
		}
		for(int i = 0; i <= bl-len; i++){
			if(myS.contains(b.substring(i,i+len))){
				return true;
			}
		}
		return false;
	}
}
