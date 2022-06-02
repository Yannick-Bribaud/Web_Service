package com.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.dao.DaoImpl;
import com.dao.IDao;

@WebService(name="WSSoumission", targetNamespace ="http://services.soumission.com/")
public class WSSoumission {
	
	IDao dao = new DaoImpl();
	@WebMethod
	public int soumettre(@WebParam(name="matricule")String matricule, @WebParam(name="duree")int duree) {
		return dao.soumettre(matricule, duree);	
	}

}
