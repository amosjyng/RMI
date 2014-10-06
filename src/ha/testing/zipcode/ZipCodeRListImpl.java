package ha.testing.zipcode;

import java.io.IOException;
import java.net.UnknownHostException;

public class ZipCodeRListImpl implements ZipCodeRList
{
    String city;
    String zipcode;
    ZipCodeRList next;
    
    // this constructor creates the terminal of the list.
    // it is assumed this is called at the outset.
    public ZipCodeRListImpl()
    {
        city = null;
        zipcode = null;
        next = null;
    }
    
    // this is the standard constructor.
    public ZipCodeRListImpl(String c, String z, ZipCodeRList n)
    {
        city = c;
        zipcode = z;
        next = n;
    }
    
    // finding the zip code only for that cell.
    // its client can implement recursive search.
    @Override
    public String find(String c)
    {
        System.out.println("Zipcode is currently " + zipcode + ", city is " + city + " and requested city is " + c);
        if (c.equals(city))
            return zipcode;
        else
            return null;
    }
    
    // this is essentially cons.
    @Override
    public ZipCodeRList add(String c, String z)
    {
        return new ZipCodeRListImpl(c, z, this);
    }
    
    // this is essentially car.
    @Override
    public ZipCodeRList next()
    {
        return next;
    }
    
    public static void main(String[] args) throws UnknownHostException, IOException
    {
        if (args.length != 4)
        {
            System.out
                    .println("USAGE: java ha.testing.zipcode.ZipCodeRListImpl <server address> <server port> "
                            + "<client address> <client port>");
        }
        else
        {
            ha.rmi.Registry registry = ha.rmi.Registry.getRegistry(args[0],
                    Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
            
            ZipCodeRList zcs = new ZipCodeRListImpl();
            registry.bind("zipcoder server", zcs);
        }
    }
}
