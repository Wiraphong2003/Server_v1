import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientManager {
    private String server;
    private int port;

    //Streams für Serialisierte Objekte
    private ObjectInputStream inputObjectStream = null;
    private ObjectOutputStream outputObjectStream = null;

    //Konstruktor: Server und Port werden benötigt
    public ClientManager(String server,int port){
        this.server = server;
        this.port = port;
    }

    //Sendet ein Object an eine Serverinstanz
    public void sendObjectToServer(Object obj){
        try(Socket socket = new Socket(server,port)){

            outputObjectStream = new ObjectOutputStream(socket.getOutputStream());
            outputObjectStream.writeObject(obj);

            outputObjectStream.flush();
            outputObjectStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Bekommt ein Object von einer Serverinstanz
    public Object getObjectFromServer(){
        return new Object();
    }

    public static void main(String[] args) {
        new ClientManager("192.168.100.154",50101);
    }
}
