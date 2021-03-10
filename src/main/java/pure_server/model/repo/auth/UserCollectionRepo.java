package pure_server.model.repo.auth;

import pure_server.model.dto.BookDTO;
import pure_server.model.entities.User;

public interface UserCollectionRepo {
    User getUserByName(String userName);
}
