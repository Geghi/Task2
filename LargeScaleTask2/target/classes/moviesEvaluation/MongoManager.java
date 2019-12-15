package main.resources.moviesEvaluation;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.*;

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

	public boolean checkUser(String username, String password) {
//		BasicDBObject query = new BasicDBObject();
//		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
//		obj.add(new BasicDBObject("username", username));
//		obj.add(new BasicDBObject("password", password));
//		MongoCursor<Document> cursor = userCollection.find(query).iterator();
		
		MongoCursor<Document> cursor = userCollection.find(and(eq("username", username),eq("password", password))).iterator();
		try {
			if (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
				return true;
			}
		} finally {
			cursor.close();
		}
		
		return false;
	}
	
	public void registerUser(String name, String username, String password, String country) {
		Document document = new Document("name", name)
				.append("username", username)
				.append("password", password)
				.append("country", country);

		userCollection.insertOne(document);
	}

}
