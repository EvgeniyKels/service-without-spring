package pure_server.model.repo.main;

import pure_server.model.entities.BookEntity;

import java.util.Optional;

public interface BookCollectionRepo {
    String insertNewBook(BookEntity book);
    Optional<BookEntity> getBookById(String bookUuid);

    String updateBook(BookEntity bookEntity);
}
