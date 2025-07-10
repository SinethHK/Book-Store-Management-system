/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resource;

/**
 *
 * @author Sineth HK
 */





import DAO.AuthorDAO;
import model.Author;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private final AuthorDAO authorDAO = AuthorDAO.getInstance();

    @POST
    public Response createAuthor(Author author) {
        Author savedAuthor = authorDAO.save(author);
        return Response.status(Response.Status.CREATED).entity(savedAuthor).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") Long id, Author author) {
        author.setId(id);
        Author updatedAuthor = authorDAO.update(author);
        if (updatedAuthor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedAuthor).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") Long id) {
        boolean deleted = authorDAO.delete(id);
        if (deleted) {
            return Response.noContent().build(); // 204 No Content on successful deletion
        }
        return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found if author doesn't exist
    }

    @GET
    public Response getAllAuthors() {
        return Response.ok(authorDAO.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") Long id) {
        Author author = authorDAO.findById(id);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(author).build();
    }
}