package ha.testing.zipcode;

import java.util.ArrayList;
import java.util.Arrays;

import ha.rmi.Reference;
import ha.rmi.RemoteException;
import ha.rmi.Stub;

public class ZipCodeServerStub extends Stub implements ZipCodeServer
{
    private static final long serialVersionUID = 6903716231910131113L;
    String objectString;
    
    public ZipCodeServerStub(Reference r)
    {
        super(r);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void initialise(ZipCodeList newlist) throws RemoteException
    {
        ha.rmi.Registry.getRegistry().invoke(r, "initialise",
                Arrays.asList((Class) ZipCodeList.class), Arrays.asList((Object) newlist));
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public String find(String city) throws RemoteException
    {
        return (String) ha.rmi.Registry.getRegistry().invoke(r, "find",
                Arrays.asList((Class) String.class), Arrays.asList((Object) city));
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public ZipCodeList findAll() throws RemoteException
    {
        return (ZipCodeList) ha.rmi.Registry.getRegistry().invoke(r, "findAll",
                new ArrayList<Class>(), new ArrayList<Object>());
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void printAll() throws RemoteException
    {
        ha.rmi.Registry.getRegistry().invoke(r, "printAll", new ArrayList<Class>(),
                new ArrayList<Object>());
    }
    
}
