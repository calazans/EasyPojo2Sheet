package br.com.easypojo2sheet.core.writer.excel;

import br.com.easypojo2sheet.core.metadata.ColumnMetadata;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável por expandir objetos que contêm listas em múltiplas linhas.
 */
public class RowExpander {
    
    /**
     * Representa uma linha expandida com informação de merge.
     */
    public static class ExpandedRow<T> {
        private final T originalItem;
        private final Object listItem;
        private final int listItemIndex;
        private final int totalListItems;
        private final boolean isFirstRow;
        private final boolean isLastRow;
        
        public ExpandedRow(T originalItem, Object listItem, int listItemIndex, int totalListItems) {
            this.originalItem = originalItem;
            this.listItem = listItem;
            this.listItemIndex = listItemIndex;
            this.totalListItems = totalListItems;
            this.isFirstRow = listItemIndex == 0;
            this.isLastRow = listItemIndex == totalListItems - 1;
        }
        
        public T getOriginalItem() {
            return originalItem;
        }
        
        public Object getListItem() {
            return listItem;
        }
        
        public int getListItemIndex() {
            return listItemIndex;
        }
        
        public int getTotalListItems() {
            return totalListItems;
        }
        
        public boolean isFirstRow() {
            return isFirstRow;
        }
        
        public boolean isLastRow() {
            return isLastRow;
        }
        
        public boolean shouldMerge() {
            return totalListItems > 1;
        }
    }
    
    /**
     * Expande uma lista de objetos em linhas, considerando campos que são listas.
     * 
     * @param data lista original de dados
     * @param columns metadados das colunas
     * @return lista expandida de linhas
     */
    public static <T> List<ExpandedRow<T>> expandRows(List<T> data, List<ColumnMetadata> columns) {
        List<ExpandedRow<T>> expandedRows = new ArrayList<>();
        
        // Encontra o campo que deve ser expandido
        ColumnMetadata expandColumn = findExpandColumn(columns);
        
        if (expandColumn == null) {
            // Nenhuma coluna para expandir, retorna lista simples
            for (T item : data) {
                expandedRows.add(new ExpandedRow<>(item, null, 0, 1));
            }
            return expandedRows;
        }
        
        // Expande cada item
        for (T item : data) {
            try {
                Field listField = expandColumn.getField();
                listField.setAccessible(true);
                Object fieldValue = listField.get(item);
                
                if (fieldValue instanceof List) {
                    List<?> list = (List<?>) fieldValue;
                    
                    if (list.isEmpty()) {
                        // Lista vazia, adiciona linha única
                        expandedRows.add(new ExpandedRow<>(item, null, 0, 1));
                    } else {
                        // Cria uma linha para cada item da lista
                        for (int i = 0; i < list.size(); i++) {
                            expandedRows.add(new ExpandedRow<>(item, list.get(i), i, list.size()));
                        }
                    }
                } else {
                    // Não é uma lista, adiciona linha única
                    expandedRows.add(new ExpandedRow<>(item, null, 0, 1));
                }
                
            } catch (Exception e) {
                // Em caso de erro, adiciona linha única
                expandedRows.add(new ExpandedRow<>(item, null, 0, 1));
            }
        }
        
        return expandedRows;
    }
    
    /**
     * Encontra a primeira coluna configurada para expandir linhas.
     */
    private static ColumnMetadata findExpandColumn(List<ColumnMetadata> columns) {
        return columns.stream()
                .filter(ColumnMetadata::shouldExpandRows)
                .findFirst()
                .orElse(null);
    }
}
