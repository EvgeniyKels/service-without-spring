package pure_server.dao.main;

import com.mongodb.client.result.DeleteResult;
import pure_server.model.entities.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookCollectionRepo {
    String insertNewBook(BookEntity book);
    List<BookEntity> getBooksByIds(List<String> collect);
    Optional<BookEntity> getBookById(String bookUuid);
    Optional<BookEntity> updateBook(BookEntity bookEntity);
    List<BookEntity> findAll();
    boolean existById(String id);
    DeleteResult deleteById(String bookId);
    List<String> insertManyNewBooks(List<BookEntity> books);
}
