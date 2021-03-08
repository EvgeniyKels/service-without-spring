package pure_server.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;

public class MongoConfig {
    private static MongoConfig mongoConfig;
    private static MongoClient mongoClient;

    private MongoConfig(String host, Integer port) {
        mongoClient = new MongoClient(host, port);
    }
    //TODO thread save
    public static MongoConfig getInstance(String host, Integer port) {
        if (mongoConfig == null) {
            mongoConfig = new MongoConfig(host, port);
        }
        return mongoConfig;
    }

    public MongoDatabase getAuthDb(String userName, String databaseName, String pass) {
        if(userName != null && pass != null) {
            MongoCredential.createCredential(userName, databaseName, pass.toCharArray());
        }
        return mongoClient.getDatabase(databaseName);
    }
}
