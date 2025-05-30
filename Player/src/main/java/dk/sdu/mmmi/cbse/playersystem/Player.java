package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class Player extends Entity {
    private double dx = 0;
    private double dy = 0;
    private long lastShotTime = 0;
    private long fireRate = 100; // 0,100 sek

    public Player() {
        setAttribute("type", "player");
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public long getLastShotTime() {
        return lastShotTime;
    }

    public void setLastShotTime(long lastShotTime) {
        this.lastShotTime = lastShotTime;
    }

    public long getFireRate() {
        return fireRate;
    }

    public void setFireRate(long fireRate) {
        this.fireRate = fireRate;
    }
}
