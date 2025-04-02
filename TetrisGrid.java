//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {
	private boolean [][] grid;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid=grid;
	}
	private boolean emptyLine;
	private boolean fullLine;
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		emptyLine = true;
		 fullLine = true;
		for(int i = 0; i<grid[0].length; i++){
			fullLine = true;
			emptyLine = true;
			lineState(i);
			if(emptyLine){
				break;
			}
			if(fullLine){
				for(int j = i; j<grid[0].length - 1; j++ ){
					lineState(i);
					if(emptyLine) break;
					for(int k = 0; k < grid.length; k++){
						grid[k][j]=grid[k][j+1];
					}

				}
				if(!emptyLine){
					for(int k = 0; k < grid.length; k++){
						grid[k][grid[0].length-1]=false;
					}
				}
			}


		}
	}
	private void lineState(int i){
		for (int j = 0; j<grid.length; j++) {
			if (!grid[j][i]) {
				fullLine = false;

			}else{
				emptyLine = false;
			}
			if(!emptyLine && !fullLine){
				break;
			}


		}
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		clearRows();
		return grid;
	}
}
