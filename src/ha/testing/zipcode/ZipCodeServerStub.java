package ha.testing.zipcode;

import java.util.Arrays;

import ha.rmi.RemoteException;

public class ZipCodeServerStub implements ZipCodeServer
{
    String objectString;
    
    public ZipCodeServerStub(String objectString)
    {
        this.objectString = objectString;
    }

    @Override
    public void initialise(ZipCodeList newlist) throws RemoteException
    {
        ha.rmi.Registry.getClient().invoke(objectString, "initialise", Arrays.asList(newlist));
    }

    @Override
    public String find(String city) throws RemoteException
    {
        return (String) ha.rmi.Registry.getClient().invoke(objectString, "find", Arrays.asList(city));
    }

    @Override
    public ZipCodeList findAll() throws RemoteException
    {
        return (ZipCodeList) ha.rmi.Registry.getClient().invoke(objectString, "findAll", Arrays.asList());
    }

    @Override
    public void printAll() throws RemoteException
    {
        ha.rmi.Registry.getClient().invoke(objectString, "printAll", Arrays.asList());
    }
    
}
