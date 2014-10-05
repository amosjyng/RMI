package ha.testing.zipcode;

import ha.rmi.*;

public class ZipCodeServerImpl implements ZipCodeServer
{
    ZipCodeList l;
    
    // this is a constructor.
    public ZipCodeServerImpl() throws RemoteException
    {
        super();
        
        l = null;
    }
    
    // when this is called, marshalled data
    // should be sent to this remote object,
    // and reconstructed.
    public void initialise(ZipCodeList newlist) throws RemoteException
    {
        l = newlist;
    }
    
    // basic function: gets a city name, returns the zip code.
    public String find(String request) throws RemoteException
    {
        // search the list.
        ZipCodeList temp = l;
        while (temp != null && !temp.getCity().equals(request))
            temp = temp.getNext();
        
        // the result is either null or we found the match.
        if (temp == null)
            return null;
        else
            return temp.getZipCode();
    }
    
    // this very short method should send the marshalled
    // whole list to the local site.
    public ZipCodeList findAll() throws RemoteException
    {
        return l;
    }
    
    // this method does printing in the remote site, not locally.
    public void printAll() throws RemoteException
    {
        ZipCodeList temp = l;
        while (temp != null)
        {
            System.out.println("city: " + temp.getCity() + ", " + "code: " + temp.getZipCode()
                    + "\n");
            temp = temp.getNext();
        }
    }
    
    public static void main(String[] args) throws Exception
    {
        if (args.length != 4)
        {
            System.out
                    .println("USAGE: java ha.testing.zipcode.ZipCodeServerImpl <server address> <server port> "
                            + "<client address> <client port>");
        }
        else
        {
            ha.rmi.Registry registry = ha.rmi.Registry.getRegistry(args[0],
                    Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
            
            ZipCodeServer zcs = new ZipCodeServerImpl();
            registry.bind("zipcode server", zcs);
        }
    }
}
