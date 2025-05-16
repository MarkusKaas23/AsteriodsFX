package dk.sdu.mmmi.cbse;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class Enemy extends Entity {
    private double dx = 0;
    private double dy = 0;

    public Enemy() {
        setAttribute("type", "enemy");
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
}
