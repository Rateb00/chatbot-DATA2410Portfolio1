import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();     //arraylist to send messages to the clients with thread handling
    private Socket socket;                          // socket to connect
    private BufferedReader in;                      //read the messages
    private BufferedWriter out;                     //send the messages
    private String username;                        //clients usernames

    public ClientHandler(Socket socket) {
        try {
                this.socket = socket;
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.username = in.readLine();                  //send the username of the client once its connected
                clientHandlers.add(this);                       //to recive messages from other, add the client handler
        }catch (IOException e) {                                //gracefully close everything
            removeclient();
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
                r.printStackTrace();
            }
        }

    }

    @Override
    public void run() {
        while (socket.isConnected()){
            try {
                String message = in.readLine();                                      //read what th client send
                sendmessage(message);                                                //send the message to other
                if (message.contains("bye")){                       //if client message include word bye
                    sendmessage("ok bye " + username);   //remove client og send melding
                    removeclient();
                    //i couild make a string with bad words, so that if client misbehave then remove him
                    //inside the if setning add some bade words and then call function removeclient

                }
                //if the client dose not write one of the words mentioned in the string then send message
                else if (!(message.contains("eat") || message.contains("fight") || message.contains("sleep") || message.contains("sing")
                        || message.contains("play") || message.contains("cry") || message.contains("complain") || message.contains("yell")
                        || message.contains("work")))
                {
                    sendmessage("i dont understand, please choose one of the suggestion above ");
                }
                String [] matches = new String[] {"eat", "fight", "sleep", "sing", "play", "cry", "complain" , "yell", "work"}; //string with words to extract
                for (String s : matches) {                                           //make a string that icludes the words you want your bot to extract
                    if (message.contains(s)) {                                       //if the message from client includes one of this words
                        sendmessage("Alice: " + Alice(s) + "Bob: " + Bob(s) + // bot answers
                                "Dora: " + Dora(s) + "Chunk: " + Chunk(s));
                    }
                }



            }catch (IOException e) {
                removeclient();
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
                    r.printStackTrace();
                }
                break;
            }
        }

    }



    public void sendmessage(String messagesend){
        //use client thread to send messages so that other users can get messages too
        //when a message is recived, loop through each connection and send it down
        for (ClientHandler clientHandler : clientHandlers){
                try {                            //i choose to send the messages to the client who send it too
                    clientHandler.out.write(messagesend);
                    clientHandler.out.newLine();
                    clientHandler.out.flush();
                }catch (IOException e){          //gracefully close everything
                    removeclient();
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
                        r.printStackTrace();
                    }
                }
        }
    }


public void removeclient() {
    clientHandlers.remove(this);                                     //when the client dissconnect remove
    sendmessage("Server: " + username + " has left ");      //notify all clients
    System.out.println(username + " has left");                        //send message to the server
}




    //define my 4 bots and let them extract the client word and respond to it
    public static String Alice(String alice) throws IOException {                                   //a Bot function that takes a string from client input
        return "i think " + alice + "ing sounds great \n";                                          //return a string with the extracted word from client line
    }

    public static String Bob(String bob) throws IOException {                                        //a Bot function that takes a string from client input
        String [] bobactions = new String[] {"playing", "singing", "hugging", "working"};           //make a string with different actions
        String bobaction = bobactions [ (int) (Math.random()*bobactions.length)];                   //make a method to provide random actions
        return bob + "ing sounds ok!, but i was thinking maybe we could do some " + bobaction + "\n" ;  //return a string with the extracted word from client line
                                                                                                        //and suggest something else instead
    }

    public static String Dora(String dora) throws IOException {                                   //a Bot function that takes a string from client input
        return "No i dont like " + dora + "ing ,can we do something else? \n";                     //return a string with the extracted word from client line
    }

    public static String Chunk(String chunk) throws IOException {                                  //a Bot function that takes a string from client input
        return " yes time for " + chunk + "ing " ;                                                //return a string with the extracted word from client line
    }
}




