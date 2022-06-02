package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.sendMail.JavaMail;
import BD_access.AccessBaseDonnee;


public class DaoImpl implements IDao {

	@Override
	public boolean authentifier(String login, String password, String role) {
		try {
			int found=0; String query; ResultSet resultat=null;
			Connection connection = AccessBaseDonnee.getInstance();
			
			if(role.equalsIgnoreCase("Employe")){
				
				query = "SELECT * FROM t_employe WHERE login ='"+login+"'and password ='"+password+"'and role ='"+role+"'";
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				resultat = preparedStatement.executeQuery(query); 
			}
			else{
				
				query = "SELECT * FROM t_manager WHERE login ='"+login+"'and password ='"+password+"'and role ='"+role+"'";
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				resultat = preparedStatement.executeQuery(query); 
			}
			
			while(resultat.next()){ found++; }
			
			if(found==1) {return true;}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return false;
	}

	@Override
	public int soumettre(String matricule, int duree) {
		
		try {
			
			if(duree>0 && duree<=24)
			{
				int found=0; int id_empl=0; int identifiant=0;
				Connection connection = AccessBaseDonnee.getInstance();
				String query="Select * From t_employe where matricule_emp='"+matricule+"'";
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultat = preparedStatement.executeQuery(query); 
			
			while(resultat.next()){ 
				found++;
				id_empl =resultat.getInt("id"); 
			}
			
			if(found==1) {
				found=0;
				
				String statut="IN PROGRESS";
				query ="Insert into t_demandeconges (id_emp,statut,jours)"
					     + "values (?,?,?)";
				 preparedStatement = connection.prepareStatement(query);
				 preparedStatement.setInt(1, id_empl );
				 preparedStatement.setString(2, statut);
				 preparedStatement.setInt(3, duree );
				 preparedStatement.execute();
			 
			    query= "select id from t_demandeconges where id_emp='"+id_empl+"'and statut='"+statut+"'";
				preparedStatement = connection.prepareStatement(query);
				resultat = preparedStatement.executeQuery(query);
				
				while(resultat.next()){ 
					found++;
				    identifiant=resultat.getInt("id"); 
				 }
				
				if(found>0){
			      return identifiant;
				}
		   }  
		  else { return -1; }
	   }	
		
		 } catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	
	
	@SuppressWarnings("resource")
	@Override
	public int valider(int identifiant) {
		
		try {
				int found=0;int jours=0; String status; int id_emp=0; int solde=0;  
					
				Connection connection = AccessBaseDonnee.getInstance();
				String query="select * from t_demandeconges where id='"+identifiant+"'";
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultat = preparedStatement.executeQuery(query); 
			
				while(resultat.next()) {
					found++;
				id_emp=resultat.getInt("id_emp"); 
				status=resultat.getString("statut");
				jours=resultat.getInt("Jours");
			
			}
			
				if(found==1)
				{
					query="select * from t_employe where id='"+id_emp+"'";
					preparedStatement = connection.prepareStatement(query);
					resultat = preparedStatement.executeQuery(query); 
				
					while(resultat.next()) {
				 solde=resultat.getInt("solde");
				}
				
				if((jours>0 && jours<=3) && (solde-jours>=0)){
					
					status="APPROVED";
					solde=solde-jours;
					
					query="Update t_demandeconges set statut = ? where id='"+identifiant+"'";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, status);
					preparedStatement.execute();
					
					query="Update t_employe set solde = ? where id='"+id_emp+"'";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setInt(1, solde);
					preparedStatement.execute();
					
					return 1;
				}
				
				
				if(jours>solde){
					status="REJECTED";
					query="Update t_demandeconges set statut = ? where id='"+identifiant+"'";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, status);
					preparedStatement.execute();
					
					return 0;	
				}	
				else{
					status="IN PROGRESS";
					query="Update t_demandeconges set statut = ? where id ='"+identifiant+"'";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, status);
					preparedStatement.execute();
									
					return -1;
				}
		  
			}	
	   } catch (SQLException e) {
			e.printStackTrace();
}
		return -1;
	}
	
	

	@SuppressWarnings("resource")
	@Override
	public boolean Approuver(int identifiant, boolean statut, String message) {
		
			try {
				
			int found=0;int jours=0; int id_emp=0; int solde=0; String decision;
			
			Connection connection = AccessBaseDonnee.getInstance();
			String query="select * from t_demandeconges where id='"+identifiant+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultat = preparedStatement.executeQuery(query); 
			
			while(resultat.next()) {
				found++;
			id_emp=resultat.getInt("id_emp"); 
			jours=resultat.getInt("Jours");
			
			}
			
			query="select solde from t_employe where id='"+id_emp+"'";
			preparedStatement = connection.prepareStatement(query);
			resultat = preparedStatement.executeQuery(query); 
			
			while(resultat.next()) {
			 solde=resultat.getInt("solde");
			}
			
			if(found==1)
			{
				if(jours>3 && jours<=solde)
				{
					if(statut) {

					decision="APPROVED";
					solde=solde-jours;
					query ="Update t_demandeconges set statut = ? where id='"+identifiant+"'";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, decision);
					preparedStatement.execute();
					
					query="Update t_employe set solde = ? where id='"+id_emp+"'";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setInt(1, solde);
					preparedStatement.execute();
					
						return statut;	
					}
					else{
						
						decision="REJECTED";
					    query="Update t_demandeconges set statut = ? where id='"+identifiant+"'";
						preparedStatement = connection.prepareStatement(query);
						preparedStatement.setString(1, decision);
						preparedStatement.execute();
						
						return statut;
					}
				}
			}
				
	} catch (SQLException e) {
		e.printStackTrace();
}
		return false;
	}

	@Override
	public boolean Notifier(int idDemande, String Objet, String message,String role) {
		
		try {
					int found=0;int id_emp=0; String EmailEmploye=null; String Statut=null;
					int id_mngr=0; String EmailManager=null;
					Connection connection = AccessBaseDonnee.getInstance();
					String query="select * from t_demandeconges where id='"+idDemande+"'";
					PreparedStatement preparedStatement = connection.prepareStatement(query);
					ResultSet resultat = preparedStatement.executeQuery(query); 
					
					while(resultat.next()) {
						found++;
					id_emp=resultat.getInt("id_emp"); 
					Statut=resultat.getString("statut");
			}
			
			if(found==1)
			{
				  found=0;
				query="select * from t_employe where id='"+id_emp+"'";
				preparedStatement = connection.prepareStatement(query);
				resultat = preparedStatement.executeQuery(query); 
				
				while(resultat.next()) {
					found++;
				id_mngr=resultat.getInt("id_manager");
				EmailEmploye=resultat.getString("email_emp");
				}
				
				 query="select t_manager.email from t_manager  where id='"+id_mngr+"'";
			     preparedStatement = connection.prepareStatement(query);
				 resultat = preparedStatement.executeQuery(query); 
						
				    while(resultat.next()) {
				    EmailManager=resultat.getString("email");
					}
				  
				
				    
		   if(role.equalsIgnoreCase("Employe") || role.equalsIgnoreCase("Manager"))
		     {
				    
				if(Statut.equalsIgnoreCase("IN PROGRESS") && role.equalsIgnoreCase("Employe")) { 
					
					JavaMail.EnvoyerMail(EmailEmploye, "testaccount", EmailManager, Objet, message);
					Objet ="REPONSE VOTRE DEMANDE"; message ="Veuillez patienter, votre demande est en attente de reponse.\n Merci!!!";
					JavaMail.EnvoyerMail(EmailManager, "alpharomeo997", EmailEmploye, Objet, message); 
				    return true;
			     }
				
				if(Statut.equalsIgnoreCase("APPROVED") && role.equalsIgnoreCase("Employe")){
					JavaMail.EnvoyerMail(EmailEmploye, "testaccount", EmailManager, Objet, message);
					Objet ="REPONSE VOTRE DEMANDE"; message ="Votre demande de congés a été approuvée.\n Merci!!!";
					JavaMail.EnvoyerMail(EmailManager, "alpharomeo997", EmailEmploye, Objet, message);
					return true;
				 }
				
				if(Statut.equalsIgnoreCase("REJECTED") && role.equalsIgnoreCase("Employe") ) {
					JavaMail.EnvoyerMail(EmailEmploye, "testaccount", EmailManager, Objet, message);
					Objet ="REPONSE VOTRE DEMANDE"; 
					message =" Votre demande de congés a été rejétée car votre Solde est insuffisant.\n Merci!!!";
					JavaMail.EnvoyerMail(EmailManager, "alpharomeo997", EmailEmploye, Objet, message);
					return true;
				 }
				
				if((Statut.equalsIgnoreCase("APPROVED") || Statut.equalsIgnoreCase("REJECTED")) && role.equalsIgnoreCase("Manager")){
					JavaMail.EnvoyerMail(EmailManager, "alpharomeo997", EmailEmploye, Objet, message);
					return true;
				}
					
			}
			else{	
			  return false;
			}
	  }	
	
	} catch (SQLException e) {
		e.printStackTrace();
	}
		
		return false;
	}

}
