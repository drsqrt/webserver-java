package com.github.webserver.threadpool;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {

  private static final Logger logger = Logger.getLogger(Server.class.getName());
  private static final int PORT = 8011, POOL_SIZE = 10;
  private final ExecutorService threadPool; /// shout out to Author: Doug Lea

  public Server(int poolSize) {
    this.threadPool = Executors.newFixedThreadPool(poolSize);
  }

  public void handleClient(Socket clientSocket) {
    try (PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true)) {
      toSocket.println("Hello from server " + clientSocket.getInetAddress());
    } catch (IOException ex) {
      logger.severe(ex.getMessage());
    }
  }

  public static void main(String[] args) {
    Server server = new Server(POOL_SIZE);

    try(ServerSocket serverSocket = new ServerSocket(PORT)) {
      serverSocket.setSoTimeout(70000);
      logger.info("Server is listening on port " + PORT);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        server.threadPool.execute(() -> server.handleClient(clientSocket));
      }
    } catch (IOException ex) {
      logger.severe(ex.getMessage());
    } finally {
      server.threadPool.shutdown();
    }
  }
}
