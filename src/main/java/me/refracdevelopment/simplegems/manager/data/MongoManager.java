package me.refracdevelopment.simplegems.manager.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.configuration.ConfigurationManager;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

@Getter
public class MongoManager {

    private SimpleGems plugin;
    private MongoClient client;
    private MongoCollection<Document> statsCollection;

    public MongoManager(SimpleGems plugin) {
        this.plugin = plugin;
    }

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
        return !ConfigurationManager.Setting.MONGODB_USE_CLIENT_URI_ENABLED.getBoolean();
    }

    private boolean isAuthenticationEnabled() {
        return ConfigurationManager.Setting.MONGODB_AUTHENTICATION_ENABLED.getBoolean();
    }

    private ConnectionString getConnectionString() {
        if (!this.isDirectConnectionEnabled()) {
            return new ConnectionString(ConfigurationManager.Setting.MONGODB_USE_CLIENT_URI.getString());
        }

        StringBuilder uriBuilder = new StringBuilder("mongodb://");
        uriBuilder.append(ConfigurationManager.Setting.MONGODB_ADDRESS.getString())
                .append(":")
                .append(ConfigurationManager.Setting.MONGODB_PORT.getInt());

        if (this.isAuthenticationEnabled()) {
            uriBuilder.append("/?authSource=")
                    .append(ConfigurationManager.Setting.MONGODB_AUTHENTICATION_DATABASE.getString())
                    .append("&retryWrites=true");
        }

        if (ConfigurationManager.Setting.MONGODB_SSL_ENABLED.getBoolean()) {
            uriBuilder.append("&ssl=true");
        }

        return new ConnectionString(uriBuilder.toString());
    }

    private MongoCredential getClientCredentials() {
        if (this.isAuthenticationEnabled()) {
            return MongoCredential.createCredential(
                    ConfigurationManager.Setting.MONGODB_AUTHENTICATION_USERNAME.getString(),
                    ConfigurationManager.Setting.MONGODB_AUTHENTICATION_DATABASE.getString(),
                    ConfigurationManager.Setting.MONGODB_AUTHENTICATION_PASSWORD.getString().toCharArray()
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