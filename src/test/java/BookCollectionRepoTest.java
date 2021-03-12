import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pure_server.config.context.AppContext;
import pure_server.dao.main.BookCollectionRepo;
import pure_server.dao.main.BookCollectionRepoImpl;
import pure_server.model.entities.BookEntity;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static pure_server.config.constants.RepositoriesConstants.*;

public class BookCollectionRepoTest {
    private static final String AUTHOR = "author";
    private static final String BOOK_NAME = "its a book";
    private static final String DESCRIPTION = "description";
    private BookCollectionRepo bookCollectionRepo;
    private MongoCollection<Document> bookCollection;
    private ObjectMapper om;

    @BeforeEach
    void beforeEach() {
        AppContext appContext = AppContext.getInstance();
        om = appContext.getOm();
        bookCollection = appContext.getMainDb().getCollection(BOOK_COLLECTION_NAME);
        bookCollectionRepo = new BookCollectionRepoImpl(appContext.getMainDb(), om);
        bookCollection.drop();
    }

    @Test
    void insertBook() throws JsonProcessingException {
        BookEntity bookForInsertion = createBook(null, AUTHOR, BOOK_NAME, DESCRIPTION);
        testBook(bookForInsertion);
    }

    @Test
    void insertBookWithPredefinedId() throws JsonProcessingException {
        BookEntity bookForInsertion = createBook(UUID.randomUUID().toString(), AUTHOR, BOOK_NAME, DESCRIPTION);
        testBook(bookForInsertion);
    }

//    @Test
    void insertBookWithEmptyField() throws JsonProcessingException {
        BookEntity bookForInsertion = createBook(null, null, "", ""); //TODO POJO validation
        testBook(bookForInsertion);
    }

    @Test
    void insertNullInsteadOfBook() {
        BookEntity bookForInsertion = createBook(null, AUTHOR, BOOK_NAME, DESCRIPTION);
        assertThrows(NullPointerException.class, () -> bookCollectionRepo.insertNewBook(null));
    }

    @Test
    void insertBookExistsInDb() {
        BookEntity bookForInsertion = createBook(null, AUTHOR, BOOK_NAME, DESCRIPTION);
        String uuid = bookCollectionRepo.insertNewBook(bookForInsertion);
        BookEntity equalBookForInsertion = createBook(uuid, AUTHOR, BOOK_NAME, DESCRIPTION);
        assertEquals(WRONG_FORMAT_OF_THE_GIVEN_DTO, bookCollectionRepo.insertNewBook(equalBookForInsertion));
    }

    @Test
    void getBookById() {
        BookEntity bookForInsertion = createBook(null, AUTHOR, BOOK_NAME, DESCRIPTION);
        String uuid = bookCollectionRepo.insertNewBook(bookForInsertion);
        Optional<BookEntity> bookOpt = bookCollectionRepo.getBookById(uuid);
        assertTrue(bookOpt.isPresent());
        assertEquals(bookForInsertion, bookOpt.get());
    }

    @Test
    void getBookByNull() {
        BookEntity bookForInsertion = createBook(null, AUTHOR, BOOK_NAME, DESCRIPTION);
        bookCollectionRepo.insertNewBook(bookForInsertion);
        assertThrows(NullPointerException.class, () -> bookCollectionRepo.getBookById(null));
    }

    @Test
    void getBookByNotExistingUUID() {
        BookEntity bookForInsertion = createBook(null, AUTHOR, BOOK_NAME, DESCRIPTION);
        bookCollectionRepo.insertNewBook(bookForInsertion);
        Optional<BookEntity> bookOpt = bookCollectionRepo.getBookById(UUID.randomUUID().toString());
        assertFalse(bookOpt.isPresent());
    }

    @Test
    void isBookExistsByUUID() {
        BookEntity bookForInsertion = createBook(null, AUTHOR, BOOK_NAME, DESCRIPTION);
        String uuid = bookCollectionRepo.insertNewBook(bookForInsertion);
        assertTrue(bookCollectionRepo.existById(uuid));
    }

    @Test
    void idBookExistsByWrongUUID() {
        BookEntity bookForInsertion = createBook(null, AUTHOR, BOOK_NAME, DESCRIPTION);
        bookCollectionRepo.insertNewBook(bookForInsertion);
        assertFalse(bookCollectionRepo.existById(UUID.randomUUID().toString()));
    }

    private void testBook(BookEntity bookForInsertion) throws JsonProcessingException {
        assertNull(bookForInsertion.get_id());
        String uuid = bookCollectionRepo.insertNewBook(bookForInsertion);
        assertNotNull(uuid);
        assertEquals(uuid.length(), UUID.randomUUID().toString().length());
        BookEntity bookEntity = om.readValue(Objects.requireNonNull(bookCollection.find(Filters.eq(ID_COLUMN_NAME, uuid)).first()).toJson(), BookEntity.class);
        assertNull(bookEntity.get_id());
        assertNotNull(bookForInsertion.getId());
        assertEquals(bookForInsertion, bookEntity);
    }

    private BookEntity createBook(String id, String author, String bookName, String description) {
        return new BookEntity(id, author, bookName, description);
    }


}
