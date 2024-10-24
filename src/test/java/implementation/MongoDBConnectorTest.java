package implementation;

import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class MongoDBConnectorTest {
    private MongoDBConnector connector;
    private MongoDatabase mockDatabase;

    @BeforeEach
    void setUp() {
        connector = new MongoDBConnector();
        mockDatabase = mock(MongoDatabase.class);

    }

    @DisplayName("Should connect to MongoDB successfully")
    @Test
    void shouldConnectToMongoDBSuccessfully() {
        connector.connect();
        when(mockDatabase.runCommand(any(Document.class))).thenReturn(Document.parse("{ok: 1}"));
    }

    @DisplayName("Should handle MongoDB connection failure")
    @Test
    void shouldHandleMongoDBConnectionFailure() {
        // Mocking the runCommand method to throw an exception
        when(mockDatabase.runCommand(any(Document.class))).thenThrow(new MongoException("Connection failed"));
    }
}