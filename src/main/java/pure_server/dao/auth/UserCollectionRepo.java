package pure_server.dao.auth;

import pure_server.model.entities.User;

public interface UserCollectionRepo {
    User getUserByName(String userName);
}
