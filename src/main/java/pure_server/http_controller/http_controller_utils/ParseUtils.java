package pure_server.http_controller.http_controller_utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParseUtils {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getSimpleName());
    private final ObjectMapper objectMapper;
    public ParseUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T parseObjectFromRequestBody(HttpExchange exchange, Class<T> clazz) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        int i;

        List<Character> characters = new ArrayList<>();
        while ((i = requestBody.read()) != -1) {
            characters.add((char) i);
        }
        char[] chars = new char[characters.size()];
        for (int j = 0; j < chars.length; j++) {
            chars[j] = characters.get(j);
        }
        String resultJson = new String(chars);
        T object = null;
        try {
            object = objectMapper.readValue(resultJson, clazz);
        } catch (Exception exception) {
            LOGGER.info(exception.getLocalizedMessage());
        }
        return object;
    }
    public Map<String, String> splitQuery(String rawQuery) {
        if (rawQuery == null || rawQuery.equals("")) {
            return Collections.emptyMap();
        }
        System.out.println(rawQuery);

        return Pattern.compile("&").splitAsStream(rawQuery)
                .map(x -> x.split("=")).collect(Collectors.toMap(x -> x[0], x -> x[1]));
    }
}