/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Sineth HK
 */





import model.Author;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorDAO {
    private static AuthorDAO instance;
    private final Map<Long, Author> authors = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File storageFile = new File("authors.json");

    public AuthorDAO() {
        // Load existing authors from file when the DAO is created
        loadFromFile();
        // If no data exists, initialize with sample data
        if (authors.isEmpty()) {
            Author author = new Author(idGenerator.incrementAndGet(), "J.K. Rowling", "Author of Harry Potter series");
            authors.put(author.getId(), author);
            saveToFile(); // Save the initial data to file
        }
    }

    public static AuthorDAO getInstance() {
        if (instance == null) {
            synchronized (AuthorDAO.class) {
                if (instance == null) {
                    instance = new AuthorDAO();
                }
            }
        }
        return instance;
    }

    public Author save(Author author) {
        Long id = idGenerator.incrementAndGet();
        author.setId(id);
        authors.put(id, author);
        saveToFile(); // Save to file after adding
        return author;
    }

    public Author findById(Long id) {
        return authors.get(id);
    }

    public Map<Long, Author> findAll() {
        return new HashMap<>(authors);
    }

    public Author update(Author author) {
        if (authors.containsKey(author.getId())) {
            authors.put(author.getId(), author);
            saveToFile(); // Save to file after updating
            return author;
        }
        return null;
    }

    public boolean delete(Long id) {
        if (authors.containsKey(id)) {
            authors.remove(id);
            saveToFile(); // Save to file after deleting
            return true;
        }
        return false;
    }

    private void saveToFile() {
        try {
            objectMapper.writeValue(storageFile, authors);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save authors to file", e);
        }
    }

    private void loadFromFile() {
        if (storageFile.exists()) {
            try {
                Map<Long, Author> loadedAuthors = objectMapper.readValue(storageFile, objectMapper.getTypeFactory().constructMapType(Map.class, Long.class, Author.class));
                authors.putAll(loadedAuthors);
                if (!loadedAuthors.isEmpty()) {
                    Long maxId = loadedAuthors.keySet().stream().max(Long::compareTo).orElse(0L);
                    idGenerator.set(maxId);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load authors from file", e);
            }
        }
    }

    public Object getById(Long authorId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}