package pure_server.model.dto;

import java.util.Objects;

public class BookDTO {
    private String id;
    private String author;
    private String bookName;
    private String description;

    public BookDTO() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return author.equals(bookDTO.author) &&
                bookName.equals(bookDTO.bookName) &&
                description.equals(bookDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, bookName, description);
    }
}
