package rmi.java.testing;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ZipCodeServer extends Remote
{
    public void initialise(ZipCodeList newlist) throws RemoteException;
    
    public String find(String city) throws RemoteException;
    
    public ZipCodeList findAll() throws RemoteException;
    
    public void printAll() throws RemoteException;
}
