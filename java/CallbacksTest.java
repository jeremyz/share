
abstract class Callback<TRet, TParam>
{
    public abstract TRet call(TParam p);
}

class Caller
{
    private Integer doCall(String s, Callback<Integer, String> cb)
    {
        return cb.call(s);
    }

    public Integer convert(final String s)
    {
        return doCall(s, new Callback<Integer, String>() {
            public Integer call(String s) { return Integer.parseInt(s); }
        });
    }
}

public class CallbacksTest
{
    public static void main(String [] args )
    {
        Caller c = new Caller();
        System.out.println(c.getClass().getName() + " " + c.convert("666"));
    }
}
