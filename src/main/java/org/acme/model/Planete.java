package org.acme.model;


import java.util.ArrayList;

public class Planete extends Astre{
    private float taille;
    private float masse;
    private String nom;
    private double positionX;
    private double positionY;
    private double vitesseX;
    private double vitesseY;
    private String color;


    public Planete(String nom, float taille, float masse,  double positionX, double positionY, double vitesseX, double vitesseY) {
        this.taille = taille;
        this.masse = masse;
        this.nom = nom;
        this.positionX = (positionX);
        this.positionY = (positionY);
        this.vitesseX = (vitesseX);
        this.vitesseY= (vitesseY);
    }


    public Vecteur getVitesse() {
        return new Vecteur(vitesseX,vitesseY);
    }


    /**
     * @return la les arguments séparés par des espaces pour générer des fichiers de sauvergardes */
    @Override
    public String getArgString(){
        return nom + " "+ taille+ " " + masse+ " " + (int) positionX+ " " + (int) positionY + " "  + (int) vitesseX+ " " + (int) vitesseY +"\n";
    }

    @Override
    public String toString() {
        return nom + " {" +
                " taille = " + taille +
                ", masse = " + masse +
                ", coordonnées = X " + positionX +
                ", Y : " + positionY +
                '}'+'\n';
    }

    public float getTaille() {
        return taille;
    }

    public float getMasse() {
        return masse;
    }

    public String getNom() {
        return nom;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getVitesseX() {
        return vitesseX;
    }

    public double getVitesseY() {
        return vitesseY;
    }

    /**
     * prend tout les attribut de la planetes en parametre te les set avec la valeurs choisies */
    public void setAll(float taille, float masse, String nom, double positionX, double positionY, double vitesseX, double vitesseY){
        this.taille = taille;
        this.masse = masse;
        this.nom = nom;
        this.positionX= (positionX);
        this.positionY=(positionY);
        this.vitesseX=(vitesseX);
        this.vitesseY=(vitesseY);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor(){
        return  this.color;
    }

    @Override
    public void setTaille(float taille) {
        this.taille = taille;
    }


    public void setMasse(float masse) {
        this.masse = masse;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public double positionXProperty() {
        return positionX;
    }

    @Override
    public double positionYProperty() {
        return positionY;
    }

    @Override
    public double vitesseXProperty() {
        return vitesseX;
    }

    @Override
    public double vitesseYProperty() {
        return vitesseY;
    }

    @Override
    public void setPositionX(double positionX) {
        this.positionX = (positionX);
    }

    @Override
    public void setPositionY(double positionY) {
        this.positionY = (positionY);
    }

    @Override
    public void setVitesseX(double vitesseX) {
        this.vitesseX = (vitesseX);
    }

    @Override
    public void setVitesseY(double vitesseY) {
        this.vitesseY = (vitesseY);
    }

}