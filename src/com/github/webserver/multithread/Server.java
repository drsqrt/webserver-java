package com.github.webserver.multithread;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Server {

  private static final Logger logger = Logger.getLogger(Server.class.getName());
  private static final int PORT = 8011;

  private Consumer<Socket> getConsumer() {
    return socket -> {
      try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
        writer.println("Hello from server : " + socket.getInetAddress());
        socket.close();
      } catch (IOException e) {
        logger.severe(e.getMessage());
      }
    };
  }

  private void run() {
    try (ServerSocket server = new ServerSocket(PORT)) {
      logger.info("Server is listening on port : " + PORT);
      server.setSoTimeout(60000);
      int receivedConnection = 0;
      while (true) {
        Socket connection = server.accept();
        logger.info("Connection accepted from client : " + connection.getLocalAddress());
        Thread thread = new Thread(() -> this.getConsumer().accept(connection));
        thread.start();
        logger.info("Connection Number : " + receivedConnection++);
      }
    } catch (IOException e) {
      logger.severe(e.getMessage());
    }

  }

  public static void main(String[] args) {
    new Server().run();
  }
}
