package ha.rmi;

import java.io.Serializable;

public class Reference implements Serializable
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