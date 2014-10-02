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
    private ServerSocket commandSocket;
    
    /**
     * Which port this RMI server listens for results of method invocations on
     */
    private ServerSocket resultsSocket;
    
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
        
        public String getHost()
        {
            return host;
        }
        
        public int getPort()
        {
            return port;
        }
    }
    
    public RegistryServer(int commandPort, int resultsPort) throws IOException
    {
        commandSocket = new ServerSocket(commandPort);
        resultsSocket = new ServerSocket(resultsPort);
    }
    
    /**
     * Listen for requests for bindings/method invocations from clients, and act accordingly
     */
    private void listenForConnections()
    {
        try
        {
            System.out.println("Waiting for command...");
            Socket acceptedSocket = commandSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(acceptedSocket.getInputStream());
            String command = (String) ois.readObject();
            
            System.out.println("Command \"" + command + "\" received.");
            if (command.equals(BIND))
            {
                String objectString = (String) ois.readObject();
                Reference reference = (Reference) ois.readObject();
                System.out.println("Binding some object from " + reference.getHost() + ":" + reference.getPort() + " to \"" + objectString + "\"");
                references.put(objectString, reference);
            }
            else if (command.equals(INVOKE))
            {
                // first send the other machine the method invocation request
                Message invocationRequest = (Message) ois.readObject();
                Reference reference = references.get(invocationRequest.getObjectString());
                Socket objectHostSocket = new Socket(reference.getHost(), reference.getPort());
                ObjectOutputStream oos = new ObjectOutputStream(objectHostSocket.getOutputStream());
                
                oos.writeObject(invocationRequest);
                
                oos.close();
                objectHostSocket.close();
                
                // now wait for the other machine to return the results of the method invocation
                // and pass those results on to the listening machine
                Socket rs = resultsSocket.accept();
                ObjectInputStream resultsStream = new ObjectInputStream(rs.getInputStream());
                Socket returnSocket = new Socket(invocationRequest.getReturnAddress(), invocationRequest.getReturnPort());
                ObjectOutputStream returnStream = new ObjectOutputStream(returnSocket.getOutputStream());
                
                returnStream.writeObject(resultsStream.readObject());
                
                returnStream.close();
                returnSocket.close();
                resultsStream.close();
                resultsSocket.close();
            }
            else
            {
                System.err.println("Command \"" + command + "\" unrecognized!");
            }
            
            ois.close();
            acceptedSocket.close();
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
    }
    
    public static void main(String[] args) throws NumberFormatException, IOException
    {
        if (args.length != 2)
        {
            System.out.println("USAGE: java ha.rmi.RegistryServer <command port> <results port>");
        }
        else
        {
            RegistryServer server = new RegistryServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            
            while (true)
            {
                server.listenForConnections();
            }
        }
    }
}
