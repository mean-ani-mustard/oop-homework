import junit.framework.TestCase;

import java.util.*;

public class AppearancesTest extends TestCase {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}
	
	public void testSameCount1() {
		List<String> a = stringToList("abbccc");
		List<String> b = stringToList("cccbba");
		assertEquals(3, Appearances.sameCount(a, b));
	}
	
	public void testSameCount2() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1)));
	}

	public void test3() {
		List<String> a = Collections.emptyList();
		List<String> b = Collections.emptyList();
		List<String> c = Arrays.asList("x", "y", "z");
		List<String> d = Arrays.asList("a", "b", "c");
		assertEquals(0, Appearances.sameCount(a, b));
		assertEquals(0, Appearances.sameCount(a, c));
		assertEquals(0, Appearances.sameCount(d, c));
	}

	public void test4(){
		Collection<Character> a = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'c'));
		Collection<Character> b = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'c'));
		assertEquals(3, Appearances.sameCount(a, b));
	}

}
