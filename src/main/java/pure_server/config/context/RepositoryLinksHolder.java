package pure_server.config.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import pure_server.dao.auth.UserCollectionRepo;
import pure_server.dao.auth.UserCollectionRepoImpl;
import pure_server.dao.main.BookCollectionRepo;
import pure_server.dao.main.BookCollectionRepoImpl;

//TODO scan repo package and init all repos by reflection
public class RepositoryLinksHolder {
    private static RepositoryLinksHolder repositoryLinksHolder;
    private final UserCollectionRepo userCollectionRepo;
    private final BookCollectionRepo bookCollectionRepo;

    private RepositoryLinksHolder(MongoDatabase authDb, MongoDatabase mainDb, ObjectMapper om) {
        this.userCollectionRepo = new UserCollectionRepoImpl(authDb);
        this.bookCollectionRepo = new BookCollectionRepoImpl(mainDb, om);
    }

    public static RepositoryLinksHolder getInstance(MongoDatabase authDb, MongoDatabase mainDb, ObjectMapper om) {
        if (repositoryLinksHolder == null) {
            repositoryLinksHolder = new RepositoryLinksHolder(authDb, mainDb, om);
        }
        return repositoryLinksHolder;
    }

    public UserCollectionRepo getUserCollectionRepo() {
        return userCollectionRepo;
    }

    public BookCollectionRepo getBookCollectionRepo() {
        return bookCollectionRepo;
    }
}
