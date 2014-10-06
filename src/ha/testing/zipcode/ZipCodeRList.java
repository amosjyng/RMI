package ha.testing.zipcode;

import ha.rmi.Remote;
import ha.rmi.RemoteException;

public interface ZipCodeRList extends Remote
{
    public String find(String city) throws RemoteException;
    
    public ZipCodeRList add(String city, String zipcode) throws RemoteException;
    
    public ZipCodeRList next() throws RemoteException;
}
