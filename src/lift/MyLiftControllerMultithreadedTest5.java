package lift;

/**
 * COMP2007 Lift Simulator Coursework.
 * Framework for Unit Testing MyLiftController class.
 * 
 * This attempts to test that the 'lift thread' DOES NOT return
 * until all threads entering and exiting the lift at a particular
 * floor have entered or exited ... thereupon it can close the doors.
 *
 * There is a possibility that this test will fail occasionally
 * since threads will be switched just outside the monitor
 * region before registering they have left ... hence this
 * test should be run multiple times to see percentage of
 * failures (this failure of the test should be uncommon).
 * 
 * @author K. Bryson
 */

import org.junit.Assert;
import edu.umd.cs.mtc.MultithreadedTestCase;

public class MyLiftControllerMultithreadedTest5 extends MultithreadedTestCase {

    private MyLiftController lift;

    // This is used to count how many people may have missed the door.
    // It should only be accessed while holding the MyLiftController's intrinsic lock.
    private int people_count = 0;

    @Override
    public void initialize() {
        lift = new MyLiftController();
    }

    // Person thread 1.
    public void thread1() throws InterruptedException {
        // Person calls the lift to floor 4, going UP.
        lift.callLift(4, LiftController.Direction.UP);

        // Count this person as missed if not exited on time.
        synchronized (this) {
           people_count++;
        }

        synchronized(lift) {
           lift.selectFloor(6);
           
           synchronized (this) {
              people_count--;
           }
        }
    }

    // Person thread 2.
    public void thread2() throws InterruptedException {
        // Person calls the lift to floor 4, going UP.
        lift.callLift(4, LiftController.Direction.UP);
        
        // Count this person as missed if not exited on time.
        synchronized (this) {
           people_count++;
        }

        synchronized(lift) {
           lift.selectFloor(6);
           
           synchronized (this) {
              people_count--;
           }
        }
    }

    // Person thread 3.
    public void thread3() throws InterruptedException {
        // Person calls the lift to floor 6, going UP.
        // Count this person as missed if not entered on time.

        synchronized (this) {
           people_count++;
        }

        synchronized (lift) {
           lift.callLift(6, LiftController.Direction.UP);

           synchronized (this) {
              people_count--;
           }
        }
    }

    // Person thread 4.
    public void thread4() throws InterruptedException {
        // Person calls the lift to floor 6, going UP.
        // Count this person as missed if not entered on time.
        synchronized (this) {
           people_count++;
        }

        synchronized (lift) {
           lift.callLift(6, LiftController.Direction.UP);

           synchronized (this) {
              people_count--;
           }
        }
    }

    // Person thread 5.
    public void thread5() throws InterruptedException {
        // Person calls the lift to floor 6, going UP.
        synchronized (this) {
           people_count++;
        }

        synchronized (lift) {
           lift.callLift(6, LiftController.Direction.UP);

           synchronized (this) {
              people_count--;
           }
        }
    }


    // Lift thread 6.
    public void thread6() throws InterruptedException {

        // Force Lift thread to wait until person threads are blocked.
        waitForTick(1);
        
        for (int level = 0; level < 4; level++) {
            // Level 0,1,2 and 3.
            lift.liftAtFloor(level, LiftController.Direction.UP);
        }

        // Level 4 ... should open doors ...
        lift.liftAtFloor(4, LiftController.Direction.UP);

        lift.doorsOpen(4);
        lift.doorsClosed(4);

        // Lift thread should wait until both person threads 'select' floor.
        waitForTick(2);

        // Should not open at floor 5.
        lift.liftAtFloor(5, LiftController.Direction.UP);

        // Should open on level 6 for people to enter/exit.
        lift.liftAtFloor(6, LiftController.Direction.UP);

        // All 5 people should now be waiting to enter/exit the lift on floor 6.
        synchronized(this) {
            Assert.assertEquals(5, people_count);
        }
        
        // Open the doors even if incorrect previous assertion.
        lift.doorsOpen(6);

        // BEFORE CLOSING THE DOOR ... PEOPLE THREADS SHOULD HAVE
        // ENTERED/EXITED and so the people_count should now be zero.
        
        synchronized(this) {
            Assert.assertEquals(0, people_count);
        }
        lift.doorsClosed(6);
    }

    @Override public void finish() {

    }
}
