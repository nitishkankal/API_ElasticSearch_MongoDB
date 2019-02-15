package com.ApiServices.authorization.jwt.rest;


import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

//import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.ApiServices.authorization.jwt.filter.JWTTokenNeeded;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**

* @author nitish.k
 *         --
 */
@Path("/echo")
@Produces(TEXT_PLAIN)
@Api(value = "EchoEndpoint Test API and JWT")
public class EchoEndpoint {

    // ======================================
    // =          Injection Points          =
    // ======================================

  //  @Inject
   // private Logger logger;

    // ======================================
    // =          Business methods          =
    // ======================================

    @GET
    public Response echo(@QueryParam("message") String message) {
        return Response.ok().entity(message == null ? "no message" : message).build();
    }

    @GET
    @Path("jwt")
	 @ApiResponses(value = {
	    		@ApiResponse(code = 200, message = "successful operation"),
	    		 @ApiResponse(code = 400, message = "Bad Request / Invalid parameters supplied"),
	    		 @ApiResponse(code = 401, message = "Unauthorized access - plases use valid token / Token expired- generate new one "),  
	    		 @ApiResponse(code = 404, message = "Not found"),  
	    		@ApiResponse(code = 500, message = "Internal server error")  
	    })
    @ApiOperation(value = "Show message", notes = "return back message if JWT token is valid")
    @JWTTokenNeeded
    public Response echoWithJWTToken(@QueryParam("message") String message) {
    	
        return Response.ok().entity(message == null ? "no message" : message).build();
    }
}
