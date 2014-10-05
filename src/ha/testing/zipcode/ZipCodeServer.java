package ha.testing.zipcode;

import ha.rmi.*;

public interface ZipCodeServer
{
    public void initialise(String newlistName) throws RemoteException;
    
    public String find(String city) throws RemoteException;
    
    public ZipCodeList findAll() throws RemoteException;
    
    public void printAll() throws RemoteException;
}
