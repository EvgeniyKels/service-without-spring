package pure_server;

import com.sun.net.httpserver.HttpServer;
import pure_server.config.context.AppContext;
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

        AppContext appContext = AppContext.getInstance();

        HttpServer server = null;

        try {
             server = HttpServer.create(new InetSocketAddress(appContext.getPort()), 0);
             LOGGER.info("server started");
        } catch (IOException e) {
            LOGGER.info("server failed: " + e.getLocalizedMessage());
        }

        HttpController httpController = new HttpController(
                server,
                new BookService(
                        appContext.getBookCollectionRepo()),
                appContext.getUserCollectionRepo(),
                appContext.getOm());

        server.setExecutor(null);
        server.start();

    }
}
