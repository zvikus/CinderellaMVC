package by.cinderella.model.organizer;

public enum Seller {
    WB("Wildberries"),
    IKEA("IKEA"),
    OZON("Ozon"),
    ALIEXPRESS("AliExpress"),
    ABRA_BY("abra.by"),
    THREE_PRICE("Три цены"),
    VEK_21("21 ВЕК"),
    OZ_BY("OZ.BY"),
    GROSHYK("Грошык"),
    FIXPRICE("Fix Price");

    public final String label;

    private Seller(String label) {
        this.label = label;
    }
}
