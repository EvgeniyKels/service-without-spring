package pure_server.http_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.*;
import pure_server.authentification.ServerAuthenticationImpl;
import pure_server.http_controller.http_contexts.HttpContextHolder;
import pure_server.http_controller.http_controller_utils.HttpUtils;
import pure_server.http_controller.http_controller_utils.ParseUtils;
import pure_server.dao.auth.UserCollectionRepo;
import pure_server.service.IBookService;

public class HttpController {
    private final HttpServer server;
    private final HttpContextHolder httpContextHolder;
    private final Authenticator authenticator;

    public HttpController(HttpServer server, IBookService bookService, UserCollectionRepo userCollectionRepo, ObjectMapper om) {
        this.server = server;
        ParseUtils parseUtils = new ParseUtils(om);
        HttpUtils httpUtils = new HttpUtils(om);
        this.httpContextHolder = new HttpContextHolder(bookService, parseUtils, httpUtils, om);
        this.authenticator = new ServerAuthenticationImpl("realm", userCollectionRepo);
        createApi();
    }

    private void createApi() {
        server.createContext("/api/book", httpContextHolder.getBookHandler());
//        server.createContext("/api/book", httpContextHolder.getBookHandler()).setAuthenticator(authenticator);
        server.createContext("/api/books", httpContextHolder.getBooksHandler()).setAuthenticator(authenticator);
    }
}