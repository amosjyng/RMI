package rmi.java.testing;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ZipCodeListImpl extends UnicastRemoteObject implements ZipCodeList
{
    private static final long serialVersionUID = -2300815110310515265L;
    
    private String city;
    private String ZipCode;
    private ZipCodeList next;
    
    public ZipCodeListImpl(String c, String z, ZipCodeList n) throws RemoteException
    {
        super();
        
        city = c;
        ZipCode = z;
        next = n;
    }

    @Override
    public String getCity() throws RemoteException
    {
        return city;
    }

    @Override
    public String getZipCode() throws RemoteException
    {
        return ZipCode;
    }

    @Override
    public ZipCodeList getNext() throws RemoteException
    {
        return next;
    }
}
