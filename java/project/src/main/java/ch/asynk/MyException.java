package ch.asynk;

public class MyException extends Exception
{
    private static final long serialVersionUID = 01235;

    public MyException(String message)
    {
        super(message);
    }
}
