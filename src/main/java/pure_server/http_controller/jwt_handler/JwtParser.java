package pure_server.http_controller.jwt_handler;

import com.nimbusds.jose.JOSEException;
import jakarta.xml.bind.ValidationException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

public interface JwtParser {
    boolean checkToken(String jwtToken) throws ValidationException, ParseException, IOException, JOSEException;
}
