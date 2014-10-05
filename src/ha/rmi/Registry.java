package ha.rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the RMI client that connects to the RMI server to perform object bindings/method
 * invocations
 * 
 * Anything wishing to connect to the RMI server should extend this base class
 *
 */
public class Registry extends Thread
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
     * The address of this machine
     */
    private String clientAddress;
    
    /**
     * The port on this machine to listen for method invocations
     */
    private Integer clientInvocationsPort;
    
    protected Registry(String serverAddress, Integer serverPort, String clientAddress,
            Integer clientInvocationsPort)
    {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.clientAddress = clientAddress;
        this.clientInvocationsPort = clientInvocationsPort;
        
        this.start();
    }
    
    public static Registry getRegistry(String serverAddress, Integer serverPort,
            String clientAddress, Integer clientPort)
    {
        if (instance == null)
        {
            instance = new Registry(serverAddress, serverPort, clientAddress, clientPort);
        }
        
        return instance;
    }
    
    public static Registry getClient()
    {
        return instance;
    }
    
    /**
     * Bind an object to a string both locally and on the RMI server.
     * 
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
       
        oos.writeObject(new Reference(objectString,clientAddress, clientInvocationsPort));
        oos.close();
        s.close();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object get(String objectString, Class stubClass) throws RemoteException
    {
        try
        {
            
            return stubClass.getConstructor(Reference.class).newInstance(lookup(objectString));
        }
        catch (InvocationTargetException e)
        {
            e.getTargetException().printStackTrace();
            throw new RemoteException(e.getTargetException().getMessage());
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | NoSuchMethodException | SecurityException e)
        {
            e.printStackTrace();
            throw new RemoteException("Can't retrieve object \"" + objectString
                    + "\" from RMI server!");
        }
    }
    public HashMap<String,Reference> list() throws RemoteException{
      try{ 
        Socket referencemapSocket = new Socket(serverAddress, serverPort);
        ObjectOutputStream referencemapOS = new ObjectOutputStream(
                referencemapSocket.getOutputStream());
        referencemapOS.writeObject(RegistryServer.LIST);
        
        
        ObjectInputStream referencemapIS = new ObjectInputStream(referencemapSocket.getInputStream());
        HashMap<String,Reference> l=(HashMap<String,Reference>) referencemapIS.readObject();
        
        //System.out.println("==> Got reference for " + method);
        referencemapOS.close();
        referencemapIS.close();
        referencemapSocket.close();
        return l;
        
      }catch (IOException e)
      {
        throw new RemoteException(e.getMessage());
      }
      catch (ClassNotFoundException e)
      {
          throw new RemoteException(e.getMessage());
      }
     
      
      
      
      
    }
    
    public Reference lookup(String objectString) throws RemoteException{
      try
      {
          Socket referenceSocket = new Socket(serverAddress, serverPort);
          ObjectOutputStream referenceOS = new ObjectOutputStream(
                  referenceSocket.getOutputStream());
          referenceOS.writeObject(RegistryServer.LOOKUP);
          referenceOS.writeObject(objectString);
          
          ObjectInputStream referenceIS = new ObjectInputStream(referenceSocket.getInputStream());
          Reference reference = (Reference) referenceIS.readObject();
          //System.out.println("==> Got reference for " + method);
          referenceOS.close();
          referenceIS.close();
          referenceSocket.close();
          return reference;
      }catch (IOException e)
      {
        throw new RemoteException(e.getMessage());
      }
      catch (ClassNotFoundException e)
      {
          throw new RemoteException(e.getMessage());
      }
      
      
      }
    
    
    
    @SuppressWarnings("rawtypes")
    public Object invoke(Reference reference, String methodString, List<Class> parameterTypes,
            List<Serializable> parameters) throws RemoteException
    {
        try
        {
            // invoke remotely
            //System.out.println("<== Invoking " + method + " at " + reference);
            Socket invocationSocket = new Socket(reference.getHost(), reference.getPort());
            ObjectOutputStream invocationOS = new ObjectOutputStream(
                    invocationSocket.getOutputStream());
            invocationOS.writeObject(new Message(reference.getName(), methodString, parameterTypes,
                    parameters));
            
            
            ObjectInputStream invocationIS = new ObjectInputStream(
                    invocationSocket.getInputStream());
            Object result = invocationIS.readObject();
            //System.out.println("==> Got result for " + method + " from " + reference);
            
            invocationOS.close();
            invocationIS.close();
            invocationSocket.close();
            
            return result;
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
            throw new RemoteException(e.getMessage());
        }
    }
    
    /**
     * Keep listening for method invocations and execute them whenever they're received
     * 
     * @throws IOException
     */
    private void listenForMethodInvocations() throws IOException, RemoteException
    {
        @SuppressWarnings("resource")
        ServerSocket ss = new ServerSocket(clientInvocationsPort);
        System.out.println("Waiting for method invocations from RMI server on port "
                + clientInvocationsPort + "...");
        while (true)
        {
            try
            {
                // read invocation request
                Socket s = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message invocationRequest = (Message) ois.readObject();
                /*System.out.println("==> Remote request for " + invocationRequest.getObjectString()
                        + "." + invocationRequest.getMethod());*/
                
                // execute invocation
                Object requestedObject = localObjects.get(invocationRequest.getObjectString());
                Method requestedMethod = requestedObject.getClass().getMethod(
                        invocationRequest.getMethod(), invocationRequest.getParameterTypes());
                Object result = requestedMethod.invoke(requestedObject,
                        invocationRequest.getParameters());
                
                // return result to RMI server
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                oos.writeObject(result);
                /*System.out
                        .println("<== Returned remote result for "
                                + invocationRequest.getObjectString() + "."
                                + invocationRequest.getMethod());*/
                
                ois.close();
                oos.close();
                s.close();
            }
            catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                    | IllegalAccessException | IllegalArgumentException e)
            {
                System.err.println("Couldn't read message invocation request!");
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                System.err.println("Invocation error:");
                e.getTargetException().printStackTrace();
                throw new RemoteException(e.getTargetException().getMessage());
            }
        }
    }
    
    @Override
    public void run()
    {
        try
        {
            listenForMethodInvocations();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
