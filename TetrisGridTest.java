import junit.framework.TestCase;
import java.util.*;

public class TetrisGridTest extends TestCase {
	
	// Provided simple clearRows() test
	// width 2, height 3 grid
	public void testClear1() {
		boolean[][] before =
		{	
			{true, true, false, },
			{false, true, true, }
		};
		
		boolean[][] after =
		{	
			{true, false, false},
			{false, true, false}
		};

		boolean[][] before0 =
				{
						{true, true, false },
						{false, true, true },
						{true, true, false },
						{false, true, true }
				};

		boolean[][] after0 =
				{
						{true, false, false},
						{false, true, false},
						{true, false, false},
						{false, true, false}
				};
		
		TetrisGrid tetris = new TetrisGrid(before);
		TetrisGrid tetris0 = new TetrisGrid(before0);
		tetris.clearRows();
		tetris0.clearRows();
		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
		assertTrue( Arrays.deepEquals(after0, tetris0.getGrid()) );
	}

	public void testClear2(){

		boolean[][] before = {
			{true, true, false, true, false, false},
			{false, true, true, true, true, false },
			{false, true, true, true, false, false},
			{false, true, true, true, false, false}
		};

		boolean[][] after = {
			{true,  false,  false, false, false, false},
			{false,  true,  true, false, false, false },
			{false, true,  false, false, false, false},
			{false,  true,  false, false, false, false}
		};

		boolean[][] before0 = {
				{true},
				{true},
				{true},
				{true},
				{true},
				{true}

		};

		boolean[][] after0 =
				{{false},
						{false},
						{false},
						{false},
						{false},
						{false}

				};
		TetrisGrid tetris = new TetrisGrid(before);
		TetrisGrid tetris0 = new TetrisGrid(before0);
		tetris.clearRows();
		tetris0.clearRows();
		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
		assertTrue( Arrays.deepEquals(after0, tetris0.getGrid()) );

	}
	public void testClear3(){

		boolean[][] before = {
				{true, true, true, true, false, false},
				{false, true, true, true, true, false },
				{false, true, true, true, false, false},
				{false, true, true, true, false, false}
		};

		boolean[][] after = {
				{true,   false, false, false, false, false},
				{false,   true, false, false, false, false },
				{false,   false, false, false, false, false},
				{false,   false, false, false, false, false}
		};


		TetrisGrid tetris = new TetrisGrid(before);

		tetris.clearRows();

		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );

	}

}
	

