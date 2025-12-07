package br.com.easypojo2sheet.core.metadata;

import java.util.List;

/**
 * Metadados extraídos de uma classe anotada com @SpreadSheet.
 */
public class SheetMetadata {
    private final Class<?> entityClass;
    private final String sheetName;
    private final boolean autoSizeColumns;
    private final boolean freezeHeader;
    private final int startRow;
    private final List<ColumnMetadata> columns;

    public SheetMetadata(Class<?> entityClass, String sheetName, boolean autoSizeColumns,
                         boolean freezeHeader, int startRow, List<ColumnMetadata> columns) {
        this.entityClass = entityClass;
        this.sheetName = sheetName;
        this.autoSizeColumns = autoSizeColumns;
        this.freezeHeader = freezeHeader;
        this.startRow = startRow;
        this.columns = columns;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getSheetName() {
        return sheetName;
    }

    public boolean isAutoSizeColumns() {
        return autoSizeColumns;
    }

    public boolean isFreezeHeader() {
        return freezeHeader;
    }

    public int getStartRow() {
        return startRow;
    }

    public List<ColumnMetadata> getColumns() {
        return columns;
    }
}
