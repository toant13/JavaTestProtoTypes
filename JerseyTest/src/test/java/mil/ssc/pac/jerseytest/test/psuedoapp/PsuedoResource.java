package mil.ssc.pac.jerseytest.test.psuedoapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/second")
public class PsuedoResource {
	@GET 
    @Path("/msg")
    @Produces("text/plain")
    public String getIt() {
        return "Hi second jersey!";
    }
}
