package pure_server;

import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.HttpServer;
import pure_server.config.IPropertiesReader;
import pure_server.config.MongoConfig;
import pure_server.config.PropertiesReader;
import pure_server.http_controller.HttpController;
import pure_server.service.BookService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.logging.Logger;

import static pure_server.config.constants.PropertiesConstant.*;


public class ServerMain {
    private static final Logger LOGGER = Logger.getLogger(ServerMain.class.getSimpleName());
    public static void main(String[] args) {
        IPropertiesReader propertiesReader = new PropertiesReader();
        Properties properties = propertiesReader.receiveInitialProperties();

        MongoConfig mongoConfig = MongoConfig.getInstance(properties.getProperty(DB_HOST), Integer.parseInt(properties.getProperty(DB_PORT)));
        MongoDatabase authDb = mongoConfig.getAuthDb(properties.getProperty(DB_USER_NAME), properties.getProperty(AUTH_DB_NAME), properties.getProperty(PASS));

        HttpServer server = null;

        try {
             server = HttpServer.create(new InetSocketAddress(Integer.parseInt(properties.getProperty(SERVER_PORT))), 0);
             LOGGER.info("server started");
        } catch (IOException e) {
            LOGGER.info("server failed: " + e.getLocalizedMessage());
        }

        HttpController httpController = new HttpController(server, new BookService(), authDb);

        server.setExecutor(null);
        server.start();

    }
}
