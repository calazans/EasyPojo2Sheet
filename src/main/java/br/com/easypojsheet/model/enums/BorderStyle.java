package br.com.easypojsheet.model.enums;

/**
 * Estilos de borda de células.
 */
public enum BorderStyle {
    NONE,
    THIN,
    MEDIUM,
    THICK,
    DASHED,
    DOTTED,
    DOUBLE;

    /**
     * Converte para o enum do Apache POI.
     */
    public org.apache.poi.ss.usermodel.BorderStyle toPoiBorderStyle() {
        return switch (this) {
            case NONE -> org.apache.poi.ss.usermodel.BorderStyle.NONE;
            case THIN -> org.apache.poi.ss.usermodel.BorderStyle.THIN;
            case MEDIUM -> org.apache.poi.ss.usermodel.BorderStyle.MEDIUM;
            case THICK -> org.apache.poi.ss.usermodel.BorderStyle.THICK;
            case DASHED -> org.apache.poi.ss.usermodel.BorderStyle.DASHED;
            case DOTTED -> org.apache.poi.ss.usermodel.BorderStyle.DOTTED;
            case DOUBLE -> org.apache.poi.ss.usermodel.BorderStyle.DOUBLE;
        };
    }
}
