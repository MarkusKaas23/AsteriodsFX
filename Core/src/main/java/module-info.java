module Core {
    requires Common;
    requires javafx.graphics;
    requires CommonAsteroids;
    requires CommonBullet;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires ScoreSystem;


    exports dk.sdu.mmmi.cbse.main;

    opens dk.sdu.mmmi.cbse.main to spring.core, spring.beans, spring.context;
    uses dk.sdu.mmmi.cbse.common.services.IScoreService;
    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
}
