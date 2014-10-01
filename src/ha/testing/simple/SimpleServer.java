package ha.testing.simple;

import ha.rmi.RemoteException;

public interface SimpleServer
{
    public String getSomething() throws RemoteException;
}
