package ha.testing.simple;

import java.io.IOException;

public class ComputationServerImpl implements ComputationServer
{
    private static String id;
    
    @Override
    public String getSomething()
    {
        return "Booyah!" + id;
    }
    
    public static void main(String[] args) throws IOException, NumberFormatException
    {
        if (args.length != 6)
        {
            System.out.println("USAGE: java ha.testing.simple.SimpleServerImpl <server address> <server command port> "
                               + "<server results port> <client address> <client port> <ID number>");
        }
        else
        {
            ha.rmi.Registry registry =
                    ha.rmi.Registry.getRegistry(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]),
                                            args[3], Integer.parseInt(args[4]));
            
            ComputationServer ss = new ComputationServerImpl();
            id = args[5];
            registry.bind("simple server " + id, ss);
            registry.listenForMethodInvocations();
        }
    }
}
