package pure_server.http_controller;

import com.nimbusds.jose.JOSEException;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import jakarta.xml.bind.ValidationException;
import pure_server.http_controller.jwt_handler.JwtParser;
import pure_server.http_controller.jwt_handler.JwtParserImpl;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class JwtAuth extends Authenticator {
    private static final String AUTHORIZATION = "Authorization";

    private final JwtParser jwtParser = new JwtParserImpl();

    @Override
    public Result authenticate(HttpExchange exchange) {
        Headers requestHeaders = exchange.getRequestHeaders();
        String jwtToken = null;
        for (Map.Entry<String, List<String>> header: requestHeaders.entrySet()) {
            if (header.getKey().equals(AUTHORIZATION)) {
                jwtToken = header.getValue().get(0);
                break;
            }
        }
        if (jwtToken == null || jwtToken.isBlank()) {
            return new Failure(403);
        }
        try {
            if (jwtParser.checkToken(jwtToken)) {
                return new Success(new HttpPrincipal("", ""));//TODO
            } else {
                return new Failure(403);
            }
        } catch (ValidationException | JOSEException | ParseException | IOException e) {
            e.printStackTrace();
            return new Failure(403);
        }
    }
}
