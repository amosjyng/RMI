package rmi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * T is the type of the object contained in the marshaled object, marshal is just write and transmit
 * 
 * @author hanz
 *
 * @param <T>
 */
public class MarshalledObject<T> extends Object implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  byte[] objBytes;

  byte[] locBytes;

  int hash;

  MarshalledObject(T obj) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = null;
    try {
      out = new ObjectOutputStream(bos);
      out.writeObject(obj);

      objBytes = bos.toByteArray();

      // bos.flush();
    } catch (IOException ex) {

    }
    hash = 0;
    for (int i = 0; i < objBytes.length; i++)
      hash = hash * 31 + objBytes[i];
    if (locBytes != null) {
      for (int i = 0; i < locBytes.length; i++)
        hash = hash * 31 + locBytes[i];
    }

  }

  public T get() throws ClassNotFoundException {
    ByteArrayInputStream bis = new ByteArrayInputStream(objBytes);
    ObjectInput in = null;
    try {
      in = new ObjectInputStream(bis);
      return (T) in.readObject(); 
     
    }catch (IOException ex){
      
    }
    
    return null;
  }

  public int hashcode() {
    return hash;
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof MarshalledObject))
      return false;

    // hashCode even differs, don't do the time-consuming comparisons
    if (obj.hashCode() != hash)
      return false;

    MarshalledObject aobj = (MarshalledObject) obj;
    if (objBytes == null || aobj.objBytes == null)
      return objBytes == aobj.objBytes;
    if (objBytes.length != aobj.objBytes.length)
      return false;
    for (int i = 0; i < objBytes.length; i++) {
      if (objBytes[i] != aobj.objBytes[i])
        return false;
    }
    // Ignore comparison of locBytes(annotation)
    return true;

  }

}
