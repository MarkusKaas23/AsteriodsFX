package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author corfixen
 */
public class AsteroidPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        Entity asteroid = createAsteroid(gameData);
        for (int i = 0; i < 4; i++) {
            world.addEntity(createAsteroid(gameData));
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            world.removeEntity(asteroid);
        }
    }

    public Entity createAsteroid(GameData gameData) {
        Entity asteroid = new Asteroid();
        Random rnd = new Random();
        float size = rnd.nextInt(25) + 5;
        asteroid.setPolygonCoordinates(size, -size, -size, -size, -size, size, size, size);
        asteroid.setSize(size);
        asteroid.setRadius(size);
        asteroid.setRotation(rnd.nextInt(360));

        int edge = rnd.nextInt(4); // 0=top, 1=bottom, 2=left, 3=right
        int width = gameData.getDisplayWidth();
        int height = gameData.getDisplayHeight();

        switch (edge) {
            case 0: // Top
                asteroid.setX(rnd.nextInt(width));
                asteroid.setY(0);
                break;
            case 1: // Bottom
                asteroid.setX(rnd.nextInt(width));
                asteroid.setY(height);
                break;
            case 2: // Left
                asteroid.setX(0);
                asteroid.setY(rnd.nextInt(height));
                break;
            case 3: // Right
                asteroid.setX(width);
                asteroid.setY(rnd.nextInt(height));
                break;
        }

        return asteroid;
    }
}
