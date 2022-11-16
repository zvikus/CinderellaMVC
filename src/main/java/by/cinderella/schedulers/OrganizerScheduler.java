package by.cinderella.schedulers;

import by.cinderella.model.currency.Currency;
import by.cinderella.model.organizer.Organizer;
import by.cinderella.repos.OrganizerRepo;
import by.cinderella.services.CinderellaMailSender;
import by.cinderella.services.OrganizerService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableScheduling
public class OrganizerScheduler {
    @Autowired
    private OrganizerService organizerService;

    @Value("${application.url}")
    private String applicationUrl;

    @Value("${application.notification.email}")
    private String notificationEmail;

    @Autowired
    private CinderellaMailSender mailSender;

    @Autowired
    private OrganizerRepo organizerRepo;

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000, initialDelay = 10000)
    @Async
    public void wildberriesPriceScheduler() throws IOException, InterruptedException {
        while (Currency.RUB.rate.getCurrencyRate() == null || Currency.RUB.rate.getCurrencyRate().equals(1.0)) {
            Thread.sleep(4000);
        }

        List<Organizer> organizers = organizerService.getWildberriesOrganizers();
        List<Organizer> failedOrganizers = new ArrayList<>();

        for(Organizer organizer : organizers) {
            if (organizer.getArticleNumber() != null
                    && !organizer.getArticleNumber().isEmpty()) {
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
                    if (organizer.getPrice() == null) {
                        failedOrganizers.add(organizer);
                    }
                }
            }
        }

        if (!failedOrganizers.isEmpty()) {
            StringBuilder message = new StringBuilder(
                    "Приветствую! \n" +
                            "Не удалось проставить цены для следующих органайзеров: \n"
            );

            for (Organizer o:failedOrganizers) {
                message.
                        append(applicationUrl).
                        append("/admin/organizer/").
                        append(o.getId()).
                        append("/edit").
                        append("\n");
            }

            message.append( "Хорошего дня!");

            mailSender.send(notificationEmail,
                    "Сообщение об ошибке в обновлении цен",
                    message.toString());
        }
    }
}
