package ha.testing.simple;

import ha.rmi.RemoteException;

import java.lang.reflect.InvocationTargetException;

public class SimpleClient
{
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        if (args.length != 4)
        {
            System.out.println("USAGE: java ha.testing.simple.SimpleClient <server address> <server command port> "
                               + "<client address> <client port>");
        }
        else
        {
            ha.rmi.Client rmiClient = ha.rmi.Client.getClient(args[0], Integer.parseInt(args[1]), null,
                                                              args[2], Integer.parseInt(args[3]));
            SimpleServer ss = (SimpleServer) rmiClient.get("simple server", SimpleServerStub.class);
            System.out.println("Asking local instance of simple server for something...");
            try
            {
                System.out.println(ss.getSomething());
            }
            catch (RemoteException e)
            {
                System.err.println("Aww! Failed to get something from the local simple server!");
                e.printStackTrace();
            }
        }
        
    }
}
