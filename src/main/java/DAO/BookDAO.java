/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Sineth HK
 */


import model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class BookDAO {
    private static BookDAO instance;
    private final Map<Long, Book> books = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File storageFile = new File("books.json");

    public BookDAO() {
        // Load existing books from file when the DAO is created
        loadFromFile();
        // If no data exists, initialize with sample data
        if (books.isEmpty()) {
            Book book = new Book(idGenerator.incrementAndGet(), "The Lord of the Rings", 1L, "978-0-618-05326-7", 1954, 20.99, 100);
            books.put(book.getId(), book);
            saveToFile(); // Save the initial data to file
        }
    }

    public static BookDAO getInstance() {
        if (instance == null) {
            synchronized (BookDAO.class) {
                if (instance == null) {
                    instance = new BookDAO();
                }
            }
        }
        return instance;
    }

    public Book save(Book book) {
        Long id = idGenerator.incrementAndGet();
        book.setId(id);
        books.put(id, book);
        saveToFile(); // Save to file after adding
        return book;
    }

    public Book findById(Long id) {
        return books.get(id);
    }

    public Map<Long, Book> findAll() {
        return new HashMap<>(books);
    }

    public Book update(Book book) {
        if (books.containsKey(book.getId())) {
            books.put(book.getId(), book);
            saveToFile(); // Save to file after updating
            return book;
        }
        return null;
    }

    public boolean delete(Long id) {
        if (books.containsKey(id)) {
            books.remove(id);
            saveToFile(); // Save to file after deleting
            return true;
        }
        return false;
    }

    private void saveToFile() {
        try {
            objectMapper.writeValue(storageFile, books);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save books to file", e);
        }
    }

    private void loadFromFile() {
        if (storageFile.exists()) {
            try {
                Map<Long, Book> loadedBooks = objectMapper.readValue(storageFile, objectMapper.getTypeFactory().constructMapType(Map.class, Long.class, Book.class));
                books.putAll(loadedBooks);
                if (!loadedBooks.isEmpty()) {
                    Long maxId = loadedBooks.keySet().stream().max(Long::compareTo).orElse(0L);
                    idGenerator.set(maxId);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load books from file", e);
            }
        }
    }

    public Object getById(Long bookId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}