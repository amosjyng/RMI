package rmi.server;

import java.io.OutputStream;
import java.io.PrintStream;
import java.rmi.server.ServerNotActiveException;

public abstract class RemoteServer extends RemoteObject{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  protected RemoteServer(){
    
  }
  protected RemoteServer(RemoteRef ref){
    
  }
  public static String getClientHost()throws ServerNotActiveException{
    return null;
    
  }
  public static PrintStream getLog(){
    return null;
    
  }
  public static void setLog(OutputStream out){
    
  }

}
