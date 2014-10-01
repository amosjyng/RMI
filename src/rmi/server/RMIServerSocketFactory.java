package rmi.server;

import java.net.ServerSocket;

public interface RMIServerSocketFactory {
  ServerSocket createServerSocket(int port);

}
