package ha.testing.zipcode;

import ha.rmi.Reference;
import ha.rmi.RemoteException;
import ha.rmi.Stub;

import java.io.Serializable;
import java.util.ArrayList;

public class ZipCodeListStub extends Stub implements ZipCodeList
{
    private static final long serialVersionUID = 2322491834267192060L;
    
    public ZipCodeListStub(Reference r) {
      super(r);
      // TODO Auto-generated constructor stub
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public String getCity() throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(r, "getCity",
                new ArrayList<Class>(), new ArrayList<Serializable>());
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public String getZipCode() throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(r, "getZipCode",
                new ArrayList<Class>(), new ArrayList<Serializable>());
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public ZipCodeList getNext() throws RemoteException
    {
        return (ZipCodeList) ha.rmi.Registry.getClient().invoke(r, "getNext",
                new ArrayList<Class>(), new ArrayList<Serializable>());
    }
    
}
