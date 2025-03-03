package org.acme.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Astre implements Serializable {
    private float taille;
    private float masse;
    private String nom;
    private double positionX;
    private double positionY;
    private double vitesseX;
    private double vitesseY;

    private String color;

    private boolean isGrabbed = false; // par défaut false

    public boolean isGrabbed() {
        return isGrabbed;
    }
    public void setGrabbed(boolean grabbed) {
        this.isGrabbed = grabbed;
    }

    public abstract String getArgString();
    public abstract String toString();


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

    public void setTaille(float taille) {
        this.taille = taille;
    }

    public void setMasse(int masse) {
        this.masse = masse;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public void setVitesseX(double vitesseX) {
        this.vitesseX =vitesseX;
    }

    public void setVitesseY(double vitesseY) {
        this.vitesseY = vitesseY;
    }

    public abstract  void setAll(float taille, float masse, String nom, double positionX, double positionY, double vitesseX, double vitesseY);

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor(){
        return  this.color;
    }

    public double positionXProperty() {
        return positionX;
    }

    public double positionYProperty() {
        return positionY;
    }

    public double vitesseXProperty() {
        return vitesseX;
    }

    public double vitesseYProperty() {return vitesseY;}

    public void incrementTaille(double increment){this.taille += increment;}

    public void incrementMasse(double increment){this.masse += increment;}

    /**
     * Calcule la somme des forces gravitationnelles que reçoit 'current' par rapport aux autres
     */
    public static Vecteur calculerSommeForces(List<Astre> liste, Astre current) {
        Vecteur acceleration = new Vecteur(0, 0);

        // 1) Gravité initiale : pour tous les astres, sauf 'current' et le curseur.
        for (Astre a : liste) {
            if (a == current) continue;
            // On ignore l'astre curseur dans le calcul gravitationnel.
            if ("CURSOR".equalsIgnoreCase(a.getNom())) continue;

            double distanceX = (a.getPositionX() - current.getPositionX()) * Simulation.scaleDistance;
            double distanceY = (a.getPositionY() - current.getPositionY()) * Simulation.scaleDistance;
            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + 1.f);

            double facteur = Simulation.g * (a.getMasse() * Simulation.scaleMasse) / Math.pow(distance, 3);
            acceleration.incrementXBy(distanceX * facteur);
            acceleration.incrementYBy(distanceY * facteur);
        }

        // 2) Force de répulsion exercée par le curseur, si présent et si current n'est pas lui-même le curseur.
        if (!"CURSOR".equalsIgnoreCase(current.getNom())) {
            Astre cursor = null;
            for (Astre a : liste) {
                if ("CURSOR".equalsIgnoreCase(a.getNom())) {
                    cursor = a;
                    break;
                }
            }
            if (cursor != null) {
                double dx = current.getPositionX() - cursor.getPositionX();
                double dy = current.getPositionY() - cursor.getPositionY();
                double dist = Math.sqrt(dx * dx + dy * dy) + 1e-6; // éviter division par zéro
                double k = 1e5; // coefficient de répulsion à ajuster selon l'effet souhaité
                double magnitude = k / (dist * dist);

                double fx = (dx / dist) * magnitude;
                double fy = (dy / dist) * magnitude;

                // L'accélération est la force divisée par la masse de current
                double accelX = fx / current.getMasse();
                double accelY = fy / current.getMasse();

                acceleration.incrementXBy(accelX);
                acceleration.incrementYBy(accelY);
            }
        }

        return acceleration;
    }


    /**
     * Ajoute l'accélération à la vitesse actuelle, en tenant compte du pas de temps
     */
    public static void addVitesse(List<Astre> listeA, Astre current){
        // Si l'astre est "saisi", on n'update pas la vitesse
        if (current.isGrabbed()) {
            return;
        }
        Vecteur vAcc = calculerSommeForces(listeA, current);
        current.setVitesseX(
                current.getVitesseX() + (vAcc.getX() * Simulation.scaleTemps)
        );
        current.setVitesseY(
                current.getVitesseY() + (vAcc.getY() * Simulation.scaleTemps)
        );
    }

    /**
     * Met à jour la position de l'astre en fonction de sa vitesse
     */
    public static void setPositions(Astre current){
        // Idem, si l'astre est saisi => on n'update pas
        if (current.isGrabbed()) {
            return;
        }
        current.setPositionX(
                current.getPositionX() + (current.getVitesseX() * Simulation.scaleTemps) / Simulation.scaleDistance
        );
        current.setPositionY(
                current.getPositionY() + (current.getVitesseY() * Simulation.scaleTemps) / Simulation.scaleDistance
        );
    }

    /**
     * Calcule la distance entre deux astres
     */
    public static double distanceCentres(Astre a1, Astre a2){
        double distanceX = (a1.getPositionX() - a2.getPositionX());
        double distanceY = (a1.getPositionY() - a2.getPositionY());
        return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }

    /**
     * Vérifie si les astres sont en contact
     */
    public static boolean verifCollision(Astre a, Astre b) {
        if ("CURSOR".equalsIgnoreCase(a.getNom()) || "CURSOR".equalsIgnoreCase(b.getNom())) {
            return false;
        }
        return distanceCentres(a, b) <= (a.getTaille()/2 + b.getTaille()/2);
    }

    /**
     * Fusionne deux astres en collision selon la conservation du moment cinétique
     * On conserve l'astre le plus lourd et on retire l'autre de la liste
     */
    public static void collisionFusion(Astre a, Astre b, List<Astre> listA) {
        // La masse et la taille augmentent
        a.incrementMasse(b.getMasse());
        a.incrementTaille(b.getTaille() / 2);

        // Conservation de la quantité de mouvement
        double newVx = ((a.getVitesseX() * a.getMasse()) + (b.getVitesseX() * b.getMasse()))
                / (a.getMasse() + b.getMasse());
        double newVy = ((a.getVitesseY() * a.getMasse()) + (b.getVitesseY() * b.getMasse()))
                / (a.getMasse() + b.getMasse());
        a.setVitesseX(newVx);
        a.setVitesseY(newVy);

        // Couleur approximative ou simple "fusion"
        String mergedColor = "(mix: " + a.getColor() + " + " + b.getColor() + ")";
        a.setColor(mergedColor);

        // Retrait du second astre
        listA.remove(b);
    }
}