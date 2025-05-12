import junit.framework.TestCase;

public class MetropolisTester extends TestCase {
    private MetropolisdbModel model;

    protected void setUp() throws Exception {
        model = new MetropolisdbModel();
    }

    public void testEmptyDatabase() {
        model.searchMetropolises("", "", "", "Population Larger Then", "Partial Match");
        assertTrue( model.getRowCount()>0); // Assuming DB is empty or cleared before
    }

    public void testAddAndRetrieve() {
        model.addMetropolis("TestCity", "TestContinent", "123456");
        model.searchMetropolises("TestCity", "", "", "Population Larger Then", "Exact Match");
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();
        assertTrue(rowCount>0);
        assertEquals(3, columnCount);
        assertEquals("TestCity", model.getValueAt(rowCount-1, 0));
    }

    public void testSearchExactMatch() {

        model.addMetropolis("ExactCity", "ExactLand", "999999");
        int a = model.getRowCount();
        model.addMetropolis("ExactCity", "ExactLand", "999999");
        assertEquals(a+1, model.getRowCount());
        model.searchMetropolises("ExactCity", "ExactLand", "999999", "Population Larger Then", "Exact Match");
        model.searchMetropolises("ანანო1378}}}{--905863","","999999", "Population Larger Then", "Exact Match"); //string that would have never been added
        assertEquals(0,model.getRowCount());
    }

    public void testSearchPartialMatch() {
        model.addMetropolis("PartCity", "PartContinent", "111");
        model.searchMetropolises("Part", "Part", "", "Population Larger Then", "Partial Match");
        assertTrue(model.getRowCount() > 0);
    }

    public void testPopulationFilterSmaller() {
        model.addMetropolis("SmallPopCity", "PopCont", "5");
        model.searchMetropolises("", "", "10", "Population Smaller Then", "Partial Match");
        boolean found = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals("SmallPopCity")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    public void testInvalidAdd() {

        model.addMetropolis("", "", "");
        int initialCount = model.getRowCount();
        model.addMetropolis("", "", "");
        assertEquals(initialCount, model.getRowCount()); // should not add invalid data
    }

    public void testNonNumberEntered(){

        model.addMetropolis("new york", "north america", "temo");
        int initialCount = model.getRowCount();
        model.addMetropolis("new york", "north america", "temo");
        assertEquals(initialCount, model.getRowCount()); // should not add invalid data

    }



}
