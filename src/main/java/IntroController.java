import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class IntroController implements Initializable{
	@FXML
	private VBox intro;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void clientScene() throws IOException {
		Parent clientVBox = FXMLLoader.load(getClass().getResource("FXML/client.fxml"));
		intro.getScene().setRoot(clientVBox);
	}
	
	public void serverScene() throws IOException {
		Parent serverVBox = FXMLLoader.load(getClass().getResource("FXML/server.fxml"));
		intro.getScene().setRoot(serverVBox);
	}

}
