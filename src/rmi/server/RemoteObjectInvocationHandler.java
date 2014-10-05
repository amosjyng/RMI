package rmi.server;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RemoteObjectInvocationHandler extends RemoteObject implements InvocationHandler{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public RemoteObjectInvocationHandler(RemoteRef ref){
    
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // TODO Auto-generated method stub
    return null;
  }
  
}
