
package br.com.easypojo2sheet.core.processor;

import br.com.easypojo2sheet.annotation.SheetColumns;
import br.com.easypojo2sheet.annotation.Spreadsheet;
import br.com.easypojo2sheet.annotation.SheetColumn;
import br.com.easypojo2sheet.annotation.SheetIgnore;
import br.com.easypojo2sheet.core.metadata.ColumnMetadata;
import br.com.easypojo2sheet.core.metadata.SheetMetadata;
import br.com.easypojo2sheet.exception.ExcelExportException;
import br.com.easypojo2sheet.model.enums.HorizontalAlignment;
import br.com.easypojo2sheet.model.enums.ListRenderStrategy;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        // Processa campos
        extractFieldColumns(clazz, columns);

        // Processa métodos
        extractMethodColumns(clazz, columns);

        // Ordena por order, depois por nome
        columns.sort(Comparator
            .comparingInt(ColumnMetadata::getOrder)
            .thenComparing(ColumnMetadata::getColumnName)
        );

        return columns;
    }

    private static void extractFieldColumns(Class<?> clazz, List<ColumnMetadata> columns) {
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
    }

    private static void extractMethodColumns(Class<?> clazz, List<ColumnMetadata> columns) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(SheetIgnore.class)) {
                continue;
            }

            SheetColumn annotation = method.getAnnotation(SheetColumn.class);
            if (annotation != null) {
                validateMethod(method);
                createColumnMetadataFromMethod(method, annotation,columns);
            }
        }
    }

    private static void validateMethod(Method method) {
        if (method.getParameterCount() > 0) {
            throw new IllegalArgumentException(
                    "Método " + method.getName() + " não pode ter parâmetros"
            );
        }
        if (method.getReturnType() == void.class) {
            throw new IllegalArgumentException(
                    "Método " + method.getName() + " deve retornar um valor"
            );
        }
    }

    private static void createColumnMetadataFromMethod(Method method, SheetColumn columnAnnotation, List<ColumnMetadata> columns) {
        // Nome da coluna: usa o valor da anotação ou o nome do método (sem 'get' se for getter)
        String columnName = columnAnnotation.name().isEmpty()
                ? extractMethodColumnName(method)
                : columnAnnotation.name();

        int order = columnAnnotation.order();
        int width = columnAnnotation.width();
        String propertyPath = columnAnnotation.property();
        String dateFormat = columnAnnotation.dateFormat();
        String numberFormat = columnAnnotation.numberFormat();
        HorizontalAlignment horizontalAlignment = columnAnnotation.align();
        var verticalAlignment = columnAnnotation.valign();
        ListRenderStrategy listStrategy = columnAnnotation.listStrategy();
        String separator = columnAnnotation.separator();

        method.setAccessible(true);

        columns.add(ColumnMetadata.builder()
                .method(method)
                .columnName(columnName)
                .order(order)
                .width(width)
                .propertyPath(propertyPath)
                .listStrategy(listStrategy)
                .separator(separator)
                .dateFormat(dateFormat)
                .numberFormat(numberFormat)
                .align(horizontalAlignment)
                .valign(verticalAlignment)
                .build());
    }

    private static String extractMethodColumnName(Method method) {
        String methodName = method.getName();

        // Remove prefixo 'get' ou 'is' se for um getter padrão
        if (methodName.startsWith("get") && methodName.length() > 3) {
            return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        }

        if (methodName.startsWith("is") && methodName.length() > 2) {
            return Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
        }

        return methodName;
    }

    private static void createColumnMetadata(Field field, SheetColumn columnAnnotation, List<ColumnMetadata> columns) {
        var columnName = field.getName();
        var order = Integer.MAX_VALUE;
        var width = -1;
        String propertyPath = null;
        String dateFormat = null;
        String  numberFormat = null;
        var horizontalAlignment=HorizontalAlignment.AUTO;
        var verticalAlignment = br.com.easypojo2sheet.model.enums.VerticalAlignment.CENTER;
        var listStategy = ListRenderStrategy.AGGREGATE;
        String separator = null;

        if (columnAnnotation != null) {
            columnName = columnAnnotation.name().isEmpty()? field.getName(): columnAnnotation.name();
            order = columnAnnotation.order();
            width = columnAnnotation.width();
            propertyPath = columnAnnotation.property();
            dateFormat = columnAnnotation.dateFormat();
            numberFormat = columnAnnotation.numberFormat();
            horizontalAlignment = columnAnnotation.align();
            verticalAlignment = columnAnnotation.valign();
            listStategy = columnAnnotation.listStrategy();
            separator = columnAnnotation.separator();
        }
        field.setAccessible(true);

        columns.add(ColumnMetadata.builder()
                .field(field)
                .columnName(columnName)
                .order(order)
                .width(width)
                .propertyPath(propertyPath)
                .listStrategy(listStategy)
                .separator(separator)
                .dateFormat(dateFormat)
                .numberFormat(numberFormat)
                .align(horizontalAlignment)
                .valign(verticalAlignment)
                .build());
    }
}
