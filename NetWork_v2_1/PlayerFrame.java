import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerFrame extends JFrame {
    private  int width,height;
    private Container contaentPane;
    private PlayerSprite me;
    private PlayerSprite enemy;
    private DrawingComponent dc;
    private Timer animationTimer;
    private boolean up,down,left,rigth;
    private Socket socket;
    private int playerID;
    private ReadFromserver rfsRunnable;
    private WriteToserver wtsRunnable;
    public PlayerFrame(int w,int h){
        width = w;
        height =h;
        up=false;
        down = false;
        left =false;
        rigth =false;
    }
    public  void  setUpGUI(){
        contaentPane = this.getContentPane();
        this.setTitle("Player#"+playerID);
        contaentPane.setPreferredSize(new Dimension(width,height));
        createSprites();
        dc = new DrawingComponent();
        contaentPane.add(dc);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        setUpAnimationTimer();
        setUpKeyListnener();
        setMouseListnener();
    }
    private  void  createSprites(){
        if (playerID==1){
            me = new PlayerSprite(100,400,50,Color.BLUE);
            enemy = new PlayerSprite(490,400,50,Color.RED);
        }else{
            enemy = new PlayerSprite(100,400,50,Color.BLUE);
            me = new PlayerSprite(490,400,50,Color.RED);
        }

    }
    private void setUpAnimationTimer(){
        int inervar =10;
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double speed =5;
                if (up){
                    me.moveV(-speed);
                }
                else if(down){
                    me.moveV(speed);
                }
                else if (left){
                    me.moveH(-speed);
                }
                else if (rigth){
                    me.moveH(speed);
                }
                dc.repaint();
            }
        };
        animationTimer = new Timer(inervar,al);
        animationTimer.start();
    }

    private void setMouseListnener(){
        MouseMotionListener Ml = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println("Mouse: "+e.getLocationOnScreen());
            }
        };
    }
    private void setUpKeyListnener(){
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int KeyCode = e.getKeyCode();
                System.out.println("KeyCode: "+KeyCode);
                switch (KeyCode){
                    case KeyEvent.VK_UP:
                        up  =true;
                        break;
                    case KeyEvent.VK_DOWN:
                        down  =true;
                        break;
                    case KeyEvent.VK_LEFT:
                        left  =true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        rigth  =true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int KeyCode = e.getKeyCode();
                switch (KeyCode){
                    case KeyEvent.VK_UP:
                        up  =false;
                        break;
                    case KeyEvent.VK_DOWN:
                        down  =false;
                        break;
                    case KeyEvent.VK_LEFT:
                        left  =false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        rigth  =false;
                        break;
                }
            }
        };
        contaentPane.addKeyListener(kl);
        contaentPane.setFocusable(true);
    }
    private void connectToServer(){
        try {
            socket = new Socket("localhost",12345);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
            System.out.println("You are Player#"+playerID);
            if (playerID == 1){
                System.out.println("Waiting fro Player #2 to Connect..");
            }
            rfsRunnable = new ReadFromserver(in);
            wtsRunnable = new WriteToserver(out);
            rfsRunnable.waitForStarMsg();
        }catch (IOException ex){
            System.out.println("IOExecption from connectToServer()");
        }
    }
    private class DrawingComponent extends JComponent{
        protected void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D) g;
            enemy.drawSperite(g2d);
            me.drawSperite(g2d);
        }
    }
    private  class  ReadFromserver implements Runnable{
        private DataInputStream dataIn;
        public  ReadFromserver(DataInputStream in){
            dataIn = in;
            System.out.println("RFS Runnable");
        }
        @Override
        public void run() {
            try {
                while (true){
                    if (enemy !=null) {
                        enemy.setX(dataIn.readDouble());
                        enemy.setY(dataIn.readDouble());
                    }
                }
            }catch (IOException ex){
                System.out.println("IOEcrption From RFS Run()");
            }
        }
        public void  waitForStarMsg(){
            try {
                String startMsg = dataIn.readUTF();
                System.out.println("Message from server "+startMsg);
                Thread readThread = new Thread(rfsRunnable);
                Thread writeThread = new Thread(wtsRunnable);
                readThread.start();
                writeThread.start();
            }catch (IOException ex){

            }
        }
    }

    private  class  WriteToserver implements Runnable{
        private DataOutputStream dataOut;
        public  WriteToserver(DataOutputStream in){
            dataOut = in;
            System.out.println("WTS Runnable");
        }
        @Override
        public void run() {
            try {
                while (true){
                    if (me!=null) {
                        dataOut.writeDouble(me.getX());
                        dataOut.writeDouble(me.getY());
                        dataOut.flush();
                    }
                    try {
                        Thread.sleep(25);
                    }catch (InterruptedException ex){
                        System.out.println("InterruptedException");
                    }
                }
            }catch (IOException ex){
                System.out.println("from WTS run()");
            }

        }
    }

    public static void main(String[] args) {
        PlayerFrame playerFrame = new PlayerFrame(640,480);
        playerFrame.connectToServer();
        playerFrame.setUpGUI();
    }
}
