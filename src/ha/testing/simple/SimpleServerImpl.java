package ha.testing.simple;

import java.io.IOException;

public class SimpleServerImpl implements SimpleServer
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
            ha.rmi.Client rmiClient =
                    ha.rmi.Client.getClient(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]),
                                            args[3], Integer.parseInt(args[4]));
            
            SimpleServer ss = new SimpleServerImpl();
            rmiClient.bind("simple server", ss);
            rmiClient.listenForMethodInvocations();
        }
    }
}
