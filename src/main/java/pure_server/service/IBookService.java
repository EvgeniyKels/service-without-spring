package pure_server.service;

import pure_server.model.dto.BookDTO;

import java.util.List;
import java.util.Map;


public interface IBookService {
    Map<String, String> insertBook(BookDTO book);
    Map<String, String> insertManyBooks(List<BookDTO>bookDTOS);
    Map<BookDTO, String> updateBook(BookDTO book);
    List<BookDTO> getAllBooks();
    BookDTO getBookById(String bookId);
    Map<String, String> removeBookById(String bookId);
}