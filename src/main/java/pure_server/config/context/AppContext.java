package pure_server.config.context;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import pure_server.config.properties_reader.IPropertiesReader;
import pure_server.config.properties_reader.PropertiesReader;
import pure_server.dao.auth.UserCollectionRepo;
import pure_server.dao.auth.UserCollectionRepoImpl;
import pure_server.dao.main.BookCollectionRepo;
import pure_server.dao.main.BookCollectionRepoImpl;

import java.util.Properties;

import static pure_server.config.constants.PropertiesConstant.*;

public final class AppContext {
    private static AppContext appContext;
    private final Properties properties;
    private final MongoDatabase authDb;
    private final MongoDatabase mainDb;
    private final ObjectMapper om;
    private UserCollectionRepo userCollectionRepo;
    private BookCollectionRepo bookCollectionRepo;

    private AppContext() {
        IPropertiesReader propertiesReader = new PropertiesReader();
        this.properties = propertiesReader.receiveInitialProperties();
        MongoConfig mongoConfig = MongoConfig.getInstance(properties.getProperty(DB_HOST), Integer.parseInt(properties.getProperty(DB_PORT)));
        this.authDb = mongoConfig.getDb(properties.getProperty(DB_USER_NAME), properties.getProperty(AUTH_DB_NAME), properties.getProperty(PASS));
        this.mainDb = mongoConfig.getDb(properties.getProperty(DB_USER_NAME), properties.getProperty(MAIN_DB_NAME), properties.getProperty(PASS));
        this.om = createObjectMapper();
        initializeRepositories();
        if((properties.getProperty(ENV).equals(DEV_ENV) || properties.get(ENV).equals(TEST_ENV))
                && Boolean.parseBoolean(properties.getProperty(INITIAL_INSERT))) {
            //TODO insert initial values
        }
    }

    private void initializeRepositories() {
        userCollectionRepo = new UserCollectionRepoImpl(this.authDb);
        bookCollectionRepo = new BookCollectionRepoImpl(this.mainDb, this.om);
    }

    public static AppContext getInstance() {
        if (appContext == null) {
            appContext = new AppContext();
        }
        return appContext;
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return om;
    }

    public AppContext getAppContext() {
        return appContext;
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty(SERVER_PORT));
    }

    public UserCollectionRepo getUserCollectionRepo() {
        return userCollectionRepo;
    }

    public BookCollectionRepo getBookCollectionRepo() {
        return bookCollectionRepo;
    }

    public ObjectMapper getOm() {
        return om;
    }

    public MongoDatabase getAuthDb() {
        return authDb;
    }

    public MongoDatabase getMainDb() {
        return mainDb;
    }

    public String getJwkSetUrl() {
        return properties.getProperty(JWK_SET_URL);
    }
}