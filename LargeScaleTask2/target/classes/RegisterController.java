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
import main.resources.moviesEvaluation.MainApp;

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
	private Label errorLabel;

	@FXML //Redirect to the Login Page.
	public void loadLoginPage(MouseEvent event) throws IOException {
		Parent Home = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(Home);
		window.setScene(scene);
		window.show();
	}

	@FXML
	public void registerAction(ActionEvent event) throws IOException {
		String name = nameField.getText();
		String username = userField.getText();
		String password = passwordField.getText();
		String confirm = confirmField.getText();
		String country = countryField.getText();

		//check if all fields are not empty.
		if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirm.isEmpty() || country.isEmpty()) {
			errorLabel.setText("All the fields are required.");
			errorLabel.setVisible(true);
			return;
		}
		
		//Check if passwords are equals.
		if(!password.equals(confirm)) {
			errorLabel.setText("Passwords does not match, retry!");
			passwordField.setText("");
			confirmField.setText("");
			errorLabel.setVisible(true);
			return;
		}
		
		//TODO add a country check.
		
		//Register the user in the Database.
		MainApp.managerM.registerUser(name, username, password, country);
		//Redirect to the Login page.
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
