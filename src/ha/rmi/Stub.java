package ha.rmi;

public class Stub
{
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
