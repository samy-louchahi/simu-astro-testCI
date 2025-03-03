package org.acme.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.acme.model.Astre;
import org.acme.service.SimulationService;

@ServerEndpoint("/ws/simulation")
@ApplicationScoped
public class SimulationWebSocket {

    @Inject
    SimulationService simulationService;

    private ObjectMapper mapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket ouvert, session " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            SimulationMessage msg = mapper.readValue(message, SimulationMessage.class);

            switch (msg.getType()) {
                case "pointer":
                    // curseur invisible => repulsion
                    simulationService.updatePointerPosition(msg.getX(), msg.getY());
                    break;

                case "drag":
                    // déplacement direct d'un astre
                    System.out.println("onMessage> DRAG pour astre=" + msg.getAstre()
                            + ", x=" + msg.getX() + ", y=" + msg.getY());
                    simulationService.moveAstre(msg.getAstre(), msg.getX(), msg.getY());
                    break;

                case "grab":
                    // on saisit l’astre => isGrabbed = true
                    System.out.println("onMessage> GRAB pour astre=" + msg.getAstre());
                    Astre grabbed = simulationService.findAstreByName(msg.getAstre());
                    if (grabbed != null) {
                        grabbed.setGrabbed(true);
                    }
                    break;

                case "launch":
                    System.out.println("onMessage> LAUNCH pour astre=" + msg.getAstre()
                            + ", vx=" + msg.getVx() + ", vy=" + msg.getVy());
                    // on relâche l’astre => isGrabbed=false, setVitesse
                    Astre launched = simulationService.findAstreByName(msg.getAstre());
                    if (launched != null && launched.isGrabbed()) {
                        launched.setGrabbed(false);
                        launched.setVitesseX(msg.getVx());
                        launched.setVitesseY(msg.getVy());
                    }
                    break;

                default:
                    System.out.println("Type de message inconnu : " + msg.getType());
                    break;
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du traitement du message WebSocket : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("WebSocket fermé, session " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Erreur sur la session " + session.getId());
        throwable.printStackTrace();
    }
}