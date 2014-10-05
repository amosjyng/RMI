package rmi.server;

import java.net.MalformedURLException;

public abstract class RMIClassLoaderSpi extends Object{

  public RMIClassLoaderSpi(){
    
  }
  public abstract Class<?> loadClass (String codeBase, String name,ClassLoader defaultLoader)throws MalformedURLException, ClassNotFoundException;
  public abstract Class<?> loadProxyClass (String codeBase, String[] interfaces,ClassLoader defaultLoader)throws MalformedURLException, ClassNotFoundException;
  public abstract ClassLoader getClassLoader (String codebase)throws MalformedURLException;
  public abstract String getClassAnnotation (Class<?> cl);
}
