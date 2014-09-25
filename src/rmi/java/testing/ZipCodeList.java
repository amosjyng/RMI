package rmi.java.testing;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ZipCodeList extends UnicastRemoteObject
{
    private static final long serialVersionUID = -2300815110310515265L;
    
    String city;
    String ZipCode;
    ZipCodeList next;
    
    public ZipCodeList(String c, String z, ZipCodeList n) throws RemoteException
    {
        super();
        
        city = c;
        ZipCode = z;
        next = n;
    }
}
