package by.cinderella.schedulers;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class WildberriesScheduler {
    /*@Scheduled(fixedDelay = 60 * 1000)
    public void priceScheduler() {
        System.out.println(
                "Fixed delay task - " + System.currentTimeMillis() / 1000);
    }*/
}
