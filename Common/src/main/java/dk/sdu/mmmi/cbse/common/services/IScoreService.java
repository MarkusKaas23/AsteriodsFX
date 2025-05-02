package dk.sdu.mmmi.cbse.common.services;

public interface IScoreService {
    int getScore();
    int getHighScore();
    void incrementScore();
    void setHighScore(int highScore);
    void setScore(int score);

}
