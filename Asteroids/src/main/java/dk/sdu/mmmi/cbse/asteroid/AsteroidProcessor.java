package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Random;

public class AsteroidProcessor implements IEntityProcessingService {

    private IAsteroidSplitter asteroidSplitter = new AsteroidSplitterImpl();
    private int asteroidMinCount = 10;
    @Override
    public void process(GameData gameData, World world) {
        SpawnAsteroids(gameData, world);
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            double changeX = Math.cos(Math.toRadians(asteroid.getRotation()));
            double changeY = Math.sin(Math.toRadians(asteroid.getRotation()));

            asteroid.setX(asteroid.getX() + changeX * 0.75);
            asteroid.setY(asteroid.getY() + changeY * 0.75);

            // Wrap X
            if (asteroid.getX() < 0) {
                asteroid.setX(gameData.getDisplayWidth());
            } else if (asteroid.getX() > gameData.getDisplayWidth()) {
                asteroid.setX(0);
            }

            // Wrap Y
            if (asteroid.getY() < 0) {
                asteroid.setY(gameData.getDisplayHeight());
            } else if (asteroid.getY() > gameData.getDisplayHeight()) {
                asteroid.setY(0);
            }
        }
    }
    Random rnd = new Random();
    int toSpawn = 1 + rnd.nextInt(3);
    public void SpawnAsteroids(GameData gameData, World world){
        int asteroidCount = world.getEntities(Asteroid.class).size();
        if (asteroidCount < asteroidMinCount) {
            AsteroidPlugin asteroidPlugin = new AsteroidPlugin();
            for (int i = 0; i < 3; i++) {
                Entity asteroid = asteroidPlugin.createAsteroid(gameData);
                world.addEntity(asteroid);
            }
        }
    }


    /**
     * Dependency Injection using OSGi Declarative Services
     */
    public void setAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = asteroidSplitter;
    }

    public void removeAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = null;
    }


}
