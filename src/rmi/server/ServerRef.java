package rmi.server;

import java.rmi.Remote;
import java.rmi.server.RemoteStub;

public interface ServerRef extends RemoteRef{
  static long serialVersionUID=111;
  public RemoteStub exportObject(Remote obj,Object data);

}
