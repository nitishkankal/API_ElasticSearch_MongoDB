package com.ApiServices.authorization.jwt.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.ApiServices.authorization.jwt.util.KeyGenerator;
import com.ApiServices.authorization.jwt.util.SimpleKeyGenerator;

import io.jsonwebtoken.Jwts;

/**
 * 
 * @author nitish.k --
 */
@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {

	// ======================================
	// = Injection Points =
	// ======================================

	//@Inject
	//private Logger logger;

	//@Inject
	private KeyGenerator keyGenerator;

	// ======================================
	// = Business methods =
	// ======================================

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		// logger.info("#### authorizationHeader : " + authorizationHeader);
		System.out.println("#### authorizationHeader : " + authorizationHeader);
		// Check if the HTTP Authorization header is present and formatted correctly
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
		//	 logger.severe("#### invalid authorizationHeader : " + authorizationHeader);
			System.out.println("#### invalid authorizationHeader : " + authorizationHeader);
			throw new NotAuthorizedException("Authorization header must be provided");
		}

		// Extract the token from the HTTP Authorization header
		String token = authorizationHeader.substring("Bearer".length()).trim();

		try {

			// Validate the token
			keyGenerator = new SimpleKeyGenerator();
			String key = keyGenerator.retriveKey();
			Jwts.parser().setSigningKey(key).parseClaimsJws(token);
		//	logger.info("#### valid token : " + token);
			System.out.println("#### valid token : " + token);

		} catch (Exception e) {
	//	 logger.severe("#### invalid token : " + token);
			System.out.println("#### invalid token : " + token);
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("unautorized User cannot access krub").build());
		}
	}
}