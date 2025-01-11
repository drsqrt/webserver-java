package com.github.webserver.multithread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class Client {

  private static final Logger logger = Logger.getLogger(Client.class.getName());
  private static final int PORT = 8011;

  private void run() {
    for (int i = 0; i < 100; i++) {
      Thread thread = new Thread(() -> {
        try {
          InetAddress localhost = InetAddress.getLocalHost();
          Socket socket = new Socket(localhost, PORT);
          try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
               BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
          ) {
            writer.println("Hello from client");
            logger.info("Message from server : " + reader.readLine());
          }
          socket.close();
        } catch (IOException e) {
          logger.severe(e.getMessage());
        }
      });
      thread.start();
    }
  }

  public static void main(String[] args) {
    new Client().run();
  }
}
