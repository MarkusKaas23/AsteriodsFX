package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

@Configuration
@ComponentScan("dk.sdu.mmmi.cbse")
public class ModuleConfig {

    @Bean
    public GameData gameData() {
        return new GameData();
    }

    @Bean
    public World world() {
        return new World();
    }

    @Bean
    public List<IEntityProcessingService> entityProcessingServices() {
        return loadServices(IEntityProcessingService.class);
    }

    @Bean
    public List<IGamePluginService> gamePluginServices() {
        return loadServices(IGamePluginService.class);
    }

    @Bean
    public List<IPostEntityProcessingService> postEntityProcessingServices() {
        return loadServices(IPostEntityProcessingService.class);
    }
    @Bean
    public IScoreService scoreService() {
        // TODO: Provide a concrete implementation, e.g. new ScoreServiceImpl()
        return new IScoreService() {
            private int score = 0;
            private int highScore = 0;

            @Override
            public int getScore() {
                return score;
            }

            @Override
            public int getHighScore() {
                return highScore;
            }

            @Override
            public void incrementScore() {

            }

            @Override
            public void setHighScore(int highScore) {

            }

            @Override
            public void setScore(int score) {

            }
        };
    }

    @Bean
    public Game game(List<IGamePluginService> plugins,
                     List<IEntityProcessingService> entityProcessors,
                     List<IPostEntityProcessingService> postProcessors) {
        return new Game(plugins, entityProcessors, postProcessors);
    }

    private <T> List<T> loadServices(Class<T> serviceClass) {
        return ServiceLoader.load(Main.layer, serviceClass)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }
}
