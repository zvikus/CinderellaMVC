package by.cinderella.schedulers;

import by.cinderella.model.currency.Currency;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Configuration
@EnableScheduling
public class CurrencyScheduler {
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void currencyScheduler() throws IOException {
        String urlString = new String("https://www.nbrb.by/api/exrates/rates?periodicity=0");
        URL url = new URL(urlString);
        InputStream is = url.openStream();
        int ptr = 0;
        StringBuffer buffer = new StringBuffer();
        while ((ptr = is.read()) != -1) {
            buffer.append((char)ptr);
        }
        try {
            Object obj = new JSONParser().parse(buffer.toString());
            JSONArray ja = (JSONArray) obj;
            for (Object o : ja.toArray()) {
                JSONObject jo = (JSONObject) o;
                for (Currency currency : Currency.values()) {
                    if (currency.CUR_ABBREVIATION.equals(jo.get("Cur_Abbreviation"))) {
                        currency.rate.setCurrencyRate((Double) jo.get("Cur_OfficialRate"));
                    }
                }

            }
        } catch (Exception ex) {
            System.out.println("Currency update failed");
        }
    }
}
