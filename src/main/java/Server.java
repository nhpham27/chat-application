import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;
/*
 * 
 */

public class Server{

	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	// synchronized locks
	Object lock = new Object();
	Object lock2 = new Object();
	Object lock3 = new Object();
	
	Server(Consumer<Serializable> call){
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(5555);){

			    while(true) {
		    		ClientThread c = new ClientThread(mysocket.accept(), count);
					callback.accept("client has connected to server: " + "Client #" + count);
					clients.add(c);
					c.start();
					
					count++;
				}//end of while
			}//end of try
			catch(Exception e) {
				callback.accept("Server socket did not launch");
			}// end of catch
		}
	}
	

		class ClientThread extends Thread{
			
		
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			MessageInfo info;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
				info = new MessageInfo();
			}
			
			// send message to the clients
			public void updateMessage() {
				// state that this is a message from a client
				info.isMessage = true;
				String tempStr = info.message;
				info.message = "Client #" + Integer.toString(count) + " said: " + tempStr;
				
				for(int i = 0; i < clients.size(); i++) {
					try {
						// get client
						ClientThread temp = clients.get(i);
						
						// only send message to the clients in recipients list
						if(info.recipientList.contains(temp.count)) {
							temp.out.writeObject(this.info);
							temp.out.reset();
						}
					}
					catch(Exception e) {}
				}
			}
			
			public void updateClients(boolean newClient) {
				// clear the list of clients
				info.clientsList.clear();
				// copy the current list of clients
				for(ClientThread x : clients) {
					info.clientsList.add(x.count);
				}
				// get the join or left message from the clients
				if(newClient == true) {
					info.message = "New client: client #"+ Integer.toString(this.count);
				}
				else
					info.message = "Client #"+Integer.toString(this.count)+" has left!";
				
				// state that this is not a message
				info.isMessage = false;
				for(int i = 0; i < clients.size(); i++) {
					ClientThread temp = clients.get(i);
					try {
						// send data to clients
						if(temp.count == this.count) {
							// the received client is the client who has just joined
							String s = info.message;
							info.message = "You have connected to the server!";
							temp.out.writeObject(this.info);
							info.message = s;
						}
						else {
							// other clients
							temp.out.writeObject(this.info);
						}
						temp.out.reset();
					}
					catch(Exception e) {}
				}
			}
			
			public void run(){
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				
				// send the name of the client
				try {
					out.writeObject(Integer.toString(count));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// update all clients
				synchronized(lock) {
					updateClients(true);
		    	}
					
				while(true) {
				    try {
				    	// wait for the messages from clients
				    	info = (MessageInfo)in.readObject();
				    	// update server and send message to clients
				    	synchronized(lock2) {
				    		String tempMessage = "Client: " + count + " sent: " + info.message;
				    		tempMessage += " | to clients: ";
				    		for(Integer x : info.recipientList) {
				    			tempMessage += Integer.toString(x) + " ";
				    		}
				    		callback.accept(tempMessage);
					    	updateMessage();
				    	}
				    }
				    catch(Exception e) {
				    	// one client left
				    	synchronized(lock3) {
				    		// update server
				    		callback.accept("Client: " + count + " has left!");
				    		// remove client from list
					    	clients.remove(this);
					    	// update information in clients
					    	updateClients(false);
				    	}
				    	break;
				    }
				}
			}//end of run
			
		}//end of client thread
}


	
	

	
