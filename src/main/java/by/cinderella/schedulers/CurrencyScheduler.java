package by.cinderella.schedulers;

import by.cinderella.model.currency.Currency;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Configuration
@EnableScheduling
public class CurrencyScheduler {
    @Value("${apilayer.api.key}")
    private String apikey;

    /*@Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void currencyScheduler() throws IOException {
        String urlString = new String("https://www.nbrb.by/api/exrates/rates?periodicity=0");
        URL url = new URL(urlString);
        InputStream is = url.openStream();
        int ptr = 0;
        try {
            StringBuffer buffer = new StringBuffer();
            while ((ptr = is.read()) != -1) {
                buffer.append((char)ptr);
            }

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
    }*/

    /*@Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void currencyScheduler() throws IOException {
        String urlString = new String("https://cdn.cur.su/api/cbr.json");
        URL url = new URL(urlString);
        InputStream is = url.openStream();
        int ptr = 0;
        try {
            StringBuffer buffer = new StringBuffer();
            while ((ptr = is.read()) != -1) {
                buffer.append((char)ptr);
            }

            Object obj = new JSONParser().parse(buffer.toString());
            JSONObject jo = (JSONObject) obj;

            JSONObject rates = (JSONObject) jo.get("rates");

            Double baseRate = (Double) rates.get(Currency.BYN.CUR_ABBREVIATION);

            for (Currency currency : Currency.values()) {
                Double rate = new Double("" +  rates.get(currency.CUR_ABBREVIATION));

                rate = baseRate/rate;
                currency.rate.setCurrencyRate(rate);
            }
        } catch (Exception ex) {
            System.out.println("Currency update failed");
        }
    }*/


    //https://apilayer.com/marketplace/currency_data-api
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void currencySchedulerReserve() throws IOException, ParseException {

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://api.apilayer.com/currency_data/live?source=BYN&currencies=USD%2CRUB%2CUAH%2CKZT")
                .addHeader("apikey", apikey)
                .method("GET", null)
            .build();
        Response response = client.newCall(request).execute();



        Object obj = new JSONParser().parse(response.body().string());
        JSONObject jo = (JSONObject) obj;

        JSONObject rates = (JSONObject) jo.get("quotes");

        for (Currency currency : Currency.values()) {
            if (!currency.CUR_ABBREVIATION.equals(Currency.BYN.CUR_ABBREVIATION)) {
                Double rate = 1 / new Double("" + rates.get(Currency.BYN.CUR_ABBREVIATION + currency.CUR_ABBREVIATION));
                currency.rate.setCurrencyRate(rate);
            }
        }

    }
}
