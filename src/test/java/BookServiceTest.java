import com.mongodb.client.result.DeleteResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pure_server.config.constants.BookServiceConstants;
import pure_server.dao.main.BookCollectionRepo;
import pure_server.model.dto.BookDTO;
import pure_server.model.entities.BookEntity;
import pure_server.service.BookService;
import pure_server.service.IBookService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pure_server.config.constants.BookServiceConstants.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final int NUM_OF_BOOKS_FOR_INSERT = 10;
    private IBookService bookService;
    private String testUUID;
    private BookDTO testBook;
    @Mock
    private BookCollectionRepo bookCollectionRepo;
    @BeforeEach
    void beforeEach() {
        bookService = new BookService(bookCollectionRepo);
        testUUID = UUID.randomUUID().toString();
        testBook = new BookDTO(null, TestConstant.AUTHOR, TestConstant.BOOK_NAME, TestConstant.DESCRIPTION);
    }

    @Test
    void insertBook() {
        when(bookCollectionRepo.insertNewBook(any(BookEntity.class))).thenReturn(testUUID);
        Map<String, String> response = bookService.insertBook(testBook);
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(INSERTED, response.values().stream().findAny().orElse(""));
    }
    @Test
    void insertBookWithNonNullId() {
        String fakeUUID = UUID.randomUUID().toString();
        Map<String, String> response = bookService.insertBook(new BookDTO(fakeUUID, TestConstant.AUTHOR, TestConstant.BOOK_NAME, TestConstant.DESCRIPTION));
        assertNotNull(response);
        assertEquals(1, response.size());
        assertTrue(response.containsKey(fakeUUID));
        assertEquals(BookServiceConstants.WRONG_FORMAT_OF_THE_REQUEST_BODY, response.get(fakeUUID));
    }
    @Test
    void insertManyBooks() {
        List<String>uuidList = new ArrayList<>();
        BookDTO[]bookDTOS = new BookDTO[NUM_OF_BOOKS_FOR_INSERT];
        for (int i = 0; i < NUM_OF_BOOKS_FOR_INSERT; i++) {
            bookDTOS[i] = new BookDTO(null, TestConstant.AUTHOR, TestConstant.BOOK_NAME, TestConstant.DESCRIPTION);
            uuidList.add(UUID.randomUUID().toString());
        }
        when(bookCollectionRepo.insertManyNewBooks(anyList())).thenReturn(uuidList);

        Map<String, String> resultMap = bookService.insertManyBooks(List.of(bookDTOS));
        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            assertNotNull(entry.getKey());
            assertTrue(uuidList.contains(entry.getKey()));
            assertEquals(entry.getValue(), INSERTED);
        }
    }
    @Test
    void insertManyWhenOneBookIdEqualsNull() {
        BookDTO[]bookDTOS = new BookDTO[NUM_OF_BOOKS_FOR_INSERT];
        for (int i = 0; i < NUM_OF_BOOKS_FOR_INSERT; i++) {
            bookDTOS[i] = new BookDTO(i == 0 ? UUID.randomUUID().toString() : null, TestConstant.AUTHOR, TestConstant.BOOK_NAME, TestConstant.DESCRIPTION);
        }
        Map<String, String> resultMap = bookService.insertManyBooks(List.of(bookDTOS));
        assertEquals(1, resultMap.size());
        assertNotNull(resultMap.keySet().stream().findAny().orElse(null));
        assertEquals(resultMap.values().stream().findAny().orElse(null), WRONG_FORMAT_OF_THE_REQUEST_BODY);
    }

    @Test
    void getBookById() {
        BookEntity bookEntity = new BookEntity(null, testBook.getAuthor(), testBook.getBookName(), testBook.getDescription());
        when(bookCollectionRepo.getBookById(testUUID)).thenReturn(Optional.of(bookEntity));
        BookDTO bookById = bookService.getBookById(testUUID);
        assertEquals(testBook, bookById);
    }

    @Test
    void getBookByEmptyId() {
        BookDTO bookById = bookService.getBookById("");
        assertNull(bookById);
    }

    @Test
    void getAllBooks() {
        List<BookEntity>bookEntities = new ArrayList<>();
        for (int i = 0; i < NUM_OF_BOOKS_FOR_INSERT; i++) {
            bookEntities.add(new BookEntity(null, TestConstant.AUTHOR, TestConstant.BOOK_NAME, TestConstant.DESCRIPTION));
        }
        when(bookCollectionRepo.findAll()).thenReturn(bookEntities);
        assertEquals(NUM_OF_BOOKS_FOR_INSERT, bookService.getAllBooks().size());
    }

    @Test
    void removeBookByIdNotSucessfully() {
        when(bookCollectionRepo.deleteById(anyString())).thenReturn(new DeleteResult() {
            @Override
            public boolean wasAcknowledged() {
                return false;
            }

            @Override
            public long getDeletedCount() {
                return 0;
            }
        });
        Map<String, String> stringStringMap = bookService.removeBookById(testUUID);
        assertEquals(1, stringStringMap.size());
        String key = stringStringMap.keySet().stream().findAny().orElse(null);
        assertEquals(testUUID, key);

        assertEquals(ERROR_ON_REMOVE, stringStringMap.values().stream().findAny().orElse(null));
    }

    @Test
    void removeBookByIdSucces() {
        when(bookCollectionRepo.deleteById(anyString())).thenReturn(new DeleteResult() {
            @Override
            public boolean wasAcknowledged() {
                return false;
            }

            @Override
            public long getDeletedCount() {
                return 1;
            }
        });
        Map<String, String> resultMap = bookService.removeBookById(testUUID);
        assertEquals(1, resultMap.size());
        String key = resultMap.keySet().stream().findAny().orElse(null);
        assertEquals(testUUID, key);

        assertEquals(REMOVED, resultMap.values().stream().findAny().orElse(null));
    }

    @Test
    void removeByEmptyId() {
        Map<String, String> resultMap = bookService.removeBookById("");
        assertEquals(1, resultMap.size());
        String key = resultMap.keySet().stream().findAny().orElse(null);
        assertEquals("", key);

        assertEquals(WRONG_FORMAT_OF_THE_REQUEST_BODY, resultMap.values().stream().findAny().orElse(null));
    }

    @Test
    void updateNotExistingBook() {
        when(bookCollectionRepo.existById(anyString())).thenReturn(false);
        BookDTO book = new BookDTO(UUID.randomUUID().toString(), "", "", "");
        Map<BookDTO, String> bookDTOStringMap = bookService.updateBook(book);
        assertEquals(1, bookDTOStringMap.size());
        assertEquals(book, bookDTOStringMap.keySet().stream().findAny().orElse(null));
        assertEquals(BOOK_WITH_GIVEN_ID_NOT_EXISTS, bookDTOStringMap.get(book));
    }

    @Test
    void updateIfRepoReturnEmptyOptional() {
        when(bookCollectionRepo.existById(anyString())).thenReturn(true);
        when(bookCollectionRepo.updateBook(any(BookEntity.class))).thenReturn(Optional.empty());
        BookDTO book = new BookDTO(UUID.randomUUID().toString(), "", "", "");
        Map<BookDTO, String> bookDTOStringMap = bookService.updateBook(book);
        assertEquals(1, bookDTOStringMap.size());
        assertEquals(book, bookDTOStringMap.keySet().stream().findAny().orElse(null));
        assertEquals(ERROR_ON_UPDATE, bookDTOStringMap.get(book));
    }

    @Test
    void successfulUpdate() {
        when(bookCollectionRepo.existById(anyString())).thenReturn(true);
        BookDTO book = new BookDTO(UUID.randomUUID().toString(), "", "", "");
        when(bookCollectionRepo.updateBook(any(BookEntity.class))).thenReturn(
                Optional.of(
                        new BookEntity(
                                book.getId(), book.getAuthor(), book.getBookName(), book.getDescription())));
        Map<BookDTO, String> bookDTOStringMap = bookService.updateBook(book);
        assertEquals(1, bookDTOStringMap.size());
        assertEquals(book, bookDTOStringMap.keySet().stream().findAny().orElse(null));
        assertEquals(UPDATED, bookDTOStringMap.get(book));
    }
}
