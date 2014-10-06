package ha.rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the RMI server that stores object references, not the server doing actual computations.
 *
 */
public class RegistryServer
{
    public static String BIND = "bind";
    public static String LOOKUP = "look up";
    public static String LIST = "list";
    
    /**
     * Which port this RMI server listens for object bindings/method invocations on
     */
    private ServerSocket serverSocket;
    
    /**
     * Mapping of strings to the objects the strings refer to
     */
    private Map<String, Reference> references = new HashMap<String, Reference>();
    
    public RegistryServer(int commandPort) throws IOException
    {
        serverSocket = new ServerSocket(commandPort);
    }
    
    /**
     * Listen for requests for bindings/method invocations from clients, and act accordingly
     */
    private void listenForConnections()
    {
        try
        {
            Socket acceptedSocket = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(acceptedSocket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(acceptedSocket.getOutputStream());
            String command = (String) ois.readObject();
            
            if (command.equals(BIND))
            {
                // String objectString = (String) ois.readObject();
                Reference reference = (Reference) ois.readObject();
                System.out.println("Binding some object from " + reference.getHost() + ":"
                        + reference.getPort() + " to \"" + reference.getName() + "\"");
                references.put(reference.getName(), reference);
            }
            else if (command.equals(LOOKUP))
            {
                // send them the reference and have them make the connection themselves
                
                String objectString = (String) ois.readObject();
                System.out.println("Reference for \"" + objectString + "\" requested");
                oos.writeObject(references.get(objectString));
            }
            else if (command.equals(LIST))
            {
                // send them the list of reference and have them make the connection themselves
                
                oos.writeObject(references);
            }
            else
            {
                System.err.println("Command \"" + command + "\" unrecognized!");
            }
            
            ois.close();
            oos.close();
        }
        catch (IOException e)
        {
            System.err.println("Error encountered while listening for client!");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Error encountered while trying to read object from client!");
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
    
    public static void main(String[] args) throws NumberFormatException, IOException
    {
        if (args.length != 1)
        {
            System.out.println("USAGE: java ha.rmi.RegistryServer <port>");
        }
        else
        {
            RegistryServer server = new RegistryServer(Integer.parseInt(args[0]));
            
            while (true)
            {
                server.listenForConnections();
            }
        }
    }
}
