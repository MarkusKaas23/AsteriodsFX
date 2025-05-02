import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

module Collision {
    exports dk.sdu.mmmi.cbse.collisionsystem;
    requires Common;
    requires CommonBullet;
    requires CommonAsteroids;
    requires Asteroid;
    requires Player;
    requires EnemyShip;
    uses dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
    uses dk.sdu.mmmi.cbse.common.services.IScoreService;
    provides IPostEntityProcessingService with dk.sdu.mmmi.cbse.collisionsystem.CollisionDetector;
}