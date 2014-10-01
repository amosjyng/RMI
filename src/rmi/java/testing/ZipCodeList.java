package rmi.java.testing;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ZipCodeList extends Remote
{
    public String getCity() throws RemoteException;
    public String getZipCode() throws RemoteException;
    public ZipCodeList getNext() throws RemoteException;
}
