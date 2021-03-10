package pure_server.model.dto;

public class BookDTO {
    private String author;
    private String bookName;
    private String description;

    public BookDTO(String author, String bookName, String description) {
        this.author = author;
        this.bookName = bookName;
        this.description = description;
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
