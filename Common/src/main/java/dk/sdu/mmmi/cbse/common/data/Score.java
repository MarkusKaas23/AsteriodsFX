package dk.sdu.mmmi.cbse.common.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Score {
    private int score;
    private int highScore;
    private static final String FILE_NAME = "highscore.txt";
    private static Score instance;

    private Score() {
        this.score = 0;
        this.highScore = readHighScoreFromFile();
    }

    public static Score getInstance() {
        if (instance == null) {
            instance = new Score();
        }
        return instance;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    public void incrementScore(int increment) {
        this.score += increment;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int newHighScore) {
        this.highScore = newHighScore;
        writeHighScoreToFile(newHighScore);
    }

    private int readHighScoreFromFile() {
        File file = new File(FILE_NAME);
        try {
            if (!file.exists()) {
                file.createNewFile();
                return 0;
            }

            Scanner scanner = new Scanner(file);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.close();
                return value;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void writeHighScoreToFile(int score) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write(String.valueOf(score));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
