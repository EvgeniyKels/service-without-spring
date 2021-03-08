import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import pure_server.authentification.ServerAuthentication;
import pure_server.model.entities.User;
import pure_server.model.repo.UserCollectionRepo;

@ExtendWith(MockitoExtension.class)
public class ServerAuthenticationTest {
    private static final String CORRECT_USER_NAME = "user";
    private static final String CORRECT_PASS = "correct_pass";
    private static final String WRONG_PASS = "wrong_pass";
    private static final String WRONG_USER_NAME = "wrong_user_name";
    private final String REALM = "realm";
    private ServerAuthentication serverAuthentication;

    @Mock
    private UserCollectionRepo userCollectionRepo;

    @BeforeEach
    void beforeEach() {
        serverAuthentication = new ServerAuthentication(REALM, userCollectionRepo);
        assertNotNull(userCollectionRepo);
    }

    @Test
    void test_01_correct_credentials() {
        when(userCollectionRepo.getUserByName(CORRECT_USER_NAME)).thenReturn(getExistingUser());
        assertTrue(serverAuthentication.checkCredentials(CORRECT_USER_NAME, CORRECT_PASS));
    }

    private User getExistingUser() {
        return new User("user_id", CORRECT_USER_NAME, CORRECT_PASS);
    }

    @Test
    void test_02_wrong_pass() {
        when(userCollectionRepo.getUserByName(CORRECT_USER_NAME)).thenReturn(getExistingUser());
        assertFalse(serverAuthentication.checkCredentials(CORRECT_USER_NAME, WRONG_PASS));
    }

    @Test
    void test_03_wrong_login() {
        when(userCollectionRepo.getUserByName(WRONG_USER_NAME)).thenReturn(null);
        assertFalse(serverAuthentication.checkCredentials(WRONG_USER_NAME, CORRECT_PASS));
    }
}
