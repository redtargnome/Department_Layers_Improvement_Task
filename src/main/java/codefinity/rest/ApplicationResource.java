package codefinity.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class ApplicationResource {
    @GET
    public Map<String, String> index() {
        return Map.of("status", "running", "application", "Department Layers Improvement Task");
    }
}
