package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 *
 * @author Emil
 */
public class Player extends Entity {
/* Health feat.  - Not used
    private int health = 3; /

    public int getHealth() {
        return health;
    }

    public void decrementHealth() {
        health--;
    }

    public boolean isDead() {
        return health <= 0;
    }

 */
    private long lastShotTime;


    public long getLastShotTime() {
    return lastShotTime;
    }

    public long getFireRate() {
        return 100L;
    }

    public void setLastShotTime(long currentTime) {
    this.lastShotTime = currentTime;
    }
}
