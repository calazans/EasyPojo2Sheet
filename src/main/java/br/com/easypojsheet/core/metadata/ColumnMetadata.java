package br.com.easypojsheet.core.metadata;

import br.com.easypojsheet.model.enums.HorizontalAlignment;
import br.com.easypojsheet.model.enums.VerticalAlignment;

import java.lang.reflect.Field;

/**
 * Metadados extraídos de um campo anotado com @SheetColumn.
 */
public class ColumnMetadata {
    private final Field field;
    private final String columnName;
    private final int order;
    private final int width;
    private final String propertyPath;
    private final String dateFormat;
    private final String numberFormat;
    private final HorizontalAlignment horizontalAlignment;
    private final VerticalAlignment verticalAlignment;

    public ColumnMetadata(Field field, String columnName, int order, int width,
                          String propertyPath, String dateFormat, String numberFormat,
                          HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
        this.field = field;
        this.columnName = columnName;
        this.order = order;
        this.width = width;
        this.propertyPath = propertyPath;
        this.dateFormat = dateFormat;
        this.numberFormat = numberFormat;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
    }

    public Field getField() {
        return field;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getOrder() {
        return order;
    }

    public int getWidth() {
        return width;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public boolean hasPropertyPath() {
        return propertyPath != null && !propertyPath.isEmpty();
    }
}
