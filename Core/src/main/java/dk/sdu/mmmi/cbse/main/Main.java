package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.Enemy;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.*;
import dk.sdu.mmmi.cbse.common.services.*;
import dk.sdu.mmmi.cbse.playersystem.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

public class Main extends Application {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();

    private Text asteroidText;
    private Text highscoreText;
    private Text gameOverText = new Text("GAME OVER");
    private boolean gameOverDisplayed = false;
    private IScoreService scoreService;

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage window) {
        // Load score service
        for (IScoreService service : ServiceLoader.load(IScoreService.class)) {
            scoreService = service;
        }

        // Setup HUD
        asteroidText = new Text(10, 20, "Destroyed asteroids: " + scoreService.getScore());
        highscoreText = new Text(550, 20, "Highscore: " + scoreService.getHighScore());
        asteroidText.setFill(Color.BLACK);
        asteroidText.setFont(Font.font("Phosphate", 25));
        highscoreText.setFill(Color.BLACK);
        highscoreText.setFont(Font.font("Phosphate", 25));

        //GameOver text
        gameOverText.setFont(Font.font("Phosphate", 80));
        gameOverText.setFill(Color.RED);
        gameOverText.setVisible(false);  // Hidden by default
        gameOverText.setX(gameData.getDisplayWidth() / 2.0 - 200);
        gameOverText.setY(gameData.getDisplayHeight() / 2.0);
        gameWindow.getChildren().add(gameOverText);

        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().addAll(asteroidText, highscoreText);

        // ✅ STEP 5 — Make the gameWindow accessible to other modules
        gameData.setGameNode(gameWindow);

        // Setup scene and input
        Scene scene = new Scene(gameWindow);
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

        // Load and start all game plugins
        for (IGamePluginService plugin : getPluginServices()) {
            plugin.start(gameData, world);
        }

        // Draw initial entities
        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygon.setFill(Color.GRAY);
            polygon.setStroke(Color.DARKGRAY);
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);

        }

        render();

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();
    }

    @Override
    public void stop() {
        for (IGamePluginService plugin : getPluginServices()) {
            plugin.stop(gameData, world);
        }
    }

    private void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
                asteroidText.setText("Destroyed asteroids: " + scoreService.getScore());
                highscoreText.setText("Highscore: " + scoreService.getHighScore());
            }
        }.start();
    }

    private void update() {
        for (IEntityProcessingService eps : getEntityProcessingServices()) {
            eps.process(gameData, world);
        }
        for (IPostEntityProcessingService peps : getPostEntityProcessingServices()) {
            peps.process(gameData, world);
        }
    }

    private void draw() {
        // Remove disappeared entities
        for (Entity e : polygons.keySet()) {
            if (!world.getEntities().contains(e)) {
                Polygon poly = polygons.get(e);
                gameWindow.getChildren().remove(poly);
            }
        }
        polygons.keySet().removeIf(e -> !world.getEntities().contains(e));


        // Draw current entities
        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = new Polygon(entity.getPolygonCoordinates());
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            if (entity instanceof Player) {
                polygon.setFill(Color.LIGHTBLUE);
            }

            if (entity instanceof Bullet){
                polygon.setFill(Color.DARKBLUE);
            }

            if (entity instanceof Enemy){
                polygon.setFill(Color.RED);
            }

            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());

            boolean playerAlive = world.getEntities().stream().anyMatch(e -> e instanceof Player);

            if (!playerAlive && !gameOverDisplayed) {
                gameOverText.setVisible(true);
                gameOverDisplayed = true;


            }
        }
    }

    private Collection<? extends IGamePluginService> getPluginServices() {
        return ServiceLoader.load(IGamePluginService.class)
                .stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return ServiceLoader.load(IEntityProcessingService.class)
                .stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return ServiceLoader.load(IPostEntityProcessingService.class)
                .stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
