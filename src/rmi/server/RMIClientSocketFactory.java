package rmi.server;

import java.io.IOException;
import java.net.Socket;

public interface RMIClientSocketFactory {
  Socket createSocket(String host,int port)throws IOException;

}
