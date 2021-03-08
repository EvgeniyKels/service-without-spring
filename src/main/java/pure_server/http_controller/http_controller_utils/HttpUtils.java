package pure_server.http_controller.http_controller_utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import pure_server.model.dto.responses.BookResponseDto;

import java.io.IOException;

import static pure_server.config.constants.HttpControllerConstants.NON_SUCCESS;
import static pure_server.config.constants.HttpControllerConstants.SUCCESS;

public class HttpUtils {
//    private final Logger LOGGER = Logger.getLogger(this.getClass().getSimpleName());
//    private final ObjectMapper objectMapper;
    public HttpUtils(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
    }
    public void addSuccessStringToResponse(BookResponseDto.BookResponseBuilder bookResponseBuilder, boolean result) {
        if (result) {
            bookResponseBuilder.addTextResult(SUCCESS);
        } else {
            bookResponseBuilder.addTextResult(NON_SUCCESS);
        }
    }

    public void handleSuccess(HttpExchange exchange, String body) throws IOException {
        exchange.sendResponseHeaders(200, body.getBytes().length);
    }

    public void handleNonSuccess(int code, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(code, 0);
    }
}
