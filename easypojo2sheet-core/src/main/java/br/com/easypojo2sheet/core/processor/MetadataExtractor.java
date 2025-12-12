
package br.com.easypojo2sheet.core.processor;

import br.com.easypojo2sheet.annotation.SheetColumns;
import br.com.easypojo2sheet.annotation.Spreadsheet;
import br.com.easypojo2sheet.annotation.SheetColumn;
import br.com.easypojo2sheet.annotation.SheetIgnore;
import br.com.easypojo2sheet.core.metadata.ColumnMetadata;
import br.com.easypojo2sheet.core.metadata.SheetMetadata;
import br.com.easypojo2sheet.exception.ExcelExportException;
import br.com.easypojo2sheet.model.enums.HorizontalAlignment;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Extrai metadados de classes anotadas usando reflection.
 */
public class MetadataExtractor {

    /**
     * Extrai metadados de uma classe anotada com @SpreadSheet.
     */
    public static SheetMetadata extractMetadata(Class<?> clazz) throws ExcelExportException {
        if (!clazz.isAnnotationPresent(Spreadsheet.class)) {
            throw new ExcelExportException("Classe " + clazz.getName() + " não está anotada com @SpreadSheet");
        }

        Spreadsheet sheetAnnotation = clazz.getAnnotation(Spreadsheet.class);
        var sheetName = sheetAnnotation.name().isEmpty()? clazz.getSimpleName() : sheetAnnotation.name();

        List<ColumnMetadata> columns = extractColumns(clazz);

        return new SheetMetadata(clazz,sheetName,sheetAnnotation.autoSizeColumns(),sheetAnnotation.freezeHeader()
                ,sheetAnnotation.startRow(),columns);
    }

    /**
     * Extrai metadados das colunas (campos anotados).
     */
    private static List<ColumnMetadata> extractColumns(Class<?> clazz) {
        List<ColumnMetadata> columns = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // Ignora campos marcados com @SheetIgnore
            if (field.isAnnotationPresent(SheetIgnore.class)) {
                continue;
            }

            SheetColumns sheetColumnsAnnotation = field.getAnnotation(SheetColumns.class);
            // Campos que possuem mais de uma anotacao que são campos de objetos aninhados
            if (sheetColumnsAnnotation != null) {
                for(SheetColumn column : sheetColumnsAnnotation.value()){
                    createColumnMetadata(field, column, columns);
                }
            }

            if (sheetColumnsAnnotation == null) {
                // Campos sem @SheetColumn são incluídos com valores padrão
                SheetColumn columnAnnotation = field.getAnnotation(SheetColumn.class);
                createColumnMetadata(field, columnAnnotation, columns);
            }

        }

        // Ordena por order, depois por nome
        columns.sort(Comparator
            .comparingInt(ColumnMetadata::getOrder)
            .thenComparing(ColumnMetadata::getColumnName)
        );

        return columns;
    }

    private static void createColumnMetadata(Field field, SheetColumn columnAnnotation, List<ColumnMetadata> columns) {
        var columnName = field.getName();
        var order = Integer.MAX_VALUE;
        var width = -1;
        var propertyPath = "";
        var dateFormat = "";
        var numberFormat = "";
        var horizontalAlignment=HorizontalAlignment.AUTO;
        var verticalAlignment = br.com.easypojo2sheet.model.enums.VerticalAlignment.CENTER;

        if (columnAnnotation != null) {
            columnName = columnAnnotation.name().isEmpty()? field.getName(): columnAnnotation.name();
            order = columnAnnotation.order();
            width = columnAnnotation.width();
            propertyPath = columnAnnotation.property();
            dateFormat = columnAnnotation.dateFormat();
            numberFormat = columnAnnotation.numberFormat();
            horizontalAlignment = columnAnnotation.align();
            verticalAlignment = columnAnnotation.valign();
        }
        field.setAccessible(true);

        columns.add(new ColumnMetadata(
                field, columnName, order, width, propertyPath,
            dateFormat, numberFormat, horizontalAlignment, verticalAlignment
        ));
    }
}
