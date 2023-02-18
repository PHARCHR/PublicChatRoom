import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientController implements Runnable 
{
    /*Each object of this class will be responsible for communicating 
     * with a client, and it will implement the interface Runnable.*/
    /*Runnable is an interface which is implemented in a class whose instances
     * will be executed by a seprate thread which is the main point of this
     * project because if we didn't spawn a new thread for each client our char room 
     * will only be able to connect to one server and one client.
    */

    /*Thread is a sequence of instructions with in a program that can 
     * be executed independently of each other
     */

    /*Let's create a constructor that will accept a socket object */

    public static ArrayList<ClientController> clients = new ArrayList<>();
    /*This List keeps track of all our client and whenever a client sends a
     * message we go through our list of clients and send a message to each client.
    static because we want it to belong to the class not to each object */

    private Socket clientSocket;
    // This reader will used to read messages that have been sent by the clients
    private BufferedReader clientMessageReader;
    // This writer will be used to send messages to our clients
    private BufferedWriter clientMessageWriter;
    // This String will be responsible for storing name of the client
    private String clientName;

    public ClientController(Socket socketAccepted)
    {
        try
        {
            clientSocket = socketAccepted; 
            // Changing our byte stream to character stream with each time we recieve or send a message

            this.clientMessageWriter = new BufferedWriter(new OutputStreamWriter(socketAccepted.getOutputStream()));
            //used to send  a message

            this.clientMessageReader = new BufferedReader(new InputStreamReader(socketAccepted.getInputStream()));
            // used to recieve message

            this.clientName = clientMessageReader.readLine();
            // first entered line is client's user name from client

            //add the each client's contoller to the List 
            clients.add(this);

            // Send a message that tell other clients a new client has entered
            broadcastMessage(clientName + " has entered the group chat.");

        }
        catch(IOException e)
        {
            closeEverything(socketAccepted, clientMessageReader, clientMessageWriter);
        }

    }


    @Override
    public void run()
    {
        // Everything run on this method will be implemented on 
        // a separate thread that is -- listening for messages
        // and sending some messages

        // variable used to recieve message from clients
        String messageFromClient;

        // while there is connection with a client listen for a message
        while (clientSocket.isConnected())
        {
            // we want to run on seprated thread because we don't want 
            // to close our whole operation 
            try
            {
                messageFromClient = clientMessageReader.readLine();
                broadcastMessage(messageFromClient);
            }
            catch(IOException ioe)
            {
                closeEverything(clientSocket,clientMessageReader,clientMessageWriter);

                // when client disconnects we will break out of the while loop
                break;
            }

        }

    }

    

    public void broadcastMessage(String messageRecieved)
    {
        for (ClientController eachClient : clients)
        {
            try
            {
                // if client is not sender send the message
                // doesn't send a message back to the sender
                if(!eachClient.clientName.equals(this.clientName))
                {
                    eachClient.clientMessageWriter.write(messageRecieved);
                    //since our client will be waiting new line 
                    // to detect end of message we have to explicitly send a new line
                    eachClient.clientMessageWriter.newLine();
                    // flush our buffer
                    eachClient.clientMessageWriter.flush();

                }

            }
            catch(IOException ioe)
            {
                closeEverything(clientSocket,clientMessageReader,clientMessageWriter);

            }
        }
    }

    // method used to remove a disconnected client
    public void removeClientHandler()
    {
        // if the user left the chat don't send a message to them
        // so we remove them from the client list
        clients.remove(this);

        // tell everyone in the chat room that a user has left
        broadcastMessage(this.clientName + " left the chat room." );
    }

    // method used to close everything

    public void closeEverything(Socket thesocket, BufferedReader theReader, BufferedWriter theWriter)
    {
        removeClientHandler();
        try
        {
            // for streams we only need to close the wrapped streams.
            // because the outer streams close when the innder streams close
            // no need to close input and output stream writer and reader.

            if(theReader != null)// not to catch null pointer exception
            {
                theReader.close();
            }
            if(theWriter != null)// not to catch null pointer exception
            {
                theReader.close();
            }

            //closing a socket will also close sockets input and output stream
            if(thesocket != null)// not to catch null pointer exception
            {
                thesocket.close();
            }

        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

    }
}
