package pure_server.http_controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.*;
import pure_server.authentification.ServerAuthentication;
import pure_server.http_controller.http_contexts.HttpContextHolder;
import pure_server.http_controller.http_controller_utils.HttpUtils;
import pure_server.http_controller.http_controller_utils.ParseUtils;
import pure_server.model.repo.UserCollectionRepoImpl;
import pure_server.service.IBookService;

public class HttpController {
    private final HttpServer server;
    private final HttpContextHolder httpContextHolder;
    private final Authenticator authenticator;

    public HttpController(HttpServer server, IBookService bookService, MongoDatabase mongoDatabase) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.server = server;
        ParseUtils parseUtils = new ParseUtils(objectMapper);
        HttpUtils httpUtils = new HttpUtils(objectMapper);
        this.httpContextHolder = new HttpContextHolder(bookService, parseUtils, httpUtils, objectMapper);
        this.authenticator = new ServerAuthentication("realm", new UserCollectionRepoImpl(mongoDatabase));
        createApi();
    }

    private void createApi() {
        server.createContext("/api/book", httpContextHolder.getBookHandler()).setAuthenticator(authenticator);
        server.createContext("/api/books", httpContextHolder.getBooksHandler()).setAuthenticator(authenticator);
    }
}