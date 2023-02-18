import java.net.*;
import java.io.*;
import java.util.*;

public class Client 
{
    private Socket clientsSocket;
    private BufferedReader messageRead;
    private BufferedWriter messageWrite;
    private String clientName;

    public Client(Socket socket, String userName)
    {
        try
        {
            clientsSocket = socket;
            messageWrite= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            messageRead= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientName = userName;

        }
        catch(IOException ioe)
        {
            closeEverything(socket,messageRead,messageWrite);
        }
    }


    public void messageSender()
    {
        try
        {
            messageWrite.write(clientName);
            messageWrite.newLine();;
            messageWrite.flush();

            Scanner kb = new Scanner(System.in);
            /*While the client is still connected read what the user is writting and
             * send the message with the name of the client to know who send it.
             */
            while(clientsSocket.isConnected())
            {
                String messagedTyped = kb.nextLine();
                messageWrite.write(this.clientName + " --> " + messagedTyped);   
                messageWrite.newLine();
                messageWrite.flush();
                 
            }

        }
        catch(IOException ioe)
        {
            closeEverything(clientsSocket,messageRead,messageWrite);

        }
    }

    public void messageGetter()
    {
        // listens for a message that has been broadcasted
        /* We will be using thread because this is a blocking operation */
        new Thread(new Runnable() 
        {
            @Override
            public void run()
            {
                String messageBroadcasted;
                while(clientsSocket.isConnected())
                {
                    try
                    {
                        messageBroadcasted = messageRead.readLine();
                        System.out.println(messageBroadcasted);
                    }
                    catch(IOException ioe)
                    {
                        closeEverything(clientsSocket,messageRead,messageWrite);
                    }

                }
            }
            
        }).start();
    }

    public void closeEverything(Socket thesocket, BufferedReader theReader, BufferedWriter theWriter)
    {
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
