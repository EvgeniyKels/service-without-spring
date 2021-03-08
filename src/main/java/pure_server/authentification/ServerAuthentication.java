package pure_server.authentification;

import com.sun.net.httpserver.BasicAuthenticator;
import pure_server.model.entities.User;
import pure_server.model.repo.UserCollectionRepo;

public class ServerAuthentication extends BasicAuthenticator {
    private UserCollectionRepo userCollectionRepo;
    //TODO realm ?
    public ServerAuthentication(String realm, UserCollectionRepo userCollectionRepo) {
        super(realm);
        this.userCollectionRepo = userCollectionRepo;
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        User mongoUser = userCollectionRepo.getUserByName(username);
        return mongoUser != null && getHash(password).equals(mongoUser.getPasswordHash());
    }

    private String getHash(String rawPassword) {
        //TODO realize hash function

        return rawPassword;
    }
}