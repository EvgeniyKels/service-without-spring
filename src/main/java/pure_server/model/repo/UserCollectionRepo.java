package pure_server.model.repo;

import pure_server.model.entities.User;

public interface UserCollectionRepo {
    User getUserByName(String userName);
}
