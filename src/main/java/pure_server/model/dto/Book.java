package pure_server.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {
    private Integer bookId;
    private String author;
    private String bookName;
    private String description;

    public Book() {
    }

    public Book(Integer bookId, String author, String bookName, String description) {
        this.bookId = bookId;
        this.author = author;
        this.bookName = bookName;
        this.description = description;
    }

    public Integer getBookId() {
        return bookId;
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
