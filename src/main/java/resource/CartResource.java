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
import exception.BookNotFoundException;
import exception.CartNotFoundException;
import exception.CustomerNotFoundException;
import exception.InvalidInputException;
import exception.OutOfStockException;
import model.Cart;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private final CartDAO cartDAO = new CartDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final BookDAO bookDAO = new BookDAO();

    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") Long customerId, Map<String, Object> item) {
        if (customerDAO.getById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        Long bookId = Long.valueOf(item.get("bookId").toString());
        Integer quantity = Integer.valueOf(item.get("quantity").toString());
        if (bookId == null || quantity == null || quantity <= 0) {
            throw new InvalidInputException("Valid book ID and positive quantity are required");
        }
        if (bookDAO.getById(bookId) == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist");
        }
        if (bookDAO.getById(bookId).getStock() < quantity) {
            throw new OutOfStockException("Book with ID " + bookId + " is out of stock");
        }
        Cart cart = cartDAO.getByCustomerId(customerId);
        if (cart == null) {
            cart = new Cart(customerId);
        }
        cart.getItems().put(bookId, cart.getItems().getOrDefault(bookId, 0) + quantity);
        cartDAO.update(customerId, cart);
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Response getCart(@PathParam("customerId") Long customerId) {
        if (customerDAO.getById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        Cart cart = cartDAO.getByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer ID " + customerId + " does not exist");
        }
        return Response.ok(cart).build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateCartItem(@PathParam("customerId") Long customerId, @PathParam("bookId") Long bookId, Map<String, Integer> update) {
        if (customerDAO.getById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        if (bookDAO.getById(bookId) == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist");
        }
        Integer quantity = update.get("quantity");
        if (quantity == null || quantity < 0) {
            throw new InvalidInputException("Valid non-negative quantity is required");
        }
        Cart cart = cartDAO.getByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer ID " + customerId + " does not exist");
        }
        if (quantity == 0) {
            cart.getItems().remove(bookId);
        } else {
            if (bookDAO.getById(bookId).getStock() < quantity) {
                throw new OutOfStockException("Book with ID " + bookId + " is out of stock");
            }
            cart.getItems().put(bookId, quantity);
        }
        cartDAO.update(customerId, cart);
        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeItemFromCart(@PathParam("customerId") Long customerId, @PathParam("bookId") Long bookId) {
        if (customerDAO.getById(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist");
        }
        Cart cart = cartDAO.getByCustomerId(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer ID " + customerId + " does not exist");
        }
        if (!cart.getItems().containsKey(bookId)) {
            throw new BookNotFoundException("Book with ID " + bookId + " is not in the cart");
        }
        cart.getItems().remove(bookId);
        cartDAO.update(customerId, cart);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}