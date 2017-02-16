
abstract class Human
{
    enum Gender { MALE, FEMALE }
    abstract protected Gender construct();
    private Gender g;

    public Human() {
        g = construct();
    }

    public void say()
    {
        System.out.println("I am a " + g);
    }
}

class Female extends Human
{
    @Override
    protected Gender construct() { return Gender.FEMALE; }
}

class Male extends Human
{
    @Override
    protected Gender construct() { return Gender.MALE; }
}


public class PatternFactory {

    public static void main (String [] args )
    {
        (new Female()).say();
        (new Male()).say();
    }
}

