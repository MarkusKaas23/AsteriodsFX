package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.Enemy;
import dk.sdu.mmmi.cbse.asteroid.AsteroidProcessor;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.services.IScoreService;
import dk.sdu.mmmi.cbse.playersystem.Player;

import java.util.ServiceLoader;


public class CollisionDetector implements IPostEntityProcessingService {
    ServiceLoader<IAsteroidSplitter> asteroidSplitterServiceLoader = ServiceLoader.load(IAsteroidSplitter.class);
    ServiceLoader<IScoreService> scoreServiceLoader = ServiceLoader.load(IScoreService.class);
    @Override
    public void process(GameData gameData, World world) {
        // two for loops for all entities in the world
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {

                // if the two entities are identical, skip the iteration
                if (entity1.getID().equals(entity2.getID())) {
                    continue;                    
                }

                // CollisionDetection
                if (this.collides(entity1, entity2)) {
                    if(entity1 instanceof Asteroid && entity2 instanceof Bullet){
                        createAsteroidSplit(entity1, world);
                        incrementScore();
                        world.removeEntity(entity1);
                        world.removeEntity(entity2);
                    } else if (entity1 instanceof Bullet && entity2 instanceof Asteroid){
                        // If the two entities are an asteroid and a bullet, remove both
                        // and create a new asteroid
                        createAsteroidSplit(entity1, world);
                        incrementScore();
                        world.removeEntity(entity1);
                        world.removeEntity(entity2);
                    }
                    if ((entity1 instanceof Player && entity2 instanceof Enemy) || entity1 instanceof Enemy && entity2 instanceof Player) {
                        // Player must not collide with enemy
                        world.removeEntity(entity1);
                        world.removeEntity(entity2);
                    }
                    if ((entity1 instanceof Enemy && entity2 instanceof Bullet) || entity1 instanceof Bullet && entity2 instanceof Enemy) {
                        // Enemies must not collide with each other
                        world.removeEntity(entity1);
                        world.removeEntity(entity2);
                    }
                    if ((entity1 instanceof Enemy && entity2 instanceof Asteroid) || entity1 instanceof Asteroid && entity2 instanceof Enemy) {
                        // Enemies must not collide with each other
                        world.removeEntity(entity1);
                        world.removeEntity(entity2);
                    }
                    if ((entity1 instanceof Player && entity2 instanceof Bullet) || entity1 instanceof Bullet && entity2 instanceof Player) {
                        // Enemies must not collide with each other
                        world.removeEntity(entity1);
                        world.removeEntity(entity2);
                    }
                    if ((entity1 instanceof Player && entity2 instanceof Asteroid) || entity1 instanceof Asteroid && entity2 instanceof Player) {
                        // Enemies must not collide with each other
                        world.removeEntity(entity1);
                        world.removeEntity(entity2);
                    }
                }
            }
        }

    }
    public void createAsteroidSplit(Entity e, World world){
        for(IAsteroidSplitter asteroidSplitter : asteroidSplitterServiceLoader){
            // If the two entities are an asteroid and a bullet, remove both
            // and create a new asteroid
            asteroidSplitter.createSplitAsteroid(e, world);
        }
    }
    public void incrementScore(){
        for (IScoreService scoreService : scoreServiceLoader) {
            scoreService.incrementScore();
        }
    }

    public Boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }

}
