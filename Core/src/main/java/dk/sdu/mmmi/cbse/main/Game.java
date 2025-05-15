package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.*;
import dk.sdu.mmmi.cbse.common.services.*;
import dk.sdu.mmmi.cbse.scoresystem.ScoreServiceProvider;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class Game {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Pane gameWindow = new Pane();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();

    private final List<IGamePluginService> gamePluginServices;
    private final List<IEntityProcessingService> entityProcessingServices;
    private final List<IPostEntityProcessingService> postEntityProcessingServices;

    private final Text asteroidText = new Text(10, 20, "Destroyed: 0");
    private final Text highscoreText = new Text(500, 20, "Highscore: 0");
    private IScoreService scoreService;

    private final Text gameOverText = new Text(250, 300, "GAME OVER");
    private boolean gameOverShown = false;





    public Game(List<IGamePluginService> gamePluginServices,
                List<IEntityProcessingService> entityProcessingServices,
                List<IPostEntityProcessingService> postEntityProcessingServices) {
        this.gamePluginServices = gamePluginServices;
        this.entityProcessingServices = entityProcessingServices;
        this.postEntityProcessingServices = postEntityProcessingServices;


    }

    public void start(Stage stage) {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());

        final IScoreService scoreService = ScoreServiceProvider.getScoreService();


        asteroidText.setFont(Font.font("Phosphate", 24));
        asteroidText.setFill(Color.WHITE);
        highscoreText.setFont(Font.font("Phosphate", 24));
        highscoreText.setFill(Color.WHITE);

        gameOverText.setFont(Font.font("Phosphate", 48));
        gameOverText.setFill(Color.RED);
        gameOverText.setVisible(false);
        gameWindow.getChildren().add(gameOverText);


        gameWindow.getChildren().addAll(asteroidText, highscoreText);


        Scene scene = new Scene(gameWindow);
        gameData.setGameNode(gameWindow);
        setupInput(scene);

        // Start alle plugins
        gamePluginServices.forEach(plugin -> plugin.start(gameData, world));

        // Opret polygoner for alle entities
        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        stage.setScene(scene);
        stage.setTitle("Asteroids");
        stage.show();
    }

    private void setupInput(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) gameData.getKeys().setKey(GameKeys.LEFT, true);
            if (event.getCode() == KeyCode.RIGHT) gameData.getKeys().setKey(GameKeys.RIGHT, true);
            if (event.getCode() == KeyCode.UP) gameData.getKeys().setKey(GameKeys.UP, true);
            if (event.getCode() == KeyCode.SPACE) gameData.getKeys().setKey(GameKeys.SPACE, true);
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) gameData.getKeys().setKey(GameKeys.LEFT, false);
            if (event.getCode() == KeyCode.RIGHT) gameData.getKeys().setKey(GameKeys.RIGHT, false);
            if (event.getCode() == KeyCode.UP) gameData.getKeys().setKey(GameKeys.UP, false);
            if (event.getCode() == KeyCode.SPACE) gameData.getKeys().setKey(GameKeys.SPACE, false);
        });
    }

    public void render() {
        asteroidText.setText("Destroyed: " + scoreService.getScore());
        highscoreText.setText("Highscore: " + scoreService.getHighScore());

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
            }
        }.start();
    }

    private void update() {
        entityProcessingServices.forEach(service -> service.process(gameData, world));
        postEntityProcessingServices.forEach(service -> service.process(gameData, world));
    }

    private void draw() {
        // Fjern polygons for entities, der ikke længere eksisterer
        polygons.keySet().removeIf(entity -> {
            if (!world.getEntities().contains(entity)) {
                gameWindow.getChildren().remove(polygons.get(entity));
                return true;
            }
            return false;
        });

        // Tegn/opdater polygoner for alle entities
        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = new Polygon(entity.getPolygonCoordinates());
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            String type = (String) entity.getAttribute("type");
            if ("player".equals(type)) {
                polygon.setFill(Color.LIGHTBLUE);
                polygon.setStroke(Color.BLACK);
            } else if ("bullet".equals(type)) {
                polygon.setFill(Color.YELLOW);
                polygon.setStroke(Color.BLACK);
            } else if ("enemy".equals(type)) {
                polygon.setFill(Color.RED);
                polygon.setStroke(Color.BLACK);
            } else if ("asteroid".equals(type)) {
                polygon.setFill(Color.GRAY);
                polygon.setStroke(Color.BLACK);
            } else {
                polygon.setFill(Color.DARKGRAY);
                polygon.setStroke(Color.BLACK);
            }


            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());

            boolean playerAlive = world.getEntities().stream()
                    .anyMatch(e -> "player".equals(e.getAttribute("type")));

            if (!playerAlive && !gameOverShown) {
                gameOverText.setVisible(true);
                gameOverShown = true;
            }
        }
    }

    public void stop() {
        gamePluginServices.forEach(plugin -> plugin.stop(gameData, world));
    }

    public GameData getGameData() {
        return gameData;
    }

    public World getWorld() {
        return world;
    }
}
