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

public class RegisterController implements Initializable {
	@FXML
	private Button registerBtn;	
	@FXML
	private Label loginLink;
	@FXML
	private TextField nameField;
	@FXML
	private TextField userField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private PasswordField confirmField;
	@FXML
	private TextField countryField;

	@FXML
	public void loadLoginPage(MouseEvent event) throws IOException {
		Parent Home = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(Home);
		window.setScene(scene);
		window.show();
	}

	@FXML
	public void registerAction(ActionEvent event) throws IOException {
		if( nameField.getText().isEmpty() || userField.getText().isEmpty() || 
				passwordField.getText().isEmpty() || confirmField.getText().isEmpty() || 
				countryField.getText().isEmpty() ) {
				System.err.println("All the fields are required.");
				return;
			}
		if(!passwordField.getText().contentEquals(confirmField.getText())) {
			System.err.println("Passwords does not match! retry.");
			return;
		}
		
		Parent Home = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
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
