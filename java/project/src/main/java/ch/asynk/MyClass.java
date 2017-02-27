package ch.asynk;

import lombok.Getter;

public class MyClass
{
    static private String msg = "Hello World";

    private @Getter int n;

    public MyClass()
    {
        n = 666;
    }

    public void print()
    {
        System.err.println(msg);
    }

    public void exception() throws MyException
    {
        throw new MyException("it's mine");
    }
}
