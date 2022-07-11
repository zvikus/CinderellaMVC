package by.cinderella.model.organizer;

public enum Material {
    PLASTIC("Пластик"),
    ACRYLIC("Акрил"),
    PAPER("Картон"),
    GLASS("Стекло"),
    CERAMICS("Керамика"),
    METAL("Металл"),
    WOOD("Дерево"),
    TEXTILE("Ткань"),
    STICKERS("Наклейки"),
    CHEMICALS("Бытовая химия"),
    LEATHER("Кожа");

    public final String label;

    private Material(String label) {
        this.label = label;
    }
}
