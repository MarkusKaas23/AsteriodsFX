package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IScoreService;

import java.util.ServiceLoader;

public class CollisionDetector implements IPostEntityProcessingService {

    private final IAsteroidSplitter asteroidSplitter =
            ServiceLoader.load(IAsteroidSplitter.class).findFirst().orElse(null);

    private final IScoreService scoreService =
            ServiceLoader.load(IScoreService.class).findFirst().orElse(null);

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {
                if (entity1.getID().equals(entity2.getID())) continue;
                if (!collides(entity1, entity2)) continue;

                handleCollision(entity1, entity2, world);
            }
        }
    }

    private void handleCollision(Entity e1, Entity e2, World world) {
        boolean isAsteroid1 = isOfType(e1, "Asteroid");
        boolean isAsteroid2 = isOfType(e2, "Asteroid");

        boolean isBullet1 = isOfType(e1, "Bullet");
        boolean isBullet2 = isOfType(e2, "Bullet");

        boolean isPlayer1 = "player".equals(e1.getAttribute("type"));
        boolean isPlayer2 = "player".equals(e2.getAttribute("type"));

        boolean isEnemy1 = "enemy".equals(e1.getAttribute("type"));
        boolean isEnemy2 = "enemy".equals(e2.getAttribute("type"));

        if ((isAsteroid1 && isBullet2) || (isAsteroid2 && isBullet1)) {
            Entity asteroid = isAsteroid1 ? e1 : e2;
            Entity bullet = isBullet1 ? e1 : e2;

            createAsteroidSplit(asteroid, world);
            incrementScore();

            world.removeEntity(asteroid);
            world.removeEntity(bullet);
            return;
        }

        if ((isEnemy1 && isBullet2) || (isEnemy2 && isBullet1)) {
            Entity enemy = isEnemy1 ? e1 : e2;
            Entity bullet = isBullet1 ? e1 : e2;

            incrementScore();

            world.removeEntity(enemy);
            world.removeEntity(bullet);
            return;
        }


        if ((isAsteroid1 && isPlayer2) || (isAsteroid2 && isPlayer1)) {
            Entity player = isPlayer1 ? e1 : e2;
            world.removeEntity(player);
            return;
        }

        if ((isEnemy1 && isPlayer2) || (isEnemy2 && isPlayer1)) {
            Entity player = isPlayer1 ? e1 : e2;
            world.removeEntity(player);
            return;
        }

    }


    private boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }

    private boolean isOfType(Entity entity, String simpleClassName) {
        return entity.getClass().getSimpleName().equalsIgnoreCase(simpleClassName);
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
