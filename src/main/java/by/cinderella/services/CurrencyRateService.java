package by.cinderella.services;

import by.cinderella.model.currency.Currency;
import by.cinderella.model.organizer.Organizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class CurrencyRateService {
    @Autowired
    private UserService userService;

    public String getPrice(Organizer organizer) {
        Double price = organizer.getPrice();
        Currency currency = organizer.getSeller().currency;
        if (price == null) {
            return "-";
        }

//        Double priceInBYN = (price * currency.rate.getCurrencyRate()) / currency.CURRENCY_SCALE;
        Double priceInBYN = price * currency.rate.getCurrencyRate();


        Currency userCurrency = userService.getUserCurrency();
//        Double result = (priceInBYN / userCurrency.rate.getCurrencyRate()) *userCurrency.CURRENCY_SCALE;
        Double result = priceInBYN / userCurrency.rate.getCurrencyRate();


        return new DecimalFormat(userCurrency.DECIMAL_FORMAT).format(result);
    }

    public static Double getAbsolutePrice(Double price, Currency currency) {
        if (price == null) {
            return 0.0;
        }
//        return  (price * currency.rate.getCurrencyRate()) / currency.CURRENCY_SCALE;
        return  price * currency.rate.getCurrencyRate();
    }

    public static String convert(Double price, Currency base, Currency result) {
        if (price == null) {
            return null;
        }
        Double resultValue;
        resultValue = (price/result.rate.getCurrencyRate())*base.rate.getCurrencyRate();

        return new DecimalFormat(result.DECIMAL_FORMAT).format(resultValue);
    }

    public static Double getAbsolutePrice(Organizer organizer) {
        Double price = organizer.getPrice();
        Currency currency = organizer.getSeller().currency;

        return  getAbsolutePrice(price, currency);
    }
}
