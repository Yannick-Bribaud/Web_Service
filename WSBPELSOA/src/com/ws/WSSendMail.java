package com.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import com.dao.DaoImpl;
import com.dao.IDao;


@WebService(name="WSSendMail", targetNamespace = "http://services.sendMail.com/")
public class WSSendMail {
	
	IDao dao=new DaoImpl();
	
	@WebMethod
	public boolean Notifier(@WebParam(name="idDemand") int idDemande, @WebParam(name="Objet")String Objet,@WebParam(name="message")String message,@WebParam(name="role")String role) {
		return dao.Notifier(idDemande, Objet, message,role);	
	}

}
