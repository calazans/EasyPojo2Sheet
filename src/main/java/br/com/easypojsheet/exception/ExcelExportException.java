package br.com.easypojsheet.exception;

/**
 * Exception lançada durante o processo de exportação para Excel.
 */
public class ExcelExportException extends ExportException {
    
    public ExcelExportException(String message) {
        super(message);
    }
    
    public ExcelExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
