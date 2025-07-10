/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 *
 * @author Sineth HK
 */


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", exception.getClass().getSimpleName());
        errorResponse.put("message", exception.getMessage());

        int status;
        if (exception instanceof BookNotFoundException ||
            exception instanceof AuthorNotFoundException ||
            exception instanceof CustomerNotFoundException ||
            exception instanceof CartNotFoundException) {
            status = 404;
        } else if (exception instanceof InvalidInputException ||
                   exception instanceof OutOfStockException) {
            status = 400;
        } else {
            status = 500;
        }

        return Response.status(status)
                       .entity(errorResponse)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}