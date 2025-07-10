/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resource;

/**
 *
 * @author Sineth HK
 */




import DAO.CustomerDAO;
import model.Customer;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private final CustomerDAO customerDAO = CustomerDAO.getInstance();

    @POST
    public Response createCustomer(Customer customer) {
        Customer savedCustomer = customerDAO.save(customer);
        return Response.status(Response.Status.CREATED).entity(savedCustomer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, Customer customer) {
        customer.setId(id);
        Customer updatedCustomer = customerDAO.update(customer);
        if (updatedCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedCustomer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        boolean deleted = customerDAO.delete(id);
        if (deleted) {
            return Response.noContent().build(); // 204 No Content on successful deletion
        }
        return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found if customer doesn't exist
    }

    @GET
    public Response getAllCustomers() {
        return Response.ok(customerDAO.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") Long id) {
        Customer customer = customerDAO.findById(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(customer).build();
    }
}