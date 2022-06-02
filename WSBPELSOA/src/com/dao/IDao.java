package com.dao;

public interface IDao {
	
	  boolean authentifier(String login,String password,String role);
	  int soumettre(String matricule, int duree);
	  int valider(int identifiant);
	  boolean Approuver(int identifiant, boolean statut, String message);
	  boolean Notifier(int idDemande, String Objet, String message,String role);

}
