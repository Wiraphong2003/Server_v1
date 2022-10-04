import java.awt.BorderLayout;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

public class DemoServers {
    JTextArea taShow = new JTextArea();
    public static void main(String[]args) {
        ServerUI serverUI = new ServerUI();
        serverUI.setVisible(true);
    }
}
class ServerUI extends JFrame{
    JTextArea taShow = new JTextArea();

    public ServerUI(){
        setSize(300,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(taShow);
        ServerThread thread = new ServerThread(this);
        thread.start();
    }
}

class ServerThread extends Thread {
    ServerSocket serverSocket;
    ServerUI serverUI;
    MyClient frame;
    ServerThread(ServerUI serverUI) {
        this.serverUI = serverUI;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(50101);
            while (true) {
                    try {
                        System.out.println("accept1");
                        Socket socket = serverSocket.accept();

                        InputStream input = socket.getInputStream();

                        byte[] data = new byte[2048];
                        input.read(data);
                        ByteArrayInputStream bi = new ByteArrayInputStream(data);
                        ObjectInputStream si = new ObjectInputStream(bi);
                        System.out.println((MyClient) si.readObject());
                        frame = (MyClient) si.readObject();


                    }catch(Exception e ){}

            }

        } catch (Exception e) {
        }
    }
}