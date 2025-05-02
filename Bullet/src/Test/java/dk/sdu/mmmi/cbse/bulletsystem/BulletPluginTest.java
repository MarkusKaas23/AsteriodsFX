package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.bulletsystem.BulletPlugin;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.Entity;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.mockito.Mockito.*;

public class BulletPluginTest {

    @Test
    public void testStopRemovesBulletEntities() {
        GameData gameData = mock(GameData.class);
        World world = mock(World.class);

        Bullet bullet1 = new Bullet();
        Entity otherEntity = new Entity();

        when(world.getEntities()).thenReturn(List.of(bullet1, otherEntity));

        BulletPlugin plugin = new BulletPlugin();
        plugin.stop(gameData, world);

        verify(world).removeEntity(bullet1);
        verify(world, never()).removeEntity(otherEntity);
    }
}
