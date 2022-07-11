package by.cinderella.model.organizer;

import by.cinderella.model.currency.Currency;

public enum Seller {
    WB("Wildberries", Currency.RUB),
    IKEA("IKEA", Currency.BYN),
    OZON("Ozon", Currency.BYN),
    ALIEXPRESS("AliExpress", Currency.USD),
    ABRA_BY("abra.by", Currency.BYN),
    THREE_PRICE("Три цены", Currency.BYN),
    VEK_21("21 ВЕК", Currency.BYN),
    OZ_BY("OZ.BY", Currency.BYN),
    GROSHYK("Грошык", Currency.BYN),
    OMA("ОМА", Currency.BYN),
    EASYDOM("easydom.by", Currency.BYN),
    FIXPRICE("Fix Price", Currency.BYN);

    public final String label;
    public final Currency currency;

    private Seller(String label, Currency currency) {
        this.label = label;
        this.currency = currency;
    }
}
