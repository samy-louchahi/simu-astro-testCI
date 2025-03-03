package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Astre;
import org.acme.model.Planete;
import org.acme.model.Simulation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SimulationService {

    // Instance unique de Simulation pour tout le backend,
    // ou bien tu peux en gérer plusieurs si nécessaire.
    private Simulation simulation = new Simulation();

    public List<Astre> getAllAstres() {
        return simulation.getListeAstre();
    }
    public void loadFromFile(File f) throws IOException, ClassNotFoundException {
        Simulation newSimu = Simulation.setAPartirDunFichier(f);
        this.simulation = newSimu; // ou merges astres
    }


    public void addAstre(Astre astre) {
        simulation.addListeAstre(astre);
    }

    public Astre findAstreByName(String nom) {
        return simulation.getListeAstre().stream()
                .filter(a -> a.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);
    }

    public void deleteAstreByName(String nom) {
        simulation.getListeAstre().removeIf(a -> a.getNom().equalsIgnoreCase(nom));
    }

    /**
     * Fait avancer la simulation d’un nombre de steps (itérations)
     * en mettant à jour vitesses/positions et en gérant les collisions
     */
    public void advanceSimulation(int steps) {
        for(int i = 0; i < steps; i++){
            // 1) Mise à jour des vitesses (utilise un "snapshot" pour éviter des incohérences)
            List<Astre> snapshot = new ArrayList<>(simulation.getListeAstre());
            for(Astre a : snapshot) {
                List<Astre> autres = Simulation.getOther(a, snapshot);
                Astre.addVitesse(autres, a);
            }

            // 2) Mise à jour des positions
            for(Astre a : snapshot) {
                Astre.setPositions(a);
            }

            // 3) Gestion des collisions sur la liste réelle
            List<Astre> liste = simulation.getListeAstre();
            for(int idxA = 0; idxA < liste.size(); idxA++){
                Astre a = liste.get(idxA);
                for(int idxB = idxA + 1; idxB < liste.size(); idxB++){
                    Astre b = liste.get(idxB);
                    if(Astre.verifCollision(a, b)){
                        if(a.getMasse() >= b.getMasse()){
                            Astre.collisionFusion(a, b, liste);
                            idxB--;
                        } else {
                            Astre.collisionFusion(b, a, liste);
                            idxB--;
                            // "a" a fusionné => on sort de la boucle
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Modifie le taux d'accélération du temps (simuRate)
     */
    public void setSimulationRate(float rate) {
        Simulation.setSimuRate(rate);
    }

    public float getSimulationRate() {
        return Simulation.getSimuRate();
    }

    public String getSimulationInfo() {
        return simulation.toString();
    }
    // Nouvelle méthode pour réinitialiser la simulation
    public void resetSimulation() {
        // Créez une nouvelle instance de Simulation
        this.simulation = new Simulation();
    }
    // Nouvelle méthode pour mettre à jour la position du curseur (pointeur)
    // On suppose que le curseur est représenté par un astre dont le nom est "CURSOR"
    public void updatePointerPosition(double x, double y) {
        Astre cursor = findAstreByName("CURSOR");
        if (cursor == null) {
            // Crée un astre "curseur" avec des propriétés prédéfinies
            // Vous pouvez définir des valeurs par défaut pour taille et masse
            double defaultTaille = 50;
            double defaultMasse = 100;
            // Ici, on suppose que vous disposez d'un constructeur ou d'une factory dans CreateurPlanete
            cursor = new Planete("CURSOR", (float) defaultTaille, (float) defaultMasse, x, y, 0, 0);
            addAstre(cursor);
        } else {
            cursor.setPositionX(x);
            cursor.setPositionY(y);
        }
    }

    // Nouvelle méthode pour déplacer un astre (drag)
    public void moveAstre(String astreName, double x, double y) {
        Astre a = findAstreByName(astreName);
        if (a != null) {
            a.setPositionX(x);
            a.setPositionY(y);
        }
    }
}
