// Board.java

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean[][] previousState;
	private boolean DEBUG = true;
	boolean committed;
	private int [] rowWidths;
	private int [] colHeights;
	private int maxHeight;
	private int [] rowWidthsBackUp;
	private int [] colHeightsBackUp;
	int maxHightBackUp;



	// Here a few trivial methods are provided:

	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		previousState = new boolean[width][height];
		committed = true;

		colHeights = new int [width];
        Arrays.fill(colHeights,0);
		rowWidths = new int [height];
        Arrays.fill(rowWidths,0);

		maxHeight = 0;
		maxHightBackUp = 0;
		colHeightsBackUp = new int [width];
        Arrays.fill(colHeightsBackUp,0);
		rowWidthsBackUp = new int [height];
        Arrays.fill(rowWidthsBackUp,0);


	}



	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}


	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}


	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		return  maxHeight;
	}
// finds out max height
	private void newMaxHeight(){
		maxHeight=0;
		for(int i = 0; i<this.getWidth(); i++) {
			maxHeight = Math.max(maxHeight, this.getColumnHeight(i));
		}

	}

//checks if inside bounds
	private boolean outOfBounds (int a, int size){
		return !(a>=0 && a<size);
	}


	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			for(int i = 0; i < width; i++){
				if(outOfBounds(colHeights[i], maxHeight+1) || outOfBounds(colHeights[i], height+1)){
					throw new RuntimeException("Problem at column " + i);
				}
			}
			for(int i = 0; i< height; i++){
				if(outOfBounds(rowWidths[i], width+1)){
					throw new RuntimeException("Problem at row " + i);
				}
			}
		}
	}

	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.

	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		if(outOfBounds(x, width)) return -1;
		int pieceW = piece.getWidth();
		if(x + pieceW > width) return -1;
		int [] sk = piece.getSkirt();
		int hght = colHeights[x] - sk[0] ;
		for (int i = 1; i < pieceW; i++ ){
			hght = Math.max(hght, colHeights[x+i]-sk[i]);
			if(hght>=height){
				return -1;
			}
		}
		return hght;
	}


	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		if(outOfBounds(x, width)) return -1;
		return colHeights[x];
	}
   //methods to deduce new heights and row widths after some movement
	private void newColHeights(){
		Arrays.fill(colHeights, 0);
		for(int x = 0; x<width; x++){
			for (int i = height-1; i>=0; i--){
				if(grid[x][i]){
					colHeights[x]=i+1;
					break;
				};
			}
		}
	}

	private void newRowWidths(){
		int count;
		for(int y = 0; y<height; y++){
			 count = 0;
			for(int i = 0; i < width; i++){
				if(grid[i][y])count++;
			}
			rowWidths[y]=count;
		}


	}
	//counts full rows
	public int fullRows(){
		int count = 0;
        for (int rowWidth : rowWidths) {
            if (rowWidth == width) count++;
        }
		return count;

	}


	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		if(outOfBounds(y, height)) return -1;
		return rowWidths[y];
	}


	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if(outOfBounds(x, width) || outOfBounds(y, height)){
			return true;
		}
		return grid[x][y];
	}


	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.

	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
    public int place(Piece piece, int x, int y) {
        if (!committed)
            throw new RuntimeException("place commit problem");

        // Bounds check first
        if (x < 0 || y < 0 || x + piece.getWidth() > width || y + piece.getHeight() > height) {
            return PLACE_OUT_BOUNDS;
        }

        // Check for bad placement
        for (TPoint point : piece.getBody()) {
            if (grid[x + point.x][y + point.y]) {
                return PLACE_BAD;
            }
        }

        // Do backup (only after bounds and collision checks)
        for (int i = 0; i < width; i++) {
            System.arraycopy(grid[i], 0, previousState[i], 0, grid[i].length);
        }
        System.arraycopy(rowWidths, 0, rowWidthsBackUp, 0, rowWidths.length);
        System.arraycopy(colHeights, 0, colHeightsBackUp, 0, colHeights.length);
        maxHightBackUp = maxHeight;

        // Place the piece
        int result = PLACE_OK;
        for (TPoint point : piece.getBody()) {
            int px = x + point.x;
            int py = y + point.y;
            grid[px][py] = true;
            rowWidths[py]++;
            colHeights[px] = Math.max(colHeights[px], py + 1);
        }

        // Update maxHeight
        for (int i = 0; i < width; i++) {
            maxHeight = Math.max(maxHeight, colHeights[i]);
        }

        // Check if any row is filled
        for (int i = y; i < y + piece.getHeight(); i++) {
            if (rowWidths[i] == width) {
                result = PLACE_ROW_FILLED;
            }
        }

        committed = false;
        sanityCheck();
        return result;
    }



    /**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		if(fullRows()>0){
			for(int i = 0; i < maxHeight; ){
				if(rowWidths[i]==width){
					rowsCleared++;
					for(int j = i; j < maxHeight-1; j++){

						for (int a = 0; a < width; a++) {
								grid[a][j] = grid[a][j + 1];

						}
					}
					for (int a = 0; a < width; a++) {
						grid[a][maxHeight - 1] = false;
					}
					newRowWidths();



				}else{
					i++;
				}

			}
		}
		newRowWidths();
		newColHeights();
		newMaxHeight();

		sanityCheck();

		return rowsCleared;
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed) return;
		int[] tempCols = colHeights;
		colHeights = colHeightsBackUp;
		colHeightsBackUp = tempCols;

		int[] tempRows = rowWidths;
		rowWidths = rowWidthsBackUp;
		rowWidthsBackUp = tempRows;


		int tempMax = maxHeight;
		maxHeight = maxHightBackUp;
		maxHightBackUp = tempMax;


		boolean[][] tempGrid = grid;
		grid = previousState;
		previousState = tempGrid;
		sanityCheck();
		commit();


	}


	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}



	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility)
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


