//M. M. Kuttel
//Class to represent the shop, as a grid of gridblocks

 
package socialDistanceShopSampleSolution;

import java.util.concurrent.Semaphore;

/*This class is modified to include both a binary semaphore and a counting semaphore to ensure thread-safety. 
The binary semaphore functions as a mutual exclusion lock, and its purpose is to only allow one thread to occupy the shop entrance
This is accomplished by protecting the critical section where the GridBlock corresponding to the entrance is constructed 
with the binary semaphore. Initally, this semaphore has one permit, to allow the first customer to enter the shop.
The counting semaphore is required to limit the total number of customers in the shop (there is a maximal amount of Customer threads)
that can have a GridBlock assigned to it). This ensures that the simulation complies with the pandemic regulations.
The semaphore is initialised with the maxPeople parameter, which indicates the user-specified limit on people.
This creates maxPeople number of available permits, allowing threads up to the limit to enter the store before being forced to wait.
The permit for the binary semaphore is released when the Customer thread moves out of the entrance.
Counting semaphore permits are released when Customer threads call the leaveShop method, and have left the store.
*/
public class ShopGrid {
	private GridBlock [][] Blocks;
	private final int x;
	private final int y;
	public final int checkout_y;
	private final static int minX =5;//minimum x dimension
	private final static int minY =5;//minimum y dimension
	private Semaphore multiplex; //Counting semaphore variable multiplex is declared with the private access modifier
	private Semaphore entry;//Binary semaphore variable entry is declared with the private access modifier
	
	
	ShopGrid() throws InterruptedException {
		this.x=20;
		this.y=20;
		this.checkout_y=y-3;
		Blocks = new GridBlock[x][y];
		int [] [] dfltExit= {{10,10}};
		this.initGrid(dfltExit);
		multiplex = new Semaphore(1);
		//Semaphore is constructed with the new keyword, and initialised with a value of 1, since no argument is provided
		//This allows for one thread to acquire the lock without blocking (enter the store)
		entry = new Semaphore(1);
		//Semaphore is constructed with the new keyword, and initialised with a value of 1
		//This allows for one thread to acquire the mutex lock without blocking (occupy the shop entrance).
		//This is neccessary to ensure that a customer can enter the store when the simulation begins, and a;; are not left waiting indefinitely.
	}
	
	ShopGrid(int x, int y, int [][] exitBlocks,int maxPeople) throws InterruptedException {
		if (x<minX) x=minX; //minimum x
		if (y<minY) y=minY; //minimum x
		this.x=x;
		this.y=y;
		this.checkout_y=y-3;
		Blocks = new GridBlock[x][y];
		this.initGrid(exitBlocks);
		multiplex = new Semaphore(maxPeople);
		//Semaphore is constructed with the new keyword, and initialised with a value of maxPeople
		//This allows for maxPeople number of threads to acquire the lock without blocking (concurrently browse the store)
		entry = new Semaphore(1);
		//Semaphore is constructed with the new keyword, and initialised with a value of 1
		//This allows for one thread to acquire the mutex lock without blocking (occupy the shop entrance).
		//This is neccessary to ensure that a customer can enter the store when the simulation begins, and all are not left waiting indefinitely.
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
	//The following accessor methods did not require modification.
	//The variables x and y are declared as private final, and cannot be modified by other threads
	//even if they had the relevant Mutator methods defined
	//Concurrent access it always thread-safe
	//get max X for grid
	public  int getMaxX() {
		return x;
	}
	
	//get max y  for grid
	public int getMaxY() {
		return y;
	}
	//Method is not modified; access is restricted using the binary semaphore permit acquired in the enterShop() method
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
	//Method modified to ensure thread-safety, and constrain the simulation to operate correctly within the designated rules
	public GridBlock enterShop() throws InterruptedException  {
		multiplex.acquire(); //Counting semaphore is decremented by a thread. If the value of the semaphore after being decremented
		// is negative, the thread blocks and waits until permit is released, allowing it to enter the store
		entry.acquire(); //Binary semaphore is decremented by a thread. If there is an avaiable permit, the thread acquires
		//the mutex lock, and can construct a GridBlock object representing the entrance (i.e. can move into the entrance).
		//if there is no permit, the thread is suspended, and waits until the lock is released, which occurs when another thread 
		//releases the lock by incrementing the entry semaphore. This is when the Customer has moved out of the entrance.
		GridBlock entrance = whereEntrance();
		return entrance;
	}
		
	//called when customer wants to move to a location in the shop
	//Method is modified to ensure that the simulation functions correctly, within the designated constraints
	//and is thread-safe. The implementation also ensures there is liveness, and free from deadlock.
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
		//Code added to conditionally return the current GridBlock object, if a Customer thread attempts to move back to the entrance
		//If the condition evaluates to true, the Customer does not move, and the current object is returned.
		if(whereEntrance().getX() == new_x && whereEntrance().getY() == new_y){
			return currentBlock;
		}

		if ((new_x==currentBlock.getX())&&(new_y==currentBlock.getY())) //not actually moving
			return currentBlock;
		 
		GridBlock newBlock = Blocks[new_x][new_y];
		
			if (newBlock.get())  {  //get successful because block not occupied 
					currentBlock.release(); //must release current block
					if(whereEntrance().getX() == c_x && whereEntrance().getY() == c_y){ 
						//Conditional code added to ascertain if a Customer thread previously occupied the entrance block of the shop.
						//If the condition evaluates to true, then this Customer thread holds the mutex lock controlling serial entry into the store
						//i.e. acquired the permit for the semaphore, and should now release the permit to allow other threads to acquire the lock
						//and enter the store one after the other.
						entry.release(); //Binary semaphore is incremented, and one of the waiting threads is resumed and can access the restricted code section.
					}
				}
			else {
				newBlock=currentBlock;
				///Block occupied - giving up
			}
		return newBlock;
	} 
	
	//called by customer to exit the shop
	//Method modified to ensure liveness  and allow Customer threads, subject to the constraints, to browse the store concurrently.
	//The counting semaphore is incremented by a thread in this method to release a permit, and allow other waiting threads to access to store.
	//Key to ensuring that customers can continually enter the store, and are not blocked indefinitely.
	public void leaveShop(GridBlock currentBlock) throws InterruptedException  {
		currentBlock.release();
		multiplex.release(); //The counting semaphore is incremented, releasing a permit, and unblocking one of the waiting threads
	}

}


	

	

