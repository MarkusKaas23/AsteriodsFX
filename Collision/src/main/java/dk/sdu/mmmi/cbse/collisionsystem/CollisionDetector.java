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

    private final ServiceLoader<IAsteroidSplitter> asteroidSplitterServiceLoader = ServiceLoader.load(IAsteroidSplitter.class);
    private final ServiceLoader<IScoreService> scoreServiceLoader = ServiceLoader.load(IScoreService.class);

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> entities = new ArrayList<>(world.getEntities());

        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity entity1 = entities.get(i);
                Entity entity2 = entities.get(j);

                if (collides(entity1, entity2)) {
                    handleCollision(entity1, entity2, world);
                }
            }
        }
    }

    private void handleCollision(Entity e1, Entity e2, World world) {
        // Debug logging
        System.out.println("Collision: " + e1.getClass().getSimpleName() + " <-> " + e2.getClass().getSimpleName());

        if (match(Asteroid.class, Bullet.class, e1, e2)) {
            Entity asteroid = e1 instanceof Asteroid ? e1 : e2;
            createAsteroidSplit(asteroid, world);
            incrementScore();
            world.removeEntity(e1);
            world.removeEntity(e2);
        } else if (match(Player.class, Enemy.class, e1, e2)
                || match(Player.class, Bullet.class, e1, e2)
                || match(Player.class, Asteroid.class, e1, e2)
                || match(Enemy.class, Asteroid.class, e1, e2)
                || match(Enemy.class, Bullet.class, e1, e2)) {
            world.removeEntity(e1);
            world.removeEntity(e2);
        }
    }

    private boolean match(Class<?> typeA, Class<?> typeB, Entity e1, Entity e2) {
        return (typeA.isInstance(e1) && typeB.isInstance(e2)) ||
                (typeB.isInstance(e1) && typeA.isInstance(e2));
    }

    private boolean collides(Entity e1, Entity e2) {
        float dx = (float) e1.getX() - (float) e2.getX();
        float dy = (float) e1.getY() - (float) e2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (e1.getRadius() + e2.getRadius());
    }

    private void createAsteroidSplit(Entity asteroid, World world) {
        for (IAsteroidSplitter splitter : asteroidSplitterServiceLoader) {
            splitter.createSplitAsteroid(asteroid, world);
        }
    }

    private void incrementScore() {
        for (IScoreService scoreService : scoreServiceLoader) {
            scoreService.incrementScore();
        }
    }
}
