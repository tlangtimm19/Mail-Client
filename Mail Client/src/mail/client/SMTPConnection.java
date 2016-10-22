/**
 * <h1>Mail Client</h1>
 * Hello!  This is a simple program that allows users to send an email message using SMTP.
 * Currently, it isn't running, and I'm out time to bug fix. With any luck, it will be fixed within the next
 * few days after I receive a little help from my professor, or classmates.
 * 
 * @author Tyler Langtimm, James F. Kurose, and Keith W. Ross
 * @version 1.0
 * @since October 21st, 2016
 */

package mail.client;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Open an SMTP connection to a mail server and send one mail.
 *
 */
public class SMTPConnection {
    /* The socket to the server */
    private Socket connection;

    /* Streams for reading and writing the socket */
    private BufferedReader fromServer;
    private DataOutputStream toServer;

    private static final int SMTP_PORT = 25;
    private static final String CRLF = "\r\n";

    /* Are we connected? Used in close() to determine what to do. */
    private boolean isConnected = false;

    /* Create an SMTPConnection object. Create the socket and the 
       associated streams. Initialize SMTP connection. */
    public SMTPConnection(Envelope envelope) throws IOException {
	connection = new Socket(envelope.DestAddr, SMTP_PORT); /* Fill in */ //Once again, I don't understand why i'm getting this error. :/
	fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream())); /* Fill in */
	toServer = new  DataOutputStream(connection.getOutputStream()); /* Fill in */
	
	/* Fill in */
	/* Read a line from server and check that the reply code is 220.
	   If not, throw an IOException. */
	/* Fill in */
        String reply = fromServer.readLine();
        if (parseReply(reply) != 220){ //if the reply code isn't 220, throw an exeption and error message.
            System.out.println("Error in connection!");
            System.out.println(reply);
            return;
        }

	/* SMTP handshake. We need the name of the local machine.
	   Send the appropriate SMTP handshake command. */
	String localhost = (InetAddress.getLocalHost()).getHostName(); /* Fill in */
	sendCommand("HELO " + localhost, 250); /* Fill in */

	isConnected = true;
    }

    /* Send the message. Write the correct SMTP-commands in the
       correct order. No checking for errors, just throw them to the
       caller. */
    public void send(Envelope envelope) throws IOException {
	/* Fill in */
	/* Send all the necessary commands to send a message. Call
	   sendCommand() to do the dirty work. Do _not_ catch the
	   exception thrown from sendCommand(). */
	/* Fill in */
        sendCommand("MAIL FROM: " + envelope.Sender + " ", 250);  //I don't entirely understand why Sender is giving me an error message here, Sender is definently a variable of an Envelope object right?
        sendCommand("RCPT TO: " + envelope.Recipient + " ", 250); //Same issue here...
        sendCommand("DATA", 354);
        sendCommand(envelope.Message.toString() + ".", 250); //And again here. I really need to review how objects work...
    }

    /* Close the connection. First, terminate on SMTP level, then
       close the socket. */
    public void close() {
	isConnected = false;
	try {
	    sendCommand("QUIT", 221); /*Fill In*/ //Try to send a quit command and 221 reply code.
	    connection.close();
	} catch (IOException e) {  //If unable, then print this line and reopen the connection.
	    System.out.println("Unable to close connection: " + e);
	    isConnected = true;
	}
    }

    /* Send an SMTP command to the server. Check that the reply code is
       what is is supposed to be according to RFC 821. */
    private void sendCommand(String command, int rc) throws IOException {
	/* Fill in */
	/* Write command to server and read reply from server. */
	/* Fill in */
           String reply = null;
           toServer.writeBytes(command);
           reply = fromServer.readLine();
        
        
	/* Fill in */
	/* Check that the server's reply code is the same as the parameter
	   rc. If not, throw an IOException. */
	/* Fill in */
        if (parseReply(reply) != rc){
            System.out.println("Error! Command: " + command);
            System.out.println(reply);
            throw new IOException(); //Apparently this needs to be IOException, not just Exception. Thanks StackExchange.
        }
    }

    /* Parse the reply line from the server. Returns the reply code. */
    private int parseReply(String reply) {
	/* Fill in */
        //So apparently according to the oracle docs we're not supposed to use StringTokenizer, but the lab says to do so, so I guess I will here?
        StringTokenizer st = new StringTokenizer(reply);
        String replyCode = st.nextToken();
        return (new Integer(replyCode)).intValue();  //Returns the reply code, after converting it into an integer.
    }

    /* Destructor. Closes the connection if something bad happens. */
    protected void finalize() throws Throwable {
	if(isConnected) {
	    close();
	}
	super.finalize();
    }
}