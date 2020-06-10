package socialDistanceShopSampleSolution;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.atomic.*;

public class CustomerLocation  { // this is a separate class so don't have to access thread

/*
This class is not modified because it already has the requiste mechanisms implemented to ensure thread-safety.
This class is accessed by Customer threads, after moving between different blocks in shared ShopGrid
(have different GridBlock assigned to it after moving between).
An array of class objects is also shared between the Inspector class, and used to identify rule violations, by comparing the locations of each customer
the ShopView class, which uses the locations to render customers correctly in the simulation
as well as the SocialDistancingShop parent thread.
This class ensures thread-safety as well as liveness by using Atomic Variables, which guarantee the atomicity of operations.
This means that their operations are indivisible and cannot be interrupted by another process or thread.
Atomic variables are implemented with the help of hardware atomic instructions
 */
	
//can protect with Atomic variables or with synchronized	
	private final int ID; //total customers created
	private Color myColor;
	private AtomicBoolean inRoom; //AtomicBoolean variable is declared to store a boolean value indicating is a Customer thread has entered the store
	private AtomicInteger x; //AtomicInteger variable declared to store the x-coordinate of a Customer object thread
	private AtomicInteger y;//AtomicInteger variable declared to store the y-coordinate of a Customer object thread
	
	CustomerLocation(int ID ) {
		Random rand = new Random();
		float c = rand.nextFloat();
		myColor = new Color(c, rand.nextFloat(), c);	//only set at beginning	by thread
		inRoom = new AtomicBoolean(false); //AtomicBoolean is constructed and assigned a value of false
		//All customers are initially outside the store at the start of the simulation
		this.ID=ID; 
		this.x = new AtomicInteger(0); //AtomicInteger is constructed and assigned a value of 0
		this.y = new AtomicInteger(0); //AtomicInteger is constructed and assigned a value of 0
	}

	/*
	The following Accessor and Mutator methods were not modified because they in turn call atomic operations of the internal variables
	that update the respective Atomic variable.
	Use of atomic variables prevents data races from occurring, and removes the threat of deadlock that arises from using Java Locks
	or Semaphores that never have their permits released.
	*/
	//setter
	public  void  setX(int x) { this.x.set(x);}	//Calls an atomic operation that atomically sets the variable with the value of the argument
		
	//setter
	public   void  setY(int y) {	this.y.set(y);	} //Calls an atomic operation that atomically sets the variable with the value of the argument
	
	//setter
	public  void setInRoom(boolean in) {
		this.inRoom.set(in); //Calls an atomic operation that atomically sets the variable with the value of the argument
}
	//getter
	public  int getX() { return x.get();} //Value of variable is read atomically; cannot be modified until the method has executed
	
	//getter
	public  int getY() {	return y.get();	} //Value of variable is read atomically; cannot be modified until the method has executed
	
	//getter
		public  int getID() {	return ID;	}

	//getter
		public  boolean inRoom() { //Value of AtomicBoolean variable is read atomically; cannot be modified until the method has executed
			return inRoom.get();
		}
	//getter
	//Method is synchronized on the object's intrinsic lock, to ensure thread-safe access and eliminate data races

	public synchronized  Color getColor() { return myColor; }
		
}
