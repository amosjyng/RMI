package ha.testing.zipcode;

import ha.rmi.RemoteException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ZipCodeListStub implements ZipCodeList
{
    private static final long serialVersionUID = 2322491834267192060L;
    
    String objectString;
    
    public ZipCodeListStub(String objectString)
    {
        this.objectString = objectString;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public String getCity() throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(objectString, "getCity",
                new ArrayList<Class>(), new ArrayList<Serializable>());
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public String getZipCode() throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(objectString, "getZipCode",
                new ArrayList<Class>(), new ArrayList<Serializable>());
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public ZipCodeList getNext() throws RemoteException
    {
        return (ZipCodeList) ha.rmi.Registry.getClient().invoke(objectString, "getNext",
                new ArrayList<Class>(), new ArrayList<Serializable>());
    }
    
}
