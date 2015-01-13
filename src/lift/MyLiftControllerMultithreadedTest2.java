package lift;

/**
 * COMP2007 Lift Simulator Coursework.
 * Framework for Unit Testing MyLiftController class.
 * 
 * Tests whether a person blocks when pushing 'callFloor'
 * until the lift reaches that floor and opens its door
 * (assuming the lift is going in the correct direction).
 *
 * This does NOT check whether the lift controller correctly
 * indicates whether doors should open (i.e. Test 1) since we
 * are unit testing orthogonal functionality (the correct
 * blocking/release of people threads).
 * 
 * @author K. Bryson
 */

import edu.umd.cs.mtc.MultithreadedTestCase;

public class MyLiftControllerMultithreadedTest2 extends MultithreadedTestCase {

    private MyLiftController lift_controller;

    @Override
    public void initialize() {
        lift_controller = new MyLiftController();
    }

    // Person thread 1.
    public void thread1() throws InterruptedException {

        // Person calls the lift to floor 4, going UP.
        lift_controller.callLift(4, LiftController.Direction.UP);

        // Person thread should only be here when tick 2 has occurred ...
        // since this indicates the lift thread has actually opened the doors.
        assertTick(2);
    }

    // Person thread 2.
    public void thread2() throws InterruptedException {

        // Person calls the lift to floor 7, going DOWN.
        lift_controller.callLift(7, LiftController.Direction.DOWN);

        // Person thread should only be here when tick 3 has occurred ...
        assertTick(3);
    }

    // Person thread 3.
    public void thread3() throws InterruptedException {

        // Person calls the lift to floor 4, going DOWN.
        lift_controller.callLift(4, LiftController.Direction.DOWN);

        // Person thread should only be here when tick 4 has occurred ...
        assertTick(4);
    }


    // Lift thread 4.
    public void thread4() throws InterruptedException {

        // Force Lift thread to wait until person threads are blocked.
        waitForTick(1);
        
        for (int level = 0; level < 4; level++) {
            // Level 0,1,2 and 3.
            lift_controller.liftAtFloor(level, LiftController.Direction.UP);
        }

        // Level 4 ... should open doors ... but we do not test this.
        lift_controller.liftAtFloor(4, LiftController.Direction.UP);

        // The Person thread should still be blocked though ...
        waitForTick(2);

        // Open the doors ... Person Thread 1 should now continue.
        lift_controller.doorsOpen(4);
        lift_controller.doorsClosed(4);

        // No doors for 5 to 8.
        for (int level = 5; level < 8; level++) {
            lift_controller.liftAtFloor(level, LiftController.Direction.UP);
        }

        lift_controller.liftAtFloor(8, LiftController.Direction.DOWN);

        // Level 7 ... going down should open doors ...
        lift_controller.liftAtFloor(7, LiftController.Direction.DOWN);

        // The Person thread 2 should still be blocked though ...
        waitForTick(3);

        // Open the doors ... Person Thread 2 should now continue.
        lift_controller.doorsOpen(7);
        lift_controller.doorsClosed(7);

        // No doors for 6 to 5.
        for (int level = 6; level > 4; level--) {
            lift_controller.liftAtFloor(level, LiftController.Direction.DOWN);
        }

        // Level 4 ... going down should open doors ...
        lift_controller.liftAtFloor(4, LiftController.Direction.DOWN);

        // The Person thread 3 should still be blocked though ...
        waitForTick(4);

        // Open the doors ... Person Thread 3 should now continue.
        lift_controller.doorsOpen(4);
        lift_controller.doorsClosed(4);
    }
}
