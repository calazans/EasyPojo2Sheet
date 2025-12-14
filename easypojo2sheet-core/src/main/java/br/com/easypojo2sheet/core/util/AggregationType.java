package br.com.easypojo2sheet.core.util;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tipos de agregação suportados para listas.
 */
public enum AggregationType {
    
    /**
     * Soma valores numéricos de uma propriedade.
     * Exemplo: produtos.sum.preco
     */
    SUM {
        @Override
        public Object aggregate(List<?> list, String property) {
            if (list == null || list.isEmpty()) {
                return BigDecimal.ZERO;
            }
            
            return list.stream()
                    .map(item -> extractNumericValue(item, property))
                    .filter(val -> val != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    },
    
    /**
     * Calcula a média de valores numéricos.
     * Exemplo: produtos.avg.preco
     */
    AVG {
        @Override
        public Object aggregate(List<?> list, String property) {
            if (list == null || list.isEmpty()) {
                return BigDecimal.ZERO;
            }
            
            List<BigDecimal> values = list.stream()
                    .map(item -> extractNumericValue(item, property))
                    .filter(val -> val != null)
                    .collect(Collectors.toList());
            
            if (values.isEmpty()) {
                return BigDecimal.ZERO;
            }
            
            BigDecimal sum = values.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            return sum.divide(new BigDecimal(values.size()), 2, BigDecimal.ROUND_HALF_UP);
        }
    },
    
    /**
     * Retorna o valor mínimo de uma propriedade.
     * Exemplo: produtos.min.preco
     */
    MIN {
        @Override
        public Object aggregate(List<?> list, String property) {
            if (list == null || list.isEmpty()) {
                return null;
            }
            
            return list.stream()
                    .map(item -> extractNumericValue(item, property))
                    .filter(val -> val != null)
                    .min(Comparator.naturalOrder())
                    .orElse(null);
        }
    },
    
    /**
     * Retorna o valor máximo de uma propriedade.
     * Exemplo: produtos.max.preco
     */
    MAX {
        @Override
        public Object aggregate(List<?> list, String property) {
            if (list == null || list.isEmpty()) {
                return null;
            }
            
            return list.stream()
                    .map(item -> extractNumericValue(item, property))
                    .filter(val -> val != null)
                    .max(Comparator.naturalOrder())
                    .orElse(null);
        }
    },

    /**
     * Concatena valores de uma propriedade.
     * Exemplo: produtos.join.nome
     */
    JOIN {
        @Override
        public Object aggregate(List<?> list, String property) {
            return aggregate(list, property, ", ");
        }
        
        @Override
        public Object aggregate(List<?> list, String property, String separator) {
            if (list == null || list.isEmpty()) {
                return "";
            }
            
            return list.stream()
                    .map(item -> {
                        try {
                            Object value = PropertyExtractor.extractValue(item, property);
                            return value != null ? value.toString() : "";
                        } catch (Exception e) {
                            return "";
                        }
                    })
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining(separator));
        }
    },
    
    /**
     * Conta elementos distintos.
     * Exemplo: produtos.distinct.categoria
     */
    DISTINCT {
        @Override
        public Object aggregate(List<?> list, String property) {
            if (list == null || list.isEmpty()) {
                return 0;
            }
            
            return list.stream()
                    .map(item -> {
                        try {
                            return PropertyExtractor.extractValue(item, property);
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(val -> val != null)
                    .distinct()
                    .count();
        }
    },
    
    /**
     * Concatena valores distintos.
     * Exemplo: produtos.distinct_join.categoria
     */
    DISTINCT_JOIN {
        @Override
        public Object aggregate(List<?> list, String property) {
            return aggregate(list, property, ", ");
        }
        
        @Override
        public Object aggregate(List<?> list, String property, String separator) {
            if (list == null || list.isEmpty()) {
                return "";
            }
            
            return list.stream()
                    .map(item -> {
                        try {
                            Object value = PropertyExtractor.extractValue(item, property);
                            return value != null ? value.toString() : "";
                        } catch (Exception e) {
                            return "";
                        }
                    })
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .collect(Collectors.joining(separator));
        }
    };
    
    /**
     * Executa a agregação na lista.
     * 
     * @param list a lista de objetos
     * @param property a propriedade a ser agregada
     * @return o resultado da agregação
     */
    public abstract Object aggregate(List<?> list, String property);
    
    /**
     * Executa a agregação com separador customizado (para JOIN).
     * 
     * @param list a lista de objetos
     * @param property a propriedade a ser agregada
     * @param separator o separador (usado em JOIN)
     * @return o resultado da agregação
     */
    public Object aggregate(List<?> list, String property, String separator) {
        return aggregate(list, property);
    }
    
    /**
     * Extrai valor numérico de um objeto.
     */
    protected static BigDecimal extractNumericValue(Object item, String property) {
        try {
            Object value = PropertyExtractor.extractValue(item, property);
            
            if (value == null) {
                return null;
            }
            
            if (value instanceof BigDecimal) {
                return (BigDecimal) value;
            }
            
            if (value instanceof Number) {
                return new BigDecimal(value.toString());
            }
            
            return new BigDecimal(value.toString());
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Tenta converter nome para tipo de agregação.
     * 
     * @param name nome da agregação (case insensitive)
     * @return o tipo de agregação ou null se não encontrado
     */
    public static AggregationType fromString(String name) {
        if (name == null) {
            return null;
        }
        
        String normalized = name.toUpperCase().replace("-", "_");
        
        try {
            return AggregationType.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
