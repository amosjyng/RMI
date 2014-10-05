package ha.testing.zipcode;

import ha.rmi.RemoteException;

public class ZipCodeListImpl implements ZipCodeList
{
    private static final long serialVersionUID = -2300815110310515265L;
    
    private String city;
    private String ZipCode;
    private ZipCodeList next;
    
    public ZipCodeListImpl(String c, String z, ZipCodeList n)
    {
        super();
        
        city = c;
        ZipCode = z;
        next = n;
    }

    @Override
    public String getCity() throws RemoteException
    {
        return city;
    }

    @Override
    public String getZipCode() throws RemoteException
    {
        return ZipCode;
    }

    @Override
    public ZipCodeList getNext() throws RemoteException
    {
        return next;
    }
}
