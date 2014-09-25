package rmi.java.testing;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteServer implements Remote
{
    public static void main(String[] args) throws Exception
    {
        //System.setSecurityManager(new SecurityManager());
        //Registry registry = LocateRegistry.createRegistry(1099);
        Registry registry = LocateRegistry.getRegistry("127.0.0.1");
        registry.rebind("zip code server",
                (ZipCodeServer) UnicastRemoteObject.exportObject(new ZipCodeServerImpl(), 0));
    }
}
