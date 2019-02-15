package com.util;


import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Json_Utilities {

	public Json_Utilities() {
		// TODO Auto-generated constructor stub
	}

	public String Convert_to_json(List dict) {
		Gson gson = new GsonBuilder().serializeNulls().create();
		String json = gson.toJson(dict);
		return json;
	}

	// public String Convert_to_json(String dict) {
	// Gson gson = new GsonBuilder().serializeNulls().create();
	// String json = gson.toJson(dict);
	// return json;
	// }
}
