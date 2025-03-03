package org.acme.websocket;

public class SimulationMessage {
    private String type; // "pointer", "drag", "grab", "launch"
    private String astre; // nom de l'astre
    private double x;
    private double y;

    private double vx;
    private double vy;

    public SimulationMessage() {}

    public String getType()       { return type; }
    public void setType(String t) { this.type = t; }

    public String getAstre()      { return astre; }
    public void setAstre(String a){ this.astre = a; }

    public double getX()          { return x; }
    public void setX(double x)    { this.x = x; }

    public double getY()          { return y; }
    public void setY(double y)    { this.y = y; }

    public double getVx()         { return vx; }
    public void setVx(double vx)  { this.vx = vx; }

    public double getVy()         { return vy; }
    public void setVy(double vy)  { this.vy = vy; }
}