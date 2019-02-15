package com.util.db;



import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

final public class GetConnection {

	// use Uses JNDI and Datasource 
	public Connection getJNDIConnection() {
		
		//java:/jboss/datasources/1@pp_in_house
		//String DATASOURCE_CONTEXT = "java:/jboss/datasources/TheOneAppSelling"; 
		String DATASOURCE_CONTEXT = "java:/jboss/datasources/1@pp_in_house"; 
		//String DATASOURCE_CONTEXT = "java:/jboss/datasources/TheOneAppProduct"; //One app Database 	
		// this is for local conndatabase localhost
		// String DATASOURCE_CONTEXT = "java:/jboss/datasources/oneapp";
		
		Connection connection = null;
		try {
			Context initialContext = new InitialContext();
			if (initialContext == null) 
			{
			}
			//lookup = Retrieves the named object. See Context.lookup(Name) for details.
			DataSource datasource = (DataSource) initialContext.lookup(DATASOURCE_CONTEXT);
			
			//if found datasource -> get connect 
			if (datasource != null) {
				connection = datasource.getConnection();
			} else {
				// log("Failed to lookup datasource.");
			}
		} catch (NamingException ex) {
			// log("Cannot get connection: " + ex);
		} catch (SQLException ex) {
			// log("Cannot get connection: " + ex);
		}
		return connection;
	}
}