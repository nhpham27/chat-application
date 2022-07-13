import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ClientController implements Initializable{
	@FXML
	private ListView<String> chatList, clientsList, recipientsList;
	
	@FXML
	private Button sendButton;
	
	@FXML
	private TextField messageText;
	
	@FXML
	private Text chatHistory, chosenText;
	
	Client clientConnection;
	MessageInfo info;
	String recipient;
	ArrayList<Integer> list;
	
	public ClientController() {
		info = new MessageInfo();
		recipient = "None";
		list = new ArrayList<>();
		clientConnection = new Client();
		
		clientConnection.setCallBack(data->{
			Platform.runLater(()->{
				chatHistory.setText("Chat history of Client #" + Integer.toString(clientConnection.getClientNumber()));
				info = clientConnection.getInfo();
				chatList.getItems().add(info.message);
				
				// update clients list if isMessage is false
				if(info.isMessage == false) {
					clientsList.getItems().clear();
					for(Integer x : info.clientsList) {
						if(x != clientConnection.getClientNumber())
							clientsList.getItems().add("Client #" + Integer.toString(x));
					}
				}
			});
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	// add recipient to list
	public void add() {
		try {
			String str = clientsList.getSelectionModel().getSelectedItem().toString();
			chosenText.setText("Please choose the recipients");
			chosenText.setStyle("-fx-fill: black;");
			
			int temp = Integer.parseInt(str.substring(8));
			
			if(!this.list.contains(temp)) {
				recipientsList.getItems().add(str);
				this.list.add(temp);
			}
		}
		catch(NullPointerException e) {
			chosenText.setText("You have not chosen any recipient!!!");
			chosenText.setStyle("-fx-fill: red;");
		}
	}
	
	// add all recipients to list
	public void addAll() {
		recipientsList.getItems().clear();
		ObservableList<String> tempList = clientsList.getItems();
		this.list.clear();
		
		for(String s : tempList) {
			recipientsList.getItems().add(s);
			this.list.add(Integer.parseInt(s.substring(8)));
		}
	}
	
	// clear the recipient list
	public void clear() {
		recipientsList.getItems().clear();
		this.list.clear();
	}
	
	// send message to the server
	public void send() {
		if(list.isEmpty()) {// if no recipient is selected, warn the user
			chosenText.setText("You have not chosen any recipient!!!");
			chosenText.setStyle("-fx-fill: red;");
		}
		else {
			chosenText.setText("Please choose the recipients");
			chosenText.setStyle("-fx-fill: black;");
			
			chatList.getItems().add("You said: " + messageText.getText());
			clientConnection.send(messageText.getText(), list);
			messageText.clear();
			
			// clear recipient lists
			recipientsList.getItems().clear();
			this.list.clear();
		}
	}

}
