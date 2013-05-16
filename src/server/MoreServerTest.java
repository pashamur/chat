package server;

import client.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

import junit.framework.TestCase;

import main.Packet;
import main.User;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class MoreServerTest{
    
    private final int PORT = 1234;
    private Server server;
    //private Server server = new Server(PORT, true);
    private Thread t;
    
    
    /**
     * Check that upon initialization a new server has no users nor channels
    */ 
    @Test
    public void checkStart ()
    {
        try {
        server = new Server(PORT, true);
        assertEquals(server.getUserList(), "");
        assertEquals(server.getChannelList(), "");
        server.terminate();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
            //assertEquals(0, 1);
        }
        
    }
    
    
    /**
     * Login with two clients to start, create two channels.
     * Double-check that these are the only objects in the server HashMaps.  
    */ 
    @Test
    public void logIn()
    {
        try {
            server = new Server(PORT, true);
            server.addDummyUsers("Guest_0");
            server.addDummyUsers("Guest_1");
            server.createChannel("chess", 0);
            server.createChannel("bunnies", 0);
            assertEquals(server.getUserList().split(" ").length, 2);
            assertEquals(server.getChannelList().split(" ").length, 2);
            //System.out.println(server.getUserList());
            server.terminate();
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
                //assertEquals(0, 1);
            }        
    }
    
    /**
     * Basic test of creating, joining and leaving Channels.
     * Individual tests are described below.
     */
    @Test
    public void joiningChannel()
    {
        try {
            server = new Server(PORT, true);
            server.addDummyUsers("Guest_0");
            server.addDummyUsers("Guest_1");
            
            //Tests getUserList method.
            assertEquals(server.getUserList(), "Guest_0 Guest_1");
            
            //Tests getChannelList method in case of no Channels on server.
            assertEquals(server.getChannelList(), "");
            
            //Tests that the creator is automatically a member of the Channel.
            server.createChannel("chess", 0);      
            assertEquals(server.getChannel("chess").getUserCount(), 1);
            
            //Tests that joining a non-existing Channel automatically creates that Channel
            //Also tests getChannelList method.
            server.addUserToChannel(0, "bunnies");
            assertEquals(server.getChannelList().split(" ").length, 2);
            assertEquals(server.getChannelList(), "chess bunnies");
            assertTrue(server.getChannel("bunnies").hasUser(server.getUser(0)));
            
            //Tests joining a pre-existing Channel.  
            server.addUserToChannel(1, "bunnies");
            assertEquals(server.getChannel("bunnies").getUserCount(), 2);
            assertTrue(server.getChannel("bunnies").hasUser(server.getUser(1)));
            
            //Tests leaving a pre-existing Channel
            server.removeUserFromChannel(0, "bunnies");
            assertEquals(server.getChannel("bunnies").getUserCount(), 1);
            assertTrue(!server.getChannel("bunnies").hasUser(server.getUser(0)));
            
            //Tests that when the last member of a Channel leaves, it is deleted from the server.
            server.removeUserFromChannel(0, "chess");            
            assertTrue(!server.hasChannel("chess"));
            
            
            server.terminate();
            
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
                //assertEquals(0, 1);
            }        
    }
    /**
    @Test
    public void testSendMessage() {
        try {
            server = new Server(PORT, true);
            server.addDummyUsers("Guest_0");
            server.createChannel("whee", 0); 
            
            server.terminate();
            
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
    
   */ 


}
