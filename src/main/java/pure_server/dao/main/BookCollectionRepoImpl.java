package pure_server.dao.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pure_server.model.entities.BookEntity;

import static pure_server.config.constants.RepositoriesConstants.*;

import java.util.*;
import java.util.stream.Collectors;

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
        if (bookEntity.getId() != null) {
            Document first = bookCollection.find(eq(ID_COLUMN_NAME, bookEntity.getId())).first();
            if (first == null) {
                Document book = new Document(OBJECT_ID, new ObjectId());
                book.putAll(om.convertValue(bookEntity, Map.class));
                bookCollection.insertOne(book);
            } else {
                return WRONG_FORMAT_OF_THE_GIVEN_DTO;
            }
        }
        Optional<BookEntity> bookOpt = getBookById(bookEntity.getId());
        return bookOpt.isPresent() ? bookOpt.get().getId() : BOOK_DID_NOT_INSERT;
    }

    @Override
    public List<String> insertManyNewBooks(List<BookEntity> books) {
        List<Document> docs = books.stream().map(x -> {
            Document book = new Document(OBJECT_ID, new ObjectId());
            book.putAll(om.convertValue(x, Map.class));
            return book;
        }).collect(Collectors.toList());
        bookCollection.insertMany(docs);
        return getBooksByIds(
                books.stream().map(BookEntity::getId).collect(Collectors.toList())
        ).stream().map(BookEntity::getId).collect(Collectors.toList());
    }

    @Override
    public List<BookEntity> getBooksByIds(List<String> ids) {
        FindIterable<Document> documents = bookCollection.find(in(ID_COLUMN_NAME, ids));
        List<BookEntity>bookEntities = new ArrayList<>();
        for (Document doc: documents) {
            try {
                bookEntities.add(om.readValue(doc.toJson(), BookEntity.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return bookEntities;
    }

    @Override
    public Optional<BookEntity> getBookById(String bookUuid) {
        Objects.requireNonNull(bookUuid);
        Document first = bookCollection.find(eq(ID_COLUMN_NAME, bookUuid)).first();
        return Optional.ofNullable(fromDocumentToObject(first));
    }

    @Override
    public Optional<BookEntity> updateBook(BookEntity bookEntity) {
        BookEntity bookEntityBeforeUpdate = getBookById(bookEntity.getId()).get();
        if (!bookEntity.getId().equals(bookEntityBeforeUpdate.getId())) {
            return Optional.empty();
        }
        Bson idEquality = eq(ID_COLUMN_NAME, bookEntity.getId());
        List<Bson>updates = new ArrayList<>();
        if (!bookEntity.getDescription().equals(bookEntityBeforeUpdate.getDescription())) {
            updates.add(set(DESCRIPTION_COLUMN_NAME, bookEntity.getDescription()));
        }
        if (!bookEntity.getBookName().equals(bookEntityBeforeUpdate.getBookName())) {
            updates.add(set(BOOK_NAME_COLUMN_NAME, bookEntity.getBookName()));
        }
        if (!bookEntity.getAuthor().equals(bookEntityBeforeUpdate.getAuthor())) {
            updates.add(set(AUTHOR_COLUMN_NAME, bookEntity.getAuthor()));
        }
        bookCollection.updateOne(idEquality, updates);
        return getBookById(bookEntity.getId());
    }

    @Override
    public List<BookEntity> findAll() {
        return bookCollection.find().into(new ArrayList<>()).stream().map(this::fromDocumentToObject).collect(Collectors.toList());
    }

    @Override
    public boolean existById(String id) {
        return bookCollection.find(eq(ID_COLUMN_NAME, id)).first() != null;
    }

    @Override
    public DeleteResult deleteById(String bookId) {
        return bookCollection.deleteOne(eq(ID_COLUMN_NAME, bookId));
    }

    private BookEntity fromDocumentToObject(Document x) {
        if (x == null) {
            return null;
        }
        BookEntity bookEntity = null;
        try {
           bookEntity = om.readValue(x.toJson(), BookEntity.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return bookEntity;
    }
}