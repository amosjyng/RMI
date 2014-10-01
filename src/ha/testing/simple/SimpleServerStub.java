package ha.testing.simple;

import ha.rmi.RemoteException;

import java.io.IOException;
import java.net.UnknownHostException;

public class SimpleServerStub implements SimpleServer
{
    /**
     * The object string the actual object is bound to, on the RMI server
     */
    String objectString;
    
    public SimpleServerStub(String objectString)
    {
        this.objectString = objectString;
    }

    @Override
    public String getSomething() throws RemoteException
    {
        return (String) ha.rmi.Client.getClient().invoke(objectString, "getSomething");
    }
}
