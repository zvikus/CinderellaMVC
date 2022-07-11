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

        Double priceInBYN = (price * currency.rate.getCurrencyRate()) / currency.CURRENCY_SCALE;


        Currency userCurrency = userService.getUserCurrency();
        Double result = (priceInBYN / userCurrency.rate.getCurrencyRate()) *userCurrency.CURRENCY_SCALE;


        return new DecimalFormat(userCurrency.DECIMAL_FORMAT).format(result);
    }

    public static Double getAbsolutePrice(Double price, Currency currency) {
        return  (price * currency.rate.getCurrencyRate()) / currency.CURRENCY_SCALE;
    }

    public static Double getAbsolutePrice(Organizer organizer) {
        Double price = organizer.getPrice();
        Currency currency = organizer.getSeller().currency;

        return  getAbsolutePrice(price, currency);
    }
}
