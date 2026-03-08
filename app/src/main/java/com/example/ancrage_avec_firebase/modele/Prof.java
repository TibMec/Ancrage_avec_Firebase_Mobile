package com.example.ancrage_avec_firebase.modele;

import java.io.Serializable;
import java.util.List;

public class Prof implements Serializable {
    public String nom;
    public String prenom;
    public String courriel;
    public List<String> ecoles;
    public List<String> codesMatieres;
    public String dateCreation;

    public Prof() {}

    public Prof(String nom, String prenom, String courriel,List<String> ecoles, List<String> codesMatieres){
        this.nom = nom;
        this.prenom = prenom;
        this.courriel = courriel;
        this.ecoles = ecoles;
        this.codesMatieres = codesMatieres;
    }
}
