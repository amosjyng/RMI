package ha.testing.zipcode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import ha.rmi.Reference;
import ha.rmi.RemoteException;
import ha.rmi.Stub;

public class ZipCodeServerStub extends Stub implements ZipCodeServer
{
    String objectString;
    
    public ZipCodeServerStub(Reference r)
    {
      super(r);
      // TODO Auto-generated constructor stub
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void initialise(ZipCodeList newlist) throws RemoteException
    {
        ha.rmi.Registry.getClient().invoke(r, "initialise",
                Arrays.asList((Class) ZipCodeList.class), Arrays.asList((Serializable) newlist));
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public String find(String city) throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(r, "find",
                Arrays.asList((Class) String.class), Arrays.asList((Serializable) city));
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public ZipCodeList findAll() throws RemoteException
    {
        return (ZipCodeList) ha.rmi.Registry.getClient().invoke(r, "findAll",
                new ArrayList<Class>(), new ArrayList<Serializable>());
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void printAll() throws RemoteException
    {
        ha.rmi.Registry.getClient().invoke(r, "printAll",
                new ArrayList<Class>(), new ArrayList<Serializable>());
    }
    
}
