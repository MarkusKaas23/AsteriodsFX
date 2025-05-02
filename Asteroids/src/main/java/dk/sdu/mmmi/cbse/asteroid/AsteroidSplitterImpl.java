package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.Random;

/**
 *
 * @author corfixen
 */
public class AsteroidSplitterImpl implements IAsteroidSplitter {

    @Override
    public void createSplitAsteroid(Entity e, World world) {
        if(e.getSize() < 4) {
            System.out.println("Asteroid too small to split");
            return;
        }
        for (int i = 0; i < 2; i++) {
            System.out.println("Creating split asteroid");
            Entity asteroid = new Asteroid();
            Random rnd = new Random();
            float size = e.getSize() / 2;
            asteroid.setPolygonCoordinates(size, -size, -size, -size, -size, size, size, size);
            asteroid.setX(e.getX() + rnd.nextInt(10));
            asteroid.setY(e.getY() + rnd.nextInt(10));
            asteroid.setRadius(size);
            asteroid.setRotation(rnd.nextInt(90));
            world.addEntity(asteroid);
        }

    }

}
