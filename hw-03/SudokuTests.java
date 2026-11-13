import junit.framework.TestCase;

public class SudokuTests extends TestCase {


        protected void setUp() throws Exception {
            super.setUp();

        }

        public void testCase1(){
            Sudoku sudoku = new Sudoku(Sudoku.easyGrid);
            int solutions = sudoku.solve();
            assertEquals(1, solutions);
            assertNotNull(sudoku.getSolutionText());
            System.out.println("Easy grid solution:\n" + sudoku.getSolutionText());
        }

        public void testCase2(){
            Sudoku sudoku = new Sudoku(Sudoku.mediumGrid);
            int solutions = sudoku.solve();
            assertEquals(1, solutions);
            assertNotNull(sudoku.getSolutionText());
            System.out.println("Medium grid solution:\n" + sudoku.getSolutionText());
        }
        public void testCase3(){
            int[][] noSolutionGrid = Sudoku.stringsToGrid(
                    "1 2 3 4 5 6 7 8 9",
                    "1 0 0 0 0 0 0 0 0",
                    "0 0 0 0 0 0 0 0 0",
                    "0 0 0 0 0 0 0 0 0",
                    "0 0 0 0 0 0 0 0 0",
                    "0 0 0 0 0 0 0 0 0",
                    "0 0 0 0 0 0 0 0 0",
                    "0 0 0 0 0 0 0 0 0",
                    "0 0 0 0 0 0 0 0 0"
            );
            Sudoku sudoku = new Sudoku(noSolutionGrid);
            int solutions = sudoku.solve();
            assertEquals(0, solutions);
            assertEquals("No Solution", sudoku.getSolutionText());

        }
        public void testCase4(){
            int[][] multiSolutionGrid = Sudoku.stringsToGrid(
                    "3 0 0 0 0 0 0 8 0",
                    "0 0 1 0 9 3 0 0 0",
                    "0 4 0 7 8 0 0 0 3",
                    "0 9 3 8 0 0 0 1 2",
                    "0 0 0 0 4 0 0 0 0",
                    "5 2 0 0 0 6 7 9 0",
                    "6 0 0 0 2 1 0 4 0",
                    "0 0 0 5 3 0 9 0 0",
                    "0 3 0 0 0 0 0 5 0"
            );
            Sudoku sudoku = new Sudoku(multiSolutionGrid);
            int solutions = sudoku.solve();
            assertTrue(solutions > 1 && solutions <= Sudoku.MAX_SOLUTIONS);
            System.out.println("Multiple solutions found: " + solutions);
        }

    public void testCase5() {
        String gridText =
                "530070000" +
                        "600195000" +
                        "098000060" +
                        "800060003" +
                        "400803001" +
                        "700020006" +
                        "060000280" +
                        "000419005" +
                        "000080079";

        int[][] grid = Sudoku.textToGrid(gridText);
        Sudoku sudoku = new Sudoku(grid);
        int solutions = sudoku.solve();
        assertEquals(1, solutions);
        long elapsed = sudoku.getElapsed();
        assertTrue(elapsed >= 0);
        System.out.println("Text-to-grid solution:\n" + sudoku.getSolutionText());
    }
    //line coverage is 92% because main isn't called here and sudoku file involves main

}
