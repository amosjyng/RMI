package ha.testing.zipcode;

import ha.rmi.Remote;
import ha.rmi.RemoteException;

import java.io.Serializable;

public interface ZipCodeList extends Remote
{
    public String getCity() throws RemoteException;
    public String getZipCode() throws RemoteException;
    public ZipCodeList getNext() throws RemoteException;
}
