/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Sineth HK
 */



import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Long customerId;
    private Map<Long, Integer> items;

    public Cart() {
        this.items = new HashMap<>();
    }

    public Cart(Long customerId) {
        this.customerId = customerId;
        this.items = new HashMap<>();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Map<Long, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Long, Integer> items) {
        this.items = items;
    }
}