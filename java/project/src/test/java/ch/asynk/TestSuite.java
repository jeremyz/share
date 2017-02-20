package ch.asynk;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.BeforeClass;
import org.junit.AfterClass;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   Test00.class
})

public class TestSuite
{
     @BeforeClass
     public static void setUpClass()
     {
         System.out.println("BeforeClass : TestSuite");
     }

     @AfterClass
     public static void tearDownClass()
     {
         System.out.println("AfterClass : TestSuite");
     }
}
