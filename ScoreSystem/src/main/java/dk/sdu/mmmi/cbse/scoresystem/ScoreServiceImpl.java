package dk.sdu.mmmi.cbse.scoresystem;

import dk.sdu.mmmi.cbse.common.data.Score;
import dk.sdu.mmmi.cbse.common.services.IScoreService;

public class ScoreServiceImpl implements IScoreService {
    private final Score score = Score.getInstance();
    @Override
    public int getScore() {
        return score.getScore();
    }

    @Override
    public int getHighScore() {
        return score.getHighScore();
    }

    @Override
    public void incrementScore() {
        score.setScore(score.getScore() + 1);
    }

    @Override
    public void setHighScore(int highScore) {
        score.setHighScore(highScore);
    }

    @Override
    public void setScore(int score) {
        this.score.setScore(score);
    }
}
