package com.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class Paramaters_Handeling {

	private List<Param_Error> errorList;

	public Paramaters_Handeling() {
		// TODO Auto-generated constructor stub
	}

	public boolean checkParameter(HttpServletRequest request, String[] Original_paramList) {
		Map<String, String[]> user_params_pair = request.getParameterMap();

		errorList = new ArrayList<>();
		Param_Error pError = new Param_Error();

		if (user_params_pair.isEmpty()) {
			return true;
		}

		Set<String> user_params_keys = user_params_pair.keySet();
		List<String> original_list = Arrays.asList(Original_paramList);

		for (String user_req : user_params_keys) {
			if (!original_list.contains(user_req)) {
				return false;
			}

		}

		return true;

	}

	public boolean checkParameter_AllnCorrect(HttpServletRequest request, String[] Original_paramList) {
		Map<String, String[]> user_params_pair = request.getParameterMap();

		errorList = new ArrayList<>();
		Param_Error pError = new Param_Error();

		if (user_params_pair.isEmpty()) {
			return true;
		}

		Set<String> user_params_keys = user_params_pair.keySet();
		List<String> original_list = Arrays.asList(Original_paramList);
		if (user_params_keys.size() < original_list.size()) {
			return false;
		}

		for (String user_req : user_params_keys) {
			if (!original_list.contains(user_req)) {
				return false;
			}

		}

		return true;

	}

	public static String getCaseInsensitiveParameter(HttpServletRequest request, String paramater1) {
		Map params = request.getParameterMap();
		Iterator i = params.keySet().iterator();
		while (i.hasNext()) {
			String key = (String) i.next();
			String value = ((String[]) params.get(key))[0];
			if (paramater1.equalsIgnoreCase(key)) {
				return value;
			}
		}
		return null;
	}

	/**
	 * @param request
	 * @param parmList
	 * @return
	 */
	public boolean isParameterExists(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map<String, String[]> user_params_pair = request.getParameterMap();

		if (user_params_pair.isEmpty()) {
			return false;
		}

		return true;
	}

}
