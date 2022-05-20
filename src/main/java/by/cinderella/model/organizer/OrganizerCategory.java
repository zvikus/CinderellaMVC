package by.cinderella.model.organizer;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public enum OrganizerCategory {
    REFRIGERATOR_DOOR("Дверца", ParentCategory.REFRIGERATOR),
    REFRIGERATOR_FREEZER("Морозилка", ParentCategory.REFRIGERATOR),
    REFRIGERATOR_VEGETABLES("Овощной отдел", ParentCategory.REFRIGERATOR),
    REFRIGERATOR_MAIN("Основная камера", ParentCategory.REFRIGERATOR),

    CABINET_BAGS("Сумки", ParentCategory.CABINET),
    CABINET_SHOES("Обувь", ParentCategory.CABINET),
    CABINET_WEAR("Одежда", ParentCategory.CABINET),
    CABINET_BARBELL("Штанга", ParentCategory.CABINET),
    CABINET_STACKS("Стопки", ParentCategory.CABINET),
    CABINET_DECORATIONS("Украшения", ParentCategory.CABINET),
    CABINET_TIE("Галстуки", ParentCategory.CABINET),
    CABINET_TROUSERS("Брюки", ParentCategory.CABINET),
    CABINET_SCARF("Шарфы", ParentCategory.CABINET),
    CABINET_MEZZANINE("Антресоли", ParentCategory.CABINET),
    CABINET_HEADDRESS("Головные уборы", ParentCategory.CABINET),
    CABINET_SLIDING_SHELVES("Выдвижные полки", ParentCategory.CABINET),
    CABINET_WARDROBE("Гардеробная", ParentCategory.CABINET),
    CABINET_OFF_SEASON_STORAGE("Несезонное хранение", ParentCategory.CABINET),
    CABINET_UNDERWEAR_SOCKS("Носки/нижнее белье", ParentCategory.CABINET),
    CABINET_MAINTENANCE("Хозяйственный отдел", ParentCategory.CABINET),

    KITCHEN_WASHING_AREA("Зона мойки", ParentCategory.KITCHEN),
    KITCHEN_COOKING_AREA("Зона готовки", ParentCategory.KITCHEN),
    KITCHEN_GRAINS("Хранение круп", ParentCategory.KITCHEN),
    KITCHEN_SPICES("Хранение специй", ParentCategory.KITCHEN),
    KITCHEN_DISHES("Хранение посуды", ParentCategory.KITCHEN),
    KITCHEN_TEA_ZONE("Чайная зона", ParentCategory.KITCHEN),
    KITCHEN_TOP_BOXES("Верхние шкафы", ParentCategory.KITCHEN),
    KITCHEN_BOTTOM_BOXES("Нижние шкафы", ParentCategory.KITCHEN),
    KITCHEN_DRAWERS("Выдвижные ящики", ParentCategory.KITCHEN),
    KITCHEN_BAKERY("Все для выпечки", ParentCategory.KITCHEN),
    KITCHEN_VEGETABLES("Хранение овощей", ParentCategory.KITCHEN),
    KITCHEN_TRASH_BOX("Утилизация мусора", ParentCategory.KITCHEN),
    KITCHEN_RESERVES("Хранение запасов", ParentCategory.KITCHEN),
    KITCHEN_CONTAINERS("Хранение контейнеров", ParentCategory.KITCHEN),
    KITCHEN_MEZZANINE("Антресоли", ParentCategory.KITCHEN),
    KITCHEN_TOOLS("Бытовая техника", ParentCategory.KITCHEN),
    KITCHEN_PETS("Домашние животные", ParentCategory.KITCHEN),



    DOCUMENTS_VERTICAL("Вертикальное хранение", ParentCategory.DOCUMENTS),
    DOCUMENTS_HORIZONTAL("Гориз-е хранение", ParentCategory.DOCUMENTS),

    MEDICINE_MEDICINE("Аптечка", ParentCategory.MEDICINE),
    CABLES_CABLES("Хранение проводов", ParentCategory.CABLES),
    OTHER_GARAGE("Гараж", ParentCategory.OTHER),
    OTHER_BALCONY("Балкон/Кладовка", ParentCategory.OTHER),
    OTHER_RELOCATION("Переезд", ParentCategory.OTHER),

    BATHROOM_WASHBATHIN("Зона умывальника", ParentCategory.BATHROOM),
    BATHROOM_SHOWER("Зона душа", ParentCategory.BATHROOM),
    BATHROOM_WASHING("Зона стирки", ParentCategory.BATHROOM),
    BATHROOM_TOILET("Туалет", ParentCategory.BATHROOM),
    BATHROOM_RESERVES("Зона хранения", ParentCategory.BATHROOM),

    CHILDREN_CREATION("Зона творчества", ParentCategory.CHILDREN),
    CHILDREN_BOARD_GAMES("Настольные игры", ParentCategory.CHILDREN),
    CHILDREN_EDUCATION("Зона учебы", ParentCategory.CHILDREN),
    CHILDREN_CONSTRUCTORS("Конструкторы", ParentCategory.CHILDREN),
    CHILDREN_TOYS("Игрушки", ParentCategory.CHILDREN);



    public final ParentCategory parentCategory;
    public final String label;

    private OrganizerCategory(String label, ParentCategory parentCategory) {
        this.label = label;
        this.parentCategory = parentCategory;
    }

    public enum ParentCategory {
        REFRIGERATOR("Холодильник"),
        CABINET("Шкаф"),
        KITCHEN("Кухня"),
        MEDICINE("Аптечка"),
        DOCUMENTS("Документы"),
        CABLES("Провода"),
        CHILDREN("Детская"),

        OTHER("Другое"),

        BATHROOM("Ванная");

        public final String label;

        ParentCategory(String label) {
            this.label = label;
        }
    }
}
