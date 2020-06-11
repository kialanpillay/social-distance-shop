package socialDistanceShopSampleSolution;

//class to keep track of people inside and outside and left shop

/*
This class is modified to conform to the Java Monitor Pattern.
All data members are private, and all class methods are synchronised on the object's intrinsic lock.
This ensures that access to the class is thread-safe and not subject to data races.
The Java Monitor pattern is an provides a convenient and effective mechanism for process synchronization
although it reduces the liveness of the program, since threads serially access any method of the class.
The methods in this class are called by Customer threads, single CounterDisplay thread, and the Inspector thread; 
each class has access to a shared PeopleCounter object, which is declared in SocialDistancingShop.java
*/

public class PeopleCounter {
	//All variables are declared with the private access modifier.
	private int peopleOutSide; //counter for people arrived but not yet in the building
	private int peopleInside; //people inside the shop
	private int peopleLeft; //people left the shop
	private int maxPeople; //maximum for lockdown rules
	
	PeopleCounter(int max) {
		peopleOutSide = 0;
		peopleInside = 0;
		peopleLeft = 0;
		maxPeople = max; //Corrected code to initialise the internal variable with the argument, instead of 0
	}
		
	//getter
	//Method signature modified to contain the synchronized keyword; method is synchronized on the object's own lock.
	//Access to internal data members is exclusive as only one thread can be active in the Monitor object at a time.
	//Variables are safe from data races, and no data inconsistency can occur if multiple threads call the accessor.
	synchronized public int getWaiting() {
		return peopleOutSide;
	}

	//getter
	//Method signature modified to contain the synchronized keyword; method is synchronized on the object's own lock.
	//Access to internal data members is exclusive as only one thread can be active in the Monitor object at a time.
	//Variables are safe from data races, and no data inconsistency can occur if multiple threads call the accessor.
	synchronized public int getInside() {
		return peopleInside;
	}
	
	//getter
	//Method signature modified to contain the synchronized keyword; method is synchronized on the object's own lock.
	//Access to internal data members is exclusive as only one thread can be active in the Monitor object at a time.
	//Variables are safe from data races, and no data inconsistency can occur if multiple threads call the accessor.
	synchronized public int getTotal() {
		return (peopleOutSide+peopleInside+peopleLeft);
	}

	//getter
	//Method signature modified to contain the synchronized keyword; method is synchronized on the object's own lock.
	//Access to internal data members is exclusive as only one thread can be active in the Monitor object at a time
	//Variables are safe from data races, and no data inconsistency can occur if multiple threads call the accessor.
	synchronized public int getLeft() {
		return peopleLeft;
	}
	
	//getter
	//Method signature modified to contain the synchronized keyword; method is synchronized on the object's own lock.
	//Access to internal data members is exclusive as only one thread can be active in the Monitor object at a time.
	//Variables are safe from data races, and no data inconsistency can occur if multiple threads call the accessor.
	synchronized public int getMax() {
		return maxPeople;
	}
	
	//getter
	//Method signature modifed to contain the synchronized keyword; method is synchronized on the object's own lock
	//Access to internal data members is exclusive as only one thread can be active in the Monitor object at a time
	//Variables are safe from data races, and no data inconsistency can occur.
	//peopleOutSide is incremented by a single thread serially; there is no concurrent access
	synchronized public void personArrived() {
		peopleOutSide++;
	}
	
	//update counters for a person entering the shop
	//Method signature modifed to contain the synchronized keyword; method is synchronized on the object's own lock.
	//Access to internal data members is exclusive as only one thread can be active in the Monitor object at a time.
	//Variables are safe from data races, and no data inconsistency can occur.
	//Variables are updated by a single thread serially; there is no concurrent access
	synchronized public void personEntered() {
		peopleOutSide--;
		peopleInside++;
	}

	//update counters for a person exiting the shop
	//Method signature modifed to contain the synchronized keyword; method is synchronized on the object's own lock.
	//Access to internal data members is exclusive as only one thread can be active in the Monitor object at a time.
	//Variables are safe from data races, and no data inconsistency can occur.
	//Variables are updated by a single thread serially; there is no concurrent access
	synchronized public void personLeft() {
		peopleInside--;
		peopleLeft++;
		
	}

	//reset - not really used
	//Method signature modifed to contain the synchronized keyword; method is synchronized on the object's own lock.
	//Access to internal data members is exclusive as only one thread can be active in the Monitor object at a time.
	//Variables are updated by a single thread serially; there is no concurrent access
	synchronized public void resetScore() {
		peopleInside = 0;
		peopleOutSide = 0;
		peopleLeft = 0;
	}
}
