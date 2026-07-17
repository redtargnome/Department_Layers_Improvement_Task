package codefinity.rest;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.NoSuchElementException;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        int status = status(exception);
        String message = exception.getMessage() == null ? "Request failed" : exception.getMessage();
        return Response.status(status).entity(Map.of("status", status, "message", message)).build();
    }

    private int status(Exception exception) {
        if (exception instanceof WebApplicationException webException) {
            return webException.getResponse().getStatus();
        }
        if (exception instanceof NoSuchElementException || exception instanceof NotFoundException) return 404;
        if (exception instanceof IllegalArgumentException || exception instanceof NullPointerException
                || exception instanceof DateTimeParseException || exception instanceof BadRequestException) return 400;
        return 500;
    }
}
