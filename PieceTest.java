import junit.framework.TestCase;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;

	protected void setUp() throws Exception {
		super.setUp();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

	}
	
	// Here are some sample tests to get you started
	
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());

		Piece n = new Piece(Piece.S1_STR);
		assertEquals(3, n.getWidth());
		assertEquals(2, n.getHeight());
		n = n.computeNextRotation();
		assertEquals(2, n.getWidth());
		assertEquals(3, n.getHeight());

	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));

		Piece n = new Piece(Piece.S1_STR);
		assertTrue(Arrays.equals(new int[] {0,0,1} , n.getSkirt()));
		n = n.computeNextRotation();
		assertTrue(Arrays.equals(new int[] {1,0} , n.getSkirt()));

	}

	public void testBody() {
		TPoint [] tp = new TPoint[4];
		for(int i = 0; i<4; i++){
			tp[i] = new TPoint(i, 0);
		}
		Piece l = new Piece(Piece.STICK_STR);
		Piece l1 = l.computeNextRotation();

		assertTrue(l.equals(new Piece(l.getBody())));
		assertTrue(l1.equals(new Piece(tp)));
		assertFalse(l.equals(new Piece(l1.getBody())));


	}

	public void testNext(){
		Piece[] tester = s.getPieces();
		assertTrue(tester[0].equals(new Piece(Piece.STICK_STR)));
		assertTrue(tester[1].equals(new Piece(Piece.L1_STR)));
		assertTrue(tester[2].equals(new Piece(Piece.L2_STR)));
		assertTrue(tester[3].equals(new Piece(Piece.S1_STR)));
		assertTrue(tester[4].equals(new Piece(Piece.S2_STR)));
		assertTrue(tester[5].equals(new Piece(Piece.SQUARE_STR)));
		assertTrue(tester[6].equals(new Piece(Piece.PYRAMID_STR)));

		Piece sq = new Piece(Piece.SQUARE_STR);
		assertFalse(tester[Piece.S1].fastRotation().equals(s));
		assertFalse(tester[Piece.S1].equals(sRotated));
		assertTrue(sq.equals(tester[Piece.SQUARE].fastRotation()));
		assertTrue(sq.equals(tester[Piece.SQUARE].fastRotation().fastRotation()));
		assertTrue(tester[Piece.S1].fastRotation().fastRotation().fastRotation().fastRotation().fastRotation().equals(tester[Piece.S1].fastRotation()));
		assertTrue(tester[Piece.S1].fastRotation().equals(sRotated));
		assertTrue(tester[Piece.L1].fastRotation().fastRotation().fastRotation().fastRotation().equals(tester[Piece.L1]));



	}







	
}
