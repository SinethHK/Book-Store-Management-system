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

public class Order {
    private Long id;
    private Long customerId;
    private Map<Long, Integer> items;
    private double totalPrice;
    private String orderDate;

    public Order() {
        this.items = new HashMap<>();
    }

    public Order(Long id, Long customerId, Map<Long, Integer> items, double totalPrice, String orderDate) {
        this.id = id;
        this.customerId = customerId;
        this.items = items != null ? items : new HashMap<>();
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}