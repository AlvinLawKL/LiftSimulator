
package lift;

/**
 * This default lift controller doesn't work very well!
 * You need to implement this lift controller as specified.
 * @author K. Bryson
 */
public class MyLiftController implements LiftController {
	
	private volatile int currentFloor = 0;
	private volatile boolean doorIsOpen = false;
	private volatile Direction currentDirection;
	private int[] numOfPersonCalledUP = new int[Main.NUMBER_FLOORS];
	private int[] numOfPersonCalledDOWN = new int[Main.NUMBER_FLOORS];
	private int[] numOfPersonSelected = new int[Main.NUMBER_FLOORS];
	
    /* Interface for People */
    public synchronized void callLift(int floor, Direction direction) throws InterruptedException {
    	if (direction == LiftController.Direction.UP) {
    		numOfPersonCalledUP[floor] += 1;
    	}
    	else {
    		numOfPersonCalledDOWN[floor] += 1;
    	}
    	
    	while (currentFloor != floor || currentDirection != direction || !doorIsOpen) {
    		wait();
    	}
    	
    	if (direction == LiftController.Direction.UP) {
    		numOfPersonCalledUP[floor] -= 1;
    	}
    	else {
    		numOfPersonCalledDOWN[floor] -= 1;
    	}
    	notify();
    }

    public synchronized void selectFloor(int floor) throws InterruptedException{
    	numOfPersonSelected[floor] += 1;
    	notifyAll();
    	while(currentFloor != floor || !doorIsOpen) {
    		wait();
    	}
    	numOfPersonSelected[floor] -= 1;
    	notify();
    }

    
    /* Interface for Lifts */
    public boolean liftAtFloor(int floor, Direction direction) {
    	currentFloor = floor;
    	currentDirection = direction;
    	if ((numOfPersonCalledUP[floor] > 0 && direction == LiftController.Direction.UP) || (numOfPersonCalledDOWN[floor] > 0 && direction == LiftController.Direction.DOWN) || numOfPersonSelected[floor] > 0) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    public synchronized void doorsOpen(int floor) throws InterruptedException {
    	doorIsOpen = true;
    	notifyAll();
    	while ((numOfPersonCalledUP[floor] > 0 && currentDirection == LiftController.Direction.UP) || (numOfPersonCalledDOWN[floor] > 0 && currentDirection == LiftController.Direction.DOWN) || numOfPersonSelected[floor] > 0) {
    		wait();
    	}
    }

    public void doorsClosed(int floor) {
    	doorIsOpen = false;
    }

}