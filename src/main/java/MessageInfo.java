import java.io.Serializable;
import java.util.ArrayList;

public class MessageInfo implements Serializable{

	/**
	 * Default serializable serial
	 */
	private static final long serialVersionUID = 1L;
	String message;
	ArrayList<Integer> recipientList;
	ArrayList<Integer> clientsList;
	boolean isMessage;
	
	MessageInfo(){
		message = "";
		isMessage = false;
		recipientList = new ArrayList<>();
		clientsList = new ArrayList<>();
	}
}
