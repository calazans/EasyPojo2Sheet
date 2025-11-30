package br.com.easypojsheet.core.writer.excel;

import br.com.easypojsheet.core.metadata.ColumnMetadata;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

 enum ExcelCellValueType {
    STRING(String.class) {
        @Override
        void setCellValue(Cell cell, Object value, CellStyleFactory styleFactory, ColumnMetadata column) {
            cell.setCellValue((String) value);
            applyCellStyle(cell, styleFactory.getOrCreateCellStyle(column));
        }
    },
    NUMBER(Number.class) {
        @Override
        void setCellValue(Cell cell, Object value, CellStyleFactory styleFactory, ColumnMetadata column) {
            cell.setCellValue(((Number) value).doubleValue());
            applyCellStyle(cell, styleFactory.getOrCreateNumberStyle(column));
        }
    },
    DATE(Date.class) {
        @Override
        void setCellValue(Cell cell, Object value, CellStyleFactory styleFactory, ColumnMetadata column) {
            cell.setCellValue((Date) value);
            applyCellStyle(cell, styleFactory.getOrCreateDateStyle(column));
        }
    },
    BOOLEAN(Boolean.class) {
        @Override
        void setCellValue(Cell cell, Object value, CellStyleFactory styleFactory, ColumnMetadata column) {
            cell.setCellValue((Boolean) value);
            applyCellStyle(cell, styleFactory.getOrCreateCellStyle(column));
        }
    },
    LOCAL_DATE(LocalDate.class) {
        @Override
        void setCellValue(Cell cell, Object value, CellStyleFactory styleFactory, ColumnMetadata column) {
            cell.setCellValue((LocalDate) value);
            applyCellStyle(cell, styleFactory.getOrCreateDateStyle(column));
        }
    },
    LOCAL_DATE_TIME(LocalDateTime.class) {
        @Override
        void setCellValue(Cell cell, Object value, CellStyleFactory styleFactory, ColumnMetadata column) {
            cell.setCellValue((LocalDateTime) value);
            applyCellStyle(cell, styleFactory.getOrCreateDateStyle(column));
        }
    };

    private static final Map<Class<?>, ExcelCellValueType> TYPE_MAPPING = new HashMap<>();

    static {
        for (ExcelCellValueType type : values()) {
            TYPE_MAPPING.put(type.valueClass, type);
        }
    }

    private final Class<?> valueClass;

    ExcelCellValueType(Class<?> valueClass) {
        this.valueClass = valueClass;
    }


    abstract void setCellValue(Cell cell, Object value, CellStyleFactory styleFactory, ColumnMetadata column);


    protected void applyCellStyle(Cell cell, CellStyle cellStyle) {
        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }
    }


    public static ExcelCellValueType fromClass(Class<?> clazz) {
        ExcelCellValueType type = TYPE_MAPPING.get(clazz);
        if (type != null) {
            return type;
        }


        if (Number.class.isAssignableFrom(clazz)) {
            return NUMBER;
        }


        return STRING;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }
}