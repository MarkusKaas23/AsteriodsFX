package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.Random;


public class AsteroidSplitterImpl implements IAsteroidSplitter {

    @Override
    public void createSplitAsteroid(Entity e, World world) {
        if(e.getSize() < 5) {
            System.out.println("Asteroid too small to split");
            return;
        }
        Random rnd = new Random();
        int splitCount = 2 + rnd.nextInt(3);

        for (int i = 0; i < splitCount; i++) {
            System.out.println("Creating split asteroid");
            Entity asteroid = new Asteroid();

            float size = e.getSize() / 3;
            asteroid.setPolygonCoordinates(
                    size, 0f,
                    size * 0.6f, size * 0.8f,
                    0f, size,
                    -size * 0.7f, size * 0.6f,
                    -size, 0f,
                    -size * 0.4f, -size * 0.8f,
                    size * 0.5f, -size * 0.7f
            );
            asteroid.setX(e.getX() + rnd.nextInt(10));
            asteroid.setY(e.getY() + rnd.nextInt(10));
            asteroid.setRadius(size);
            asteroid.setSize(size);
            asteroid.setRotation(rnd.nextInt(360));
            world.addEntity(asteroid);
        }

    }

}
