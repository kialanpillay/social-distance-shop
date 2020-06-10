package socialDistanceShopSampleSolution;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.*;

// GridBlock class to represent a block in the shop.

/* 
This class is modified to include a binary semaphore that functions as an mutual exclusion (mutex) lock.
The purpose of this lock (the semaphore) is to ensure that only one Customer thread can be assigned a particular GridBlock object
i.e. only one Customer may occupy a block in the simulation at a time. This is required to satisfy the rules of the simulation
and enforce pandemic safety rules. This is done by protecting critical sections in the code, where the boolean variable isOccupied
is read or written to. This is critical in ensuring that multiple Customer threads do not end up being assigned the same GridBlock
object.
A binary semaphore is a semaphore that has an integer value that can range only between 0 and 1.
*/

public class GridBlock {
	private boolean isOccupied;
	private final boolean isExit; 
	private final boolean isCheckoutCounter;
	private int [] coords; // the coordinate of the block.
	private int ID;
	private Semaphore mutex; //Semaphore variable mutex is declared with the private access modifier
	//Restricts access to critical sections in the code, where the isOccupied variable is read/written to.
	
	public static int classCounter=0;
	
	GridBlock(boolean exitBlock, boolean checkoutBlock) throws InterruptedException {
		isExit=exitBlock;
		isCheckoutCounter=checkoutBlock;
		isOccupied= false;
		ID=classCounter;
		classCounter++;
		mutex = new Semaphore(1); //Semaphore is constructed with the new keyword, and initialised with a value of 1
		//This allows for one thread to acquire the mutex lock without blocking.
		//This is neccessary to ensure that an empty GridBlock can be occupied by a Customer thread.
	}
	
	GridBlock(int x, int y, boolean exitBlock, boolean refreshBlock) throws InterruptedException {
		this(exitBlock,refreshBlock);
		coords = new int [] {x,y};
		mutex = new Semaphore(1); //Semaphore is constructed with the new keyword, and initialised with a value of 1
		//This allows for one thread to acquire the mutex lock without blocking. 
		//This is neccessary to ensure that an empty GridBlock can be occupied by a Customer thread.
		//One permit is assigned to the Semaphore
	}
	
	//getter
	//The following accessor methods did not require modification.
	//Each Customer thread has a thread-local GridBlock, and the shared ShopGrid object has a shared 2D array of GridBlock
	//The coordinates of these blocks in ShopGrid can not be mutated, thus concurrenct access is permitted without synchronization. 
	public  int getX() {return coords[0];}  
	
	//getter
	public  int getY() {return coords[1];}
	
	//for customer to move to a block
	//Method is modified to correctly implement its function
	//Method is called by the shared ShopGrid object when Customer threads invoke move
	//Method returns a boolean value indicating if the GridBlock is occupied or not
	public boolean get() throws InterruptedException {
		mutex.acquire(); //Thread decrements the Semaphore; blocks if value of Semaphore is negative, otherwise proceeds
		//If the Semaphore has an avaiable permit, the thread can continue into the critical section
		//All other threads must wait until the current thread has exited the critical section and released the permit (lock)
		//Thread has exclusive access to the critical section; there are no data races or data inconsistency
		if(!isOccupied){ //If statement checks the internal variable, ascertaining if the block is occupied or not
			isOccupied=true; //If false, the block can be occupied by a customer. isOccupied is set to true.
			mutex.release(); //The semaphore is incremented, releasing the mutex lock, and unblocking one of the waiting threads
			return true; //Method returns true and exits
		}
		//If the block is occupied, the mutex lock is simply released, so other threads may access the critical section
		//This ensures liveness, allowing threads to progress in time.
		mutex.release(); //The semaphore is incremented, releasing the mutex lock, and unblocking one of the waiting threads
		return false; //Method returns false and exists
		
	}
		
	//for customer to leave a block
	//Method is modified to correctly implement its function
	//Method is called by the shared ShopGrid object when Customer threads invoke move, and exit the particular GridBlock
	//The block is the free, and can be assigned to another Customer thread.
	public  void release() throws InterruptedException{
		//Thread decrements the Semaphore; blocks if value of Semaphore is negative, otherwise proceeds
		//If the Semaphore has an avaiable permit, the thread can continue into the critical section.
		mutex.acquire();
		isOccupied =false; //isOccupied is set to false, signifying that the block is free for exclusive occupation
		mutex.release(); //The semaphore is incremented, releasing the mutex lock, and unblocking one of the waiting threads
	}
	
	//getter
	//Method not called by external classes, but still synchronised to ensure thread-safety.
	//Method signature modifed to contain the synchronized keyword; method is synchronized on the object's own lock.
	synchronized public boolean occupied() {
		return isOccupied;
	}
	//The following accessor methods did not require modification.
	//The variables isExit and isCheckoutCounter are declared as private final, and cannot be modified by other threads
	//even if they had the relevant Mutator methods declared
	//Concurrent access it always thread-safe
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
