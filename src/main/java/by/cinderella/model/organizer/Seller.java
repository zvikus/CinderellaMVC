package by.cinderella.model.organizer;

import by.cinderella.model.currency.Currency;

public enum Seller {
    WB("Wildberries", Currency.RUB, null),
    IKEA("IKEA", Currency.BYN, null),
    OZON("Ozon", Currency.RUB, null),
    ALIEXPRESS("AliExpress", Currency.USD, null),
    ABRA_BY("abra.by", Currency.BYN, null),
    THREE_PRICE("Три цены", Currency.BYN, null),
    VEK_21("21 ВЕК", Currency.BYN, null),
    OZ_BY("OZ.BY", Currency.BYN, null),
    GROSHYK("Грошык", Currency.BYN, null),
    OMA("ОМА", Currency.BYN, null),
    EASYDOM("easydom.by", Currency.BYN, "По промокоду \"Cinderella\" скидка 5%"),
    FIXPRICE("Fix Price", Currency.BYN, null);

    public final String label;
    public final Currency currency;
    public final String promo;

    private Seller(String label, Currency currency, String promo) {
        this.label = label;
        this.currency = currency;
        this.promo = promo;
    }
}
