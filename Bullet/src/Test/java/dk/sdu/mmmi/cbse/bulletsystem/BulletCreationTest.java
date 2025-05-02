package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.bulletsystem.BulletControlSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BulletCreationTest {

    @Test
    public void testCreateBulletPositionOffset() {
        BulletControlSystem system = new BulletControlSystem();
        GameData mockData = new GameData();

        Entity shooter = new Entity();
        shooter.setX(100);
        shooter.setY(100);
        shooter.setRotation(0); // facing right

        Entity bullet = system.createBullet(shooter, mockData);

        assertEquals(110, bullet.getX(), 0.001); // X offset by 10
        assertEquals(100, bullet.getY(), 0.001);
        assertEquals(0, bullet.getRotation(), 0.001);
    }
}
