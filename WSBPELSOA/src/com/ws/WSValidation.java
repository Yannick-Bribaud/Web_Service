package com.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.dao.DaoImpl;
import com.dao.IDao;

@WebService(name="WSValidation", targetNamespace = "http://services.validation.com/")
public class WSValidation {
	
	IDao dao = new DaoImpl();
	
	@WebMethod
	public int valider(@WebParam(name="identifiant") int identifiant) {
		return dao.valider(identifiant);
	}
	
	@WebMethod
	public boolean Approuver(@WebParam(name="identifiant")int identifiant, @WebParam(name="statut") boolean statut,@WebParam(name="message")String message) {
		return dao.Approuver(identifiant, statut, message);	
	}
	
	

}
