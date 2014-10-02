package rmi.server;

import java.io.Externalizable;
import java.io.ObjectOutput;



import java.lang.reflect.Method;

import rmi.Remote;
public interface RemoteRef extends Externalizable{
  static long serialVersionUID=3632638527362204081L;
  static String packagePrefix = "rmi.server";
  public String getRefClass(ObjectOutput out);
  public Object invoke(Remote obj, Method method, Object[] params, long opnum);
  public boolean remoteEqeuals(RemoteRef obj);
  public int remoteHashCode();
  String remoteToString();

}
