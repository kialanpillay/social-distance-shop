//M. M. Kuttel
//Class to represent the shop, as a grid of gridblocks

 
package socialDistanceShopSampleSolution;

public class ShopGrid {
	private GridBlock [][] Blocks;
	private final int x;
	private final int y;
	public final int checkout_y;
	private final static int minX =5;//minimum x dimension
	private final static int minY =5;//minimum y dimension
	
	
	ShopGrid() throws InterruptedException {
		this.x=20;
		this.y=20;
		this.checkout_y=y-3;
		Blocks = new GridBlock[x][y];
		int [] [] dfltExit= {{10,10}};
		this.initGrid(dfltExit);
	}
	
	ShopGrid(int x, int y, int [][] exitBlocks,int maxPeople) throws InterruptedException {
		if (x<minX) x=minX; //minimum x
		if (y<minY) y=minY; //minimum x
		this.x=x;
		this.y=y;
		this.checkout_y=y-3;
		Blocks = new GridBlock[x][y];
		this.initGrid(exitBlocks);
	}
	
	private  void initGrid(int [][] exitBlocks) throws InterruptedException {
		for (int i=0;i<x;i++) {
			for (int j=0;j<y;j++) {
				boolean exit=false;
				boolean checkout=false;
				for (int e=0;e<exitBlocks.length;e++)
						if ((i==exitBlocks[e][0])&&(j==exitBlocks[e][1])) 
							exit=true;
				if (j==(y-3)) {
					checkout=true; 
				}//checkout is hardcoded two rows before  the end of the shop
				Blocks[i][j]=new GridBlock(i,j,exit,checkout);
			}
		}
	}
	
	//get max X for grid
	public  int getMaxX() {
		return x;
	}
	
	//get max y  for grid
	public int getMaxY() {
		return y;
	}

	public GridBlock whereEntrance() { //hard coded entrance
		return Blocks[getMaxX()/2][0];
	}

	//is a position a valid grid position?
	public  boolean inGrid(int i, int j) {
		if ((i>=x) || (j>=y) ||(i<0) || (j<0)) 
			return false;
		return true;
	}
	
	//called by customer when entering shop
	public GridBlock enterShop() throws InterruptedException  {
		GridBlock entrance = whereEntrance();
		return entrance;
	}
		
	//called when customer wants to move to a location in the shop
	public GridBlock move(GridBlock currentBlock,int step_x, int step_y) throws InterruptedException {  
		//try to move in 
		
		int c_x= currentBlock.getX();
		int c_y= currentBlock.getY();
		
		int new_x = c_x+step_x; //new block x coordinates
		int new_y = c_y+step_y; // new block y  coordinates
		
		//restrict i an j to grid
		if (!inGrid(new_x,new_y)) {
			//Invalid move to outside shop - ignore
			return currentBlock;
			
		}

		if ((new_x==currentBlock.getX())&&(new_y==currentBlock.getY())) //not actually moving
			return currentBlock;
		 
		GridBlock newBlock = Blocks[new_x][new_y];
		
			if (newBlock.get())  {  //get successful because block not occupied 
					currentBlock.release(); //must release current block
				}
			else {
				newBlock=currentBlock;
				///Block occupied - giving up
			}
		return newBlock;
	} 
	
	//called by customer to exit the shop
	public void leaveShop(GridBlock currentBlock)   {
		currentBlock.release();
	}

}


	

	

