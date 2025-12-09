package br.com.easypojo2sheet.core.writer.excel;

import br.com.easypojo2sheet.core.metadata.ColumnMetadata;
import br.com.easypojo2sheet.core.metadata.SheetMetadata;
import br.com.easypojo2sheet.core.writer.Writer;
import br.com.easypojo2sheet.exception.ExcelExportException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;


/**
 * Responsável por escrever dados em um arquivo Excel usando Apache POI.
 */
public class ExcelWriter implements Writer {

    private final Workbook workbook;
    private final SheetMetadata metadata;
    private final CellStyleFactory styleFactory;

    public ExcelWriter(SheetMetadata metadata) {
        this.metadata = metadata;
        this.workbook = new XSSFWorkbook();
        this.styleFactory = new CellStyleFactory(workbook);
    }

    /**
     * Escreve os dados na planilha.
     */
    public <T> void write(List<T> data) throws ExcelExportException {
        if (data == null || data.isEmpty()) {
            throw new ExcelExportException("Lista de dados está vazia");
        }

        Sheet sheet = workbook.createSheet(metadata.getSheetName());
        
        // Cria header
        createHeader(sheet);
        
        // Escreve dados
        writeData(sheet, data);
        
        // Auto-size se configurado
        if (metadata.isAutoSizeColumns()) {
            autoSizeColumns(sheet);
        }
        
        // Congela header se configurado
        if (metadata.isFreezeHeader()) {
            sheet.createFreezePane(0, metadata.getStartRow() + 1);
        }
    }

    /**
     * Cria a linha de header.
     */
    private void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(metadata.getStartRow());
        CellStyle headerStyle = styleFactory.createHeaderStyle();

        int colIndex = 0;
        for (ColumnMetadata column : metadata.getColumns()) {
            Cell cell = headerRow.createCell(colIndex++);
            cell.setCellValue(column.getColumnName());
            cell.setCellStyle(headerStyle);
        }
    }


    /**
     * Escreve os dados nas linhas.
     */
    private <T> void writeData(Sheet sheet, List<T> data) throws ExcelExportException {
        int rowNum = metadata.getStartRow() + 1;
        List<ColumnMetadata> columns = metadata.getColumns();

        for (T item : data) {
            Row row = sheet.createRow(rowNum++);
            
            for (int colNum = 0; colNum < columns.size(); colNum++) {
                ColumnMetadata column = columns.get(colNum);
                Cell cell = row.createCell(colNum);
                
                try {
                    Object value = extractValue(item, column);
                    setCellValue(cell, value, column);
                } catch (Exception e) {
                    throw new ExcelExportException(
                        "Erro ao extrair valor do campo " + column.getField().getName(), e
                    );
                }
            }
        }
    }

    /**
     * Extrai o valor do campo (com suporte a objetos aninhados).
     */
    private Object extractValue(Object item, ColumnMetadata column) throws Exception {
        if (item == null) {
            return null;
        }

        if (column.hasPropertyPath()) {
            return extractNestedValue(item, column.getPropertyPath());
        }

        Field field = column.getField();
        field.setAccessible(true);
        return field.get(item);


    }

    /**
     * Extrai valor de propriedade aninhada (ex: "vendedor.nome").
     */
    private Object extractNestedValue(Object item, String propertyPath) throws Exception {
        String[] parts = propertyPath.split("\\.");
        Object current = item;

        for (String part : parts) {
            if (current == null) {
                return null;
            }
            
            Field field = current.getClass().getDeclaredField(part);
            field.setAccessible(true);
            current = field.get(current);
        }

        return current;
    }

    /**
     * Define o valor da célula com formatação básica.
     */
    private void setCellValue(Cell cell, Object value, ColumnMetadata column) {
        if (value == null) {
            cell.setBlank();
            return;
        }

        ExcelCellValueType.fromClass(value.getClass()).setCellValue(cell, value,styleFactory, column);

    }

    /**
     * Ajusta largura das colunas automaticamente.
     */
    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < metadata.getColumns().size(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Salva o workbook em um arquivo.
     */
    public void saveToFile(String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }
    }

    /**
     * Salva o workbook em um OutputStream.
     */
    public void saveToStream(OutputStream outputStream) throws IOException {
        try {
            workbook.write(outputStream);
        } finally {
            workbook.close();
        }
    }
}
