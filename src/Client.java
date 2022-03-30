import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;                          //a socket to connect to the server
    private BufferedReader in;                      //Bufferedreader to recive messages
    private BufferedWriter out;                     // Bufferedwriter to send messages
    private String username;                        // a string for username


    //Client takes socket and username as parameters, send and recive messages with bufferedreader and writer
public Client(Socket socket, String username){
    try {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.username = username;
    } catch (IOException e) {                          //gracefully close everything
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException r) {
            e.printStackTrace();
        }

    }

}


public void sendmessage() {                              //a method to send messages without spawning a thread
    try {
            out.write(username);                        // send the username of the client
            out.newLine();
            out.flush();

        Scanner scanner = new Scanner(System.in);       //scanner for user input
        while (socket.isConnected()){                   //while connected to the server
            String message = scanner.nextLine();        // scan the terminal
            out.write(username + " says : " + message);   //send the message with the username thats sent it
            out.newLine();
            out.flush();
        }
    } catch (IOException e){                            //gracefully close everything
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException r) {
            e.printStackTrace();
        }

    }

}


public void listen() {                               //a method to listen to messages with spawning a thread
    new Thread(new Runnable() {
        @Override
        public void run() {
            while (socket.isConnected()){           //while connected to the server
                try {
                    String message = in.readLine();     //listen for messages
                    System.out.println(message);        //print other users messages to console
                }catch (IOException e){
                    try {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (IOException r) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }).start();
}


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("please enter your name first >");  //ask the client for the name
        String username = scanner.nextLine();              //get the username
            System.out.println("Hei " + username + " here are some suggestion { " + actions() + ", "
                    + actions() + ", " + actions() + ", " + actions() + " } >             (bye to quite)");  //print some random suggestion for each client
        Socket socket = new Socket("localhost", 1925); // create a socket to connect to server
        Client client = new Client(socket, username); //give a client a username
        client.listen();    //read messages
        client.sendmessage(); //send messages
    }

    public static String actions() throws IOException {
        String[] action = new String[]{"What do you think about eating", "lets all cry together today ", "why dont we steal",
                "why dont we talk", "do you guys want to fight", "do you guys want to eat ", "i want to complain",
                "lets all yell", "i like work"};
        String differentaction = action[(int) (Math.random() * action.length)];

        return differentaction;
    }


}