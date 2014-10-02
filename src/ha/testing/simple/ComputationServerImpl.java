package ha.testing.simple;

import java.io.IOException;

public class ComputationServerImpl implements ComputationServer
{
    @Override
    public String getSomething()
    {
        return "Booyah!";
    }
    
    public static void main(String[] args) throws IOException, NumberFormatException
    {
        if (args.length != 5)
        {
            System.out.println("USAGE: java ha.testing.simple.SimpleServerImpl <server address> <server command port> "
                               + "<server results port> <client address> <client port>");
        }
        else
        {
            ha.rmi.Registry registry =
                    ha.rmi.Registry.getRegistry(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]),
                                            args[3], Integer.parseInt(args[4]));
            
            ComputationServer ss = new ComputationServerImpl();
            registry.bind("simple server", ss);
            registry.listenForMethodInvocations();
        }
    }
}
