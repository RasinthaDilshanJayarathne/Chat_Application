package Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    public BorderPane context;
    public JFXTextField txtUserName;

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        URL resource = getClass().getResource("View/Client.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) context.getScene().getWindow();
        window.setScene(new Scene(load));

//        Socket socket = new Socket("localhost", 6000);
//        Client client = new Client(socket, username);
//        client.listenForMessage();
//        client.sendMessage();

    }
}
