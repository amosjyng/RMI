package ha.testing.simple;

import ha.rmi.Remote;
import ha.rmi.RemoteException;

public interface ComputationServer extends Remote
{
    public String getSomething() throws RemoteException;
    
    public String sayHiTo(String name) throws RemoteException;
}
