package main.resources.moviesEvaluation;

import org.bson.Document;

import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

public class MongoManager {

	final private String DB_NAME = "Moviegram";
	private int port;
	private String ip;
	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> userCollection;
	MongoCollection<Document> filmCollection;

	public MongoManager(int p, String i) {
		port = p;
		ip = i;
		mongoClient = MongoClients.create("mongodb://" + ip + ":" + port);
		database = mongoClient.getDatabase(DB_NAME);
		userCollection = database.getCollection("usersCollection");
		filmCollection = database.getCollection("moviesCollection");
	}

	public User checkUser(String username, String password) {
		try {
			Document found = (Document) userCollection.find(and(eq("Username", username), eq("Password", password)))
					.first();

			if (found != null) {
				MainApp.user = new User(found.getString("Name"), found.getString("Username"),
						found.getString("Password"), found.getString("Country"), found.getBoolean("Admin"));
				return MainApp.user;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return MainApp.user;
	}

	public void registerUser(String name, String username, String password, String country) {
		try {
			Document document = new Document("Name", name).append("Username", username).append("Password", password)
					.append("Country", country).append("Admin", false);
			userCollection.insertOne(document);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void addFilms(String jsonText) {
		try {
			String[] jsonLine = jsonText.split("\n");
			for (int i = 0; i < jsonLine.length; i++) {
				Document doc = Document.parse(jsonLine[i]);
				filmCollection.insertOne(doc);
			}
			System.out.println("Insert Completed!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List<Film> searchFilm(String title) {
		try {
			MongoCursor<Document> cursor = filmCollection.find(regex("Title", ".*" + title + ".*", "-i")).limit(30)
					.iterator();
			List<Film> films = new ArrayList<>();
			while (cursor.hasNext()) {
				Document filmDocument = cursor.next();
				String filmTitle = filmDocument.getString("Title");
				String director = filmDocument.getString("Director");
				String production = filmDocument.getString("Production");
				String poster = filmDocument.getString("Poster");
				String year = filmDocument.getString("Year");
				String rating = filmDocument.getString("imdbRating");
				String votes = filmDocument.getString("imdbVotes");

				Film film = new Film(filmTitle, director, production, poster, year, rating, votes);
				films.add(film);
			}
			return films;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	public void addVote(Film film, int vote, Double updatedRating) {
		try {
			Document found = (Document) filmCollection
					.find(and(eq("Title", film.getTitle()), eq("Year", Integer.toString(film.getYear())))).first();
			if (found != null) {
				filmCollection.updateMany(
						and(eq("Title", film.getTitle()), eq("Year", Integer.toString(film.getYear()))),
						new Document("$set", new Document("imdbRating", Double.toString(updatedRating))
								.append("imdbVotes", Integer.toString(film.getVotes()))));
			}
			System.out.println("Vote Updated!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void deleteFilm(Film film) {
		try {
			DeleteResult deleteResult = filmCollection
					.deleteMany(and(eq("Title", film.getTitle()), eq("Year", Integer.toString(film.getYear()))));
			System.out.println("Total number of films deleted: " + deleteResult.getDeletedCount());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void quit() {
		mongoClient.close();
	}

}
