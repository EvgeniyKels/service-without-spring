package pure_server.service;

import pure_server.model.dto.Book;

import java.util.List;

public class BookService implements IBookService {
    @Override
    public boolean insertBook(Book book) {
        return false;
    }

    @Override
    public boolean updateBook(Book book) {
        return false;
    }

    @Override
    public List<Book> getAllBooks() {
        return null;
    }

    @Override
    public Book getBookById(int bookId) {
        return null;
    }

    @Override
    public boolean removeBook(int name) {
        return false;
    }

    @Override
    public boolean replaceBook(Book book) {
        return false;
    }
}