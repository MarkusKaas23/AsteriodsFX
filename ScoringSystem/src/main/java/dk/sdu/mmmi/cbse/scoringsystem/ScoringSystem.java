package dk.sdu.mmmi.cbse.scoringsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class ScoringSystem {

    private Long totalScore = 0L;

    public static void main(String[] args) {
        SpringApplication.run(ScoringSystem.class, args);
    }


    @GetMapping("/score")
    public Long calculateScore(@RequestParam("point") Long point) {
        totalScore += point;
        return totalScore;
    }


    @GetMapping("/score/total")
    public Long getTotalScore() {
        return totalScore;
    }
}
