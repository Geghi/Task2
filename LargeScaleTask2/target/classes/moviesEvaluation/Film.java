package main.resources.moviesEvaluation;

import javafx.beans.property.SimpleStringProperty;

public class Film {
	private String id;
	private SimpleStringProperty title;
	private String director;
	private String production;
	private String poster;
	private int year;
	private Double rating;
	private int votes;
	

	public Film(String _id, String title, String director, String production,
				String poster, String year, String rating, String votes) {
		
		id = _id;
		if (!rating.equals("N/A")) {
			this.rating = Double.parseDouble(rating);
		} else {
			this.rating = 0.0;
		}

		if (!votes.equals("N/A")) {
			this.votes = Integer.parseInt(votes.replaceAll(",", ""));
		} else {
			this.votes = 0;
		}

		if (!year.equals("N/A")) {
			// remove spaces and tabs and pick only the first 4 char
			this.year = Integer.parseInt(year.replaceAll("\\s+", "").substring(0, 4));
		} else {
			this.year = -1;
		}

		this.title = new SimpleStringProperty(title);
		this.director = director;
		this.production = production;
		this.poster = poster;

	}
	
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getProduction() {
		return production;
	}

	public void setProduction(String production) {
		this.production = production;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public void setTitle(SimpleStringProperty title) {
		this.title = title;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

}
