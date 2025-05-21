module Collision {
    requires Common;
    requires CommonAsteroids;
    requires CommonBullet;

    requires spring.web;


    provides dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService
            with dk.sdu.mmmi.cbse.collisionsystem.CollisionDetector;
    uses dk.sdu.mmmi.cbse.common.services.IScoreService;
    uses dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
}
