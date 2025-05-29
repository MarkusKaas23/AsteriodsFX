package dk.sdu.mmmi.cbse.mapsystem;

import dk.sdu.mmmi.cbse.mapsystem.MapPlugin;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Test til: MapPlugin
public class MapPluginTest {

    @Test
    public void testStartAddsBackground() {
        MapPlugin plugin = new MapPlugin();

        GameData gameData = new TestGameData();
        World world = new World();

        plugin.start(gameData, world);

        Pane node = gameData.getGameNode();
        assertEquals(1, node.getChildren().size());
        assertTrue(node.getChildren().get(0) instanceof ImageView);
    }

    @Test
    public void testStopRemovesBackground() {
        MapPlugin plugin = new MapPlugin();

        GameData gameData = new TestGameData();
        World world = new World();

        plugin.start(gameData, world);
        plugin.stop(gameData, world);

        assertTrue(gameData.getGameNode().getChildren().isEmpty());
    }

    static class TestGameData extends GameData {
        private final Pane gameNode = new Pane();

        @Override
        public Pane getGameNode() {
            return gameNode;
        }

        @Override
        public int getDisplayWidth() {
            return 800;
        }

        @Override
        public int getDisplayHeight() {
            return 600;
        }
    }
}
