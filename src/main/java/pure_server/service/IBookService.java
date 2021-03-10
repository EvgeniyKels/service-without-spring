package pure_server.service;

import pure_server.model.dto.BookDTO;

import java.util.List;

public interface IBookService {
    String insertBook(BookDTO book);
    boolean updateBook(String bookId, BookDTO book);
    List<BookDTO> getAllBooks();
    BookDTO getBookById(String bookId);
    boolean removeBook(int name);
    boolean replaceBook(BookDTO book);
}
