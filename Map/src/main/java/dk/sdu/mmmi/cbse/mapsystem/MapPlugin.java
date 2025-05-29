package dk.sdu.mmmi.cbse.mapsystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class MapPlugin implements IGamePluginService {

    ImageView backgroundView;

    @Override
    public void start(GameData gameData, World world) {
        Image background = new Image(getClass().getResourceAsStream("/AsteroidBG.png"));
        backgroundView = new ImageView(background);
        backgroundView.setFitWidth(gameData.getDisplayWidth());
        backgroundView.setFitHeight(gameData.getDisplayHeight());

        Pane gameNode = gameData.getGameNode();
        if (gameNode != null) {
            gameNode.getChildren().add(0, backgroundView);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        if (gameData.getGameNode() != null) {
            gameData.getGameNode().getChildren().remove(backgroundView);
        }
    }
}
