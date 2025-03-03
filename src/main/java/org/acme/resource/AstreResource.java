package org.acme.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.acme.model.Astre;
import org.acme.model.Planete;
import org.acme.service.SimulationService;


import java.util.List;
import jakarta.ws.rs.core.MediaType;


@Path("/astres")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AstreResource {

    // On injecte le service
    @Inject
    SimulationService simulationService;

    @GET
    public List<Astre> getAllAstres() {
        return simulationService.getAllAstres();
    }

    @GET
    @Path("/{nom}")
    public Astre getAstre(@PathParam("nom") String nom) {
        Astre astre = simulationService.findAstreByName(nom);
        if (astre == null) {
            throw new NotFoundException("Astre introuvable : " + nom);
        }
        return astre;
    }

    @POST
    public Astre createAstre(Planete planete) {
        simulationService.addAstre(planete);
        return planete;
    }

    @DELETE
    @Path("/{nom}")
    public void deleteAstre(@PathParam("nom") String nom) {
        simulationService.deleteAstreByName(nom);
    }
}
