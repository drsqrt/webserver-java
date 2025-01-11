package com.github.webserver.singlethread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {

  private static final Logger logger = Logger.getLogger(Server.class.getName());
  private static final int PORT = 8011;

  private void run() {
    try (ServerSocket server = new ServerSocket(PORT)) {
      logger.info("Server is listening on port : " + PORT);
      server.setSoTimeout(10000);
      while (true) {
        Socket connection = server.accept();
        logger.info("Connection accepted from client : " + connection.getLocalAddress());

        BufferedReader fromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        PrintWriter toClient = new PrintWriter(connection.getOutputStream(), true);

        toClient.println("Hello, from the server");
        logger.info("Message From client : " + fromClient.readLine());

        toClient.close();
        fromClient.close();
        connection.close();
      }
    } catch (IOException e) {
      logger.severe(e.getMessage());
    }

  }

  public static void main(String[] args) {
    new Server().run();
  }
}
