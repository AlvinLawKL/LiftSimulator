package lift;

/**
 * COMP2007 Lift Simulator Coursework.
 * Framework for Unit Testing MyLiftController class.
 * 
 * This tests whether the lift controller correctly informs the lift
 * whether to open its doors (or not) at particular levels when the
 * 'selectFloor' method is called.
 *
 * Again it just tries to test this single 'unit' of functionality while
 * not reporting other problems (tested within Test1 & Test2).
 *
 * However it tries to do this within the context of a complete person lifecycle
 * in case there are dependencies within the lift controller.
 * (So it is possible this test may fail since Test2 fails because the people
 * threads may remain blocked in 'callLift' when they should have continued.)
 * 
 * @author K. Bryson
 */

import org.junit.Assert;
import edu.umd.cs.mtc.MultithreadedTestCase;

public class MyLiftControllerMultithreadedTest3 extends MultithreadedTestCase {

    private MyLiftController lift;

    @Override
    public void initialize() {
        lift = new MyLiftController();
    }

    // Person thread 1.
    public void thread1() throws InterruptedException {
        // Person calls the lift to floor 4, going UP to floor 6.
        lift.callLift(4, LiftController.Direction.UP);
        lift.selectFloor(6);
    }

    // Lift thread 2.
    public void thread2() throws InterruptedException {

        // Force Lift thread to wait until person thread
        // is blocked indicating it has called the lift.
        waitForTick(1);
        
        for (int level = 0; level < 4; level++) {
            // Level 0,1,2 and 3 ... should not open doors ... hence return FALSE
        	// but not checked due to this test being orthogonal to other tests.
            lift.liftAtFloor(level, LiftController.Direction.UP);
        }

        // Level 4 ... should open doors ...
        lift.liftAtFloor(4, LiftController.Direction.UP);

        // Open the doors.
        lift.doorsOpen(4);
        lift.doorsClosed(4);

        // This ensure the lift waits for the person to select new floor.
        waitForTick(2);

        // SHOULD NOT OPEN DOOR at level 5.
        Assert.assertFalse(lift.liftAtFloor(5, LiftController.Direction.UP));

        // SHOULD OPEN DOOR on level 6 due TO 'selectFloor()' method.
        Assert.assertTrue(lift.liftAtFloor(6, LiftController.Direction.UP));

        // Open the doors even if incorrect previous assertion.
        lift.doorsOpen(6);
        lift.doorsClosed(6);

    }
}
