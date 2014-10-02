package ha.testing.simple;

import ha.rmi.RemoteException;

public interface ComputationServer
{
    public String getSomething() throws RemoteException;
}
