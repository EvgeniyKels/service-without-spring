package pure_server.http_controller.http_contexts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pure_server.http_controller.http_controller_utils.HttpUtils;
import pure_server.http_controller.http_controller_utils.ParseUtils;
import pure_server.model.dto.BookDTO;
import pure_server.model.dto.responses.BookResponseDto;
import pure_server.service.IBookService;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static pure_server.config.constants.BookServiceConstants.NON_SUCCESS;
import static pure_server.config.constants.BookServiceConstants.SUCCESS;
import static pure_server.config.constants.HttpMethods.*;

public class HttpContextHolder {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getSimpleName());
    private final IBookService bookService;
    private final ParseUtils parseUtils;
    private final HttpUtils httpUtils;
    private final ObjectMapper objectMapper;

    public HttpContextHolder(IBookService bookService, ParseUtils parseUtils, HttpUtils httpUtils, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.parseUtils = parseUtils;
        this.httpUtils = httpUtils;
        this.objectMapper = objectMapper;
    }

    public HttpHandler getBookHandler() {
        return exchange -> {
            String requestMethod = exchange.getRequestMethod();
            Map<String, String> params = parseUtils.splitQuery(exchange.getRequestURI().getRawQuery());
            BookResponseDto.BookResponseBuilder bookResponseBuilder = new BookResponseDto.BookResponseBuilder().addTimeStamp(Instant.now().toEpochMilli());

            LOGGER.info("received \n" +
                    requestMethod + " " + exchange.getProtocol()
                    + " " + exchange.getRequestURI() + "\n" + exchange.getRequestHeaders().entrySet() +
                    "\n" + "\nrequest body length " + exchange.getRequestBody().available());

            switch (requestMethod) {
                case GET: {
                    BookDTO result = bookService.getBookById(params.get("id"));
                    if (result != null) {
                        bookResponseBuilder.addPayload(objectMapper.writeValueAsString(result)).addTextResult(SUCCESS);
                    } else {
                        bookResponseBuilder.addTextResult(NON_SUCCESS);
                    }
                    break;
                }
                case POST: {
                    BookDTO parsedBookFromBodyRequest = parseUtils.parseObjectFromRequestBody(exchange, BookDTO.class);
                    if (parsedBookFromBodyRequest == null) {
                        httpUtils.handleNonSuccess(400, exchange);
                    } else {
                        httpUtils.addSuccessStringToResponse(bookResponseBuilder, bookService.insertBook(parsedBookFromBodyRequest));
                    }
                    break;
                }
                case DELETE: {
                    httpUtils.addSuccessStringToResponse(bookResponseBuilder, bookService.removeBookById(params.get("id")));
                    break;
                }
                case PUT:
                case PATCH: {
                    BookDTO parsedBookFromBodyRequest = parseUtils.parseObjectFromRequestBody(exchange, BookDTO.class);
                    if (parsedBookFromBodyRequest == null) {
                        httpUtils.handleNonSuccess(400, exchange);
                    } else {
                        httpUtils.addSuccessStringToResponse(bookResponseBuilder, bookService.updateBook(parsedBookFromBodyRequest));
                    }
                    break;
                }
                default:
                    httpUtils.handleNonSuccess(405, exchange);
                    bookResponseBuilder = bookResponseBuilder.addTextResult(NON_SUCCESS);
                    break;
            }
            prepareBookResponse(exchange, bookResponseBuilder);
        };
    }

    public HttpHandler getBooksHandler() {
        return exchange -> {
            BookResponseDto.BookResponseBuilder bookResponseBuilder = new BookResponseDto.BookResponseBuilder().addTimeStamp(Instant.now().toEpochMilli());
            if (exchange.getRequestMethod().equals(GET)) {
                List<BookDTO> allBooks = bookService.getAllBooks();
                bookResponseBuilder = bookResponseBuilder.addTextResult(SUCCESS).addPayload(objectMapper.writeValueAsString(allBooks));
            } else {
                httpUtils.handleNonSuccess(405, exchange);
            }
            prepareBookResponse(exchange, bookResponseBuilder);
        };
    }

    private void prepareBookResponse(HttpExchange exchange, BookResponseDto.BookResponseBuilder bookResponseBuilder) throws IOException {
        BookResponseDto bookResponseDto = bookResponseBuilder.build();
        String resultJSON = objectMapper.writeValueAsString(bookResponseDto);
        if(bookResponseDto.getTextResult().equals(SUCCESS)) {
            httpUtils.handleSuccess(exchange, resultJSON);
        } else {
            httpUtils.handleNonSuccess(400, exchange);
        }
        OutputStream responseBody = exchange.getResponseBody();
        byte[] bytes = resultJSON.getBytes();
        try {
            responseBody.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("processed \n" +
                exchange.getResponseCode() + " " + exchange.getResponseHeaders()
                + " " + exchange.getResponseBody() + "\n");
        responseBody.flush();
        exchange.close();
    }
}
