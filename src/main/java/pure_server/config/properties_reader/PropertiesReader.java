package pure_server.config.properties_reader;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

public class PropertiesReader implements IPropertiesReader {
    @Override
    public Properties receiveInitialProperties() {

        Properties properties = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = new BufferedInputStream(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("server.properties")));
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        return properties;
    }
}
