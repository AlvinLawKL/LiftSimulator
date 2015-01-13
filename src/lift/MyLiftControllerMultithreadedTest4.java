package lift;

/**
 * COMP2007 Lift Simulator Coursework.
 * Framework for Unit Testing MyLiftController class.
 * 
 * This tests whether the person correctly exits the lift
 * after it has opened its doors on the correct floor.
 *
 * Again it just tries to test this 'unit' of functionality while not
 * reporting other problems (tested within Test1 / Test2 / Test3 / Test4).
 *
 * However it tries to do this in context of a complete person lifecycle in
 * case there are dependencies within the lift controller.
 * (So this test may fail since Test2 fails because the people threads
 *  may remain blocked in 'callLift' when they should have continued.)
 *  
 * @author K. Bryson
 */


import edu.umd.cs.mtc.MultithreadedTestCase;

public class MyLiftControllerMultithreadedTest4 extends MultithreadedTestCase {

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

        // Person should not leave lift until after doors have opened.
        assertTick(3);
    }

    // Lift thread 2.
    public void thread2() throws InterruptedException {

        // Force Lift thread to wait until person threads are blocked,
        // and the different person threads have called the lift.
        waitForTick(1);
        
        for (int level = 0; level < 4; level++) {
            // Level 0,1,2 and 3 ... should not open doors ... hence return False.
        	// But this is not checked due to having this test orthogonal to others.
            lift.liftAtFloor(level, LiftController.Direction.UP);
        }

        // Level 4 ... should open doors ...
        lift.liftAtFloor(4, LiftController.Direction.UP);

        // Open the doors even if incorrect previous assertion.
        lift.doorsOpen(4);
        lift.doorsClosed(4);

        // This ensure the lift waits for the person to select new floor.
        waitForTick(2);

        // Should not open at floor 5.
        lift.liftAtFloor(5, LiftController.Direction.UP);

        // Should open on floor 6 ... but not tested in this unit test.
        lift.liftAtFloor(6, LiftController.Direction.UP);

        // PERSON SHOULD NOT HAVE LEFT THE LIFT YET ... DOORS NOT OPEN.
        waitForTick(3);

        // Open the doors even if incorrect previous assertion.
        lift.doorsOpen(6);
        lift.doorsClosed(6);

    }

    @Override
    public void finish() {

    }
}
