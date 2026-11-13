import java.util.*;

public class Appearances {

	private static <T> void fillMap (Collection<T> m, Map<T, Integer> map){
		for (T item : m){
			if (!map.containsKey (item)) {
				map.put(item, 1);
			} else {
				map.put(item, map.get (item) +1);
			}
		}
	}
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		int result = 0;
		Map<T, Integer> mapA = new HashMap<>();
		Map<T, Integer> mapB = new HashMap<>();
		fillMap(a, mapA);
		fillMap(b, mapB);
		for (T x: mapA.keySet()){
			if(mapB.keySet().contains(x)){
				if(mapA.get(x).equals(mapB.get(x))){
					result++;
				}
			}
		}
		return result;
	}






	
}
