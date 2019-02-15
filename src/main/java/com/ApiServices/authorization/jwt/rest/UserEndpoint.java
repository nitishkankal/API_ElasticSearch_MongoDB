package com.ApiServices.authorization.jwt.rest;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.ApiServices.authorization.jwt.domain.Customer_User_T;
import com.ApiServices.authorization.jwt.util.KeyGenerator;
import com.ApiServices.authorization.jwt.util.SimpleKeyGenerator;
import com.util.db.GetConnection2;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author nitish.k --
 */
@Path("/users")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(value = "Login to Rest API")
public class UserEndpoint {

 
	@Context
	private UriInfo uriInfo;

 
	private KeyGenerator keyGenerator;
	GetConnection2 gconn2 = new GetConnection2();
	@POST
	@Path("/login")

	@Consumes(APPLICATION_FORM_URLENCODED)
	@Produces("application/json,application/xml")
	 @ApiResponses(value = {
	    		@ApiResponse(code = 200, message = "successful operation"),
	    		 @ApiResponse(code = 400, message = "Bad Request / Invalid parameters supplied"),
	    		 @ApiResponse(code = 404, message = "Not found"),  
	    		@ApiResponse(code = 500, message = "Internal server error")  
	    })
	@ApiOperation(value = "Login", notes = "Login for acccess APIs ")
    //@Consumes("application/json")
	public Response authenticateUser(@FormParam("UserName") String UserName, @FormParam("Password") String Password) {
	//public Response authenticateCustomer_User_T(Customer_User_T user) {

		try {

//			System.out.println("#### login/password : " + user.getLogin()+ "/" + user.getPassword());
			System.out.println("#### login/password : " + UserName+ "/" + Password);
			//logger.info("#### login/password : " + login + "/" + password);
			// Authenticate the user using the credentials provided
//			authenticate(user.getLogin(), user.getPassword());
			authenticate(UserName, Password);
			// Issue a token for the user
//			String token = issueToken(user.getLogin());
			String token = issueToken(UserName);
			System.out.println("#### token : " + token );

			// Return the token on the response
			return Response.status(200).header(AUTHORIZATION, "Bearer " + token).build();

			// return Response.status(200).entity(token).build();
		} catch (Exception e) {
			System.out.println(e.getMessage() +"\n"+e.toString());
			return Response.status(UNAUTHORIZED).build();
		}
	}

	private void authenticate(String UserName, String Password) throws Exception {

		// Authenticate against a database, 
		 // Throw an Exception if the credentials are invalid
		
		System.out.println("Fetching user from Database....................................................\n");
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = gconn2.getJNDIConnection();
		  System.out.println("sssssssssssssssssss"+UserName +Password);
		String query = "SELECT  id,UserName  FROM Customer_User_T where UserName='"+UserName+"' and Password='"+Password +"'";
		System.out.println("sssssssssssssssssss"+query);
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);


		      if (rs.next() == false) {
		        System.out.println("ResultSet in empty in Java");
		        throw new SecurityException("Invalid user/password");
		      } else {

		        do {
		          String id = rs.getString("id");
		          String username = rs.getString("UserName");
		          //String password = rs.getString("Password");
		          //System.out.println("username " + id + "_ "+username);
		        } while (rs.next());
		      }

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* ignored */}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					/* ignored */}
			}

		}
		
		
		
	}

	private String issueToken(String login) {
		
		 // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
		keyGenerator = new SimpleKeyGenerator();
		String key = keyGenerator.generateKey();
		System.out.println("#### key   : " + key );
		String jwtToken = Jwts.builder().setSubject(login).setIssuer(uriInfo.getAbsolutePath().toString()).setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusHours(6L)))
				.signWith(SignatureAlgorithm.HS256, key).compact();
		//logger.info("#### generating token for a key : " + jwtToken + " - " + key);
		 
		System.out.println("#### jwtToken   : " + jwtToken );
		return jwtToken;

	}
	
	
	@GET
	/*@Path("/listUsers")*/
	@Produces("application/json,application/xml")
	 @ApiResponses(value = {
	    		@ApiResponse(code = 200, message = "successful operation", response = Customer_User_T.class, responseContainer = "List"),
	    		 @ApiResponse(code = 400, message = "Bad Request / Invalid parameters supplied"),
	    		 @ApiResponse(code = 404, message = "Not found"),  
	    		@ApiResponse(code = 500, message = "Internal server error")  
	    })
	@ApiOperation(value = "List of all Users", notes = "List of all Users", response = Customer_User_T.class, responseContainer = "List")
	public Response findAllUsers() {
		List<Customer_User_T> allUsers = new ArrayList<Customer_User_T>();

		System.out.println("Fetching users from Database....................................................\n");
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = gconn2.getJNDIConnection();
		String query = "SELECT  * FROM Customer_User_T";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);


		      if (rs.next() == false) {
		        System.out.println("ResultSet in empty in Java");
		        throw new SecurityException("No data");
		      } else {

		        do {
		        Customer_User_T  userObj = new Customer_User_T();	
		          String id = rs.getString("id");
		          String user = rs.getString("UserName");
		          String password = rs.getString("Password");
		         // System.out.println("usre " + id + "_ "+user);
		          userObj.setId(id);
		          userObj.setUserName(user);
		          userObj.setPassword(password);
		          
		          allUsers.add(userObj);
		          
		        } while (rs.next());
		      }

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					/* ignored */}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					/* ignored */}
			}

		}
		
		
 
		return Response.ok(allUsers).build();
	}

	/*
	@POST
	public Response create(Customer_User_T user) {
		em.persist(user);
 		try {

			em.getTransaction().begin();

			String qlQuery = "SELECT t FROM TCustomer_User_T t";
			Query query = em.createQuery(qlQuery);
			allCustomer_User_Ts = query.getResultList();

			allCustomer_User_Ts.stream().forEach((x) -> System.out.println(x));

			em.getTransaction().commit();

		} finally {

			em.close();
			emf.close();
		}

		if (allCustomer_User_Ts == null)
			return Response.status(NOT_FOUND).build();

		return Response.ok(allCustomer_User_Ts).build(); 
		
		return Response.created(uriInfo.getAbsolutePathBuilder().path(user.getId()).build()).build();
	}

@GET
	@Path("/{id}")
	public Response findById(@PathParam("id") String id) {
		Customer_User_T user = em.find(Customer_User_T.class, id);

		if (user == null)
			return Response.status(NOT_FOUND).build();

		return Response.ok(user).build();
	}


	@DELETE
	@Path("/{id}")
	public Response remove(@PathParam("id") String id) {
		System.out.println("______________delete__________-");
		em.remove(em.getReference(Customer_User_T.class, id));
		return Response.noContent().build();
	}*/

	// ======================================
	// = Private methods =
	// ======================================

	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
}
