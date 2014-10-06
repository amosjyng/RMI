package ha.testing.simple;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;

import ha.rmi.Reference;
import ha.rmi.RemoteException;
import ha.rmi.Stub;

public class ComputationServerStub extends Stub implements ComputationServer
{
    private static final long serialVersionUID = 5638678938318200527L;
    
    public ComputationServerStub(Reference r)
    {
        super(r);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public String getSomething() throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(r, "getSomething",
                new ArrayList<Class>(), new ArrayList<Object>());
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public String sayHiTo(String name) throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(r, "sayHiTo",
                Arrays.asList((Class) String.class), Arrays.asList(name));
    }
}
