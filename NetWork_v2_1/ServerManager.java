import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManager {
    public static void main(String[] args) {
        new ServerManager(50101);
    }
    private final int port ; //Portnummer
    Person person;
    private Socket socket = null;

    private ObjectInputStream inputStream = null; //Objekt vom Client
    private ObjectOutputStream outputStream = null; //Objekt an Client

    private Object obj; //Objekt das übers Netzwerk gesendet wird

    //Konstruktor
    public ServerManager(int port){
        this.port = port;
    }

    //Bekommt ein Objekt von einem Client
    public Object getObjectFromClient(){

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            socket = serverSocket.accept();

            inputStream = new ObjectInputStream(socket.getInputStream());

            obj = inputStream.readObject();
            System.out.println(obj);
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    public void sendObjectToClient(Object obj){
        //Muss noch erstellt werden
    }

}