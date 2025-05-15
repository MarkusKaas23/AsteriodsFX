package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.ServiceLoader;
import static java.util.stream.Collectors.toList;

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
    public List<IEntityProcessingService> entityProcessingServiceList() {
        return ServiceLoader.load(IEntityProcessingService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }

    @Bean
    public List<IGamePluginService> gamePluginServices() {
        return ServiceLoader.load(IGamePluginService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }

    @Bean
    public List<IPostEntityProcessingService> postEntityProcessingServices() {
        return ServiceLoader.load(IPostEntityProcessingService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }

    @Bean
    public Game game(List<IGamePluginService> plugins,
                     List<IEntityProcessingService> entityProcessors,
                     List<IPostEntityProcessingService> postProcessors) {
        return new Game(plugins, entityProcessors, postProcessors);
    }
}
