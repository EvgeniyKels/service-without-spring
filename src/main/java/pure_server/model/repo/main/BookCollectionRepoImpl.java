package pure_server.model.repo.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import pure_server.model.entities.BookEntity;
import static pure_server.config.constants.RepositoriesConstants.*;

import java.util.*;
import java.util.stream.Collectors;

//https://developer.mongodb.com/quickstart/java-mapping-pojos/?utm_campaign=javapojos&utm_source=twitter&utm_medium=organic_social
public class BookCollectionRepoImpl implements BookCollectionRepo {
    private final MongoCollection<Document> bookCollection;
    private final ObjectMapper om;
    public BookCollectionRepoImpl(MongoDatabase mainDb, ObjectMapper om) {
        this.bookCollection = mainDb.getCollection(BOOK_COLLECTION_NAME);
        this.om = om;
    }
    @Override
    public String insertNewBook(BookEntity bookEntity) {
        Objects.requireNonNull(bookEntity);
        bookCollection.insertOne(new Document(om.convertValue(bookEntity, Map.class)));
        Document bookDocument = getDocument(bookEntity.getId());
        if (bookDocument == null) {
            throw new RuntimeException("error on insert new bookDocument");
        }
        return (String) bookDocument.get(ID_COLUMN_NAME);
    }

    private Document getDocument(String id) {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put(ID_COLUMN_NAME, id);
        MongoCursor<Document> cursor = bookCollection.find(basicDBObject).cursor();
        return cursor.tryNext();
    }

    @Override
    public Optional<BookEntity> getBookById(String bookUuid) {
        Document bookDocument = getDocument(bookUuid);
        BookEntity bookEntity;
        try {
            bookEntity = om.readValue(bookDocument.toJson(), BookEntity.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return bookEntity == null ? Optional.empty() : Optional.of(bookEntity);
    }

    @Override
    public String updateBook(BookEntity bookEntity) {
        BookEntity bookEntityFromDb;
        try {
            Document document = getDocument(bookEntity.getId());
            String content = document.toJson();
            bookEntityFromDb = om.readValue(content, BookEntity.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        BasicDBObject book = new BasicDBObject(om.convertValue(bookEntityFromDb, Map.class));
        BasicDBObject newBook = new BasicDBObject(om.convertValue(bookEntity, Map.class));

        Document oneAndUpdate = bookCollection.findOneAndUpdate(book, newBook);

        return "";
    }
}
