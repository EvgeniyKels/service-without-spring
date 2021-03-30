package pure_server.config.constants;

public interface PropertiesConstant {
    String DB_PORT = "db_port";
    String DB_HOST = "db_host";
    String DB_USER_NAME = "db_user_name";
    String AUTH_DB_NAME = "auth_db_name";
    String MAIN_DB_NAME = "main_db_name";
    String PASS = "pass";
    String SERVER_PORT = "server_port";
    String JWK_SET_URL = "key_set.url";
    String ENV = "env";
    String INITIAL_INSERT = "initial_insert";
    String INITIAL_DATA = "initial_data";

    //env values
    String DEV_ENV = "dev";
    String TEST_ENV = "test";
    String PROD_ENV = "prod";
}

