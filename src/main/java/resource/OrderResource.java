/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resource;

/**
 *
 * @author Sineth HK
 */



import DAO.BookDAO;
import DAO.CartDAO;
import DAO.CustomerDAO;
import DAO.OrderDAO;
import exception.CartNotFoundException;
import exception.CustomerNotFoundException;
import exception.InvalidInputException;
import exception.OrderNotFoundException;
import model.Cart;
import model.Order;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.Map;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private final OrderDAO orderDAO = new OrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final CartDAO cartDAO = new CartDAO();
    private final BookDAO bookDAO = new BookDAO();

    @POST
    public Response createOrderFromCart(@PathParam("customerId") Long customerId) {
        if (customerDAO.getById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        Cart cart = cartDAO.getByCustomerId(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new CartNotFoundException("Cart for customer ID " + customerId + " is empty or does not exist");
        }

        // Calculate total price and validate stock
        double totalPrice = 0.0;
        for (Map.Entry<Long, Integer> entry : cart.getItems().entrySet()) {
            Long bookId = entry.getKey();
            Integer quantity = entry.getValue();
            var book = bookDAO.getById(bookId);
            if (book == null) {
                throw new InvalidInputException("Book with ID " + bookId + " does not exist");
            }
            if (book.getStock() < quantity) {
                throw new InvalidInputException("Book with ID " + bookId + " is out of stock");
            }
            totalPrice += book.getPrice() * quantity;
            book.setStock(book.getStock() - quantity);
            bookDAO.update(bookId, book);
        }

        // Create order
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setItems(cart.getItems());
        order.setTotalPrice(totalPrice);
        order.setOrderDate(LocalDate.now().toString());
        Order savedOrder = orderDAO.create(order);

        // Clear the cart
        cart.getItems().clear();
        cartDAO.update(customerId, cart);

        return Response.status(Response.Status.CREATED).entity(savedOrder).build();
    }

    @GET
    public Response getOrders(@PathParam("customerId") Long customerId) {
        if (customerDAO.getById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        Map<Long, Order> customerOrders = orderDAO.getByCustomerId(customerId);
        if (customerOrders.isEmpty()) {
            throw new OrderNotFoundException("No orders found for customer ID " + customerId);
        }
        return Response.ok(customerOrders).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") Long customerId, @PathParam("orderId") Long orderId) {
        if (customerDAO.getById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        Order order = orderDAO.getById(orderId);
        if (order == null || !order.getCustomerId().equals(customerId)) {
            throw new OrderNotFoundException("Order with ID " + orderId + " does not exist for customer ID " + customerId);
        }
        return Response.ok(order).build();
    }

    @DELETE
    @Path("/{orderId}")
    public Response cancelOrder(@PathParam("customerId") Long customerId, @PathParam("orderId") Long orderId) {
        if (customerDAO.getById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        Order order = orderDAO.getById(orderId);
        if (order == null || !order.getCustomerId().equals(customerId)) {
            throw new OrderNotFoundException("Order with ID " + orderId + " does not exist for customer ID " + customerId);
        }
        orderDAO.delete(orderId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}