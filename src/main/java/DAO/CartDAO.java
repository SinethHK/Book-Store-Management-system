/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Sineth HK
 */




import model.Cart;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CartDAO {
    private static final Logger LOGGER = Logger.getLogger(CartDAO.class.getName());
    private final Map<Long, Cart> carts = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File storageFile = new File("C:/temp/carts.json");

    public CartDAO() {
        loadFromFile();
    }

    public Cart getByCustomerId(Long customerId) {
        return carts.get(customerId);
    }

    public void update(Long customerId, Cart cart) {
        carts.put(customerId, cart);
        saveToFile();
    }

    private void saveToFile() {
        try {
            objectMapper.writeValue(storageFile, carts);
        } catch (IOException e) {
            LOGGER.severe("Failed to save carts to file: " + e.getMessage());
            throw new RuntimeException("Failed to save carts to file", e);
        }
    }

    private void loadFromFile() {
        if (storageFile.exists()) {
            try {
                Map<Long, Cart> loadedCarts = objectMapper.readValue(storageFile, objectMapper.getTypeFactory().constructMapType(Map.class, Long.class, Cart.class));
                carts.putAll(loadedCarts);
            } catch (IOException e) {
                LOGGER.severe("Failed to load carts from file: " + e.getMessage());
                throw new RuntimeException("Failed to load carts from file", e);
            }
        }
    }
}