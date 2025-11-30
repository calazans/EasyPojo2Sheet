package br.com.easypojsheet.model.enums;

/**
 * Enum para alinhamento vertical de células.
 */
public enum VerticalAlignment {
    TOP,
    CENTER,
    BOTTOM;

    /**
     * Converte para o enum do Apache POI.
     */
    public org.apache.poi.ss.usermodel.VerticalAlignment toPoiAlignment() {
        return switch (this) {
            case TOP -> org.apache.poi.ss.usermodel.VerticalAlignment.TOP;
            case CENTER -> org.apache.poi.ss.usermodel.VerticalAlignment.CENTER;
            case BOTTOM -> org.apache.poi.ss.usermodel.VerticalAlignment.BOTTOM;
        };
    }
}
