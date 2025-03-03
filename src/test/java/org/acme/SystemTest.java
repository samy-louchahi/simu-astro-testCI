package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.model.Astre;
import org.acme.model.CreateurPlanete;
import org.acme.model.Simulation;
import org.acme.model.Vecteur;
import org.acme.service.SimulationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SystemTest {

    @Inject
    SimulationService simulationService;

    Simulation simu;
    List<Astre> listeSimu;

    @BeforeEach
    public void setUp(){
        CreateurPlanete factoryP = new CreateurPlanete();
        ArrayList<Astre> liste = new ArrayList<>();
        Astre p1 = factoryP.factory("p1", 100, 100, 100, 100, 100, 100);
        Astre p2 = factoryP.factory("p2", 100, 100, 200, 200, 100, 100);
        Astre p3 = factoryP.factory("p3", 100, 100, 300, 300, 100, 100);
        Astre p4 = factoryP.factory("p4", 100, 100, 400, 400, 100, 100);
        Astre p5 = factoryP.factory("p5", 100, 100, 500, 500, 100, 100);
        liste.add(p1);
        liste.add(p2);
        liste.add(p3);
        liste.add(p4);
        liste.add(p5);
        simu = new Simulation();
        simu.setListeAstre(liste);
        listeSimu = simu.getListeAstre();
    }

    @Test
    void testListAstreSame() {
        // On teste que la liste de simulation contient bien 5 astres
        assertEquals(5, listeSimu.size());
    }

    @Test
    void testSuppression(){
        // Suppression du premier astre
        Astre supprime = listeSimu.get(0);
        listeSimu.remove(supprime);
        // On vérifie qu'il n'est plus présent
        assertFalse(listeSimu.contains(supprime));
    }

    @Test
    void testAjout(){
        CreateurPlanete factoryP = new CreateurPlanete();
        Astre ajoute = factoryP.factory("ajoute", 100, 100, 600, 600, 100, 100);
        listeSimu.add(ajoute);
        assertTrue(listeSimu.contains(ajoute));
    }

    @Test
    void testSommeForces(){
        // On teste la somme des forces subies par p1 par rapport à p2
        CreateurPlanete factoryP = new CreateurPlanete();
        ArrayList<Astre> listeTest = new ArrayList<>();
        Astre p1 = factoryP.factory("p1", 100, 100, 25, 25, 100, 100);
        Astre p2 = factoryP.factory("p2", 100, 100, 0, 0, 100, 100);
        listeTest.add(p1);
        listeTest.add(p2);
        // Calcul de la force exercée sur p1 par p2
        Vecteur force = Astre.calculerSommeForces(listeTest, p1);
        // On vérifie que force a les valeurs attendues (les valeurs dépendent du calcul)
        // Ici, on compare avec une valeur calculée à l'avance (exemple fictif)
        Vecteur expected = new Vecteur(-3.775497663196204E-8, -3.775497663196204E-8);
        assertEquals(expected.getX(), force.getX(), 1e-10);
        assertEquals(expected.getY(), force.getY(), 1e-10);
    }
    @Test
    void testResetSimulation() {
        // Ajoute un astre pour être sûr que la simulation n'est pas vide
        CreateurPlanete factoryP = new CreateurPlanete();
        Astre p = factoryP.factory("TestAstre", 100, 100, 500, 500, 100, 100);
        simulationService.addAstre(p);
        assertFalse(simulationService.getAllAstres().isEmpty());
        // Réinitialise la simulation
        simulationService.resetSimulation();
        assertTrue(simulationService.getAllAstres().isEmpty());
    }

    @Test
    void testUpdatePointerPosition() {
        // Avant mise à jour, le curseur ne devrait pas exister
        Astre cursor = simulationService.findAstreByName("CURSOR");
        assertNull(cursor);
        // Mise à jour du pointeur
        simulationService.updatePointerPosition(1000, 2000);
        cursor = simulationService.findAstreByName("CURSOR");
        assertNotNull(cursor);
        assertEquals(1000, cursor.getPositionX());
        assertEquals(2000, cursor.getPositionY());
    }

    @Test
    void testMoveAstre() {
        CreateurPlanete factoryP = new CreateurPlanete();
        Astre p = factoryP.factory("TestAstre", 100, 100, 500, 500, 100, 100);
        simulationService.addAstre(p);
        simulationService.moveAstre("TestAstre", 1000, 1200);
        Astre moved = simulationService.findAstreByName("TestAstre");
        assertEquals(1000, moved.getPositionX());
        assertEquals(1200, moved.getPositionY());
    }
}
