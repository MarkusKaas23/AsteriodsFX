package dk.sdu.mmmi.cbse.common.data;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Score {
    public int score;
    public int highScore;
    private static Score instance;

    public static Score getInstance(){
        if(instance == null){
            instance = new Score();
        }
        return instance;
    }

    private Score(){
        this.score = 0;
        try{
            File file = new File("highscore.txt");
            file.createNewFile();

            Scanner scanner = new Scanner(file);
            if(scanner.hasNextInt()){
                this.highScore = scanner.nextInt();
            } else {
                this.highScore = 0;
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getScore() {
        return score;
    }
    public void setHighScore(int highScore) {
        try{
            FileWriter fileWriter = new FileWriter("highscore.txt");
            fileWriter.write(String.valueOf(highScore));
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getHighScore() {
        return highScore;
    }
    public void incrementScore(int increment) {
        this.score += increment;
    }
}
