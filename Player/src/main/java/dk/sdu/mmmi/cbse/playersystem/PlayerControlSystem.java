package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class PlayerControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Player.class)) {
            Player player = (Player) entity;

            double dx = player.getDx();
            double dy = player.getDy();

            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                player.setRotation(player.getRotation() - 2);
            }
            if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                player.setRotation(player.getRotation() + 2);
            }

            if (gameData.getKeys().isDown(GameKeys.UP)) {
                double radians = Math.toRadians(player.getRotation());
                dx += Math.cos(radians) * 0.1;
                dy += Math.sin(radians) * 0.1;
            }

            dx *= 0.95;
            dy *= 0.95;

            player.setX(player.getX() + dx);
            player.setY(player.getY() + dy);
            player.setDx(dx);
            player.setDy(dy);

            if (gameData.getKeys().isDown(GameKeys.SPACE) && isReady(System.currentTimeMillis(), player)) {
                getBulletSPIs().stream().findFirst().ifPresent(
                        spi -> world.addEntity(spi.createBullet(player, gameData))
                );
            }

            wrapAround(player, gameData);
        }
    }

    private void wrapAround(Entity player, GameData gameData) {
        if (player.getX() < 0) player.setX(gameData.getDisplayWidth());
        if (player.getX() > gameData.getDisplayWidth()) player.setX(0);
        if (player.getY() < 0) player.setY(gameData.getDisplayHeight());
        if (player.getY() > gameData.getDisplayHeight()) player.setY(0);
    }

    private boolean isReady(long currentTime, Player player) {
        if (currentTime - player.getLastShotTime() > player.getFireRate()) {
            player.setLastShotTime(currentTime);
            return true;
        }
        return false;
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
