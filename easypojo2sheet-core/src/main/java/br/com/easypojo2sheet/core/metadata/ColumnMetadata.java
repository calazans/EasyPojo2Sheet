package br.com.easypojo2sheet.core.metadata;

import br.com.easypojo2sheet.model.enums.HorizontalAlignment;
import br.com.easypojo2sheet.model.enums.ListRenderStrategy;
import br.com.easypojo2sheet.model.enums.VerticalAlignment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Metadados extraídos de um campo anotado com @SheetColumn.
 */
public class ColumnMetadata {
    private final Field field;
    private final Method method;
    private final String columnName;
    private final int order;
    private final int width;
    private final String propertyPath;
    private final String dateFormat;
    private final String numberFormat;
    private final HorizontalAlignment horizontalAlignment;
    private final VerticalAlignment verticalAlignment;
    private final String separator;
    private final boolean isListField;
    private final ListRenderStrategy listStrategy;

    public ColumnMetadata(Field field, String columnName, int order, int width,
                          String propertyPath, String dateFormat, String numberFormat,
                          HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment,
                          String separator,ListRenderStrategy listStrategy,Method method) {
        this.field = field;
        this.columnName = columnName;
        this.order = order;
        this.width = width;
        this.propertyPath = propertyPath;
        this.dateFormat = dateFormat;
        this.numberFormat = numberFormat;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        this.separator = separator;
        this.listStrategy = listStrategy;
        this.method = method;

        if (method != null) {
            this.isListField = List.class.isAssignableFrom(method.getReturnType());
        } else if (field != null) {
            this.isListField = List.class.isAssignableFrom(field.getType());
        } else {
            this.isListField = false;
        }
    }

    public Field getField() {
        return field;
    }

    public boolean isMethod() {
        return method != null;
    }
    public Object extractValue(Object instance) throws Exception {
        if (instance == null) {
            return null;
        }

        if(method != null) {
            return method.invoke(instance);
        }
        field.setAccessible(true);
        return field.get(instance);
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

    public String getSeparator() {
        return separator;
    }

    public boolean isListField() {
        return isListField;
    }

    public ListRenderStrategy getListStrategy() {
        return listStrategy;
    }

    public boolean shouldExpandRows() {
        return isListField && (listStrategy == ListRenderStrategy.EXPAND_ROWS || listStrategy == ListRenderStrategy.EXPAND_ROWS_WITH_MERGED_ROWS);
    }

    public boolean shouldMergeRows() {
        return isListField && listStrategy == ListRenderStrategy.EXPAND_ROWS_WITH_MERGED_ROWS;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Field field;
        private String columnName;
        private int order = Integer.MAX_VALUE;
        private int width = -1;
        private String propertyPath;
        private String separator = ", ";
        private String dateFormat;
        private String numberFormat;
        private HorizontalAlignment align = HorizontalAlignment.AUTO;
        private VerticalAlignment valign = VerticalAlignment.CENTER;
        private ListRenderStrategy listStrategy = ListRenderStrategy.AGGREGATE;
        private Method method;

        public Builder field(Field field) {
            this.field = field;
            return this;
        }

        public Builder columnName(String columnName) {
            this.columnName = columnName;
            return this;
        }

        public Builder order(int order) {
            this.order = order;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder propertyPath(String propertyPath) {
            this.propertyPath = propertyPath;
            return this;
        }

        public Builder separator(String separator) {
            this.separator = separator;
            return this;
        }

        public Builder dateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
            return this;
        }

        public Builder numberFormat(String numberFormat) {
            this.numberFormat = numberFormat;
            return this;
        }

        public Builder align(HorizontalAlignment align) {
            this.align = align;
            return this;
        }

        public Builder valign(VerticalAlignment valign) {
            this.valign = valign;
            return this;
        }

        public Builder listStrategy(ListRenderStrategy listStrategy) {
            this.listStrategy = listStrategy;
            return this;
        }

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public ColumnMetadata build() {
            return new ColumnMetadata(field, columnName, order, width, propertyPath,
                    dateFormat, numberFormat, align, valign,separator,listStrategy,method);
        }
    }


}
