package com.g2.chat;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import javax.imageio.ImageIO;

public class Server extends javax.swing.JFrame {
  ArrayList clientOutputStreams;
  
  public class ClientHandler implements Runnable {
    private BufferedReader reader;
    private Socket socket;
    private PrintWriter client;
    private InputStream inputStream;
    
    public ClientHandler(Socket clientSocket, PrintWriter user) {
      client = user;
      try {
        socket = clientSocket;
        InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
        reader = new BufferedReader(isReader);
        inputStream = socket.getInputStream();
      }
      catch (Exception ex) {
        ta_chat.append("Unexpected error... \n");
      }
      
    }
    
    @Override
    public void run() {
      String message, connect = "Connect", disconnect = "Disconnect", chat = "Chat" ;
      String[] data;
      
      try {
        while ((message = reader.readLine()) != null) {
            
          
          ta_chat.append(message + "\n");
          data = message.split(":");
          
          for (String token:data) {
            ta_chat.append(token + "\n");
          }
          
          if (data[2].equals(connect)) {
            tellEveryone((data[0] + ":" + data[1] + ":" + chat));
            userAdd(data[0]);
          }
          else if (data[2].equals(disconnect)) {
            tellEveryone((data[0] + ":has disconnected." + ":" + chat));
            userRemove(data[0]);
          }
          else if (data[2].equals(chat)) {
            tellEveryone(message);
          }
          else {
            ta_chat.append("No Conditions were met. \n");
          }
        }
      }
      catch (Exception ex) {
        ta_chat.append("Lost a connection. \n");
        ex.printStackTrace();
        clientOutputStreams.remove(client);
      }
    }
  }
  
  public Server() {
    initComponents();
    initServer();
  }
  
  private void initServer(){
    Thread starter = new Thread(new ServerStart());
    starter.start();
    
    ta_chat.append("Servidor iniciado.\n");
  }
  
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel = new javax.swing.JPanel();
    ta_chat = new javax.swing.JTextArea();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Chat server");
    setName("server"); // NOI18N
    setResizable(false);

    jPanel.setBackground(new java.awt.Color(51, 51, 51));

    ta_chat.setBackground(new java.awt.Color(51, 51, 51));
    ta_chat.setColumns(20);
    ta_chat.setForeground(new java.awt.Color(255, 255, 255));
    ta_chat.setRows(5);
    ta_chat.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

    javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
    jPanel.setLayout(jPanelLayout);
    jPanelLayout.setHorizontalGroup(
      jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(ta_chat, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
    );
    jPanelLayout.setVerticalGroup(
      jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(ta_chat, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents
  
  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        new Server().setVisible(true);
      }
    });
  }
  
  public class ServerStart implements Runnable {
    @Override
    public void run() {
      clientOutputStreams = new ArrayList();
      
      try {
        ServerSocket serverSock = new ServerSocket(2222);
        
        while (true) {
          Socket clientSock = serverSock.accept();
          PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
          clientOutputStreams.add(writer);
          
          Thread listener = new Thread(new ClientHandler(clientSock, writer));
          listener.start();
          ta_chat.append("Got a connection. \n");
        }
      }
      catch (Exception ex) {
        ta_chat.append("Error making a connection. \n");
      }
    }
  }
  
  public void userAdd (String data) {
    String message, add = ": :Connect", done = "Server: :Done", name = data;
    ta_chat.append("Before " + name + " added. \n");
    ta_chat.append("After " + name + " added. \n");
    
    tellEveryone(done);
  }
  
  public void userRemove (String data) {
    String message, add = ": :Connect", done = "Server: :Done", name = data;
    
    tellEveryone(done);
  }
  
  public void tellEveryone(String message) {
    Iterator it = clientOutputStreams.iterator();
    
    while (it.hasNext()) {
      try {
        PrintWriter writer = (PrintWriter) it.next();
        writer.println(message);
        ta_chat.append(message + "\n");
        writer.flush();
        ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
      }
      catch (Exception ex)        {
        ta_chat.append("Error telling everyone. \n");
      }
    }
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel jPanel;
  private javax.swing.JTextArea ta_chat;
  // End of variables declaration//GEN-END:variables
}
