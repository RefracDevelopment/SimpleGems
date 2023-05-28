package me.refracdevelopment.simplegems.database.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.manager.ConfigurationManager;
import org.bson.Document;

import java.util.Objects;
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

        MongoClientSettings.Builder settings = MongoClientSettings.builder();
        settings.applyConnectionString(connectionString);
        settings.applyToConnectionPoolSettings(builder -> builder.maxConnectionIdleTime(30, TimeUnit.SECONDS));
        settings.retryWrites(true);

        if (this.isAuthenticationEnabled()) {
            settings.credential(this.getClientCredentials());
        }

        if (this.isSSLEnabled()) {
            settings.applyToSslSettings(builder -> builder.enabled(true));
        }

        this.client = MongoClients.create(settings.build());

        if (this.isDirectConnectionEnabled()) {
            this.initializeCollections(this.client.getDatabase(ConfigurationManager.Setting.MONGODB_DATABASE.getString()));
        } else {
            this.initializeCollections(this.client.getDatabase(Objects.requireNonNull(connectionString.getDatabase())));
        }

        return this.checkConnection();
    }

    private boolean isDirectConnectionEnabled() {
        return !ConfigurationManager.Setting.MONGODB_USE_CLIENT_URI_ENABLED.getBoolean();
    }

    private boolean isAuthenticationEnabled() {
        return ConfigurationManager.Setting.MONGODB_AUTHENTICATION_ENABLED.getBoolean();
    }

    private boolean isSSLEnabled() {
        return ConfigurationManager.Setting.MONGODB_SSL_ENABLED.getBoolean();
    }

    private ConnectionString getConnectionString() {
        if (!this.isDirectConnectionEnabled()) {
            return new ConnectionString(ConfigurationManager.Setting.MONGODB_USE_CLIENT_URI.getString());
        }

        return new ConnectionString("mongodb://" + ConfigurationManager.Setting.MONGODB_ADDRESS + ":" +
                ConfigurationManager.Setting.MONGODB_PORT);
    }

    private MongoCredential getClientCredentials() {
        return MongoCredential.createCredential(
                ConfigurationManager.Setting.MONGODB_AUTHENTICATION_USERNAME.getString(),
                ConfigurationManager.Setting.MONGODB_AUTHENTICATION_DATABASE.getString(),
                ConfigurationManager.Setting.MONGODB_AUTHENTICATION_PASSWORD.getString().toCharArray()
        );
    }

    private void initializeCollections(MongoDatabase database) {
        statsCollection = database.getCollection("Player_Data");
    }

    private boolean checkConnection() {
        try {
            this.client.listDatabaseNames();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}