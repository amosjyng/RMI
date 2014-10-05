package ha.testing.zipcode;

import ha.rmi.RemoteException;

import java.util.Arrays;

public class ZipCodeListStub implements ZipCodeList
{
    private static final long serialVersionUID = 2322491834267192060L;
    
    String objectString;
    
    public ZipCodeListStub(String objectString)
    {
        this.objectString = objectString;
    }
    
    @Override
    public String getCity() throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(objectString, "getCity",
                Arrays.asList(), Arrays.asList());
    }
    
    @Override
    public String getZipCode() throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(objectString, "getZipCode",
                Arrays.asList(), Arrays.asList());
    }
    
    @Override
    public ZipCodeList getNext() throws RemoteException
    {
        return (ZipCodeList) ha.rmi.Registry.getClient().invoke(objectString, "getNext",
                Arrays.asList(), Arrays.asList());
    }
    
}
