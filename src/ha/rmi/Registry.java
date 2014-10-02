package ha.rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the RMI client that connects to the RMI server to perform object bindings/method invocations
 * 
 * Anything wishing to connect to the RMI server should extend this base class
 *
 */
public class Registry
{
    /**
     * The singleton instance of this client
     */
    private static Registry instance = null;
    
    /**
     * Mapping of strings to objects contained on this particular machine
     */
    private Map<String, Object> localObjects = new HashMap<String, Object>();
    
    /**
     * Machine where the RMI server resides on
     */
    private String serverAddress;
    
    /**
     * Port on that machine where the RMI server is listening for object bindings/method invocations
     */
    private Integer serverPort;
    
    /**
     * Port on that machine where the RMI server is listening for method invocation results
     */
    private Integer serverResultsPort;
    
    /**
     * The address of this machine
     */
    private String clientAddress;
    
    /**
     * The port on this machine to listen for information from the RMI server
     */
    private Integer clientPort;
    
    protected Registry(String serverAddress, Integer serverPort, Integer serverResultsPort, String clientAddress, Integer clientPort)
    {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.serverResultsPort = serverResultsPort;
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
    }
    
    public static Registry getRegistry(String serverAddress, Integer serverPort, Integer serverResultsPort, String clientAddress, Integer clientPort)
    {
        if (instance == null)
        {
            instance = new Registry(serverAddress, serverPort, serverResultsPort, clientAddress, clientPort);
        }
        
        return instance;
    }
    
    public static Registry getClient()
    {
        return instance;
    }
    
    /**
     * Bind an object to a string both locally and on the RMI server.
     * @param objectString
     * @param object
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public void bind(String objectString, Object object) throws UnknownHostException, IOException
    {
        System.out.println("Binding some object here to \"" + objectString + "\" on RMI server");
        
        // bind locally
        localObjects.put(objectString, object);
        // and bind remotely on RMI server as well
        Socket s = new Socket(serverAddress, serverPort);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        oos.writeObject(RegistryServer.BIND);
        oos.writeObject(objectString);
        oos.writeObject(new RegistryServer.Reference(clientAddress, clientPort));
        oos.close();
        s.close();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object get(String objectString, Class stubClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
        if (localObjects.containsKey(objectString))
        {
            return localObjects.get(objectString);
        }
        else
        {
            return stubClass.getConstructor(String.class).newInstance(objectString);
        }
    }
    
    public Object invoke(String objectString, String methodString) throws RemoteException
    {
        try
        {
            // send invocation request to server
            Socket s = new Socket(serverAddress, serverPort);
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(RegistryServer.INVOKE);
            oos.writeObject(new Message(objectString, methodString, clientAddress, clientPort));
            oos.close();
            s.close();
            
            // listen for result response from server
            ServerSocket ss = new ServerSocket(clientPort);
            Socket rs = ss.accept();
            ObjectInputStream ois = new ObjectInputStream(rs.getInputStream());
            Object result = ois.readObject();
            ois.close();
            rs.close();
            ss.close();
            
            return result;
        }
        catch (IOException e)
        {
            throw new RemoteException(e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            throw new RemoteException(e.getMessage());
        }
    }
    
    /**
     * Keep listening for method invocations and execute them whenever they're received
     * @throws IOException
     */
    public void listenForMethodInvocations() throws IOException
    {
        ServerSocket ss = new ServerSocket(clientPort);
        while (true)
        {
            try
            {
                // read invocation request
                System.out.println("Waiting for method invocations from RMI server...");
                Socket s = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message invocationRequest = (Message) ois.readObject();
                ois.close();
                System.out.println("Invocation request for local object \"" + invocationRequest.getObjectString() + "\""
                                   + " for the method \"" + invocationRequest.getMethod() + "\"");
                
                // execute invocation
                Object requestedObject = localObjects.get(invocationRequest.getObjectString());
                Method requestedMethod = requestedObject.getClass().getMethod(invocationRequest.getMethod());
                Object result = requestedMethod.invoke(requestedObject);
                
                // return result to RMI server
                Socket rs = new Socket(serverAddress, serverResultsPort);
                ObjectOutputStream ros = new ObjectOutputStream(rs.getOutputStream());
                ros.writeObject(result);
                ros.close();
                rs.close();
            }
            catch (ClassNotFoundException e)
            {
                System.err.println("Couldn't read message invocation request!");
                e.printStackTrace();
            }
            catch (NoSuchMethodException e)
            {
                System.err.println("Couldn't find relevant method!");
                e.printStackTrace();
            }
            catch (SecurityException e)
            {
                System.err.println("No! Bad security!");
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalArgumentException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
