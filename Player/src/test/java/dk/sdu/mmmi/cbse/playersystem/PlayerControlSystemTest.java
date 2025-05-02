package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControlSystemTest {

    private PlayerControlSystem controlSystem;
    private GameData gameData;
    private World world;
    private Player player;

    @BeforeEach
    void setup() {
        controlSystem = new PlayerControlSystem();
        gameData = new GameData();
        world = new World();

        // Set game display bounds
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);

        // Setup a single player entity
        player = new Player();
        player.setX(100);
        player.setY(100);
        player.setRotation(0); // Facing right
        world.addEntity(player);

        // Set keys
        GameKeys keys = new GameKeys();
        keys.setKey(GameKeys.UP, true);  // simulate UP key pressed
        gameData.setKeys(keys);
    }

    @Test
    void testPlayerMovesForward() {
        double initialX = player.getX();

        controlSystem.process(gameData, world);

        // Player should have moved along the X axis
        assertTrue(player.getX() > initialX, "Player should have moved forward along X axis");
    }
}
