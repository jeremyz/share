package ch.asynk;

import org.junit.Rule;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasProperty;

import ch.asynk.MyClass;

public class Test00
{
    private static MyClass my;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

     @BeforeClass
     public static void setUpClass()
     {
         System.out.println("  BeforeClass : Test00");
         my = new MyClass();
     }

     @AfterClass
     public static void tearDownClass()
     {
         System.out.println("  AfterClass : Test00");
     }

     @Test
     public void test_0()
     {
         assertEquals("n must be equal to 666", my.getN(), 666);
     }

     @Test
     public void test_1() throws MyException
     {
         thrown.expect(MyException.class);
         thrown.expectMessage("it's mine");
         my.exception();
     }
     @Test
     public void test_2() throws MyException
     {
         assertThat(my, hasProperty("n", is(666)));
     }
}
