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
            ha.rmi.Registry registry = ha.rmi.Registry.getRegistry(args[0], Integer.parseInt(args[1]), null,
                                                              args[2], Integer.parseInt(args[3]));
            ComputationServer ss = (ComputationServer) registry.get("simple server", ComputationServerStub.class);
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
