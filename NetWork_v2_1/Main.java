import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;

public class Main  {
    public static void main(String []args){
        MyClient myClient = new MyClient();
        myClient.setVisible(true);
    }
}

class MyClient extends JFrame implements MouseMotionListener, Serializable {
    JTextField tfSever = new JTextField("192.168.100.154");
    String name = "MyClient";
    JTextField tfMessage = new JTextField("hello");
    JButton brnSent = new JButton("send");
    int x,y;
    byte[] serializedObject = new byte[2048];
    ByteArrayOutputStream bo;
    ObjectOutputStream so;


    JPanel panel = new JPanel();
//    public MyClient() {
//        setSize(300,150);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setLayout(new GridLayout(3,1));
//
//        add(tfSever);
//        add(tfMessage);
//        add(brnSent);
//
//        brnSent.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                sendMessage();
//            }
//        });
//    }
    public MyClient(){
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panel.addMouseMotionListener(this);
        panel.setBackground(Color.DARK_GRAY);

        add(panel,BorderLayout.CENTER);

    }
    private void sendMessage(){
        //String msg = tfMessage.getText();
        try{

            bo = new ByteArrayOutputStream();

            so = new ObjectOutputStream(bo);

            so.writeObject(new MyClient());

            so.flush();
            serializedObject = bo.toByteArray();

            Socket socket = new Socket(tfSever.getText(),50101);

            PrintStream dataOut = new PrintStream(socket.getOutputStream());

            dataOut.write(serializedObject);
            System.out.println(this);
            dataOut.close();

        }catch(Exception e){}
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        sendMessage();
        x = e.getX();
        y = e.getY();

    }

    public String toString(){
        return name;
    }
}


