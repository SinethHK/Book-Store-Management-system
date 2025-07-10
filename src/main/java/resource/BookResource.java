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
import model.Book;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private final BookDAO bookDAO = BookDAO.getInstance();

    @POST
    public Response createBook(Book book) {
        Book savedBook = bookDAO.save(book);
        return Response.status(Response.Status.CREATED).entity(savedBook).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Long id, Book book) {
        book.setId(id);
        Book updatedBook = bookDAO.update(book);
        if (updatedBook == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedBook).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        boolean deleted = bookDAO.delete(id);
        if (deleted) {
            return Response.noContent().build(); // 204 No Content on successful deletion
        }
        return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found if book doesn't exist
    }

    @GET
    public Response getAllBooks() {
        return Response.ok(bookDAO.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") Long id) {
        Book book = bookDAO.findById(id);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }
}