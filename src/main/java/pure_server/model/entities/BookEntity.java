package pure_server.model.entities;

import org.bson.Document;

import java.util.UUID;

public class BookEntity {
    private final String id;
    private String author;
    private String bookName;
    private String description;

    public BookEntity(String author, String bookName, String description) {
        this.id = UUID.randomUUID().toString();
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

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
