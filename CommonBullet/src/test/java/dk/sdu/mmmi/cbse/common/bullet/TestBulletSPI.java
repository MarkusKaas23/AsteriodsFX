package dk.sdu.mmmi.cbse.common.bullet;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

// Ekstra klasse for integrations testing
public class TestBulletSPI implements BulletSPI {

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Bullet bullet = new Bullet();
        bullet.setX(shooter.getX() + 10);
        bullet.setY(shooter.getY());
        bullet.setRotation(shooter.getRotation());
        return bullet;
    }
}
