package implementation;

import com.mongodb.client.MongoDatabase;

public interface MongoDBService {
    public MongoDatabase connect();
}
