package com.util;


public class Param_Error {

	private int Error_code = 1001;
	private String Error_Type = "InValid Paramater";
	private String Invalid_Param_name = null;

	public Param_Error() {
		// TODO Auto-generated constructor stub
	}

	public int getError_code() {
		return Error_code;
	}

	public String getError_Type() {
		return Error_Type;
	}

	public String getInvalid_Param() {
		return Invalid_Param_name;
	}

	public void setInvalid_Param_name(String invalid_Param) {
		Invalid_Param_name = invalid_Param;
	}

}
