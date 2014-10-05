package ha.testing.simple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import ha.rmi.Reference;
import ha.rmi.RemoteException;
import ha.rmi.Stub;
public class ComputationServerStub extends Stub implements ComputationServer
{
   
    
    public ComputationServerStub(Reference r) {
    super(r);
    // TODO Auto-generated constructor stub
  }

    @SuppressWarnings("rawtypes")
    @Override
    public String getSomething() throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(r, "getSomething",
                new ArrayList<Class>(), new ArrayList<Serializable>());
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public String sayHiTo(String name) throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(r, "sayHiTo",
                Arrays.asList((Class) String.class),
                Arrays.asList((Serializable) name));
    }
}
