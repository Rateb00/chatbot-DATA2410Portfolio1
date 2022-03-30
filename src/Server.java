import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void start() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();                       //accept connection with the client
                System.out.println("new client is connected");               //to maintain a list of connected clients
                ClientHandler clientHandler = new ClientHandler(socket);     //implement clienthandler class
                Thread thread = new Thread(clientHandler);                   //make th thread
                thread.start();                                              // start the reun method

            }
        }catch (IOException e) {                                             //gracefully close everything
            try {
                    if (serverSocket != null){
                        serverSocket.close();
                    }
            }catch (IOException r) {
                    r.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        System.out.println("////////////Welcome to Rateb's Socket Bot////////////");
        ServerSocket serverSocket = new ServerSocket(1925);             //connect to the clients on port 2022
        Server server = new Server(serverSocket);
        server.start();                                                      //call the start method to start server
    }
}