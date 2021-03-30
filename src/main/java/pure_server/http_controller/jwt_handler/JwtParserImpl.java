package pure_server.http_controller.jwt_handler;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jwt.SignedJWT;
import pure_server.config.context.AppContext;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;


public class JwtParserImpl implements JwtParser {
    private static final String BEARER = "Bearer ";
    private static final String JWK_SET_URL = AppContext.getInstance().getJwkSetUrl();

    @Override
    public boolean checkToken(String jwtToken) throws ParseException, IOException, JOSEException {
        SignedJWT parsedToken;
        RSAKey rsaKey;
        if (jwtToken != null && jwtToken.startsWith(BEARER)) {
            String token = jwtToken.split(BEARER)[1];
            parsedToken = SignedJWT.parse(token);
            JWSHeader header = parsedToken.getHeader();

            JWKSet jwkSet = JWKSet.load(new URL(JWK_SET_URL), 5000, 5000, 10000);
            List<JWK> publicKeys = new JWKSelector(JWKMatcher.forJWSHeader(header)).select(jwkSet);
            if (publicKeys.size() == 0) {
                return false;
            }
            JWK jwk = publicKeys.get(0);
            rsaKey = jwk.toRSAKey();
        } else {
            return false;
        }
        return parsedToken.verify(new RSASSAVerifier(rsaKey));
    }
}
