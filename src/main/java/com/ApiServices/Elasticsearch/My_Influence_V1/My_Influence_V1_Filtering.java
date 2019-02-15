
package com.ApiServices.Elasticsearch.My_Influence_V1;

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


@Path("/My_Influence_V1")
@Api(value = "My_Influence_V1")
public class My_Influence_V1_Filtering implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The client. */
	static OkHttpClient client = new OkHttpClient();

	@GET
	@Path("/Filtering")
	@Produces("application/json,application/xml")
	@ApiOperation(value = "users filter product in each influencer catalog by product condition or prodct category. ", notes = "Logic : results must come from \"product_condition.keyword\" field AND  \"seller_id\" field AND contains user's query in \"product_name\" fields.\r\n" + 
			"\r\n" + 
			"Note : Actually \"seller_id\" must be \"influencer_id\" but this field lack in  it_com_goods_20180919 index so for test query purpose, I pick seller_id instead.") // Specific
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successful operation"), // response details should be here to give repose type and example in swagger
			@ApiResponse(code = 400, message = "Bad Request / Invalid parameters supplied"), @ApiResponse(code = 404, message = "Not found"),
			@ApiResponse(code = 500, message = "Internal server error") })

	public Response getByID(@ApiParam(value = "query 1 keywords type: String", required = true) @DefaultValue("") @QueryParam("query1") String query1,
			@ApiParam(value = "Seller ID keywords type: String", required = true) @DefaultValue("") @QueryParam("Seller ID") String query2,
			@ApiParam(value = "Product filtering condition type: String", required = true) @DefaultValue("product_condition.keyword") @QueryParam("filter_level") String key,
			@ApiParam(value = "Fillter keyword type: String", required = true) @DefaultValue("") @QueryParam("filter_Keyword") String value) {

		
		
		String postResponse = null;
		String json = My_Influence_V1_Filtering.curlToJsonString(query1, query2, key, value);
		//System.out.println(json);
		
		try {
			postResponse = My_Influence_V1_Filtering.doPostRequest("http://192.168.23.10:9200/it_com_goods_20180919/_search", json);
			 //System.out.println(postResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		destroy();
		return Response.ok(postResponse).build();
	}

	 
	static String curlToJsonString(String query1, String query2, String key, String value) {
		
		
		return "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"product_name\":{\"query\":\""+query1+"\",\"fuzziness\":\"AUTO\",\"prefix_length\":3,"
				+ "\"minimum_should_match\":\"25%\"}}}],\"filter\":[{\"term\":{\"seller_id\":"+query2+"}},{\"term\":{\""+key+"\":\""+value+"\"}}]}},\"size\":1000}";
	 
		 

	 

		
	
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
