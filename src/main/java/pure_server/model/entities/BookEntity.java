package pure_server.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;

import java.util.UUID;

public class BookEntity {
    @JsonIgnore
    private ObjectId _id;
    private String id;
    private String author;
    private String bookName;
    private String description;

    public BookEntity() {
    }

    public BookEntity(String id, String author, String bookName, String description) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.author = author;
        this.bookName = bookName;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getBookName() {
        return bookName;
    }

    public String getDescription() {
        return description;
    }
}
