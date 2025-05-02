import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

module EnemyShip {
    exports dk.sdu.mmmi.cbse;
    requires Common;
    requires CommonBullet;
    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
    provides IGamePluginService with dk.sdu.mmmi.cbse.EnemyPlugin;
    provides IEntityProcessingService with dk.sdu.mmmi.cbse.EnemyControlSystem;
}








