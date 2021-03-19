package pure_server.authentification;

import com.sun.net.httpserver.BasicAuthenticator;
import org.mindrot.jbcrypt.BCrypt;
import pure_server.model.entities.User;
import pure_server.dao.auth.UserCollectionRepo;

public class ServerAuthenticationImpl extends BasicAuthenticator {
    private final UserCollectionRepo userCollectionRepo;
    //TODO realm ?
    public ServerAuthenticationImpl(String realm, UserCollectionRepo userCollectionRepo) {
        super(realm);
        this.userCollectionRepo = userCollectionRepo;
    }

    @Override
    public boolean checkCredentials(String username, String passCandidate) {
        User mongoUser = userCollectionRepo.getUserByName(username);
        System.out.println(mongoUser);
        return mongoUser != null && BCrypt.checkpw(passCandidate, mongoUser.getPasswordHash());
    }
}