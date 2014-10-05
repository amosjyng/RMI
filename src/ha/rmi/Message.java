package ha.rmi;

import java.io.Serializable;
import java.util.List;

/**
 * Encapsulates information about a method invocation
 */
public class Message implements Serializable
{
    /**
     * Serial UID need for Java serialization
     */
    private static final long serialVersionUID = 1673594660979410453L;
    
    /**
     * String the object was mapped to on the RMI server
     */
    private String objectString;
    
    /**
     * Which method of the object should be invoked
     */
    private String method;
    
    /**
     * All the types of parameters used in this method invocation
     */
    @SuppressWarnings("rawtypes")
    private List<Class> parameterTypes;
    
    /**
     * All the parameters to be used in this method invocation
     */
    private List<Serializable> parameters;
    
    @SuppressWarnings("rawtypes")
    public Message(String objectString, String method, List<Class> parameterTypes,
            List<Serializable> parameters)
    {
        this.objectString = objectString;
        this.method = method;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }
    
    @Override
    public String toString()
    {
        return this.objectString + "." + this.method + "(...)";
    }
    
    public String getObjectString()
    {
        return objectString;
    }
    
    public String getMethod()
    {
        return method;
    }
    
    public Object[] getParameters()
    {
        return parameters.toArray();
    }
    
    @SuppressWarnings("rawtypes")
    public Class[] getParameterTypes()
    {
        return parameterTypes.toArray(new Class[parameterTypes.size()]);
    }
}
