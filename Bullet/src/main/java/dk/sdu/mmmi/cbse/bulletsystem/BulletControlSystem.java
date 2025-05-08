package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import javax.swing.*;
import java.util.ArrayList;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    private int expiration;
    private int timer;

    public int getExpiration() { return expiration; }
    public void setExpiration(int expiration) { this.expiration = expiration; }

    public int getTimer() { return timer; }
    public void setTimer(int timer) { this.timer = timer; }

    @Override
    public void process(GameData gameData, World world) {
        ArrayList<Entity> bulletsToRemove = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (Entity entity : world.getEntities(Bullet.class)) {
            Bullet bullet = (Bullet) entity;

            // Move bullet
            double changeX = Math.cos(Math.toRadians(bullet.getRotation()));
            double changeY = Math.sin(Math.toRadians(bullet.getRotation()));
            bullet.setX(bullet.getX() + changeX * 5);
            bullet.setY(bullet.getY() + changeY * 5);

            // Check expiration based on system time
            if (now - bullet.getCreationTime() > bullet.getExpiration()) {
                bulletsToRemove.add(bullet);
                continue;
            }

            // Out of bounds check
            if (bullet.getX() < 0 || bullet.getX() > gameData.getDisplayWidth()
                    || bullet.getY() < 0 || bullet.getY() > gameData.getDisplayHeight()) {
                bulletsToRemove.add(bullet);
            }
        }

        for (Entity bullet : bulletsToRemove) {
            world.removeEntity(bullet);
        }
    }



    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Bullet bullet = new Bullet();
        bullet.setPolygonCoordinates(3, -1.5, 3, 1.5, -3, 1.5, -3, -1.5);
        double changeX = Math.cos(Math.toRadians(shooter.getRotation()));
        double changeY = Math.sin(Math.toRadians(shooter.getRotation()));
        bullet.setX(shooter.getX() + changeX * 10);
        bullet.setY(shooter.getY() + changeY * 10);
        bullet.setRotation(shooter.getRotation());
        bullet.setRadius(1);
        bullet.setCreationTime(System.currentTimeMillis()); // Set creation timestamp
        bullet.setExpiration(1000); // 1 sek levetid
        return bullet;
    }


}
