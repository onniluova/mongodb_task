package org.example.mongodb_teht;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoDBUtil {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "management";
    private static final String COLLECTION_NAME = "management";

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public MongoDBUtil() {
        try {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(DATABASE_NAME);
            collection = database.getCollection(COLLECTION_NAME);

            database.runCommand(new Document("ping", 1));
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to MongoDB: " + e.getMessage(), e);
        }
    }

    public void createDocument(String name) {
        try {
            Document document = new Document("name", name);
            collection.insertOne(document);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create document: " + e.getMessage(), e);
        }
    }

    public Document readDocumentById(String id) {
        try {
            return collection.find(new Document("_id", new ObjectId(id))).first();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ID format: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read document: " + e.getMessage(), e);
        }
    }

    public Document readDocumentByName(String name) {
        try {
            return collection.find(new Document("name", name)).first();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read document: " + e.getMessage(), e);
        }
    }

    public void updateDocument(String id, String newName) {
        try {
            Document result = collection.updateOne(
                    new Document("_id", new ObjectId(id)),
                    new Document("$set", new Document("name", newName))
            ).wasAcknowledged() ? new Document("status", "success") : new Document("status", "failed");

            if (!result.getString("status").equals("success")) {
                throw new RuntimeException("Update was not acknowledged by the server");
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ID format: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update document: " + e.getMessage(), e);
        }
    }

    public void deleteDocument(String id) {
        try {
            Document result = collection.deleteOne(new Document("_id", new ObjectId(id)))
                    .wasAcknowledged() ? new Document("status", "success") : new Document("status", "failed");

            if (!result.getString("status").equals("success")) {
                throw new RuntimeException("Delete was not acknowledged by the server");
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ID format: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete document: " + e.getMessage(), e);
        }
    }

    public void close() {
        try {
            if (mongoClient != null) {
                mongoClient.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to close MongoDB connection: " + e.getMessage(), e);
        }
    }
}