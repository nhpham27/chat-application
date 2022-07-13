import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;



public class Client extends Thread{

	
	Socket socketClient;
	ObjectOutputStream out;
	ObjectInputStream in;
	MessageInfo info;
	int clientNumber;
	
	private Consumer<Serializable> callback;
	
	Client(){
		info = new MessageInfo();
		this.start();
	}
	
	public void run() {
		
		try {
			socketClient= new Socket("127.0.0.1",5555);
		    out = new ObjectOutputStream(socketClient.getOutputStream());
		    in = new ObjectInputStream(socketClient.getInputStream());
		    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}
		
		// get name of the client
		try {
			String str = in.readObject().toString();
			clientNumber = Integer.parseInt(str);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch(NullPointerException e1) {
			System.out.println("Cannot connect to server!");
		}
		
		while(true) {
			try {
				info = (MessageInfo) in.readObject();
				callback.accept(" ");
				in.reset();
			}
			catch(Exception e) {}
		}
	
    }
	
	public void setCallBack(Consumer<Serializable> call) {
		this.callback = call;
	}
	
	public MessageInfo getInfo() {
		return info;
	}
	
	public int getClientNumber() {
		return clientNumber;
	}
	
	public void send(String data, ArrayList<Integer> list) {
		
		try {
			info.recipientList.clear();
			for(Integer n : list) {
				info.recipientList.add(n);
			}
			
			info.message = data;
			out.writeObject(info);
			out.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
