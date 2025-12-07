package br.com.easypojo2sheet.api;

import br.com.easypojo2sheet.exception.ExcelExportException;

import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

/**
 * Builder para configurar e criar um ExcelExporter.
 */
public class ExcelExporterBuilder<T> {
    
    private List<T> data;
    private String outputFile;
    private OutputStream outputStream;
    private String sheetName;
    private Locale locale;
    private boolean streamingMode;
    private int rowAccessWindowSize = 100;

    ExcelExporterBuilder() {
    }

    /**
     * Define os dados a serem exportados.
     */
    public ExcelExporterBuilder<T> data(List<T> data) {
        this.data = data;
        return this;
    }

    /**
     * Define o caminho do arquivo de saída.
     */
    public ExcelExporterBuilder<T> outputFile(String filePath) {
        this.outputFile = filePath;
        return this;
    }

    /**
     * Define o OutputStream de saída.
     */
    public ExcelExporterBuilder<T> outputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        return this;
    }

    /**
     * Sobrescreve o nome da sheet definido na anotação.
     */
    public ExcelExporterBuilder<T> sheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    /**
     * Define o Locale para formatação.
     */
    public ExcelExporterBuilder<T> locale(Locale locale) {
        this.locale = locale;
        return this;
    }

    /**
     * Habilita modo streaming para grandes volumes.
     */
    public ExcelExporterBuilder<T> streamingMode(boolean enabled) {
        this.streamingMode = enabled;
        return this;
    }

    /**
     * Define o tamanho da janela de acesso em modo streaming.
     */
    public ExcelExporterBuilder<T> rowAccessWindowSize(int size) {
        this.rowAccessWindowSize = size;
        return this;
    }

    /**
     * Constrói o ExcelExporter.
     */
    public ExcelExporter<T> build() throws ExcelExportException {
        validate();
        return new ExcelExporter<>(this);
    }

    /**
     * Valida as configurações.
     */
    private void validate() throws ExcelExportException {
        if (data == null || data.isEmpty()) {
            throw new ExcelExportException("Data não pode ser null ou vazio");
        }
        if (outputFile == null && outputStream == null) {
            throw new ExcelExportException("Deve especificar outputFile ou outputStream");
        }
    }

    // Getters para ExcelExporter acessar
    List<T> getData() {
        return data;
    }

    String getOutputFile() {
        return outputFile;
    }

    OutputStream getOutputStream() {
        return outputStream;
    }

    String getSheetName() {
        return sheetName;
    }

    Locale getLocale() {
        return locale;
    }

    boolean isStreamingMode() {
        return streamingMode;
    }

    int getRowAccessWindowSize() {
        return rowAccessWindowSize;
    }
}
