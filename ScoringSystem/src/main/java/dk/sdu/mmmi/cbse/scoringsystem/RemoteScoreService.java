/*package dk.sdu.mmmi.cbse.scoringsystem;

import dk.sdu.mmmi.cbse.common.services.IScoreService;
import org.springframework.web.client.RestTemplate;

public class RemoteScoreService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String pointServiceUrl = "http://localhost:8080/score";

    private int cachedScore = 0;

    @Override
    public void incrementScore() {
        cachedScore = restTemplate.getForObject(pointServiceUrl + "?point=50", Long.class).intValue();
    }

    @Override
    public int getScore() {
        return cachedScore;
    }

    @Override
    public int getHighScore() {

        return 0;
    }

    @Override
    public void setHighScore(int highScore) {

    }

    @Override
    public void setScore(int score) {

    }
}

 */
