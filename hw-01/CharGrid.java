// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int minRow = grid.length;
		int minColumn = grid[0].length ;
		int maxRow = -1;
		int maxColumn = -1;
		for(int i = 0; i < grid.length; i++){
			for (int j = 0; j<grid[0].length; j++){
				if(grid[i][j]==ch){
					minRow  = Math.min(i, minRow);
					minColumn =Math.min(j, minColumn);
					maxRow = Math.max(i, maxRow);
					maxColumn =  Math.max(maxColumn, j);
				}

			}
		}
		if(maxRow==-1){
			return 0;
		}

		return (maxRow-minRow + 1)* (maxColumn-minColumn+1);
	}

	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int result = 0;
		char ch;
		for(int i = 1; i < grid.length - 1; i++){
			for(int j = 1; j< grid[0].length-1; j++){
				ch = grid[i][j];
				if(armLength(ch, i, j, +1, 0)==armLength(ch, i, j, -1,0) &&
				armLength(ch, i, j, 0,1)==armLength(ch, i, j, 0, -1)){
					result++;
				}
			}

		}
		return result;
	}


	private boolean isIn (int r, int c){
		if(r<0 || c<0) {return false;}
		if(r>= grid.length || c>=grid[0].length){return false;}
		return true;
	}

	private int armLength(char ch, int i, int j, int hdr, int vdr){
		int res = 0;
		while (isIn(i,j) && grid[i][j]==ch ){
			res++;
			i += hdr;
			j += vdr;
		}
		if(res>1){
			return res;
		}
		return (vdr==0)?hdr:vdr;


	}
	
}
