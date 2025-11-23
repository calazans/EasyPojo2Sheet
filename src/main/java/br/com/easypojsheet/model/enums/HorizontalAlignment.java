package br.com.easypojsheet.model.enums;

/**
 * Enum para alinhamento horizontal de células.
 */
public enum HorizontalAlignment {
    AUTO,
    LEFT,
    CENTER,
    RIGHT,
    JUSTIFY;

    /**
     * Converte para o enum do Apache POI.
     */
    public org.apache.poi.ss.usermodel.HorizontalAlignment toPoiAlignment() {
        return switch (this) {
            case LEFT -> org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT;
            case CENTER -> org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;
            case RIGHT -> org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT;
            case JUSTIFY -> org.apache.poi.ss.usermodel.HorizontalAlignment.JUSTIFY;
            case AUTO -> org.apache.poi.ss.usermodel.HorizontalAlignment.GENERAL;
        };
    }
}
