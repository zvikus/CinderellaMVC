package by.cinderella.schedulers;

import by.cinderella.model.currency.Currency;
import by.cinderella.model.organizer.Organizer;
import by.cinderella.repos.OrganizerRepo;
import by.cinderella.services.OrganizerService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@Configuration
@EnableScheduling
public class OrganizerScheduler {
    @Autowired
    private OrganizerService organizerService;

    @Autowired
    private OrganizerRepo organizerRepo;

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000, initialDelay = 1000)
    @Async
    public void wildberriesPriceScheduler() throws IOException, InterruptedException {
        while (Currency.RUB.rate.getCurrencyRate() == null) {
            Thread.sleep(4000);
        }

        List<Organizer> organizers = organizerService.getWildberriesOrganizers();

        for(Organizer organizer : organizers) {
            if (organizer.getArticleNumber() != null) {
                String urlString = new String("https://wbx-content-v2.wbstatic.net/price-history/" + organizer.getArticleNumber() + ".json");
                URL url = new URL(urlString.replaceAll("\\s+",""));
                InputStream is = url.openStream();
                int ptr = 0;
                StringBuffer buffer = new StringBuffer();
                while ((ptr = is.read()) != -1) {
                    buffer.append((char)ptr);
                }
                try {
                    Object obj = new JSONParser().parse(buffer.toString());
                    JSONArray jo = (JSONArray) obj;
                    JSONObject last = (JSONObject) jo.get(jo.size()-1);
                    if (!last.isEmpty()) {
                        JSONObject price = (JSONObject) last.get("price");
                        Long priceRub = (Long) price.get("RUB")/100;

                        organizer.setPrice(new Double(priceRub));

                        organizerService.save(organizer);
                    }
                } catch (Exception ex) {
                    System.out.println("Wildberries update failed! Organizer ID: " + organizer.getId() + " Article number: " + organizer.getArticleNumber());
                }
            }
        }
    }

//    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
//    @Async
//    //todo: replace
//    public void priceScheduler() throws IOException, InterruptedException {
//        List<Organizer> organizers = organizerRepo.findAll();
//
//        for(Organizer organizer : organizers) {
//            organizerService.save(organizer);
//        }
//    }
}
