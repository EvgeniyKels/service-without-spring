package pure_server.service;

import pure_server.model.dto.Book;

import java.util.List;

public interface IBookService {
    boolean insertBook(Book book);
    boolean updateBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(int bookId);
    boolean removeBook(int name);
    boolean replaceBook(Book book);
}
