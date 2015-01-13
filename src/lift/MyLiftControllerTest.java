package lift;

/**
 * COMP2007 Lift Simulator Coursework.
 * Framework for Unit Testing MyLiftController class.
 * @author K. Bryson
 */

import edu.umd.cs.mtc.TestFramework;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class MyLiftControllerTest {

    public MyLiftControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLiftSingle1() throws Throwable {
        System.out.println("TEST 1: DOES LIFT CONTROLLER CORRECTLY TELL LIFT TO OPEN DOORS ON 'callLift' METHOD?");
        TestFramework.runOnce( new MyLiftControllerMultithreadedTest1() );
    }

    @Test
    public void testLiftMultiple1() throws Throwable {
        System.out.println("TEST 1: MULTIPLE TIMES TO DETECT INTERMITTENT FAILURES.");
        TestFramework.runManyTimes( new MyLiftControllerMultithreadedTest1(),100);
    }

    @Test
    public void testLiftSingle2() throws Throwable {
        System.out.println("TEST 2: DO PERSON THREADS WAIT CORRECTLY WHEN CALLING LIFT?");
        TestFramework.runOnce( new MyLiftControllerMultithreadedTest2() );
    }

    @Test
    public void testLiftMultiple2() throws Throwable {
        System.out.println("TEST 2: MULTIPLE TIMES TO DETECT INTERMITTENT FAILURES.");
        TestFramework.runManyTimes( new MyLiftControllerMultithreadedTest2(),100);
    }

    @Test
    public void testLiftSingle3() throws Throwable {
        System.out.println("TEST 3: DOES LIFT CONTROLLER CORRECTLY TELLS LIFT TO OPEN DOORS ON 'selectFloor' METHOD?");
        TestFramework.runOnce( new MyLiftControllerMultithreadedTest3() );
    }

    @Test
    public void testLiftMultiple3() throws Throwable {
        System.out.println("TEST 3: MULTIPLE TIMES TO DETECT INTERMITTENT FAILURES.");
        TestFramework.runManyTimes( new MyLiftControllerMultithreadedTest3(),100);
    }

    @Test
    public void testLiftSingle4() throws Throwable {
        System.out.println("TEST 4: DOES PERSON THREAD LEAVE LIFT AT THE CORRECT TIME?");
        TestFramework.runOnce( new MyLiftControllerMultithreadedTest4() );
    }

    @Test
    public void testLiftMultiple4() throws Throwable {
        System.out.println("TEST 4: MULTIPLE TIMES TO DETECT INTERMITTENT FAILURES.");
        TestFramework.runManyTimes( new MyLiftControllerMultithreadedTest4(),100);
    }

    @Test
    public void testLiftSingle5() throws Throwable {
        System.out.println("TEST 5: DO ALL PEOPLE THREADS ENTER/EXIT THE LIFT BEFORE DOORS CLOSE?");
        TestFramework.runOnce( new MyLiftControllerMultithreadedTest5());
    }

    @Test
    public void testLiftMultiple5() throws Throwable {
        System.out.println("TEST 5: MULTIPLE TIMES TO DETECT INTERMITTENT FAILURES.");
        TestFramework.runManyTimes( new MyLiftControllerMultithreadedTest5(),100);
    }
}