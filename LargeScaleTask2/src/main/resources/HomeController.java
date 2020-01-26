package main.resources;

import main.resources.moviesEvaluation.Film;
import main.resources.moviesEvaluation.MainApp;
import main.resources.moviesEvaluation.Aggregator;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeController implements Initializable {

	ObservableList<String> analyticsValues = FXCollections.observableArrayList("Top Rated Films", "Top Productions", "Top Rated Country"); 
	ObservableList<String> filtersValues = FXCollections.observableArrayList("Item 1", "Item 2");
	ObservableList<Integer> voteValues = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	@FXML
	private Button voteBtn, searchBtn, searchBtnAdmin, deleteBtn, addBtn, logoutBtn;
	@FXML
	private ComboBox<Integer> voteBox;
	@FXML
	private ComboBox<String> filters, analyticsSelection;
	@FXML
	private TableView<Film> filmHomeTable, filmAdminTable ;
	@FXML 
	private TableView<Object> filmAnalyticsTable;
	@FXML
	private TableColumn<Film, String> filmHomeColumn, filmAdminColumn;
	@FXML
	private TableColumn<Object, String> filmAnalyticsColumn;
	@FXML
	private PieChart pieChart;
	@FXML
	private TextField searchText, searchTextAdmin;
	@FXML
	private TextArea jsonArea;
	@FXML
	private Label errorLabel, directorLabel, yearLabel, productionLabel, ratingLabel, voteLabel;
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
		
		voteBox.setItems(voteValues);
	}
	
	@FXML
	private void initializeBox() {
		if(MainApp.user.getAdmin()) {
			analyticsValues.add("Top Active Users");
		}
		
		analyticsSelection.setItems(analyticsValues);
		analyticsSelection.setValue("Select analytics");
		filters.setVisible(false);
		

		analyticsSelection.valueProperty().addListener((obs, oldItem, newItem) -> {
			
			// TOP RATED FILMS
            if(analyticsSelection.getValue().equals("Top Rated Films")) {
            	filters.setVisible(true);
            	filters.setItems(MainApp.managerM.getCountries());
            	updateTableAnalytics(filmAnalyticsTable, filmAnalyticsColumn, MainApp.managerM.searchFilmsSorted(), 0);
            	
            	filters.valueProperty().addListener((obss, oldItems, newItems) -> {
            		updateTableAnalytics(filmAnalyticsTable, filmAnalyticsColumn,
            				MainApp.managerM.searchFilmsSortedFiltered(filters.getSelectionModel().getSelectedItem()), 0);
            	});
            }
            
            
            // TOP PRODUCTIONS
            else if(analyticsSelection.getValue().equals("Top Productions")) {
            	filters.setVisible(false);
            	updateTableAnalytics(filmAnalyticsTable, filmAnalyticsColumn, MainApp.managerM.topProduction(), 1);
            }
            
            
            // TOP RATED COUNTRIES
            else if(analyticsSelection.getValue().equals("Top Rated Country")) {
            	filters.setItems(MainApp.managerM.getYears());
            	filters.setVisible(true);
            	
            	filmAnalyticsColumn.getColumns().clear();
            	updateTableAnalytics(filmAnalyticsTable, filmAnalyticsColumn, null, -1);
            	filters.valueProperty().addListener((obs1, oldItem1, newItem1) -> {
            		// CHIAMA LA QUERY PASSANDO L'ANNO
            		// l'anno lo prendo da
            		updateTableAnalytics(filmAnalyticsTable, filmAnalyticsColumn, MainApp.managerM.topCountries(filters.getValue()), 2);
            	});

            	
            }
            
            
            // TOP ACTIVE USER
            else if(analyticsSelection.getValue().equals("Top Active Users")) {
            	filters.setVisible(false);
            	updateTableAnalytics(filmAnalyticsTable, filmAnalyticsColumn, null, -1);
            	updateTableAnalytics(filmAnalyticsTable, filmAnalyticsColumn, MainApp.managerM.getTopUser(), 3);
            }
           
            
        });
		}
	
	// this function update the analytics table
	// different instructions must be executed related to the analytics type : 0, 1, 2,3
	private void updateTableAnalytics(TableView<Object> table, TableColumn<Object,String> filmAnalyticsColumn,
									  List<Object> list, int opcode) {
		
		// -1 -> To clean
				if(opcode == -1) {
					ObservableList<Object> items = FXCollections.observableArrayList();
					filmAnalyticsColumn.setCellValueFactory(new PropertyValueFactory<Object, String>("title"));
					table.setItems(items);
					filmAnalyticsColumn.setText("");
				}
		
		
		// 0 -> Top rated films
		if(opcode == 0) {
			ObservableList<Object> items = FXCollections.observableArrayList();
			for(int i=0; i<list.size(); i++) {
				items.add(list.get(i));
			}
			filmAnalyticsColumn.setCellValueFactory(new PropertyValueFactory<Object, String>("title"));
			table.setItems(items);
			filmAnalyticsColumn.setText("Films");
		}
		
		
		// 1 -> Top productions
		else if(opcode == 1) {
			ObservableList<Object> items = FXCollections.observableArrayList();
			for(int i=0; i<list.size(); i++) {
				items.add(list.get(i));
			}
			filmAnalyticsColumn.setCellValueFactory(new PropertyValueFactory<Object, String>("name"));
			table.setItems(items);
			filmAnalyticsColumn.setText("Top productions");
		}
		
		
		// 2 -> Top countries by year
		else if(opcode == 2) {
			ObservableList<Object> items = FXCollections.observableArrayList();
			for(int i=0; i<list.size(); i++) {
				items.add(list.get(i));
			}
			filmAnalyticsColumn.setCellValueFactory(new PropertyValueFactory<Object, String>("name"));
			table.setItems(items);
			
			filmAnalyticsColumn.setText("Top countries");
		}
		
		
		// 3 -> Top users
		else if(opcode == 3) {
			ObservableList<Object> items = FXCollections.observableArrayList();
			for(int i=0; i<list.size(); i++) {
				items.add(list.get(i));
			}
			filmAnalyticsColumn.setCellValueFactory(new PropertyValueFactory<Object, String>("name"));
			table.setItems(items);					
			filmAnalyticsColumn.setText("Top users");
		}
	}
	
	
	private void updateTable(TableView<Film> table, TableColumn<Film, String> column, int check) {
		ObservableList<Film> films;
		List<Film> filmList = new ArrayList<>();
		String title = null;
		
		
		// 0 -> update home table
		if (check == 0) {
			title = searchText.getText();
			filmList.addAll(MainApp.managerM.searchFilm(title));
		} 
		
		// 2 -> update admin table
		else {
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
		initializeTableAnalytics(filmAnalyticsColumn);
	}

	
	private void initializeTableAnalytics(TableColumn<Object, String> column) {
		column.setCellFactory(tc -> {
			TableCell<Object, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			text.wrappingWidthProperty().bind(column.widthProperty());
			text.textProperty().bind(cell.itemProperty());
			text.getStyleClass().add("columnText");
			return cell;
		});
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
			voteLabel.setText(Integer.toString(vote));
			voteBox.setVisible(false);
			voteBtn.setVisible(false);
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
			int vote = MainApp.managerM.getUserVote(MainApp.user.getId(), film.getId());
			if(vote == -1) {
				voteLabel.setText(" ");
				voteBox.setVisible(true);
				voteBtn.setVisible(true);
			}else {
				voteLabel.setText(Integer.toString(vote));
				voteBox.setVisible(false);
				voteBtn.setVisible(false);
			}
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
