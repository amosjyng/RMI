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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
     * Random number generator for binding objects to strings
     */
    private Random random;
    
    /**
     * Whether to print debugging statements or not
     */
    private static boolean DEBUG = false;
    
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
        
        this.random = new Random();
        
        this.start();
    }
    
    private static void debug(String message)
    {
        if (DEBUG)
        {
            System.err.println(message);
        }
    }
    
    /**
     * this method actually construct the client registry, client here means all machine except the registry server
     * @param serverAddress
     * @param serverPort
     * @param clientAddress
     * @param clientPort
     * @return
     */
    public static Registry getRegistry(String serverAddress, Integer serverPort,
            String clientAddress, Integer clientPort)
    {
        if (instance == null)
        {
            instance = new Registry(serverAddress, serverPort, clientAddress, clientPort);
        }
        
        return instance;
    }
    
    public static Registry getRegistry()
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
        
        oos.writeObject(new Reference(objectString, clientAddress, clientInvocationsPort));
        oos.close();
        s.close();
    }
    /**
     * this method helps to create server-stub for client, server now means the machine that have the remote object, client means the machine wants the remote object 
     * @param objectString
     * @param stubClass
     * @return
     * @throws RemoteException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object get(String objectString, Class stubClass) throws RemoteException
    {
        try
        {
            if (locallookup(objectString))
            {
                Reference r = new Reference(objectString, clientAddress, clientInvocationsPort);
                return stubClass.getConstructor(Reference.class).newInstance(r);
            }
            else
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
    
    /**
     * this method will return the map of reference of all the registered remote objects, key is the name and value is reference 
     * @return
     * @throws RemoteException
     */
    public HashMap<String, Reference> list() throws RemoteException
    {
        try
        {
            Socket referencemapSocket = new Socket(serverAddress, serverPort);
            ObjectOutputStream referencemapOS = new ObjectOutputStream(
                    referencemapSocket.getOutputStream());
            referencemapOS.writeObject(RegistryServer.LIST);
            
            ObjectInputStream referencemapIS = new ObjectInputStream(
                    referencemapSocket.getInputStream());
            @SuppressWarnings("unchecked")
            HashMap<String, Reference> l = (HashMap<String, Reference>) referencemapIS.readObject();
            
            referencemapOS.close();
            referencemapIS.close();
            referencemapSocket.close();
            return l;
            
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
     * local lookup helps to handles the situation when client C wants Server B's remote object and pass another remote object as parameters locally, it can directly  
     * return C's address and port.
     * @param objectString
     * @return
     */
    public boolean locallookup(String objectString)
    {
        if (localObjects.get(objectString) != null)
        {
            return true;
        }
        else
            return false;
    }
    /**
     * this is remote look up, it will look up the reference for the stub when the stub is created
     * @param objectString
     * @return
     * @throws RemoteException
     */
    public Reference lookup(String objectString) throws RemoteException
    {
        try
        {
            Socket referenceSocket = new Socket(serverAddress, serverPort);
            ObjectOutputStream referenceOS = new ObjectOutputStream(
                    referenceSocket.getOutputStream());
            referenceOS.writeObject(RegistryServer.LOOKUP);
            referenceOS.writeObject(objectString);
            
            ObjectInputStream referenceIS = new ObjectInputStream(referenceSocket.getInputStream());
            Reference reference = (Reference) referenceIS.readObject();
            // System.out.println("==> Got reference for " + method);
            referenceOS.close();
            referenceIS.close();
            referenceSocket.close();
            return reference;
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
     * if an object implements remote interface, return stub for it(which is the reference in this context), otherwise return object itself, 
     * @param possiblyRemoteInterface
     * @param parameter
     * @return
     * @throws RemoteException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("rawtypes")
    private Serializable getObjectOrStub(Class possiblyRemoteInterface, Object parameter)
            throws RemoteException, ClassNotFoundException
    {
        if (possiblyRemoteInterface == null || parameter == null)
        {
            return null; // some functions don't return anything
        }
        else if (!Stub.class.isAssignableFrom(parameter.getClass())
                && Remote.class.isAssignableFrom(possiblyRemoteInterface))
        {
            String objectString = new Integer(random.nextInt()).toString();
            localObjects.put(objectString, parameter);
            debug("Creating " + possiblyRemoteInterface.getName() + "Stub");
            return (Serializable) get(objectString,
                    Class.forName(possiblyRemoteInterface.getName() + "Stub"));
        }
        else
        {
            return (Serializable) parameter;
        }
    }
    
    /**
     * the invoke function sends a message to the other machine to invoke the remote object located on that machine and returns the result of remote method invocation
     * to the local stub that ask for it  
     * @param reference
     * @param methodString
     * @param parameterTypes
     * @param parameters
     * @return
     * @throws RemoteException
     */
    @SuppressWarnings("rawtypes")
    public Object invoke(Reference reference, String methodString, List<Class> parameterTypes,
            List<Object> parameters) throws RemoteException
    {
        try
        {
            // marshall parameters
            List<Serializable> referenceParameters = new ArrayList<Serializable>();
            for (int i = 0; i < parameters.size(); i++)
            {
                referenceParameters.add(getObjectOrStub(parameterTypes.get(i), parameters.get(i)));
            }
            
            // invoke remotely
            Socket invocationSocket = new Socket(reference.getHost(), reference.getPort());
            ObjectOutputStream invocationOS = new ObjectOutputStream(
                    invocationSocket.getOutputStream());
            invocationOS.writeObject(new Message(reference.getName(), methodString, parameterTypes,
                    referenceParameters));
            debug("<== Invoking " + reference.getName() + "." + methodString + "(...) at "
                    + reference);
            
            ObjectInputStream invocationIS = new ObjectInputStream(
                    invocationSocket.getInputStream());
            Object result = invocationIS.readObject();
            debug("==> Got result for " + reference.getName() + ":" + methodString + "(...) from "
                    + reference);
            
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
                debug("==> Remote request for " + invocationRequest.getObjectString() + "."
                        + invocationRequest.getMethod());
                
                // execute invocation
                Object requestedObject = localObjects.get(invocationRequest.getObjectString());
                Method requestedMethod = requestedObject.getClass().getMethod(
                        invocationRequest.getMethod(), invocationRequest.getParameterTypes());
                Object result = requestedMethod.invoke(requestedObject,
                        invocationRequest.getParameters());
                
                // return result to RMI server
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                oos.writeObject(getObjectOrStub(requestedMethod.getReturnType(), result));
                
                debug("<== Returned remote result for " + invocationRequest.getObjectString() + "."
                        + invocationRequest.getMethod());
                
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
