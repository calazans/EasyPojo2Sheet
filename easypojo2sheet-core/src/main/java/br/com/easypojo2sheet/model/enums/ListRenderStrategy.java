package br.com.easypojo2sheet.model.enums;

/**
 * Estratégia de renderização para campos do tipo lista.
 */
public enum ListRenderStrategy {
    
    /**
     * Ignora a lista (padrão para listas não anotadas).
     */
    IGNORE,
    
    /**
     * Usa agregação (sum, join, etc) - comportamento padrão quando há property.
     */
    AGGREGATE,

    /**
     * Expande a lista criando múltiplas linhas.
     * A diferença é que as células das colunas que não fazem parte da lista
     * também são expandidas, em vez de terem células merged.
     */
    EXPAND_ROWS,


    /**
     * Expande a lista criando múltiplas linhas.
     * Colunas que não são da lista terão células merged.
     */
    EXPAND_ROWS_WITH_MERGED_ROWS;
}
