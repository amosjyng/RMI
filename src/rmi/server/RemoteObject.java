package rmi.server;

import java.io.Serializable;

import rmi.Remote;
public abstract class RemoteObject extends Object implements Remote, Serializable{
  protected RemoteRef ref;
  protected RemoteObject() {
  }
  protected RemoteObject(RemoteRef newref) {
  }
  public boolean equals(Object obj) {
    return false;
  }
  RemoteRef getRef() {
    return null;
  }
  public int hashCode() {
    return 0;
  }
  public String  toString() {
    return null;
  }
  static Remote toStub(Remote obj) {
    return null;
  }
}
