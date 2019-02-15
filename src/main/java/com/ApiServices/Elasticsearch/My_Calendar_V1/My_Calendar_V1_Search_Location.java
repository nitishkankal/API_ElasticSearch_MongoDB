
package com.ApiServices.Elasticsearch.My_Calendar_V1;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.ApiServices.authorization.jwt.filter.JWTTokenNeeded;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import okhttp3.RequestBody;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;


@Path("/My_Calendar_V1")
@Api(value = "My_Calendar_V1")
public class My_Calendar_V1_Search_Location implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The client. */
	static OkHttpClient client = new OkHttpClient();

	@GET
	@Path("/Search_Location")
	@Produces("application/json,application/xml")
	@ApiOperation(value = "users search for seller's location ", notes = "Logic : results must show seller's location in specific distance from user's location "
			+ "(ex. In 2 km apart from user's location) and then order by nearest location to user's location") // Specific
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successful operation"), // response details should be here to give repose type and example in swagger
			@ApiResponse(code = 400, message = "Bad Request / Invalid parameters supplied"), @ApiResponse(code = 404, message = "Not found"),
			@ApiResponse(code = 500, message = "Internal server error") })

	public Response getByID(@ApiParam(value = "query  keywords type: String", required = true) @DefaultValue("") @QueryParam("query1") String query1,
			@ApiParam(value = "distance in kilometer(km) keywords type: String", required = true) @DefaultValue("") @QueryParam("distance") String distance,
			@ApiParam(value = "User latitude type: String", required = true) @DefaultValue("") @QueryParam("latitude") String latitude,
			@ApiParam(value = "User longitude type: String", required = true) @DefaultValue("") @QueryParam("longitude") String longitude) {

		
		
		String postResponse = null;
		String json = My_Calendar_V1_Search_Location.curlToJsonString(query1, distance, latitude, longitude);
		//System.out.println(json);
		
		try {
			postResponse = My_Calendar_V1_Search_Location.doPostRequest("http://192.168.23.10:9200/seller_location_20180919/_search", json);
			 //System.out.println(postResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		destroy();
		return Response.ok(postResponse).build();
	}

	 
	static String curlToJsonString(String query1, String distance, String latitude, String longitude) {  
		
		  
		
		return	"{\"query\":{\"bool\":{\"must\":[{\"match\":{\"seller_name\":{\"fuzziness\":\"AUTO\",\"prefix_length\":2,\"minimum_should_match\":\"75%\","
 		+ "\"query\":\""+query1+"\"}}}],\"filter\":{\"geo_distance\":{\"distance\":\""+distance+"km\",\"location\":{\"lat\":"+latitude+",\"lon\":"+longitude+"}}}}},"
			+ "\"sort\":[{\"_geo_distance\":{\"location\":{\"lat\":"+latitude+",\"lon\":"+longitude+"},\"order\":\"asc\",\"unit\":\"km\",\"distance_type\":\"arc\"}}]}";

	 

		
	
	}

	String doGetRequest(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();

		okhttp3.Response response = client.newCall(request).execute();

		return response.body().string();
	}

	// post request code here

	/** The Constant JSON. */
	public static final okhttp3.MediaType JSON = okhttp3.MediaType.parse("application/json; charset=utf-8");

 
	static String doPostRequest(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		try (okhttp3.Response response = client.newCall(request).execute()) {
			if (response.code() == 308) {
				String location = response.header("Location");
				return doPostRequest(location, json);
			}

			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);

			return response.body().string();

		}
	}

	/**
	 * Destroy.
	 */
	public void destroy() {
		if (client != null) {
			ConnectionPool connectionPool = client.connectionPool();
			connectionPool.evictAll();
			// log.info("OKHTTP connections iddle: {}, all: {}", connectionPool.idleConnectionCount(),
			// connectionPool.connectionCount());
			ExecutorService executorService = client.dispatcher().executorService();
			executorService.shutdown();
			try {
				executorService.awaitTermination(3, TimeUnit.MINUTES);
				// log.info("OKHTTP ExecutorService closed.");
				System.out.println("OKHTTP ExecutorService closed.");
			} catch (InterruptedException e) {
				// log.warn("InterruptedException on destroy()", e);
				System.out.println("InterruptedException on destroy()" + e.toString());
			}
		}
	}

}
