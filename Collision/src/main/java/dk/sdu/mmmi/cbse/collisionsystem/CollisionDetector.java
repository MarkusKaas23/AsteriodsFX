package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.Enemy;
import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IScoreService;
import dk.sdu.mmmi.cbse.playersystem.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class CollisionDetector implements IPostEntityProcessingService {

    private final IAsteroidSplitter asteroidSplitter =
            ServiceLoader.load(IAsteroidSplitter.class).findFirst().orElse(null);

    private final IScoreService scoreService =
            ServiceLoader.load(IScoreService.class).findFirst().orElse(null);

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> entities = new ArrayList<>(world.getEntities());

        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity entity1 = entities.get(i);
                Entity entity2 = entities.get(j);

                if (isColliding(entity1, entity2)) {
                    handleCollision(entity1, entity2, world);
                }
            }
        }
    }

    private void handleCollision(Entity e1, Entity e2, World world) {

        if ((e1 instanceof Asteroid && e2 instanceof Bullet) || (e2 instanceof Asteroid && e1 instanceof Bullet)) {
            Entity asteroid = e1 instanceof Asteroid ? e1 : e2;
            createAsteroidSplit(asteroid, world);
            incrementScore();
            world.removeEntity(e1);
            world.removeEntity(e2);
        } else if (e1 instanceof Player || e2 instanceof Player ||
                e1 instanceof Enemy || e2 instanceof Enemy) {
            world.removeEntity(e1);
            world.removeEntity(e2);
        }
    }

    private boolean match(Class<?> typeA, Class<?> typeB, Entity e1, Entity e2) {
        return (typeA.isInstance(e1) && typeB.isInstance(e2)) ||
                (typeB.isInstance(e1) && typeA.isInstance(e2));
    }

    private boolean isColliding(Entity e1, Entity e2) {
        float dx = (float) (e1.getX() - e2.getX());
        float dy = (float) (e1.getY() - e2.getY());
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (e1.getRadius() + e2.getRadius());
    }

    private void createAsteroidSplit(Entity asteroid, World world) {
        if (asteroidSplitter != null) {
            asteroidSplitter.createSplitAsteroid(asteroid, world);
        }
    }

    private void incrementScore() {
        if (scoreService != null) {
            scoreService.incrementScore();
        }
    }

}
