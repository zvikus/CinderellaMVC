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
    CABINET_BARBELL("Штанга", ParentCategory.CABINET),
    CABINET_STACKS("Стопки", ParentCategory.CABINET),
    CABINET_DECORATIONS("Украшения", ParentCategory.CABINET),
    CABINET_TIE("Галстуки", ParentCategory.CABINET),
    CABINET_TROUSERS("Брюки", ParentCategory.CABINET),
    CABINET_SCARF("Шарфы", ParentCategory.CABINET),
    CABINET_HEADDRESS("Головные уборы", ParentCategory.CABINET),
    CABINET_SLIDING_SHELVES("Выдвижные полки", ParentCategory.CABINET),
    CABINET_WARDROBE("Гардеробная", ParentCategory.CABINET),
    CABINET_OFF_SEASON_STORAGE("Несезонное хранение", ParentCategory.CABINET),
    CABINET_UNDERWEAR_SOCKS("Носки/нижнее белье", ParentCategory.CABINET);



    public final ParentCategory parentCategory;
    public final String label;

    private OrganizerCategory(String label, ParentCategory parentCategory) {
        this.label = label;
        this.parentCategory = parentCategory;
    }

    public enum ParentCategory {
        REFRIGERATOR("Холодильник"),
        CABINET("Шкаф");

        public final String label;

        ParentCategory(String label) {
            this.label = label;
        }
    }
}
