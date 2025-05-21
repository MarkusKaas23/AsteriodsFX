package dk.sdu.mmmi.cbse.main;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {
    protected static ModuleLayer layer;

    @Override
    public void start(Stage stage) throws Exception {
        Path pluginsDir = Paths.get("plugins");

        ModuleFinder pluginsFinder = ModuleFinder.of(pluginsDir);

        List<String> plugins = pluginsFinder
                .findAll()
                .stream()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name)
                .collect(Collectors.toList());

        Configuration pluginsConfiguration = ModuleLayer
                .boot()
                .configuration()
                .resolve(pluginsFinder, ModuleFinder.of(), plugins);

        layer = ModuleLayer
                .boot()
                .defineModulesWithOneLoader(pluginsConfiguration, ClassLoader.getSystemClassLoader());

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ModuleConfig.class);

        Game game = context.getBean(Game.class);
        game.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
