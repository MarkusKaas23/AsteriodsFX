package dk.sdu.mmmi.cbse;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {
    private final Random rnd = new Random();

    @Override
    public void process(GameData gameData, World world) {
        RespawnEnemy(gameData, world);

        for (Entity entity : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) entity;

            double dx = enemy.getDx();
            double dy = enemy.getDy();

            double radians = Math.toRadians(enemy.getRotation());
            dx += Math.cos(radians) * 0.05;
            dy += Math.sin(radians) * 0.05;

            dx *= 0.95;
            dy *= 0.95;

            enemy.setX(enemy.getX() + dx);
            enemy.setY(enemy.getY() + dy);
            enemy.setDx(dx);
            enemy.setDy(dy);

            enemy.setRotation(enemy.getRotation() + (rnd.nextDouble() - 0.5) * 2);

            wrapAround(enemy, gameData);

            if (rnd.nextInt(200) == 4) {
                getBulletSPIs().stream().findFirst().ifPresent(
                        spi -> world.addEntity(spi.createBullet(enemy, gameData))
                );
            }
        }
    }

    private void RespawnEnemy(GameData gameData, World world) {
        int enemyCount = world.getEntities(Enemy.class).size();
        if (enemyCount < 3) {
            EnemyPlugin plugin = new EnemyPlugin();
            world.addEntity(plugin.createEnemy(gameData));
        }
    }

    private void wrapAround(Entity entity, GameData gameData) {
        if (entity.getX() < 0) entity.setX(gameData.getDisplayWidth());
        if (entity.getX() > gameData.getDisplayWidth()) entity.setX(0);
        if (entity.getY() < 0) entity.setY(gameData.getDisplayHeight());
        if (entity.getY() > gameData.getDisplayHeight()) entity.setY(0);
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
