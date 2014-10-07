package ha.rmi;

import java.io.Serializable;

/**
 * this is stub for all other stub to extend
 * 
 *
 */
public class Stub implements Serializable
{
    private static final long serialVersionUID = -8139526743631035631L;
    protected Reference r;
    
    public Stub(Reference r)
    {
        this.r = r;
    }
    
    public Reference getReference()
    {
        return r;
    }
    
}
