package main.resources.moviesEvaluation;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.result.DeleteResult;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static com.mongodb.client.model.Filters.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MongoManager {

	final private String DB_NAME = "Moviegram";
	private int port;
	private String ip;
	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> userCollection;
	MongoCollection<Document> filmCollection;
	final static Class<? extends List> docClazz = new ArrayList<Document>().getClass();

	public MongoManager(int p, String i) {
		port = p;
		ip = i;
		mongoClient = MongoClients.create(
		        MongoClientSettings.builder()
		                .applyToClusterSettings(builder ->
		                        builder.hosts(Arrays.asList(new ServerAddress(ip, port))))
		                .build());
		//mongoClient = new MongoClient(Arrays.asList(new ServerAddress(ip, port)));
		//mongoClient = MongoClients.create("mongodb://" + ip + ":" + port);
		//mongoClient.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		database = mongoClient.getDatabase(DB_NAME);
		userCollection = database.getCollection("usersCollection");
		filmCollection = database.getCollection("moviesCollection");
	}

	public User checkUser(String username, String password) {
		try {
			Document found = (Document) userCollection.find(and(eq("Username", username), eq("Password", password)))
					.first();

			if (found != null) {
				MainApp.user = new User(found.getObjectId("_id").toString(), found.getString("Name"),
						found.getString("Username"), found.getString("Password"), found.getString("Country"),
						found.getBoolean("Admin"));

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
				Film film = createFilmObject(filmDocument);
				films.add(film);
			}
			int i = 0, j;
			for(; i < films.size(); i++) {
				String titleTmp = films.get(i).getTitle();
				for(j = i+1; j < films.size(); j++) {
					String titleTmp2 = films.get(j).getTitle();
					if(titleTmp.equals(titleTmp2)) {
						films.remove(j);
						j--;
					}
				}
			}
			return films;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List<Object> searchFilmsSorted() {
		try {
			MongoCursor<Document> cursor = filmCollection.find().sort(Sorts.descending("ratingImdb")).limit(30).iterator();
			List<Object> films = new ArrayList<>();
			List<Film> films2 = new ArrayList<>();
			while (cursor.hasNext()) {
				Document filmDocument = cursor.next();

				Film film = createFilmObject(filmDocument);
				films2.add(film);
				films.add((Object) film);
			}
			int i = 0, j;
			for(; i < films.size(); i++) {
				String titleTmp = films2.get(i).getTitle();
				for(j = i+1; j < films2.size(); j++) {
					String titleTmp2 = films2.get(j).getTitle();
					if(titleTmp.equals(titleTmp2)) {
						films.remove(j);
						films2.remove(j);
						j--;
					}
				}
			}
			
			return films;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public List<Object> searchFilmsSortedFiltered(String country) {
		try {
			MongoCursor<Document> cursor = filmCollection.find(eq("Country", country))
															.sort(Sorts.descending("ratingImdb")).limit(30).iterator();
			List<Object> films = new ArrayList<>();
			List<Film> films2 = new ArrayList<>();
			while (cursor.hasNext()) {
				Document filmDocument = cursor.next();

				Film film = createFilmObject(filmDocument);
				films2.add(film);
				films.add((Object) film);
			}
			int i = 0, j;
			for(; i < films.size(); i++) {
				String titleTmp = films2.get(i).getTitle();
				for(j = i+1; j < films2.size(); j++) {
					String titleTmp2 = films2.get(j).getTitle();
					if(titleTmp.equals(titleTmp2)) {
						films.remove(j);
						films2.remove(j);
						j--;
					}
				}
			}
			return films;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}
	
	public List<Object> getTopUser(){
		try {
			MongoCursor<Document> cursor = userCollection.find().sort(Sorts.descending("Numrates")).iterator();
			List<Object> users = new ArrayList<>();
			while (cursor.hasNext()) {
				Document tmp = cursor.next();
				Aggregator u = new Aggregator(tmp.getString("Username") + " (" + tmp.getInteger("Numrates", 0) + ")", tmp.getInteger("Numrates", 0));
				users.add((Object)u);
				
			}
			return users;
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public ObservableList<String> getYears() {

		ObservableList<String> list = FXCollections.observableArrayList();
		try {

			ArrayList<String> a = filmCollection.distinct("Year", String.class).into(new ArrayList<String>());
			System.out.println(a);
			System.out.println(a.size());
			System.out.println(a.get(1).getClass());
			a.sort(Collections.reverseOrder());

			System.out.println(a);
			for (int i = 0; i < a.size(); i++) {
				if (a.get(i).toString().length() > 4) {
					a.remove(i);
					i--;
				} else {
					list.add((String) a.get(i));
				}
			}
			System.out.println(a);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public ObservableList<String> getCountries() {
		ObservableList<String> list = FXCollections.observableArrayList();
		try {
			ArrayList<String> a = filmCollection.distinct("Country", String.class).into(new ArrayList<String>());
			System.out.println(a);
			System.out.println(a.size());
			System.out.println(a.get(1).getClass());
			a.sort(Collections.reverseOrder());

			System.out.println(a);
			for (int i = 0; i < a.size(); i++) {
				if (a.get(i).toString().contains(",")) {
					a.remove(i);
					i--;
				} else {
					list.add((String) a.get(i));
				}
			}
			System.out.println(a);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;

	}
	
	public void addVote(Film film, int vote, Double updatedRating) {
		try {
			Document found = (Document) filmCollection
					.find(eq("_id", new ObjectId(film.getId()) )).first();
			if (found != null) {
				filmCollection.updateMany(
						eq("_id",  new ObjectId(film.getId())),
						new Document("$set", new Document("imdbRating", Double.toString(updatedRating))
								.append("imdbVotes", Integer.toString(film.getVotes()))));
				System.out.println("film: " + film.getTitle() + ", vote: " + vote);
				addUserVote(film, vote);

			}
			System.out.println("Vote Updated!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void addUserVote(Film f, int v) {
		String id = MainApp.user.getId();
		System.out.println(id);
		Document vote = new Document("Id_Film", new ObjectId(f.getId())).append("Titolo", f.getTitle()).append("Voto", v);
		userCollection.updateOne(eq("_id", new ObjectId(MainApp.user.getId())), Updates.addToSet("Rated", vote));
		userCollection.updateOne(eq("_id", new ObjectId(MainApp.user.getId())), Updates.inc("Numrates", 1));
		System.out.println("User Collection updated");
	}
	
	public int getUserVote(String id, String film) {
		Document doc = (Document) userCollection.find(and(eq("_id", new ObjectId(id)), eq("Rated.Id_Film", new ObjectId(film)))).first();
		if(doc == null) {
			return -1;
		}else {
			
			List<Document> list = doc.get("Rated", docClazz);
			Iterator it = list.iterator();
			String[] splitter = new String[3];
			while(it.hasNext()) {
				
				String str = it.next().toString();
				str = str.replaceAll("Document", "");
				str = str.replace("{{", "");
				str = str.replace("Id_Film=", "");
				str = str.replace("Titolo=", "");
				str = str.replace("Voto=", "");
				str = str.replace("}}", "");
				str = str.replace(" ", "");
				splitter = str.split(",");
				
				if(splitter[0].equals(film)) {
					return Integer.parseInt(splitter[2]);
				}
			
			}
			
			
			return -1;
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

	// ObservableList<Object>
	public ObservableList<Object> topCountries(String year) {
		String tmp, countries, nfilm;
		ObservableList<Object> ret = FXCollections.observableArrayList();
		int i = 0;
		String[] split;
		split = new String[2];
		MongoCursor<Document> cursor = filmCollection.aggregate(Arrays.asList(Aggregates.match(eq("Year", year)),
				Aggregates.group("$Country", Accumulators.sum("NFilm", 1L)),
				Aggregates.sort(Sorts.descending("NFilm")))).iterator();

		try {
			while (i < 10 && cursor.hasNext()) {
				tmp = cursor.next().toString();
				tmp = tmp.replace("Document{{_id=", "");
				tmp = tmp.replace("}}", "");
				split = tmp.split(", NFilm=");
				split[0] = split[0].replace(" ", "");
				split[1] = split[1].replace(" ", "");
				countries = split[0] + "  (" + split[1] + ")";
				nfilm = split[1];

				ret.add((Object) new Aggregator(countries, Integer.parseInt(nfilm)));
				i++;
			}
		} finally {
			cursor.close();
		}
		return ret;
	}

	public ObservableList<Object> topProduction() {
		String tmp, house, film;
		String[] split;
		split = new String[2];
		ObservableList<Object> ret = FXCollections.observableArrayList();
		int i = 0;
		MongoCursor<Document> cursor = filmCollection
				.aggregate(Arrays.asList(Aggregates.match(nin("Production", Arrays.asList("N/A", null))),
						Aggregates.group("$Production", Accumulators.sum("NFilm", 1L)),
						Aggregates.sort(Sorts.descending("NFilm"))))
				.iterator();
		try {
			while (i < 10) {
				tmp = cursor.next().toJson();
				tmp = tmp.replace("{", "");
				tmp = tmp.replace("}", "");
				tmp = tmp.replace('"', ' ');
				tmp = tmp.replace("$numberLong :", "");
				// System.out.println(tmp);
				split = tmp.split(",");
				split[0] = split[0].replace(" ", "");
				split[1] = split[1].replace(" ", "");
				house = split[0].split(":")[1] + "  (" + split[1].split(":")[1] + ")";
				film = split[1].split(":")[1];
				ret.add((Object) new Aggregator(house, Integer.parseInt(film)));
				i++;
			}
		} finally {
			cursor.close();
		}
		return ret;
	}

	public Film createFilmObject(Document filmDocument) {
		String id = filmDocument.getObjectId("_id").toString();
		String filmTitle = filmDocument.getString("Title");
		String director = filmDocument.getString("Director");
		String production = filmDocument.getString("Production");
		String poster = filmDocument.getString("Poster");
		String year = filmDocument.getString("Year");
		String rating = filmDocument.getString("imdbRating");
		String votes = filmDocument.getString("imdbVotes");

		Film film = new Film(id, filmTitle, director, production, poster, year, rating, votes);
		return film;
	}
	


	public void quit() {
		mongoClient.close();
	}

}
