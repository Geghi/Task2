package main.resources.moviesEvaluation;

import org.bson.Document;

import com.mongodb.client.*;

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

	public void quit() {
		mongoClient.close();
	}

	public boolean login(String u, String p) {

		MongoCursor<Document> cursor = userCollection.find().iterator();

		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}
		return false;
	}

	public boolean checkUser(String username) {
		
		// TODO
		MongoCursor<Document> cursor = userCollection.find().iterator();

		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}
		
		return false;
	}

}
