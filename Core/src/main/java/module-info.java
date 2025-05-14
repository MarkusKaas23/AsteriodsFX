module Core {
    requires Common;
    requires javafx.graphics;
    requires CommonAsteroids;
    requires CommonBullet;
    opens dk.sdu.mmmi.cbse.main to javafx.graphics;
    uses dk.sdu.mmmi.cbse.common.services.IScoreService;
    uses dk.sdu.mmmi.cbse.common.services.IGamePluginService;
    uses dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
}