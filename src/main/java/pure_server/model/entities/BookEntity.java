package pure_server.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.UUID;

public class BookEntity {
    private ObjectId _id;
    private String id;
    private String author;
    private String bookName;
    private String description;

    public BookEntity() {
    }

    public BookEntity(String id) {
        this.id = id;
    }

    public BookEntity(String id, String author, String bookName, String description) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.author = author;
        this.bookName = bookName;
        this.description = description;
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

    @JsonIgnore
    public ObjectId get_id() {
        return _id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return id.equals(that.id) &&
                author.equals(that.author) &&
                bookName.equals(that.bookName) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, bookName, description);
    }
}
