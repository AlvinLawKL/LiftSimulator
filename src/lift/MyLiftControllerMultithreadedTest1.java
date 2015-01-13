package lift;

/**
 * COMP2007 Lift Simulator Coursework.
 * Framework for Unit Testing MyLiftController class.
 * 
 * This just tests whether the lift controller correctly informs the lift
 * whether to open its doors (or not) at particular levels when people
 * call the lift.
 *
 * @author K. Bryson
 */

import org.junit.Assert;
import edu.umd.cs.mtc.MultithreadedTestCase;

public class MyLiftControllerMultithreadedTest1 extends MultithreadedTestCase {

    private MyLiftController lift_controller;

    @Override
    public void initialize() {
        lift_controller = new MyLiftController();
    }

    // Person thread 1.
    public void thread1() throws InterruptedException {
        // Person calls the lift to floor 4, going UP.
        lift_controller.callLift(4, LiftController.Direction.UP);
    }

    // Person thread 2.
    public void thread2() throws InterruptedException {
        // Person calls the lift to floor 7, going DOWN.
        lift_controller.callLift(7, LiftController.Direction.DOWN);
    }

    // Lift thread 3.
    public void thread3() throws InterruptedException {

        // Force Lift thread to wait until person threads are blocked,
        // and the different person threads have called the lift.
        waitForTick(1);
        
        for (int level = 0; level < 4; level++) {
            // Level 0,1,2 and 3 ... should not open doors ... hence return False.
            Assert.assertFalse(lift_controller.liftAtFloor(level, LiftController.Direction.UP));
        }

        // Level 4 ... should open doors ... hence return True.
        Assert.assertTrue(lift_controller.liftAtFloor(4, LiftController.Direction.UP));

        // Open the doors even if incorrect previous assertion ...
        lift_controller.doorsOpen(4);
        lift_controller.doorsClosed(4);

        // No doors for 5 to 8.
        for (int level = 5; level < 8; level++) {
            Assert.assertFalse(lift_controller.liftAtFloor(level, LiftController.Direction.UP));
        }

        Assert.assertFalse(lift_controller.liftAtFloor(8, LiftController.Direction.DOWN));

        // Level 7 ... going down should open doors ... hence return True.
        Assert.assertTrue(lift_controller.liftAtFloor(7, LiftController.Direction.DOWN));

        // Open the doors even if incorrect previous assertion ...
        lift_controller.doorsOpen(7);
        lift_controller.doorsClosed(7);
    }
}
