package pure_server.model.dto;

public class BookDTO {
    private String id;
    private String author;
    private String bookName;
    private String description;

    public BookDTO(String id, String author, String bookName, String description) {
        this.id = id;
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
