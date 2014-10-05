package ha.testing.simple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

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
    
    @SuppressWarnings("rawtypes")
    @Override
    public String getSomething() throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(objectString, "getSomething",
                new ArrayList<Class>(), new ArrayList<Serializable>());
    }
    
    @Override
    public String sayHiTo(String name) throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(objectString, "sayHiTo",
                Arrays.asList(String.class),
                Arrays.asList(name));
    }
}
