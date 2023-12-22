package me.refracdevelopment.simplegems.manager.data.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.refracdevelopment.simplegems.SimpleGems;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

@Getter
public class MongoManager {

    private MongoClient client;
    private MongoCollection<Document> statsCollection;

    public boolean connect() {
        ConnectionString connectionString = this.getConnectionString();

        MongoClientSettings.Builder mongoBuilder = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> builder.maxConnectionIdleTime(30, TimeUnit.SECONDS));

        MongoCredential credentials = this.getClientCredentials();
        if (credentials != null) {
            mongoBuilder.credential(credentials);
        }

        MongoClientSettings settings = mongoBuilder.build();

        this.client = MongoClients.create(settings);

        String databaseName = connectionString.getDatabase();
        if (databaseName == null) {
            databaseName = "gems"; // default database name
        }

        this.initializeCollections(this.client.getDatabase(databaseName));

        return this.checkConnection();
    }

    private boolean isDirectConnectionEnabled() {
        return !SimpleGems.getInstance().getConfigFile().getBoolean("mongodb.use-client-uri.enabled");
    }

    private boolean isAuthenticationEnabled() {
        return SimpleGems.getInstance().getConfigFile().getBoolean("mongodb.authentication.enabled");
    }

    private ConnectionString getConnectionString() {
        if (!this.isDirectConnectionEnabled()) {
            return new ConnectionString(SimpleGems.getInstance().getConfigFile().getString("mongodb.use-client-uri.uri"));
        }

        StringBuilder uriBuilder = new StringBuilder("mongodb://");
        uriBuilder.append(SimpleGems.getInstance().getConfigFile().getString("mongodb.address"))
                .append(":")
                .append(SimpleGems.getInstance().getConfigFile().getInt("mongodb.port"));

        if (this.isAuthenticationEnabled()) {
            uriBuilder.append("/?authSource=")
                    .append(SimpleGems.getInstance().getConfigFile().getString("mongodb.authentication.database"))
                    .append("&retryWrites=true");
        }

        if (SimpleGems.getInstance().getConfigFile().getBoolean("mongodb.ssl-enabled")) {
            uriBuilder.append("&ssl=true");
        }

        return new ConnectionString(uriBuilder.toString());
    }

    private MongoCredential getClientCredentials() {
        if (this.isAuthenticationEnabled()) {
            return MongoCredential.createCredential(
                    SimpleGems.getInstance().getConfigFile().getString("mongodb.authentication.username"),
                    SimpleGems.getInstance().getConfigFile().getString("mongodb.authentication.database"),
                    SimpleGems.getInstance().getConfigFile().getString("mongodb.authentication.password").toCharArray()
            );
        }
        return null;
    }

    private void initializeCollections(MongoDatabase database) {
        statsCollection = database.getCollection("Player_Data");
    }

    private boolean checkConnection() {
        try {
            this.client.listDatabaseNames().first();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}