package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.mockito.Mockito.*;

public class BulletControlSystemTest {

    private BulletControlSystem bulletSystem;
    private GameData gameData;
    private World world;

    @BeforeEach
    public void setup() {
        bulletSystem = new BulletControlSystem();
        gameData = mock(GameData.class);
        world = mock(World.class);
    }

    @Test
    public void testBulletMovesCorrectlyWithinBounds() {
        Bullet bullet = new Bullet();
        bullet.setX(100);
        bullet.setY(100);
        bullet.setRotation(0); // moves right

        when(world.getEntities(Bullet.class)).thenReturn(List.of(bullet));
        when(gameData.getDisplayWidth()).thenReturn(800);
        when(gameData.getDisplayHeight()).thenReturn(600);

        bulletSystem.process(gameData, world);

        // Bullet should move 3 units to the right
        assert (bullet.getX() > 100);
        assert (bullet.getY() == 100);
    }

    @Test
    public void testBulletRemovedWhenOutOfBounds() {
        Bullet bullet = new Bullet();
        bullet.setX(900); // outside display width
        bullet.setY(100);
        bullet.setRotation(0);

        when(world.getEntities(Bullet.class)).thenReturn(List.of(bullet));
        when(gameData.getDisplayWidth()).thenReturn(800);
        when(gameData.getDisplayHeight()).thenReturn(600);

        bulletSystem.process(gameData, world);

        verify(world).removeEntity(bullet); // Bullet should be removed
    }
}
