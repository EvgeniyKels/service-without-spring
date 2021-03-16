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

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private IBookService bookService;
    @Mock
    private BookCollectionRepo bookCollectionRepo;
    @BeforeEach
    void beforeEach() {
        bookService = new BookService(bookCollectionRepo);
    }
    @Test
    void insertBook() {
        when(bookCollectionRepo.insertNewBook(any(BookEntity.class))).thenReturn(UUID.randomUUID().toString());
        Map<String, String> response = bookService.insertBook(new BookDTO(null, TestConstant.AUTHOR, TestConstant.BOOK_NAME, TestConstant.DESCRIPTION));
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(BookServiceConstants.INSERTED, response.values().stream().findAny().orElse(""));
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

}
