package ha.testing.simple;

import ha.rmi.RemoteException;

import java.io.IOException;

public class ComputationServerImpl implements ComputationServer
{
    private static String id;
    
    @Override
    public String getSomething()
    {
        return "Booyah!" + id;
    }
    
    @Override
    public String sayHiTo(String name) throws RemoteException
    {
        return "Hi, " + name + "!";
    }
    
    public static void main(String[] args) throws IOException, NumberFormatException
    {
        if (args.length != 5)
        {
            System.out
                    .println("USAGE: java ha.testing.simple.SimpleServerImpl <server address> <server port> "
                            + "<client address> <client port> <ID number>");
        }
        else
        {
            ha.rmi.Registry registry = ha.rmi.Registry.getRegistry(args[0],
                    Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
            
            ComputationServer ss = new ComputationServerImpl();
            id = args[4];
            registry.bind("simple server " + id, ss);
        }
    }
}
