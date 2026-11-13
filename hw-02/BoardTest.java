import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;


public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.

	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
@Before
	protected void setUp() throws Exception {
		b = new Board(3, 6);

		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		b.place(pyr1, 0, 0);
		b.commit();

	}
	@Test
	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}

	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.commit();

		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}
	public void testSample3() {
		b.commit();
		String original = b.toString();

		int result = b.place(pyr2, 1, 2);
		assertEquals(Board.PLACE_OK, result);
		b.undo();
		assertEquals(original, b.toString());

		result = b.place(pyr4, 2, 1);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		b.undo();
		assertEquals(original, b.toString());

		result = b.place(pyr3, -1, 1);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		b.undo();
		assertEquals(original, b.toString());
	}

	public void testSample4() {
		b.commit();
		String original = b.toString();


		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(s, 2, 1));
		b.undo();


		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(sRotated, 1, 6));
		b.undo();


		assertEquals(Board.PLACE_OUT_BOUNDS, b.place(s, -1, 0));
		b.undo();

		assertEquals(original, b.toString());
	}
	public void testSample5() {
		Board b = new Board(3, 6);
		int result;

		b.commit();

		// Place pyr1 first, higher
		result = b.place(pyr1, 0, 3);
		b.commit();

		// Now place pyr3 lower, should cause row fill
		result = b.place(pyr3, 0, 1);
		assertEquals(Board.PLACE_ROW_FILLED, result);


		assertEquals(4, b.getColumnHeight(0));
		assertEquals(5, b.getColumnHeight(1));
		assertEquals(4, b.getColumnHeight(2));
		assertEquals(-1, b.getColumnHeight(3));
		assertEquals(5, b.getMaxHeight());


		assertEquals(1, b.getRowWidth(1));
		assertEquals(3, b.getRowWidth(2));
		assertEquals(3, b.getRowWidth(3));


		result = b.clearRows();
		assertEquals(2, result);

		assertEquals(0, b.getColumnHeight(0));
		assertEquals(3, b.getColumnHeight(1));


		assertFalse(b.getGrid(0, 0));
		assertFalse(b.getGrid(0, 1));

	}


	public void testSample6() {
		b.commit();
		assertEquals(2, b.dropHeight(sRotated, 0));

	}



	// Make  more tests, by putting together longer series of
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.




}
