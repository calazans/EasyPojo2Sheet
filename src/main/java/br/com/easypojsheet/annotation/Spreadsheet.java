package br.com.easypojsheet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para marcar uma classe como exportável para planilha.
 * Usada no nível de classe para definir configurações da planilha.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Spreadsheet {
    
    /**
     * Nome da sheet na planilha.
     * @return nome da sheet, ou string vazia para usar o nome da classe
     */
    String name() default "";
    
    /**
     * Chave de internacionalização para o nome da sheet.
     * @return chave i18n, ou string vazia se não usar
     */
    String nameKey() default "";
    
    /**
     * Ajusta automaticamente a largura das colunas baseado no conteúdo.
     * @return true para auto-size, false caso contrário
     */
    boolean autoSizeColumns() default true;
    
    /**
     * Congela a linha de cabeçalho para rolagem.
     * @return true para congelar header, false caso contrário
     */
    boolean freezeHeader() default true;
    
    /**
     * Linha inicial onde os dados começam (0-based).
     * Útil para deixar espaço para títulos customizados.
     * @return número da linha inicial
     */
    int startRow() default 0;
}
