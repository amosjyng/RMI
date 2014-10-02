package rmi.server;

public interface ServerRef extends RemoteRef{
  static long serialVersionUID=111;
  public RemoteStub exportObject(Remote obj,Object data);

}
