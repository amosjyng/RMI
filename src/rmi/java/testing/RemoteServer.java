package rmi.java.testing;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteServer extends UnicastRemoteObject implements Remote
{
    protected RemoteServer() throws RemoteException
    {
        super();
    }

    public static void main(String[] args) throws Exception
    {
        //System.setSecurityManager(new SecurityManager());
        Registry registry = LocateRegistry.createRegistry(1099);
        //Registry registry = LocateRegistry.getRegistry("127.0.0.1");
        registry.rebind("zip code server", new ZipCodeServerImpl());
        System.out.println("Server running and added to registry");
        while (true);
    }
}
