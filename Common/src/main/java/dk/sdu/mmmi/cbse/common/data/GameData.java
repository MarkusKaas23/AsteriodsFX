package dk.sdu.mmmi.cbse.common.data;

import javafx.scene.layout.Pane;

public class GameData {

    private int displayWidth  = 800 ;
    private int displayHeight = 800;
    private GameKeys keys = new GameKeys();


    public GameKeys getKeys() {
        return keys;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    private Pane gameNode;

    public Pane getGameNode() {
        return gameNode;
    }

    public void setGameNode(Pane gameNode) {
        this.gameNode = gameNode;
    }

    public void setKeys(GameKeys keys) {
        this.keys = keys;
    }
}
