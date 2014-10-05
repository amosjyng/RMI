package ha.testing.simple;

import ha.rmi.RemoteException;

import java.lang.reflect.InvocationTargetException;

public class SimpleClient
{
    public static void main(String[] args) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException, RemoteException
    {
        if (args.length != 5)
        {
            System.out
                    .println("USAGE: java ha.testing.simple.SimpleClient <server address> <server port> "
                            + "<client address> <client port> <ID number>");
        }
        else
        {
            ha.rmi.Registry registry = ha.rmi.Registry.getRegistry(args[0],
                    Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
            ComputationServer ss = (ComputationServer) registry.get("simple server " + args[4],
                    ComputationServerStub.class);
            System.out.println("Asking local instance of simple server for something...");
            try
            {
                System.out.println(ss.getSomething());
                System.out.println(ss.sayHiTo("fucker"));
            }
            catch (RemoteException e)
            {
                System.err.println("Aww! Failed to get something from the local simple server!");
                e.printStackTrace();
            }
        }
        
    }
}
