package ha.testing.simple;

import ha.rmi.RemoteException;

public class ComputationServerStub implements ComputationServer
{
    /**
     * The object string the actual object is bound to, on the RMI server
     */
    String objectString;
    
    public ComputationServerStub(String objectString)
    {
        this.objectString = objectString;
    }

    @Override
    public String getSomething() throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(objectString, "getSomething");
    }
}
