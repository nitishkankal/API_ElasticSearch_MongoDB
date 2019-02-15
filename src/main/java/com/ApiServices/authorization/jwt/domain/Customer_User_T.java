package com.ApiServices.authorization.jwt.domain;


import java.io.Serializable;
import javax.persistence.*;


 
@Entity
@Table(name = "Customer_User_T")
/*@NamedQueries({
        @NamedQuery(name = Customer_User_T.FIND_ALL, query = "SELECT u FROM Customer_User_T u ORDER BY u.UserName DESC"),
        @NamedQuery(name = Customer_User_T.FIND_BY_LOGIN_PASSWORD, query = "SELECT u FROM Customer_User_T u WHERE u.UserName = :UserName AND u.Password = :Password"),
        @NamedQuery(name = Customer_User_T.COUNT_ALL, query = "SELECT COUNT(u) FROM Customer_User_T u")
})*/
public class Customer_User_T implements Serializable {
	private static final long serialVersionUID = 1L;

 
/*    public static final String FIND_ALL = "Customer_User_T.findAll";
    public static final String COUNT_ALL = "Customer_User_T.countAll";
    public static final String FIND_BY_LOGIN_PASSWORD = "Customer_User_T.findByLoginAndPassword";
*/
    
	@Id
	@Column(unique = true, nullable = false)
	private String id;

	private String UserName;

	private String Password;

	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getUserName() {
		return UserName;
	}

	 

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

 

}