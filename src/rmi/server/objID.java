package rmi.server;

import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

public class objID extends Object implements Serializable{
  static int ACTIVATOR_ID；
  static int DGC_ID；
  static int REGISTRY_ID；
  public void objID() {
  }
  public void ObjID(int objNum) {
  }
  public boolean  equals(Object obj) {
    return false;
  }
  public int  hashCode() {
    return 0;
  }
  public static ObjID read(ObjectInput in) {
    return null;
  }
  String  toString() {
    return null;
  }
  void  write(ObjectOutput out);

}
