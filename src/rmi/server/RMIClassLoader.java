package rmi.server;

import java.net.MalformedURLException;
import java.net.URL;

public class RMIClassLoader extends Object{
  public static Class<?> loadClass(URL codebase,String name)throws MalformedURLException,ClassNotFoundException{
    return null;
    
  }
  public static Class<?> loadClass(String codebase,String name)throws MalformedURLException,ClassNotFoundException{
    return null;
    
  }
  public static Class<?> loadClass(String codebase,String name,ClassLoader defaultLoader)throws MalformedURLException,ClassNotFoundException{
    return null;
    
  }
  public static Class<?> loadProxyClass(String codebase,String[] interfaces,ClassLoader defaultLoader)throws ClassNotFoundException,MalformedURLException{
                    return null;
    
  }
  public static ClassLoader getClassLoader(String codebase){
    return null;
    
  }
  public static String getClassAnnotation(Class<?> cl){
    return null;
    
  }
  public static RMIClassLoaderSpi getDefaultProviderInstance(){
    return null;
    
  }
}
