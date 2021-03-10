package pure_server.model.repo.auth;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import pure_server.model.entities.User;

import static pure_server.config.constants.RepositoriesConstants.USER_COLLECTION_NAME;
import static pure_server.config.constants.RepositoriesConstants.USER_NAME_COLUMN;

public class UserCollectionRepoImpl implements UserCollectionRepo{
    private final MongoDatabase authDb;

    public UserCollectionRepoImpl(MongoDatabase authDb) {
        this.authDb = authDb;
    }

    @Override
    public User getUserByName(String userName) {
        MongoCollection<Document> collection = authDb.getCollection(USER_COLLECTION_NAME);
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put(USER_NAME_COLUMN, userName);
        MongoCursor<Document> cursor = collection.find(whereQuery).cursor();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
        return new User();
    }
}