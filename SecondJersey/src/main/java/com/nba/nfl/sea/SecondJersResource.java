package com.nba.nfl.sea;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


@Path("/second")
public class SecondJersResource {

	@GET 
    @Path("/msg")
    @Produces("text/plain")
    public String getIt() {
        return "Hi second jersey!";
    }
}
