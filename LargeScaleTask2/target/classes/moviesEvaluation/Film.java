package main.resources.moviesEvaluation;

import javafx.beans.property.SimpleStringProperty;

public class Film {
	private SimpleStringProperty title;

	
	public Film(String title) {
		this.title = new SimpleStringProperty(title);
		
	}


	public String getTitle() {
		return title.get();
	}


	public void setTitle(String title) {
		this.title.set(title);;
	}
	
	
	
}
