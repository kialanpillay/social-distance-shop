package socialDistanceShopSampleSolution;

// GridBlock class to represent a block in the shop.

public class GridBlock {
	private boolean isOccupied;
	private final boolean isExit; 
	private final boolean isCheckoutCounter;
	private int [] coords; // the coordinate of the block.
	private int ID;
	
	public static int classCounter=0;
	
	GridBlock(boolean exitBlock, boolean checkoutBlock) throws InterruptedException {
		isExit=exitBlock;
		isCheckoutCounter=checkoutBlock;
		isOccupied= false;
		ID=classCounter;
		classCounter++;
	}
	
	GridBlock(int x, int y, boolean exitBlock, boolean refreshBlock) throws InterruptedException {
		this(exitBlock,refreshBlock);
		coords = new int [] {x,y};
	}
	
	//getter
	public  int getX() {return coords[0];}  
	
	//getter
	public  int getY() {return coords[1];}
	
	//for customer to move to a block
	public boolean get() throws InterruptedException {
		isOccupied=true;
		return true;
	}
		
	//for customer to leave a block
	public  void release() {
		isOccupied =false;
	}
	
	//getter
	public boolean occupied() {
		return isOccupied;
	}
	
	//getter
	public  boolean isExit() {
		return isExit;	
	}

	//getter
	public  boolean isCheckoutCounter() {
		return isCheckoutCounter;
	}
	
	//getter
	public int getID() {return this.ID;}
}
