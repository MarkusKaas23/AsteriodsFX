package dk.sdu.mmmi.cbse.scoresystem;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IScoreService;

import java.util.ServiceLoader;

public class ScorePlugin implements IGamePluginService {
    ServiceLoader<IScoreService> loader = ServiceLoader.load(IScoreService.class);
    @Override
    public void start(GameData gameData, World world) {
        for (IScoreService scoreService : loader) {
            scoreService.setScore(0);
            scoreService.setHighScore(scoreService.getHighScore());
            System.out.println("ScorePlugin started");
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (IScoreService scoreService : loader) {
            if(scoreService.getScore() > scoreService.getHighScore()){
                scoreService.setHighScore(scoreService.getScore());
            }
            System.out.println("ScorePlugin stopped");
        }
    }
}
