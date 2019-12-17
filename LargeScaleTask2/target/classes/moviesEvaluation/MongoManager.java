package main.resources.moviesEvaluation;

import org.bson.Document;

import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;

public class MongoManager {

	final private String DB_NAME = "MoviesEvaluation";
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
		userCollection = database.getCollection("Users");
		filmCollection = database.getCollection("Films");

	}

	public User checkUser(String username, String password) {
		Document found = (Document) userCollection.find(and(eq("username", username), eq("password", password)))
				.first();
		try {
			if (found != null) {
				MainApp.user = new User(found.getString("name"), found.getString("username"),
						found.getString("password"), found.getString("country"), found.getBoolean("admin"));
				System.out.println(found);
				return MainApp.user;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return MainApp.user;
	}

	public void registerUser(String name, String username, String password, String country) {
		Document document = new Document("name", name).append("username", username).append("password", password)
				.append("country", country).append("admin", false);

		userCollection.insertOne(document);
	}

	public void addFilms(String jsonText) {
		try {
			System.out.println(jsonText);
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
				Film film = new Film(filmDocument.getString("Title"));
				films.add(film);
			}
			return films;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}

	public void quit() {
		mongoClient.close();
	}

}
