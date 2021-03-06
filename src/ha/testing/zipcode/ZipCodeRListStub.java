package ha.testing.zipcode;

import java.util.ArrayList;
import java.util.Arrays;

import ha.rmi.*;

public class ZipCodeRListStub extends Stub implements ZipCodeRList
{
    private static final long serialVersionUID = -6980922357266049093L;

    public ZipCodeRListStub(Reference r)
    {
        super(r);
    }
    
    @Override
    public String find(String city) throws RemoteException
    {
        return (String) Registry.getRegistry().invoke(r, "find", Arrays.asList((Class) String.class),
                Arrays.asList((Object) city));
    }
    
    @Override
    public ZipCodeRList add(String city, String zipcode) throws RemoteException
    {
        return (ZipCodeRList) Registry.getRegistry().invoke(r, "add",
                Arrays.asList((Class) String.class, (Class) String.class), Arrays.asList((Object) city, (Object) zipcode));
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public ZipCodeRList next() throws RemoteException
    {
        return (ZipCodeRList) Registry.getRegistry().invoke(r, "next", new ArrayList<Class>(),
                new ArrayList<Object>());
    }
    
}
