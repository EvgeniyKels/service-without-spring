import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pure_server.dao.main.BookCollectionRepo;
import pure_server.model.entities.BookEntity;
import pure_server.service.BookService;
import pure_server.service.IBookService;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private IBookService bookService;
    @Mock
    private BookCollectionRepo bookCollectionRepo;
    @BeforeEach
    void beforeEach() {
        bookService = new BookService(bookCollectionRepo);
        when(bookCollectionRepo.insertNewBook(any(BookEntity.class))).thenReturn(UUID.randomUUID().toString());
    }
    @Test
    void insertBook() {

    }
}
