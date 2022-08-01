package by.cinderella.model.currency;

import java.io.Serializable;

public enum Currency implements Serializable {
    USD("USD", "Доллар США", 1, "0.0#"),
    RUB("RUB", "Российский рубль", 100, "#"),
    UAH("UAH", "Гривна", 100, "#"),
    KZT("KZT", "Тенге", 1000,"#"),

    BYN("BYN", "Белорусский рубль", 1, new Rate(1.0), "0.0#")
    ;

    public final String CUR_ABBREVIATION;
    public final String LABEL;
    public final String DECIMAL_FORMAT;
    public final Rate rate;
    public final int CURRENCY_SCALE;

    Currency(String CUR_ABBREVIATION, String LABEL, int CURRENCY_SCALE, String DECIMAL_FORMAT) {
        this.CUR_ABBREVIATION = CUR_ABBREVIATION;
        this.CURRENCY_SCALE = CURRENCY_SCALE;
        this.LABEL = LABEL;
        this.DECIMAL_FORMAT = DECIMAL_FORMAT;
        this.rate = new Rate();
    }

    Currency(String CUR_ABBREVIATION, String LABEL, int CURRENCY_SCALE, Rate value, String DECIMAL_FORMAT) {
        this.CUR_ABBREVIATION = CUR_ABBREVIATION;
        this.CURRENCY_SCALE = CURRENCY_SCALE;
        this.DECIMAL_FORMAT = DECIMAL_FORMAT;
        this.LABEL = LABEL;
        this.rate = value;
    }

    public static class Rate {
        private Double currencyRate;

        public Rate() {}

        public Rate(Double currencyRate) {
            this.currencyRate = currencyRate;
        }

        public Double getCurrencyRate() {
            return currencyRate;
        }

        public void setCurrencyRate(Double currencyRate) {
            this.currencyRate = currencyRate;
        }
    }
}
