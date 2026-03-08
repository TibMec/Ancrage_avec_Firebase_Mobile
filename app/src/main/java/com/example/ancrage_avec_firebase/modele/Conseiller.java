package com.example.ancrage_avec_firebase.modele;

import java.io.Serializable;
import java.util.List;

public class Conseiller implements Serializable {
    public String nom;
    public String prenom;
    public String courriel;
    public String tel;
    public List<String> codesMatieres;

    public Conseiller() {}

    public Conseiller(String nom, String prenom, String courriel, String tel, List<String> codesMatieres){
        this.nom = nom;
        this.prenom = prenom;
        this.courriel = courriel;
        this.tel = tel;
        this.codesMatieres = codesMatieres;
    }
}