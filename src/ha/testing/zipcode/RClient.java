package ha.testing.zipcode;

// a client for ZipCodeRList.
// it uses ZipCodeRList as an interface, and test
// all methods by printing all data.

// It reads data from a file containing the service name and city-zip 
// pairs in the following way:
//   city1
//   zip1
//   ...
//   ...
//   end.

import ha.rmi.Registry;
import ha.rmi.RemoteException;

import java.io.*;

public class RClient
{
    
    // the main takes three arguments:
    // (0) a host.
    // (1) a port.
    // (2) a service name.
    // (3) a file name as above.
    public static void main(String[] args) throws IOException, RemoteException
    {
        if (args.length != 6)
        {
            System.out
                    .println("USAGE: java ha.testing.zipcode.RClient <server address> <server port> "
                            + "<client address> <client port> <service name> <data file>");
            System.exit(0);
        }
        
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String client = args[2];
        int clientPort = Integer.parseInt(args[3]);
        String serviceName = args[4];
        BufferedReader in = new BufferedReader(new FileReader(args[5]));
        
        // locate the registry and get ror.
        Registry sr = Registry.getRegistry(host, port, client, clientPort);
        
        // get (create) the stub out of ror.
        ZipCodeRList rl = (ZipCodeRList) sr.get(serviceName, ZipCodeRListStub.class);
        
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
                l = new ZipCodeList(city.trim(), code.trim(), l);
        }
        // the final value of l should be the initial head of
        // the list.
        
        // we print out the local zipcodelist.
        System.out.println("This is the original list.");
        ZipCodeList temp = l;
        while (temp != null)
        {
            System.out.println("city: " + temp.city + ", " + "code: " + temp.ZipCode);
            temp = temp.next;
        }
        
        // test "add".
        System.out.println("testing add.");
        temp = l;
        ZipCodeRList rtemp = rl;
        while (temp != null)
        {
            rl = rl.add(temp.city, temp.ZipCode);
            temp = temp.next;
        }
        System.out.println("add tested.");
        // rl should contain the initial head of the list.
        
        // test "find" and "next" by printing all.
        // This is also the test that "add" performed all right.
        System.out.println("\n This is the remote list, printed using find/next.");
        temp = l;
        rtemp = rl;
        while (temp != null)
        {
            // here is a test.
            String res = rtemp.find(temp.city);
            System.out.println("city: " + temp.city + ", " + "code: " + res);
            temp = temp.next;
            rtemp = rtemp.next();
        }
    }
}
