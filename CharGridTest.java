
// Test cases for CharGrid -- a few basic tests are provided.

import junit.framework.TestCase;

public class CharGridTest extends TestCase {
	
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
		assertEquals(0, cg.charArea('d'));
	}
	
	
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}

	public void testPlus1(){
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'a', 'a', 'a'},
				{' ', 'a', 'a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());

	}

	public void testPlus2() {
		char[][] grid = {
				{' ', 'w', ' ', 's', ' '},
				{'w', 'w', 'w', 's', 's'},
				{' ', 'w', ' ', 's', ' '},
				{'*', '*', '*', '*', '*'},
				{' ', '*', ' ', '*', ' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());
	}

	public void testPlus3(){
		char[][] grid = {
				{' ', 'w', ' ', 's', ' ', 'a', 'd'},
				{'w', 'w', 'w', 's', 's', 'd', 'd'},
				{' ', 'w', '*', 's', ' ', ' ', 'd'},
				{' ', '*', '*', '*', ' ', ' ', ' '},
				{' ', '*', '*', '*', ' ', ' ', ' '}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(3, cg.countPlus());
	}

	public void testPlus4(){
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
		};
		char [][] grid0 = new char[][]{
				{ '2'}
		};
		CharGrid cg = new CharGrid(grid);
		CharGrid cg0 = new CharGrid(grid0);
		assertEquals(0, cg.countPlus());
		assertEquals(0, cg0.countPlus());
	}

	public void testPlus5() {
		char[][] grid = {
				{'s', 's', 's', 's', 's'},
				{'s', 's', 's', 's', 's'},
				{'s', 's', 's', 's', 's'},
				{'s', 's', 's', 's', 's'},
				{'s', 's', 's', 's', 's'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());  // The entire grid is one big plus
	}




	
	
	
	
}
