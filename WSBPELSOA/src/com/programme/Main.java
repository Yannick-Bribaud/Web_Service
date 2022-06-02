package com.programme;

import javax.xml.ws.Endpoint;
import com.ws.WSAuthentification;
import com.ws.WSSendMail;
import com.ws.WSSoumission;
import com.ws.WSValidation;

public class Main {

		public static final String WS_URL_Authentifier="http://localhost:8185/services/" ;
		public static final String WS_URL_Soumission="http://localhost:8186/services/" ;
		public static final String WS_URL_Validation="http://localhost:8187/services/" ;
		public static final String WS_URL_notification="http://localhost:8188/services/" ;
		
   public static void main(String[] args) {
		
		WSAuthentification authentification = new WSAuthentification();
		WSSoumission soumettre = new WSSoumission();
		WSValidation valider = new WSValidation();
		WSSendMail sendMail = new WSSendMail();
		
		Endpoint endpointAuthentifier= Endpoint.create(authentification);
		Endpoint endpointSoumission= Endpoint.create(soumettre);
		Endpoint endpointValidation= Endpoint.create(valider);
		Endpoint endpointNotification= Endpoint.create(sendMail);
		
		endpointAuthentifier.publish(WS_URL_Authentifier);
		endpointSoumission.publish(WS_URL_Soumission);
		endpointValidation.publish(WS_URL_Validation);
		endpointNotification.publish(WS_URL_notification);
		
		boolean statutAuthentification=endpointAuthentifier.isPublished();
		boolean statutoumission=endpointSoumission.isPublished();
		boolean statutValidation=endpointValidation.isPublished();
		boolean statutNotification=endpointNotification.isPublished();
		
		if(statutAuthentification && statutoumission && statutValidation && statutNotification ) {
			System.out.println("Services Publiés..."); 	
		}
	}

}
