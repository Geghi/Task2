package main.resources;

import main.resources.moviesEvaluation.Film;
import main.resources.moviesEvaluation.MainApp;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeController implements Initializable {

	ObservableList<String> analyticsValues = FXCollections.observableArrayList("Item 1", "Item 2");
	ObservableList<String> filtersValues = FXCollections.observableArrayList("Item 1", "Item 2");
	ObservableList<Integer> voteValues = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	@FXML
	private Button voteBtn, searchBtn, searchBtnAdmin, deleteBtn, addBtn, logoutBtn;
	@FXML
	private ComboBox<Integer> voteBox;
	@FXML
	private ComboBox<String> filters, analyticsSelection;
	@FXML
	private TableView<Film> filmHomeTable, filmAnalyticsTable, filmAdminTable;
	@FXML
	private TableColumn<Film, String> filmHomeColumn, filmAnalyticsColumn, filmAdminColumn;
	@FXML
	private PieChart pieChart;
	@FXML
	private TextField searchText, searchTextAdmin;
	@FXML
	private TextArea jsonArea;
	@FXML
	private Label errorLabel, directorLabel, yearLabel, productionLabel, ratingLabel;
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab adminTab, analyticsTab, homeTab;
	@FXML
	private ImageView imgPoster;

	@FXML
	public void inizializePieChart() {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(new PieChart.Data("Horror", 50),
				new PieChart.Data("Action", 30), new PieChart.Data("Fantasy", 90), new PieChart.Data("Anime", 10));
		pieChart.setData(pieChartData);
		pieChart.setStartAngle(90);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (!MainApp.user.getAdmin())
			tabPane.getTabs().remove(adminTab);

		else
			initializeTable(filmAdminColumn);
		initializeTables();
		initializeBox();
		inizializePieChart();
		updateTable(filmAnalyticsTable, filmAnalyticsColumn, 1);
	}

	@FXML
	private void initializeBox() {
		analyticsSelection.setValue("Item 1");
		analyticsSelection.setItems(analyticsValues);
		filters.setValue("Item 1");
		filters.setItems(filtersValues);
		voteBox.setValue(1);
		voteBox.setItems(voteValues);
	}

	private void updateTable(TableView<Film> table, TableColumn<Film, String> column, int check) {
		ObservableList<Film> films;
		List<Film> filmList = new ArrayList<>();
		String title = null;
		if (check == 0) {
			title = searchText.getText();
			filmList.addAll(MainApp.managerM.searchFilm(title));

		} else if (check == 1) {
			// TODO Analytics query
			films = FXCollections.observableArrayList();
		} else {
			title = searchTextAdmin.getText();
			filmList.addAll(MainApp.managerM.searchFilm(title));
		}

		films = FXCollections.observableArrayList();
		films.addAll(filmList);

		column.setCellValueFactory(new PropertyValueFactory<Film, String>("title"));
		table.setItems(films);
	}

	private void initializeTables() {
		initializeTable(filmHomeColumn);
		initializeTable(filmAnalyticsColumn);
	}

	private void initializeTable(TableColumn<Film, String> column) {
		column.setCellFactory(tc -> {
			TableCell<Film, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			text.wrappingWidthProperty().bind(column.widthProperty());
			text.textProperty().bind(cell.itemProperty());
			text.getStyleClass().add("columnText");
			return cell;
		});
	}

	@FXML
	private void addFilm(ActionEvent event) {
		try {
			errorLabel.setText("Be patient, we need to implement that function!");
			errorLabel.setVisible(true);
			MainApp.managerM.addFilms(jsonArea.getText());
			return;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	private void logoutAction(ActionEvent event) throws IOException {
		MainApp.user = null;
		Parent Home = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(Home);
		window.setScene(scene);
		window.show();
		System.out.println("Logout completed.");
	}

	@FXML
	public void deleteFilm(ActionEvent event) {
		try {
			Film film = filmAdminTable.getSelectionModel().getSelectedItem();
			MainApp.managerM.deleteFilm(film);
			updateTable(filmAdminTable, filmAdminColumn, 2);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	public void searchFilm(ActionEvent event) {
		try {
			Button btn = (Button) event.getSource();
			if (btn.getId().equals("searchBtn")) {
				if (searchText.getText().isEmpty()) {
					errorLabel.setText("Please insert a title.");
					errorLabel.setVisible(true);
					return;
				}
				updateTable(filmHomeTable, filmHomeColumn, 0);
			} else if (btn.getId().equals("searchBtnAdmin")) {
				if (searchTextAdmin.getText().isEmpty()) {
					errorLabel.setText("Please insert a title.");
					errorLabel.setVisible(true);
					return;
				}
				updateTable(filmAdminTable, filmAdminColumn, 2);
			}
			errorLabel.setVisible(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	public void addVote(ActionEvent event) {
		try {
			int vote = voteBox.getSelectionModel().getSelectedItem();
			Film film = filmHomeTable.getSelectionModel().getSelectedItem();

			Double updatedRating = ((film.getVotes() * film.getRating()) + vote) / (film.getVotes() + 1);
			film.setVotes(film.getVotes() + 1);
			Double roundedRating = (double) Math.round(updatedRating * 10) / 10;
			film.setRating(roundedRating);
			ratingLabel.setText(Double.toString(roundedRating));
			MainApp.managerM.addVote(film, vote, roundedRating);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	public void showFilmInformations(MouseEvent event) {
		try {
			Film film = filmHomeTable.getSelectionModel().getSelectedItem();
			directorLabel.setText(film.getDirector());
			yearLabel.setText(Integer.toString(film.getYear()));
			productionLabel.setText(film.getProduction());
			Double rating = (double) Math.round(film.getRating() * 10) / 10;
			ratingLabel.setText(Double.toString(rating));
			Image image;
			try {
				image = new Image(film.getPoster());
			} catch (Exception ex) {
				image = new Image("/Images/default.png");
			}

			imgPoster.setImage(image);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	public void tabSwap(Event event) {
		// TODO exception to handle.
		try {
//			if(errorLabel.isVisible())  // I don't know why this piece give an exception..
//				errorLabel.setVisible(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
