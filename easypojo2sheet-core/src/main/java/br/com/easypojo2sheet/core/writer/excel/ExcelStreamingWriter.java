
package br.com.easypojo2sheet.core.writer.excel;

import br.com.easypojo2sheet.core.metadata.ColumnMetadata;
import br.com.easypojo2sheet.core.metadata.SheetMetadata;
import br.com.easypojo2sheet.core.writer.Writer;
import br.com.easypojo2sheet.exception.ExcelExportException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;


/**
 * Responsável por escrever dados em um arquivo Excel usando Apache POI com streaming.
 * Otimizado para grandes volumes de dados usando SXSSFWorkbook.
 */
public class ExcelStreamingWriter implements Writer {

    private static final int DEFAULT_WINDOW_SIZE = 100; // Número de linhas mantidas em memória
    private final SXSSFWorkbook workbook;
    private final SheetMetadata metadata;
    private final CellStyleFactory styleFactory;

    /**
     * Construtor com tamanho de janela padrão (100 linhas em memória).
     */
    public ExcelStreamingWriter(SheetMetadata metadata) {
        this(metadata, DEFAULT_WINDOW_SIZE);
    }

    /**
     * Construtor com tamanho de janela customizado.
     * @param metadata metadados da planilha
     * @param windowSize número de linhas mantidas em memória (-1 para ilimitado, não recomendado)
     */
    public ExcelStreamingWriter(SheetMetadata metadata, int windowSize) {
        this.metadata = metadata;
        // SXSSFWorkbook mantém apenas windowSize linhas em memória
        this.workbook = new SXSSFWorkbook(windowSize);
        this.styleFactory = new CellStyleFactory(workbook);
        // Compressão dos arquivos temporários para economizar espaço em disco
        this.workbook.setCompressTempFiles(false);
    }

    /**
     * Escreve os dados na planilha.
     */
    public <T> void write(List<T> data) throws ExcelExportException {
        try {
            SXSSFSheet sheet = workbook.createSheet(metadata.getSheetName());
            
            // Configurar auto-flush se for SXSSFSheet
            sheet.setRandomAccessWindowSize(DEFAULT_WINDOW_SIZE);


            createHeader(sheet);
            writeData(sheet, data);
            autoSizeColumns(sheet);

        } catch (Exception e) {
            throw new ExcelExportException("Erro ao escrever dados no Excel", e);
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
     * Escreve os dados nas linhas com processamento em batch.
     */
    private <T> void writeData(Sheet sheet, List<T> data) throws ExcelExportException {
        int rowIndex = 1;
        
        try {
            for (T item : data) {
                Row row = sheet.createRow(rowIndex++);
                int colIndex = 0;

                for (ColumnMetadata column : metadata.getColumns()) {
                    Cell cell = row.createCell(colIndex++);
                    Object value = extractValue(item, column);
                    setCellValue(cell, value, column);
                }
                
            }
        } catch (Exception e) {
            throw new ExcelExportException("Erro ao processar dados na linha " + rowIndex, e);
        }
    }

    /**
     * Extrai o valor do campo (com suporte a objetos aninhados).
     */
    private Object extractValue(Object item, ColumnMetadata column) throws Exception {
        if (item == null) {
            return null;
        }

        String propertyPath = column.getPropertyPath();
        if (propertyPath != null && !propertyPath.isEmpty()) {
            return extractNestedValue(item, propertyPath);
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
     * Define o valor da célula com formatação e estilo cacheado.
     */
    private void setCellValue(Cell cell, Object value, ColumnMetadata column) {
        if (value == null) {
            cell.setBlank();
            return;
        }

        ExcelCellValueType.fromClass(value.getClass()).setCellValue(cell, value,styleFactory, column);
    }


    /**
     * Ajusta largura das colunas.
     * ATENÇÃO: autoSizeColumn é custoso com SXSSF, use com cuidado!
     */
    private void autoSizeColumns(Sheet sheet) {

        for (int i = 0; i < metadata.getColumns().size(); i++) {
            ColumnMetadata column = metadata.getColumns().get(i);
            
            if (column.getWidth() > 0) {
                // Usar largura definida (mais eficiente)
                sheet.setColumnWidth(i, column.getWidth() * 256);
            } else {
                // Calcular largura baseada no header + margem
                String headerText = column.getColumnName();
                int estimatedWidth = Math.max(headerText.length() * 256, 10 * 256);
                sheet.setColumnWidth(i, estimatedWidth);
            }
        }
    }

    /**
     * Salva o workbook em um arquivo.
     */
    public void saveToFile(String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            saveToStream(fos);
        } finally {
            // IMPORTANTE: Limpar arquivos temporários
            dispose();
        }
    }

    /**
     * Salva o workbook em um OutputStream.
     */
    public void saveToStream(OutputStream outputStream) throws IOException {
        try {
            workbook.write(outputStream);
            outputStream.flush();
        } finally {
            // IMPORTANTE: Limpar arquivos temporários
            dispose();
        }
    }

    /**
     * Libera recursos e arquivos temporários criados pelo SXSSF.
     * SEMPRE chame este método após terminar de usar o workbook!
     */
    public void dispose() throws IOException {
        if (workbook != null) {
            workbook.close();
        }
    }

    /**
     * Retorna o workbook (útil para customizações adicionais).
     */
    public SXSSFWorkbook getWorkbook() {
        return workbook;
    }
}
