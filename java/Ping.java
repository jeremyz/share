
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class Ping {

    public static void tryPing(String ipAddress)
    {
        boolean reachable = false;
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            reachable = inet.isReachable(5000);
        }
        catch (UnknownHostException e) {}
        catch (IOException e) {}
        System.out.println(ipAddress + " is " + (reachable ? "is reachable" : "is NOT reachable"));
    }

    public static void main(String[] args) throws UnknownHostException, IOException
    {
        tryPing("127.0.0.1");
        tryPing("asynk.ch");
        tryPing("asynk.cc");
    }
}
