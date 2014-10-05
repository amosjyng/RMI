package ha.rmi;

import java.io.Serializable;

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
