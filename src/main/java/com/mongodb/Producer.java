/**
 * 
 */
package com.mongodb;

 
import com.mongodb.MongoTest;

import java.net.UnknownHostException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
 

@ApplicationScoped
public class Producer {
  
@Produces
public MongoClient mongoClient() {
   // return new MongoClient("localhost", 27017);
  // mongodb+srv://admin:<PASSWORD>@oneappmongo-ismsq.mongodb.net/test?retryWrites=true
  MongoClientURI uri = new MongoClientURI("mongodb+srv://admin:admin@oneappmongo-ismsq.mongodb.net/test?retryWrites=true");

		MongoClient mongoClient = new MongoClient(uri);
		
		return mongoClient;

   }
}
