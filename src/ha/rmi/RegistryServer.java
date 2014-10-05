package ha.rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
    public static String INVOKE = "invoke";
    
    /**
     * Which port this RMI server listens for object bindings/method invocations on
     */
    private ServerSocket serverSocket;
    
    /**
     * Mapping of strings to the objects the strings refer to
     */
    private Map<String, Reference> references = new HashMap<String, Reference>();
    
    public static class Reference implements Serializable
    {
        /**
         * Generated UID for Java serialization
         */
        private static final long serialVersionUID = 6295695796518967426L;
        
        /**
         * Which machine this object is located on
         */
        private String host;
        
        /**
         * Which port that machine is listening on for method invocation requests
         */
        private int port;
        
        public Reference(String host, int port)
        {
            this.host = host;
            this.port = port;
        }
        
        @Override
        public String toString()
        {
            return host + ":" + port;
        }
        
        public String getHost()
        {
            return host;
        }
        
        public int getPort()
        {
            return port;
        }
    }
    
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
                String objectString = (String) ois.readObject();
                Reference reference = (Reference) ois.readObject();
                System.out.println("Binding some object from " + reference.getHost() + ":"
                        + reference.getPort() + " to \"" + objectString + "\"");
                references.put(objectString, reference);
            }
            else if (command.equals(INVOKE))
            {
                // send them the reference and have them make the connection themselves
                
                String objectString = (String) ois.readObject();
                oos.writeObject(references.get(objectString));
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
