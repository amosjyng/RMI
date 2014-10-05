package ha.rmi;

import java.io.Serializable;

public class Reference implements Serializable
{
    /**
     * Generated UID for Java serialization
     */
    private static final long serialVersionUID = 6295695796518967426L;
    
    /**
     * what the name of the object in registry 
     */
    private String name;
    
    /**
     * Which machine this object is located on
     */
    private String host;
    
    /**
     * Which port that machine is listening on for method invocation requests
     */
    private int port;
    
    public Reference(String name,String host, int port)
    {
        this.name=name;
        this.host = host;
        this.port = port;
    }
    
    @Override
    public String toString()
    {
        return name+":"+host + ":" + port;
    }
    
    public String getName(){
      return name;
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