package pure_server.service;

import pure_server.model.dto.BookDTO;
import pure_server.model.entities.BookEntity;
import pure_server.model.repo.main.BookCollectionRepo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookService implements IBookService {
    private BookCollectionRepo bookCollectionRepo;

    public BookService(BookCollectionRepo bookCollectionRepo) {
        this.bookCollectionRepo = bookCollectionRepo;
    }

    @Override
    public String insertBook(BookDTO book) {
        Objects.requireNonNull(book);
        BookEntity bookEntity = new BookEntity(book.getAuthor(), book.getBookName(), book.getDescription());
        return bookCollectionRepo.insertNewBook(bookEntity);
    }

    @Override
    public boolean updateBook(String bookId, BookDTO book) {
        Objects.requireNonNull(book);
        if (bookId == null) {
            throw new NullPointerException("book id null");
        }
        Optional<BookEntity> bookOpt = bookCollectionRepo.getBookById(bookId);
        if (bookOpt.isEmpty()) {
            throw new RuntimeException("book not exists");
        }
        BookEntity bookEntity = bookOpt.get();
        bookEntity.setAuthor(book.getAuthor());
        bookEntity.setBookName(book.getBookName());
        bookEntity.setDescription(book.getDescription());

        bookCollectionRepo.updateBook(bookEntity);
        return false;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return null;
    }

    @Override
    public BookDTO getBookById(String bookId) {
        return null;
    }

    @Override
    public boolean removeBook(int name) {
        return false;
    }

    @Override
    public boolean replaceBook(BookDTO book) {
        return false;
    }
}