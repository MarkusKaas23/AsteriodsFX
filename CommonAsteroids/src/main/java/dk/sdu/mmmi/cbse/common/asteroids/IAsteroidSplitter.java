package dk.sdu.mmmi.cbse.common.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;

import dk.sdu.mmmi.cbse.common.data.World;

//IAsteroidSplit
public interface IAsteroidSplitter {
    void createSplitAsteroid(Entity e, World w);

}
