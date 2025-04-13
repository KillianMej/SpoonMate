package org.exemple.spoonmate;


public class Table {
 private int id;
 private int restau_id;
 private int numero;
 private int taille;
 private boolean libre;

     public Table() {
         this.restau_id=restau_id;
         this.numero=numero;
         this.taille=taille;
         this.libre=libre;
     }

    public Table(int restau_id, int numero, int taille, boolean libre) {
        this.restau_id = restau_id;
        this.numero = numero;
        this.taille = taille;
        this.libre = libre;
    }

    public void afficherInfo() {
        System.out.println("Table #" + numero);
        System.out.println("ID : " + id + " | Restaurant ID : " + restau_id);
        System.out.println("Taille : " + taille + " personnes");
        System.out.println("Disponible : " + (libre ? "Oui" : "Non"));
    }

    public void occuper() {
        this.libre = false;
    }

    public void liberer() {
        this.libre = true;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public int getRestauId() {
        return restau_id;
    }

    public int getNumero() {
        return numero;
    }

    public int getTaille() {
        return taille;
    }

    public boolean isLibre() {
        return libre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRestauId(int restauId) {
        this.restau_id = restau_id;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public void setLibre(boolean libre) {
        this.libre = libre;
    }
 }

