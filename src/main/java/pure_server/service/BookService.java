package pure_server.service;

import pure_server.model.dto.BookDTO;
import pure_server.model.entities.BookEntity;
import pure_server.dao.main.BookCollectionRepo;

import java.util.*;
import java.util.stream.Collectors;

import static pure_server.config.constants.BookServiceConstants.*;

public class BookService implements IBookService {

    private final BookCollectionRepo bookCollectionRepo;


    public BookService(BookCollectionRepo bookCollectionRepo) {
        this.bookCollectionRepo = bookCollectionRepo;
    }

    @Override
    public Map<String, String> insertBook(BookDTO book) {
        Objects.requireNonNull(book);
        Map<String, String>resMap = new HashMap<>();
        if (book.getId() != null) {
            resMap.put(book.getId(), WRONG_FORMAT_OF_THE_REQUEST_BODY);
            return resMap;
        }
        return List.of(bookCollectionRepo.insertNewBook(mapDtoToEntity(book))).stream().collect(Collectors.toMap(x -> x, x -> INSERTED));
    }

    @Override
    public Map<String, String> insertManyBooks(List<BookDTO> books) {
        Objects.requireNonNull(books);
        Map<String, String>resMap = new HashMap<>();
        for (BookDTO book : books) {
            if (book.getId() != null) {
                resMap.put(book.getId(), WRONG_FORMAT_OF_THE_REQUEST_BODY);
                return resMap;
            }
        }
        return bookCollectionRepo.insertManyNewBooks(
                books.stream().map(this::mapDtoToEntity).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(x -> x, x -> INSERTED));
    }

    @Override
    public BookDTO getBookById(String bookId) {
        Objects.requireNonNull(bookId);
        if (bookId.length() == 0) {
            return null;
        }
        return bookCollectionRepo.getBookById(bookId).map(this::mapEntityToDto).orElse(null);
    }

    @Override
    public Map<BookDTO, String> updateBook(BookDTO book) {
        Objects.requireNonNull(book);
        Map<BookDTO, String>resMap = new HashMap<>();
        if (!bookCollectionRepo.existById(book.getId())){
            resMap.put(book, BOOK_WITH_GIVEN_ID_NOT_EXISTS);
        }
        if (bookCollectionRepo.updateBook(mapDtoToEntity(book)).map(this::mapEntityToDto).orElse(null) == null) {
            resMap.put(book, ERROR_ON_UPDATE);
        } else {
            resMap.put(book, UPDATED);
        }

        return resMap;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<BookEntity> bookEntities = bookCollectionRepo.findAll();
        return bookEntities.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> removeBookById(String bookId) {
        Objects.requireNonNull(bookId);
        Map<String, String>resMap = new HashMap<>();
        if (bookId.length() == 0) {
            resMap.put(bookId, WRONG_FORMAT_OF_THE_REQUEST_BODY);
        }
        if (bookCollectionRepo.deleteById(bookId).getDeletedCount() == 1) {
            resMap.put(bookId, REMOVED);
        } else {
            resMap.put(bookId, ERROR_ON_REMOVE);
        }
        return resMap;
    }

    private BookDTO mapEntityToDto(BookEntity bookEntity) {
        return new BookDTO(bookEntity.getId(), bookEntity.getAuthor(), bookEntity.getBookName(), bookEntity.getDescription());
    }

    private BookEntity mapDtoToEntity(BookDTO bookDTO) {
        return new BookEntity(bookDTO.getId(), bookDTO.getAuthor(), bookDTO.getBookName(), bookDTO.getDescription());
    }
}