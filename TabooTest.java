// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;

public class TabooTest extends TestCase {

    public void test1(){
        List<String> list1 = new ArrayList<>(Arrays.asList("a", "c", "a", "b", "d"));
        List<String> list2 = new ArrayList<>(Arrays.asList("a", "a", "c", "d", "l", null));
        List<String> list3 = new ArrayList<>(Arrays.asList("ab", "a", "c", "m", "m"));
        List<String> list4 = new ArrayList<>(Arrays.asList("m", "a", "r", "y"));

        Taboo<String> taboo1 = new Taboo<>(list1);
        Taboo<String> taboo2 = new Taboo<>(list2);
        Taboo<String> taboo3 = new Taboo<>(list3);
        Taboo<String> taboo4 = new Taboo<>(list4);

        Set<String> expected1 = new HashSet<>(Arrays.asList("c", "b"));
        Set<String> expected2 = new HashSet<>(Collections.singletonList("a"));
        Set<String> expected3 = new HashSet<>(Collections.singletonList("d"));

        assertEquals(expected1, taboo1.noFollow("a"));
        assertEquals(expected2, taboo1.noFollow("c"));
        assertEquals(expected3, taboo1.noFollow("b"));

        Set<String> expected4 = new HashSet<>(Arrays.asList("a", "c"));
        Set<String> expected5 = new HashSet<>(Collections.singletonList("d"));
        Set<String> expected6 = new HashSet<>();

        assertEquals(expected4, taboo2.noFollow("a"));
        assertEquals(expected5, taboo2.noFollow("c"));
        assertEquals(expected6, taboo3.noFollow("x"));
        assertEquals(expected6, taboo4.noFollow("x"));

        List<String> toReduce1 = new ArrayList<>(Arrays.asList("a", "c", "a", "b", "d"));
        List<String> expectedReduced1 = new ArrayList<>(Arrays.asList("a", "a", "d"));
        List<String> toReduce2 = new ArrayList<>(Arrays.asList("a", "a", "c", "d", "l", null));
        List<String> expectedReduced2 = new ArrayList<>(Arrays.asList("a", "d",  null));
        List<String> toReduce3 = new ArrayList<>(Arrays.asList("ab", "a", "c", "m", "m"));
        List<String> expectedReduced3 = new ArrayList<>(Arrays.asList("ab", "c"));
        List<String> toReduce4 = new ArrayList<>(Arrays.asList("m", "a", "r", "y"));
        List<String> expectedReduced4 = new ArrayList<>(Arrays.asList("m", "r"));

        taboo1.reduce(toReduce1);
        taboo2.reduce(toReduce2);
        taboo3.reduce(toReduce3);
        taboo4.reduce(toReduce4);

        assertEquals(expectedReduced1, toReduce1);
        assertEquals(expectedReduced2, toReduce2);
        assertEquals(expectedReduced3, toReduce3);
        assertEquals(expectedReduced4, toReduce4);



    }

    public void test2(){
        List<String> rules1 = Arrays.asList("");
        List<String> arr = Arrays.asList("");
        List<String> arr2 = Arrays.asList("", "b");
        Taboo<String> taboo1 = new Taboo<String>(rules1);
        Set<String> set1 = taboo1.noFollow("b");
        Set<String> ans = new HashSet<>();
        taboo1.reduce(arr);
        taboo1.reduce(arr2);
        assertTrue(arr.equals(Arrays.asList("")));
        assertTrue(set1.equals(ans));
        assertTrue(arr2.equals(Arrays.asList("", "b")));





    }

    public void test3() {
        List<Integer> rules = Arrays.asList(1, 2, 3, 1, 4, 5);
        Taboo<Integer> taboo = new Taboo<>(rules);

        Set<Integer> expectedNoFollow1 = new HashSet<>(Arrays.asList(2, 4));
        Set<Integer> expectedNoFollow2 = new HashSet<>(Arrays.asList(3));
        Set<Integer> expectedNoFollow4 = new HashSet<>(Arrays.asList(5));

        assertEquals(expectedNoFollow1, taboo.noFollow(1));
        assertEquals(expectedNoFollow2, taboo.noFollow(2));
        assertEquals(expectedNoFollow4, taboo.noFollow(4));

        List<Integer> toReduce = new ArrayList<>(Arrays.asList(1, 2, 3, 1, 4, 5, 3));
        List<Integer> expectedReduced = new ArrayList<>(Arrays.asList(1, 3, 4, 3)); // Removes forbidden sequences

        taboo.reduce(toReduce);
        assertEquals(expectedReduced, toReduce);
    }





}
