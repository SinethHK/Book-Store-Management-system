/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Sineth HK
 */



import model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class OrderDAO {
    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());
    private final Map<Long, Order> orders = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File storageFile = new File("C:/temp/orders.json");

    public OrderDAO() {
        loadFromFile();
    }

    public Order create(Order order) {
        Long id = idGenerator.incrementAndGet();
        order.setId(id);
        orders.put(id, order);
        saveToFile();
        return order;
    }

    public Order getById(Long orderId) {
        return orders.get(orderId);
    }

    public Map<Long, Order> getByCustomerId(Long customerId) {
        Map<Long, Order> customerOrders = new HashMap<>();
        for (Map.Entry<Long, Order> entry : orders.entrySet()) {
            if (entry.getValue().getCustomerId().equals(customerId)) {
                customerOrders.put(entry.getKey(), entry.getValue());
            }
        }
        return customerOrders;
    }

    public void update(Long orderId, Order order) {
        orders.put(orderId, order);
        saveToFile();
    }

    public void delete(Long orderId) {
        orders.remove(orderId);
        saveToFile();
    }

    private void saveToFile() {
        try {
            objectMapper.writeValue(storageFile, orders);
        } catch (IOException e) {
            LOGGER.severe("Failed to save orders to file: " + e.getMessage());
            throw new RuntimeException("Failed to save orders to file", e);
        }
    }

    private void loadFromFile() {
        if (storageFile.exists()) {
            try {
                Map<Long, Order> loadedOrders = objectMapper.readValue(storageFile, objectMapper.getTypeFactory().constructMapType(Map.class, Long.class, Order.class));
                orders.putAll(loadedOrders);
                if (!loadedOrders.isEmpty()) {
                    Long maxId = loadedOrders.keySet().stream().max(Long::compareTo).orElse(0L);
                    idGenerator.set(maxId);
                }
            } catch (IOException e) {
                LOGGER.severe("Failed to load orders from file: " + e.getMessage());
                throw new RuntimeException("Failed to load orders from file", e);
            }
        }
    }
}