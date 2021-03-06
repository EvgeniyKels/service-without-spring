package pure_server.http_controller.http_controller_utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import pure_server.model.dto.responses.BookResponseDto;

import java.io.IOException;

import static pure_server.config.constants.BookServiceConstants.NON_SUCCESS;
import static pure_server.config.constants.BookServiceConstants.SUCCESS;


public class HttpUtils {
    public HttpUtils(ObjectMapper objectMapper) {
    }
    public void addSuccessStringToResponse(BookResponseDto.BookResponseBuilder bookResponseBuilder, Object result) {
        if (result != null) {
            //TODO
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
