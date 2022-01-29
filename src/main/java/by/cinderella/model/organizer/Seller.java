package by.cinderella.model.organizer;

public enum Seller {
    WB("Wildberries"),
    IKEA("IKEA"),
    OZON("Ozon"),
    ALIEXPRESS("AliExpress"),
    FIXPRICE("Fix Price");

    public final String label;

    private Seller(String label) {
        this.label = label;
    }
}
