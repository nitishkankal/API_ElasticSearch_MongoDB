
package com.ApiServices.Elasticsearch.My_Inventory_V1;

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


@Path("/My_Inventory_V1")
@Api(value = "My_Inventory_V1")
public class My_Inventory_V1_FilterAndSort implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The client. */
	static OkHttpClient client = new OkHttpClient();

	@GET
	@Path("/FilterAndSort")
	@Produces("application/json,application/xml") 
	@ApiOperation(value = "users search for conpon first , then apply filter parameters and finally apply sort parameters. ", notes = "Logic : results must order by sort parameters (asc,desc) in example ; "
			+ "sort by _score first and then sort by coupon_quantity.") // Specific
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successful operation"), // response details should be here to give repose type and example in swagger
			@ApiResponse(code = 400, message = "Bad Request / Invalid parameters supplied"), @ApiResponse(code = 404, message = "Not found"),
			@ApiResponse(code = 500, message = "Internal server error") })

	public Response getByID(@ApiParam(value = "Query 1 search keyword type: String", required = true) @DefaultValue("") @QueryParam("query1") String query1,
			@ApiParam(value = "Query 1 search keyword type: String", required = true) @DefaultValue("") @QueryParam("query2") String query2, 
			@ApiParam(value = "Filter :category level type: String", required = true) @DefaultValue("categories_name.lv2.keyword") @QueryParam("filter_level") String key,
			@ApiParam(value = "Filter keyword type: String", required = true) @DefaultValue("") @QueryParam("filter_Keyword") String value,
			@ApiParam(value = "Sorting method type: String", required = true) @DefaultValue("coupon_quantity") @QueryParam("sorting_method") String key1,
			@ApiParam(value = "Sorting order type: String", required = true) @DefaultValue("desc") @QueryParam("Sorting_order") String value1) {

		
		
		String postResponse = null;
		String json = My_Inventory_V1_FilterAndSort.curlToJsonString(query1, query2 , key,value,key1,value1);
		System.out.println(json);
		
		try {
			postResponse = My_Inventory_V1_FilterAndSort.doPostRequest("http://192.168.23.10:9200/coupon_20180919/_search", json);
			 System.out.println("-----------"+postResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		destroy();
		return Response.ok(postResponse).build();
	}

	 
	static String curlToJsonString(String query1, String query2, String key, String value,String key1,String value1) {
		
		 
		
		return  "{\"query\":{\"bool\":{\"should\":[{\"multi_match\":{\"minimum_should_match\":\"75%\",\"type\":\"cross_fields\","
				+ "\"query\":\""+query1+"\",\"fields\":[\"coupon_name\",\"seller_name\"],\"_name\":\"cross\"}},"
				+ "{\"multi_match\":{\"minimum_should_match\":\"75%\",\"type\":\"best_fields\",\"fuzziness\":\"AUTO\",\"prefix_length\":3,"
				+ "\"query\":\""+query2+"\",\"fields\":[\"coupon_name\",\"seller_name\"],\"_name\":\"fuzziness\"}}],\"minimum_should_match\":1,"
				+ "\"filter\":{\"term\":{\""+key+"\":\""+value+"\"}}}},"
				+ "\"size\":1000,\"sort\":[{\""+key1+"\":{\"order\":\""+value1+"\"}}]}";
		 
	 
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
