
import java.util.List;          // interface
import java.util.Vector;        // implementation of interface List

interface TestInterface {
    public void run();
}

class Test0 implements TestInterface {

    // simple inheritance and method overriding -> polymorphism
    class Thing {
        public void sayHello() { System.out.println(" class Thing   says I'm a "+this.getClass().getName()); }
    }
    class Chicken extends Thing {
        @Override
        public void sayHello() { System.out.println(" class Chicken says I'm a "+this.getClass().getName()); }
    }
    class Egg extends Thing {
        @Override
        public void sayHello() { System.out.println(" class Egg     says I'm a "+this.getClass().getName()); }
    }

    public void talk(Thing t) { t.sayHello(); }

    @Override
    public void run() {
        System.out.println(this.getClass().getName());
        talk(new Thing());
        talk(new Chicken());
        talk(new Egg());
    }
}

class Test1 implements TestInterface {

    // simple inheritance and method overriding -> polymorphism
    // forcing method override with abstract class (can't be instanciated) and method
    abstract class Thing {
        // no default behavior
        public abstract void sayHello();
        // usage of abstract method that can't be modified
        public final void talk() { sayHello(); }
    }
    class Chicken extends Thing {
        @Override
        public void sayHello() { System.out.println(" class Chicken says I'm a "+this.getClass().getName()); }
    }
    class Egg extends Thing {
        @Override
        public void sayHello() { System.out.println(" class Egg     says I'm a "+this.getClass().getName()); }
    }


    @Override
    public void run() {
        System.out.println(this.getClass().getName());
        (new Chicken()).talk();
        (new Egg()).talk();
    }
}

class Test2 implements TestInterface {

    // wrong usage of an interface, this should be used for non related classes that would implement something in common
    // ie: Interface Comparable, which specify the behavior of a particular data type
    // or to take advantage of multiple inheritance of types.
    interface Thing {
        public void sayHello();
    }
    class Chicken implements Thing {
        @Override
        public void sayHello() { System.out.println(" class Chicken says I'm a "+this.getClass().getName()); }
    }
    class Egg implements Thing {
        @Override
        public void sayHello() { System.out.println(" class Egg says     I'm a "+this.getClass().getName()); }
    }

    public void talk(Thing t) { t.sayHello(); }

    @Override
    public void run() {
        System.out.println(this.getClass().getName());
        talk(new Chicken());
        talk(new Egg());
    }
}

public class PatternInterfaceInheritance
{
    public static void main (String [] args )
    {
        List<TestInterface> tests = new Vector<TestInterface>();
        tests.add(new Test0());
        tests.add(new Test1());
        tests.add(new Test2());

        for (TestInterface test : tests) test.run();
    }
}

