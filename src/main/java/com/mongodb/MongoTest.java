
package com.mongodb;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
 

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.ApiServices.authorization.jwt.filter.JWTTokenNeeded;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

 


@Path("/MongoClient")
@Api(value = "MongoClient")
public class MongoTest implements Serializable {

	 
	private static final long serialVersionUID = 1L;

	/** The client. */

	@Inject
	MongoClient mongoClient;
	
	@GET
	
	@Produces("application/json,application/xml")
	@ApiOperation(value = "MongoClient sss", notes = "Logic ") // Specific
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successful operation"), // response details should be here to give repose type and example in swagger
			@ApiResponse(code = 400, message = "Bad Request / Invalid parameters supplied"), @ApiResponse(code = 404, message = "Not found"),
			@ApiResponse(code = 500, message = "Internal server error") })

	public Response getData() {
		 
		 
		MongoDatabase db = mongoClient.getDatabase("testDB");
		MongoCollection<Document> collection = db.getCollection("Users");
		 
		List<Document> documents = (List<Document>) collection.find().into(new ArrayList<Document>());
 
               for(Document document : documents){
                   System.out.println(document);
               }
               
		
	 
	  
		return Response.ok(documents).build();
	}

	 
	 

}
