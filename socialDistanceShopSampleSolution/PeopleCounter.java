package socialDistanceShopSampleSolution;

//class to keep track of people inside and outside and left shop
public class PeopleCounter {
	private int peopleOutSide; //counter for people arrived but not yet in the building
	private int peopleInside; //people inside the shop
	private int peopleLeft; //people left the shop
	private int maxPeople; //maximum for lockdown rules
	
	PeopleCounter(int max) {
		peopleOutSide = 0;
		peopleInside = 0;
		peopleLeft = 0;
		maxPeople = max;
	}
		
	//getter
	synchronized public int getWaiting() {
		return peopleOutSide;
	}

	//getter
	synchronized public int getInside() {
		return peopleInside;
	}
	
	//getter
	synchronized public int getTotal() {
		return (peopleOutSide+peopleInside+peopleLeft);
	}

	//getter
	synchronized public int getLeft() {
		return peopleLeft;
	}
	
	//getter
	synchronized public int getMax() {
		return maxPeople;
	}
	
	//getter
	synchronized public void personArrived() {
		peopleOutSide++;
	}
	
	//update counters for a person entering the shop
	synchronized public void personEntered() {
		peopleOutSide--;
		peopleInside++;
	}

	//update counters for a person exiting the shop
	synchronized public void personLeft() {
		peopleInside--;
		peopleLeft++;
		
	}

	//reset - not really used
	synchronized public void resetScore() {
		peopleInside = 0;
		peopleOutSide = 0;
		peopleLeft = 0;
	}
}
