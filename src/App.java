import java.util.*;
import java.net.*;
import java.io.*;
public class App {
    public static void main(String[] args)throws IOException
    {
        Scanner kb = new Scanner(System.in);
        System.out.print("Please enter your username: ");
        String theUserName = kb.nextLine();
        // Getting the user name

        // the server will be listening on port "7890."
        Socket clintSocket = new Socket("localhost",7890);
        Client theClint = new Client(clintSocket,theUserName);
        theClint.messageGetter();
        theClint.messageSender();
    }
}
