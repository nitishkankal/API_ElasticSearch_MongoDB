
package com.ApiServices.Elasticsearch.My_List;

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


@Path("/My_List")
@Api(value = "My_List")
public class My_List implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The client. */
	static OkHttpClient client = new OkHttpClient();

	@GET
	@Path("/Suggest_Community_Lists")
	@Produces("application/json,application/xml")
	@ApiOperation(value = " users search for friend list by customer_username and system will show both friend's list and suggest community list", notes = "Logic :Friend list will search from database , Suggest Community list search from Elasticsearch") // Specific
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successful operation"), // response details should be here to give repose type and example in swagger
			@ApiResponse(code = 400, message = "Bad Request / Invalid parameters supplied"), @ApiResponse(code = 404, message = "Not found"),
			@ApiResponse(code = 500, message = "Internal server error") })

	public Response getByID(@ApiParam(value = "Search wildcard keyword type: String", required = true) @DefaultValue("") @QueryParam("query1") String query1) {

		
		
		String postResponse = null;
		String json = My_List.curlToJsonString(query1);
		//System.out.println(json);
		
		try {
			postResponse = My_List.doPostRequest("http://192.168.23.10:9200/customer_info_20180919/_search", json);
			 //System.out.println(postResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		destroy();
		return Response.ok(postResponse).build();
	}

	 
	static String curlToJsonString(String query1) {

		
		return	"{\"query\":{\"wildcard\":{\"customer_username\":{\"value\":\""+query1+"\"}}}}";
	 

		
	
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
