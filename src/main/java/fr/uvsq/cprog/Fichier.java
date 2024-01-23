package fr.uvsq.cprog;

import java.io.Serializable;

/**
 * Cette classe représente un objet "Fichier" avec un nom et un texte associé.
 * Cette classe est sérialisable.
 */
public class Fichier implements Serializable {
  private String nom;
  private String texteAssocie;

  // Constructeur par défaut (nécessaire pour la sérialisation)
  public Fichier() {
  }

  public Fichier(String nom, String texteAssocie) {
    this.nom = nom;
    this.texteAssocie = texteAssocie;
  }

  // Getters et setters
  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getTexteAssocie() {
    return texteAssocie;
  }

  public void setTexteAssocie(String texteAssocie) {
    this.texteAssocie = texteAssocie;
  }
}
