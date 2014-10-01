package ha.rmi;

public class RemoteException extends Exception
{
    private static final long serialVersionUID = 9097252917535125449L;

    public RemoteException(String message)
    {
        super(message);
    }
    
}
