package implementation;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnector implements MongoDBService {
    private String connectionString;
    private MongoClient mongoClient;

    public MongoDBConnector() {
        mongoClient = MongoClients.create(connectionString);
    }

    public MongoDatabase connect() {
        // Initialize MongoDB connection
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        // connect to database
        mongoClient = MongoClients.create(settings);

        return mongoClient.getDatabase("DigimonTCGCatalog");
    }
}
