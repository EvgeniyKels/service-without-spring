import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pure_server.config.context.AppContext;
import pure_server.model.entities.BookEntity;
import pure_server.model.repo.main.BookCollectionRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static pure_server.config.constants.RepositoriesConstants.*;

public class BookCollectionRepoTest {
    private BookCollectionRepo bookCollectionRepo;
    private MongoCollection<Document> bookCollection;

    @BeforeEach
    void beforeEach() {
        AppContext appContext = AppContext.getInstance(); //TODO add new properties for tests
        bookCollectionRepo = appContext.getBookCollectionRepo();
        bookCollection = appContext.getMainDb().getCollection(BOOK_COLLECTION_NAME);
    }

    @Test
    void insertNewBookWithCorrectFields() {
        BookEntity bookEntity = new BookEntity("author", "name", "descriptor");
        String uuid = bookCollectionRepo.insertNewBook(bookEntity);
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put(ID_COLUMN_NAME, bookEntity.getId());
        MongoCursor<Document> cursor = bookCollection.find(basicDBObject).cursor();
        Document document = cursor.tryNext();
        assertNotNull(document);
        assertEquals(bookEntity.getId(), document.get(ID_COLUMN_NAME));
        assertEquals(bookEntity.getAuthor(), document.get(AUTHOR));
        assertEquals(bookEntity.getBookName(), document.get(BOOK_NAME));
        assertEquals(bookEntity.getDescription(), document.get(DESCRIPTION));
        assertEquals(uuid, document.get(ID_COLUMN_NAME));
    }

    @Test
    void update() {
        BookEntity bookEntityBeforeUpdate = new BookEntity("author", "name", "descriptor");
        String uuid = bookCollectionRepo.insertNewBook(bookEntityBeforeUpdate);
        BookEntity bookEntityAfterUpdate = new BookEntity("author_up", "name_up", "descriptor_up");
        String updatedUuid = bookCollectionRepo.updateBook(bookEntityAfterUpdate);
        assertEquals(uuid, updatedUuid);

//        assertEquals(bookEntityBeforeUpdate.getId(), bookEntityFromServiceAfterUpdate.getId());
//        assertEquals(bookEntityAfterUpdate.getId(), bookEntityFromServiceAfterUpdate.getId());
//
//        assertNotEquals(bookEntityBeforeUpdate.getAuthor(), bookEntityFromServiceAfterUpdate.getAuthor());
//        assertEquals(bookEntityAfterUpdate.getAuthor(), bookEntityFromServiceAfterUpdate.getAuthor());
//
//        assertNotEquals(bookEntityBeforeUpdate.getBookName(), bookEntityFromServiceAfterUpdate.getId());
//        assertEquals(bookEntityAfterUpdate.getId(), bookEntityFromServiceAfterUpdate.getId());
//
//        assertNotEquals(bookEntityBeforeUpdate.getId(), bookEntityFromServiceAfterUpdate.getId());
//        assertEquals(bookEntityAfterUpdate.getId(), bookEntityFromServiceAfterUpdate.getId());

    }

}
