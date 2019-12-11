package main.resources;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	@FXML
	private Button loginBtn;
	@FXML
	private Label registerLink;
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;

	@FXML
	public void loadRegisterPage(MouseEvent event) throws IOException {
		Parent Home = FXMLLoader.load(getClass().getClassLoader().getResource("Register.fxml"));
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(Home);
		window.setScene(scene);
		window.show();
	}

	@FXML
	public void loginAction(ActionEvent event) throws IOException {
		
		if(usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
			System.err.println("You have to specify a Username and a Password");
			return;
		}
		Parent Home = FXMLLoader.load(getClass().getClassLoader().getResource("Home.fxml"));
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(Home);
		window.setScene(scene);
		window.show();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}
}
