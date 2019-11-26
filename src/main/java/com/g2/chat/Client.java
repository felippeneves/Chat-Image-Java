package com.g2.chat;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class Client extends javax.swing.JFrame{
  private String username;
  private ArrayList<String> users = new ArrayList();
  
  private Socket socket;
  private BufferedReader reader;
  private PrintWriter writer;
  
  private ByteArrayOutputStream byteArrayOutputStream;
  private OutputStream outputStream;
  
  public static final String NEXT_LINE = "\n";
  
  public void ListenThread() {
    Thread IncomingReader = new Thread(new IncomingReader());
    IncomingReader.start();
  }
  
  public void userAdd(String data) {
    users.add(data);
  }
  
  public void userRemove(String data) {
      addText("data + \" agora está desconectado.");
  }
  
  public void writeUsers() {
    String[] tempList = new String[(users.size())];
    users.toArray(tempList);
    
    for (String token:tempList) {
      //users.append(token + "\n");
    }
  }
  
  
  public void disconnect() {
    try {
      writer.println(username + ": :Disconnect");
      
      writer.flush();
      
      socket.close();
    } catch (IOException ex) {
        addText("Não foi possivel desconectar.");
    }
  }
  
  public Client() {
    initComponents();
    connect();
  }
  
  private void connect(){
    //username = JOptionPane.showInputDialog(null, "Digite seu nome: ");
    Random random = new Random();
    
    int idUser = random.nextInt(99999);
    username = "User " + idUser;

    try {
      socket = new Socket("localhost", 2222);
      
      InputStreamReader streamreader = new InputStreamReader(socket.getInputStream());
      reader = new BufferedReader(streamreader);
      writer = new PrintWriter(socket.getOutputStream());
      byteArrayOutputStream = new ByteArrayOutputStream();
      outputStream = socket.getOutputStream();
      
      writer.println(username + ": se conectou.:Connect");
      writer.flush();
      
      this.setTitle("Chat - " + username);
    }
    catch (Exception ex) {
        addText("Erro ao conectar.");
        
    }
    
    ListenThread();
  }
  
  private void sendMessage() {
    if (!tfChat.getText().equals("")) {
      try {
        writer.println(username + ": " + tfChat.getText() + ":Chat");
        writer.flush();
      } catch (Exception ex) {
          addText("Erro ao enviar a mensagem.");
      }
    }
    
    tfChat.setText("");
    tfChat.requestFocus();
  }
  
  private void sendPhoto(String path) {
      
      try {
        String extensao = path.split("[.]")[1];
        ByteArrayOutputStream baos=new ByteArrayOutputStream(1000);
        BufferedImage img=ImageIO.read(new File(path));
        ImageIO.write(img, extensao, baos);
        baos.flush();
        byte[] encodedBytes = Base64.getEncoder().encode(baos.toByteArray());
        baos.close();
        writer.println(username + " photo:" + new String(encodedBytes) + ":Chat");
        writer.flush();
      }
      catch (Exception ex) {
          
      }
      
  }
  
  public class IncomingReader implements Runnable {
    @Override
    public void run() {
      String[] data;
      String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";
      
      try {
        while ((stream = reader.readLine()) != null) {
          data = stream.split(":");
          
          if (data[2].equals(chat)) {
              
              if(data[0].contains("photo")) {
                String encodedStrig = data[1];
                byte [] decodedBytes = Base64.getDecoder().decode(encodedStrig);
                
                 BufferedImage imag=ImageIO.read(new ByteArrayInputStream(decodedBytes));
                 Image image = imag.getScaledInstance(100, 100, 100);
                 
                 
                 tp_chat.insertIcon(ResizeImage(image));
//                 addText(NEXT_LINE);
                 addText(data[0] + ": Enviou a imagem");
              }
              else {
                addText(data[0] + ": " + data[1]);
              }
          }
          else if (data[2].equals(connect)) {
//            ta_chat.removeAll();
            userAdd(data[0]);
          }
          else if (data[2].equals(disconnect)) {
            userRemove(data[0]);
          }
          else if (data[2].equals(done)) {
            writeUsers();
            users.clear();
          }
        }
      }catch(Exception ex) {
        //
      }
    }
  }
  
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tp_chat = new javax.swing.JTextPane();
        tfChat = new javax.swing.JTextField();
        btSend = new javax.swing.JButton();
        btPhoto = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat");
        setBackground(new java.awt.Color(51, 51, 51));
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("client"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel.setBackground(new java.awt.Color(51, 51, 51));

        tp_chat.setEditable(false);
        tp_chat.setBackground(new java.awt.Color(70, 70, 70));
        tp_chat.setFont(new java.awt.Font("Monospaced", 0, 13)); // NOI18N
        tp_chat.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(tp_chat);

        tfChat.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tfChat.setMargin(new java.awt.Insets(10, 10, 10, 10));
        tfChat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfChatKeyPressed(evt);
            }
        });

        btSend.setBackground(new java.awt.Color(40, 167, 69));
        btSend.setForeground(new java.awt.Color(255, 255, 255));
        btSend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ic_send.png"))); // NOI18N
        btSend.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btSend.setInheritsPopupMenu(true);
        btSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSendActionPerformed(evt);
            }
        });

        btPhoto.setBackground(new java.awt.Color(40, 167, 69));
        btPhoto.setForeground(new java.awt.Color(255, 255, 255));
        btPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ic_library.png"))); // NOI18N
        btPhoto.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btPhoto.setInheritsPopupMenu(true);
        btPhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPhotoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanelLayout.createSequentialGroup()
                        .addComponent(tfChat, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfChat, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btSend, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
  
    private void btSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSendActionPerformed
      sendMessage();
    }//GEN-LAST:event_btSendActionPerformed
    
  private void tfChatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfChatKeyPressed
    if (evt.getKeyCode() == 10){
      sendMessage();
    }
  }//GEN-LAST:event_tfChatKeyPressed
  
  private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    disconnect();
  }//GEN-LAST:event_formWindowClosed

    private void btPhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPhotoActionPerformed
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "png");
        file.addChoosableFileFilter(filter);
        int result = file.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION) {
            File fileSelected = file.getSelectedFile();
            String path = fileSelected.getAbsolutePath();
            sendPhoto(path);
//            addText("Usuario: ");
//            tp_chat.insertIcon(ResizeImage(path));
//            
//            
//            
//            addText("\n");
        }
    }//GEN-LAST:event_btPhotoActionPerformed
  
    public void addText(String text) {
        addText(text, true);
    }
    
     public void addText(String text, boolean nextLine)  {
        StyledDocument doc = tp_chat.getStyledDocument();

        try {
           String textFormated = nextLine ? text + NEXT_LINE : text;
           doc.insertString(doc.getLength(), textFormated, null);
        }
        catch(BadLocationException ex){
        }
            
    }
    
     public ImageIcon ResizeImage(Image image){
        Image newImg = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }
     
//    public ImageIcon ResizeImage(String path){
//        ImageIcon image = new ImageIcon(path);
//        Image img = image.getImage();
//        Image newImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
//        return new ImageIcon(newImg);
//    }
    
  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      
      public void run() {
        new Client().setVisible(true);
      }
    });
  }
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btPhoto;
    private javax.swing.JButton btSend;
    private javax.swing.JPanel jPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField tfChat;
    private javax.swing.JTextPane tp_chat;
    // End of variables declaration//GEN-END:variables
}
