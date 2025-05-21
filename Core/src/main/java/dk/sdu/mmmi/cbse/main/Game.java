package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.*;
import dk.sdu.mmmi.cbse.common.services.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
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

    private final Text gameOverText = new Text(250, 300, "GAME OVER");
    private boolean gameOverShown = false;

    private AnimationTimer gameLoop;

    private RestTemplate restTemplate;

    private final String scoreServiceUrl = "http://localhost:8080/score";

    private int highscore = 0;
    private final Path highscoreFile = Paths.get("Highscore.txt");

    public Game(
            List<IGamePluginService> gamePluginServices,
            List<IEntityProcessingService> entityProcessingServices,
            List<IPostEntityProcessingService> postEntityProcessingServices
    ) {
        this.gamePluginServices = gamePluginServices;
        this.entityProcessingServices = entityProcessingServices;
        this.postEntityProcessingServices = postEntityProcessingServices;
        this.restTemplate = new RestTemplate();
    }

    public void start(Stage stage) {
        loadHighscore();  // Load highscore from file at start

        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());

        // Setup texts
        asteroidText.setFont(Font.font("Phosphate", 24));
        asteroidText.setFill(Color.WHITE);

        highscoreText.setFont(Font.font("Phosphate", 24));
        highscoreText.setFill(Color.WHITE);
        highscoreText.setText("Highscore: " + highscore);

        gameOverText.setFont(Font.font("Phosphate", 48));
        gameOverText.setFill(Color.RED);
        gameOverText.setVisible(false);

        gameWindow.getChildren().addAll(asteroidText, highscoreText, gameOverText);

        // Setup scene and input
        Scene scene = new Scene(gameWindow);
        gameData.setGameNode(gameWindow);
        setupInput(scene);

        // Start plugins
        gamePluginServices.forEach(plugin -> plugin.start(gameData, world));

        // Create polygons for existing entities
        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        stage.setScene(scene);
        stage.setTitle("Asteroids");
        stage.show();

        // START GAME LOOP HERE!
        render();
    }

    private void setupInput(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.LEFT) gameData.getKeys().setKey(GameKeys.LEFT, true);
            else if (code == KeyCode.RIGHT) gameData.getKeys().setKey(GameKeys.RIGHT, true);
            else if (code == KeyCode.UP) gameData.getKeys().setKey(GameKeys.UP, true);
            else if (code == KeyCode.SPACE) gameData.getKeys().setKey(GameKeys.SPACE, true);
        });

        scene.setOnKeyReleased(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.LEFT) gameData.getKeys().setKey(GameKeys.LEFT, false);
            else if (code == KeyCode.RIGHT) gameData.getKeys().setKey(GameKeys.RIGHT, false);
            else if (code == KeyCode.UP) gameData.getKeys().setKey(GameKeys.UP, false);
            else if (code == KeyCode.SPACE) gameData.getKeys().setKey(GameKeys.SPACE, false);
        });
    }

    public void render() {
        if (gameLoop == null) {
            gameLoop = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    update();
                    draw();
                    gameData.getKeys().update();

                    String url = scoreServiceUrl + "/total";

                    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                    Long score = Long.valueOf(response.getBody());

                    //int currentScore = scoreServiceUrl.getScore();
                    asteroidText.setText("Destroyed: " + score);

/*

                    if (currentScore > highscore) {
                        highscore = currentScore;
                        saveHighscore();
                    }
                    highscoreText.setText("Highscore: " + highscore);

 */
                }
            };
            gameLoop.start();
        }
    }

    private void update() {
        entityProcessingServices.forEach(service -> service.process(gameData, world));
        postEntityProcessingServices.forEach(service -> service.process(gameData, world));
    }

    private void draw() {
        // Remove polygons for entities no longer present
        polygons.keySet().removeIf(entity -> {
            if (!world.getEntities().contains(entity)) {
                gameWindow.getChildren().remove(polygons.get(entity));
                return true;
            }
            return false;
        });

        // Draw/update polygons for all entities
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
        }

        boolean playerAlive = world.getEntities().stream()
                .anyMatch(e -> "player".equals(e.getAttribute("type")));

        if (!playerAlive && !gameOverShown) {
            gameOverText.setVisible(true);
            gameOverShown = true;
        }
    }

    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        gamePluginServices.forEach(plugin -> plugin.stop(gameData, world));
    }

    private void loadHighscore() {
        if (Files.exists(highscoreFile)) {
            try {
                String content = Files.readString(highscoreFile).trim();
                highscore = Integer.parseInt(content);
            } catch (IOException | NumberFormatException e) {
                System.err.println("Failed to load highscore: " + e.getMessage());
                highscore = 0;
            }
        } else {
            highscore = 0;
        }
    }

    private void saveHighscore() {
        try {
            Files.writeString(highscoreFile, String.valueOf(highscore));
        } catch (IOException e) {
            System.err.println("Failed to save highscore: " + e.getMessage());
        }
    }

    public GameData getGameData() {
        return gameData;
    }

    public World getWorld() {
        return world;
    }
}
