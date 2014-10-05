package rmi.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import sunw.io.Serializable;

public final class UID extends Object implements Serializable{
  public UID(){
    
  }
  public UID(short wellKnownId){
    
  }
  public boolean equals(Object other) {
    return false;
  }
  public int hashCode(){
    return 0;
    
  }
  public static UID read(DataInput in)throws IOException{
    return null;
    
  }
  public String toString(){
    return null;
    
  }
  public void write(DataOutput out)throws IOException{
    
  }

}
