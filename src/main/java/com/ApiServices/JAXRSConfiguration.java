package com.ApiServices;

 

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.ApiServices.authorization.jwt.filter.JWTTokenNeeded;

/**
 
 *
 * @author NitishK
 */

@ApplicationPath("api/v1")
public class JAXRSConfiguration extends Application {

}
