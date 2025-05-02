package dk.sdu.mmmi.cbse;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class EnemyControlSystem implements IEntityProcessingService {
    private double rotationSpeed;
    private boolean inBox = true;

    @Override
    public void process(GameData gameData, World world) {
        RespawnEnemy(gameData, world);
        for (Entity enemy : world.getEntities(Enemy.class)) {
            Random rnd = new Random();

            double changeX = Math.cos(Math.toRadians(enemy.getRotation()));
            double changeY = Math.sin(Math.toRadians(enemy.getRotation()));

            enemy.setX(enemy.getX() + changeX);
            enemy.setY(enemy.getY() + changeY);

            if (inBox) {
                // Apply initial random rotation when inside the box
                if (rotationSpeed == 0) { // Initialize rotationSpeed only once
                    rotationSpeed = 2 * rnd.nextDouble();
                }
            } else {
                // When out of the box, change the rotation direction
                rotationSpeed = 2 * (rnd.nextDouble() - 0.5); // New random rotation direction
            }

            enemy.setRotation(enemy.getRotation() + rotationSpeed);

            if (enemy.getX() < 0) {
                enemy.setX(1);
                inBox = false;
            }
            else inBox = !(enemy.getX() < 150);

            if (enemy.getX() > gameData.getDisplayWidth()) {
                enemy.setX(gameData.getDisplayWidth()-1);
                inBox = false;
            }
            else inBox = !(enemy.getX() > 450);


            if (enemy.getY() < 0) {
                enemy.setY(1);
                inBox = false;
            }
            else inBox = !(enemy.getY() < 150);

            if (enemy.getY() > gameData.getDisplayHeight()) {
                enemy.setY(gameData.getDisplayHeight()-1);
                inBox = false;
            }
            else inBox = !(enemy.getY() > 450);

            int shootDecider = rnd.nextInt(200) + 1;

            if(shootDecider == 50){
                for (int i = 0; i < 5; i++) {
                    getBulletSPIs().stream().findFirst().ifPresent(
                            spi -> world.addEntity(spi.createBullet(enemy, gameData))
                    );
                }
            }

        }

    }
    private void RespawnEnemy(GameData gameData, World world) {
        int enemyCount = world.getEntities(Enemy.class).size();
        if (enemyCount >= 1) {
            return;
        }
        Random rnd = new Random();
        EnemyPlugin enemyPlugin = new EnemyPlugin();
        Entity enemy = enemyPlugin.createEnemy(gameData);
        world.addEntity(enemy);
    }
    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

}
