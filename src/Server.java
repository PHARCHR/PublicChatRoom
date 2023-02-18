import java.io.IOException;
import java.net.*;

public class Server
 {
    /* This sever class will be responsible for listening to clients who wish
     to connect  when they do it will spawn a new thread when they do.
     */

     private ServerSocket serverSocket;
    /* This severSocket object will be responsible for listening for incoming  clients 
     * and creates a socket object to communicate with them.
    */
    public Server(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }

    public static void main(String[] args) throws IOException
    {
        // server will be listening to clients that make a 
        // connection to the port 7890
        ServerSocket serverSocket = new ServerSocket(7890);
        Server ourServer = new Server(serverSocket);
        ourServer.startServer();
        /*when a client connect to the server their port must match 
        server's port 7890*/
    }

    public void startServer()
    {
        /* This method will be responsible for keeping the
         * server running.
         */
        try
        {
            /*Since we want our server running indefinitely
             * while the server isn't closed we want to be waiting for a client 
             * to connect, this can be done with ServerSocket "accept()" method
            */
            while(!serverSocket.isClosed())
            {
                Socket clientConnected = serverSocket.accept();
                // When  a client is connected we will show a new client is connected.
                // For now on the standard output
                System.out.println("A new client has been connected.");

                ClientController controlClient = new ClientController(clientConnected);

                /*What ever is overridden of run method of the class that implements runnable 
                 * is what is executed on a seprate thread but to spawn a new thread we need to 
                 * create a new thread and pass an object of instance of a class that implements runnable
                */

                Thread eachThread = new Thread(controlClient);
                // Creating the thread
                eachThread.start();
                // running the thread
            }
            /* when a client is connected the methods returns a socket which can be used to 
             * connect with the connected client.
            */
        } 
        catch(IOException e)
        {

        } 

    }

    public void closeTheServerSocket()
    {
        /*This method is used to shutdown our  server socket if any error occurs*/
        try
        {
            /*Making sure our server socket is not null. */
            if(serverSocket != null)
            {
                serverSocket.close();
            }
        }
        catch(IOException E)
        {
            E.printStackTrace();
        }
    }
 }