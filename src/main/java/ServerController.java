import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class ServerController implements Initializable{
	@FXML
	private ListView<String> historyList;
	
	Server serverConnection;
	
	public ServerController() {
		serverConnection = new Server(data->{
			Platform.runLater(()->{
				historyList.getItems().add(data.toString());
			});
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
