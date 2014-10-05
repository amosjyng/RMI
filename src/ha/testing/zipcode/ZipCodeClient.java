package ha.testing.zipcode;

//a client for ZipCodeServer.
//it uses ZipCodeServer as an interface, and test
//all methods.

//It reads data from a file containing the service name and city-zip 
//pairs in the following way:
//city1
//zip1
//...
//...
//end.

import java.io.*;

public class ZipCodeClient
{
    
    // the main takes three arguments:
    // (0) a host.
    // (1) a port.
    // (2) a service name.
    // (3) a file name as above.
    public static void main(String[] args) throws Exception
    {
        if (args.length != 5)
        {
            System.out
                    .println("USAGE: java ha.testing.zipcode.ZipCodeClient <server address> <server port> "
                            + "<client address> <client port> <data file>");
            System.exit(0);
        }
        ha.rmi.Registry registry = ha.rmi.Registry.getRegistry(args[0], Integer.parseInt(args[1]),
                args[2], Integer.parseInt(args[3]));
        ZipCodeServer zcs = (ZipCodeServer) registry.get("zipcode server", ZipCodeServerStub.class);
        
        BufferedReader in = new BufferedReader(new FileReader(args[4]));
        
        // reads the data and make a "local" zip code list.
        // later this is sent to the server.
        // again no error check!
        ZipCodeList l = null;
        boolean flag = true;
        while (flag)
        {
            String city = in.readLine();
            String code = in.readLine();
            if (city == null)
                flag = false;
            else
                l = new ZipCodeListImpl(city.trim(), code.trim(), l);
        }
        in.close();
        // the final value of l should be the initial head of
        // the list.
        
        // we print out the local zipcodelist.
        System.out.println("This is the original list.");
        ZipCodeList temp = l;
        registry.bind("l", l);
        while (temp != null)
        {
            System.out.println("city: " + temp.getCity() + ", " + "code: " + temp.getZipCode());
            temp = temp.getNext();
        }
        
        // test the initialise.
        zcs.initialise((ZipCodeList) ha.rmi.Registry.getClient().get("l", ZipCodeListStub.class));
        System.out.println("\n Server initalised.");
        
        // test the find.
        System.out.println("\n This is the remote list given by find.");
        temp = l;
        while (temp != null)
        {
            // here is a test.
            String res = zcs.find(temp.getCity());
            System.out.println("city: " + temp.getCity() + ", " + "code: " + res);
            temp = temp.getNext();
        }
        
        // test the findall.
        System.out.println("\n This is the remote list given by findall.");
        // here is a test.
        temp = zcs.findAll();
        while (temp != null)
        {
            System.out.println("city: " + temp.getCity() + ", " + "code: " + temp.getZipCode());
            temp = temp.getNext();
        }
        
        // test the printall.
        System.out.println("\n We test the remote site printing.");
        // here is a test.
        zcs.printAll();
        
        //in.close();
    }
}
