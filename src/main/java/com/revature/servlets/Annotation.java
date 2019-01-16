package com.revature.servlets;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/test")
public class Annotation {
	
	@GET
	public Response get() {
		String output = "Annotation get working";
		return Response.status(200).entity(output).build();
	}

}
