package br.com.easypojo2sheet.exception;

/**
 * Exception específica para erros de extração de propriedades.
 */
public  class PropertyExtractionException extends RuntimeException {
    public PropertyExtractionException(String message) {
        super(message);
    }

    public PropertyExtractionException(String message, Throwable cause){
        super(message, cause);
    }
}