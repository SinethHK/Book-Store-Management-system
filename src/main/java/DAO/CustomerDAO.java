/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Sineth HK
 */




import model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerDAO {
    private static CustomerDAO instance;
    private final Map<Long, Customer> customers = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File storageFile = new File("customers.json");

    public CustomerDAO() {
        // Load existing customers from file when the DAO is created
        loadFromFile();
        // If no data exists, initialize with sample data
        if (customers.isEmpty()) {
            Customer customer = new Customer(idGenerator.incrementAndGet(), "John Doe", "john.doe@example.com", "password123");
            customers.put(customer.getId(), customer);
            saveToFile(); // Save the initial data to file
        }
    }

    public static CustomerDAO getInstance() {
        if (instance == null) {
            synchronized (CustomerDAO.class) {
                if (instance == null) {
                    instance = new CustomerDAO();
                }
            }
        }
        return instance;
    }

    public Customer save(Customer customer) {
        Long id = idGenerator.incrementAndGet();
        customer.setId(id);
        customers.put(id, customer);
        saveToFile(); // Save to file after adding
        return customer;
    }

    public Customer findById(Long id) {
        return customers.get(id);
    }

    public Map<Long, Customer> findAll() {
        return new HashMap<>(customers);
    }

    public Customer update(Customer customer) {
        if (customers.containsKey(customer.getId())) {
            customers.put(customer.getId(), customer);
            saveToFile(); // Save to file after updating
            return customer;
        }
        return null;
    }

    public boolean delete(Long id) {
        if (customers.containsKey(id)) {
            customers.remove(id);
            saveToFile(); // Save to file after deleting
            return true;
        }
        return false;
    }

    private void saveToFile() {
        try {
            objectMapper.writeValue(storageFile, customers);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save customers to file", e);
        }
    }

    private void loadFromFile() {
        if (storageFile.exists()) {
            try {
                Map<Long, Customer> loadedCustomers = objectMapper.readValue(storageFile, objectMapper.getTypeFactory().constructMapType(Map.class, Long.class, Customer.class));
                customers.putAll(loadedCustomers);
                if (!loadedCustomers.isEmpty()) {
                    Long maxId = loadedCustomers.keySet().stream().max(Long::compareTo).orElse(0L);
                    idGenerator.set(maxId);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load customers from file", e);
            }
        }
    }

    public Object getById(Long customerId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}