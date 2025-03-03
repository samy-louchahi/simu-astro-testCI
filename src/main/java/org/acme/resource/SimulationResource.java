package org.acme.resource;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.Astre;
import org.acme.service.SimulationService;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Path("/simulation")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimulationResource {

    @Inject
    SimulationService simulationService; // On injecte LE service (unique)

    @GET
    public List<Astre> getAllAstres() {
        // On délègue au service
        return simulationService.getAllAstres();
    }

    @POST
    @Path("/advance")
    public void advanceSimulation(@QueryParam("steps") @DefaultValue("1") int steps) {
        simulationService.advanceSimulation(steps);
    }

    @PUT
    @Path("/rate/{value}")
    public void setSimulationRate(@PathParam("value") float value) {
        simulationService.setSimulationRate(value);
    }

    @GET
    @Path("/info")
    @Produces(MediaType.TEXT_PLAIN)
    public String getInfo() {
        return simulationService.getSimulationInfo();
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.TEXT_PLAIN)
    public Response importSimulation(InputStream inputStream) {
        try {
            // On génère un fichier temporaire
            File tempFile = File.createTempFile("upload-", ".simu");
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            simulationService.loadFromFile(tempFile);
            // Ensuite, on charge la simulation :
            //  Simulation newSim = Simulation.setAPartirDunFichier(tempFile);
            //  simulationService.setSimulation(newSim); // ou un setListeAstre(...)

            return Response.ok("Fichier .simu importé avec succès").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Erreur : " + e.getMessage()).build();
        }
    }

    // Nouveau endpoint pour réinitialiser la simulation
    @POST
    @Path("/reset")
    public Response resetSimulation() {
        simulationService.resetSimulation();
        return Response.ok("Simulation réinitialisée.").build();
    }


}
