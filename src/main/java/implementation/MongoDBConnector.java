package implementation;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnector implements MongoDBService {
    private final String connectionString = "mongodb+srv://edward_tumuwo:jgw1JtTD3s0WyF8H@digimontcgcatalogcluste.bjlysu8.mongodb.net/?retryWrites=true&w=majority&appName=DigimonTCGCatalogCluster";
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
