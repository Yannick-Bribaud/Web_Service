package com.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.dao.DaoImpl;
import com.dao.IDao;

@WebService(name="WSAuthentification", targetNamespace="http://services.authentication.com/")
public class WSAuthentification {
	
	IDao dao =new DaoImpl();
	
	@WebMethod
	public boolean authentifier(@WebParam(name="login")String login, @WebParam(name="password")String password,@WebParam(name="role")String role) {
		return dao.authentifier(login, password, role);
	}

}
