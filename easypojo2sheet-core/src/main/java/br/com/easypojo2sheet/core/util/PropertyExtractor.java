package br.com.easypojo2sheet.core.util;


import br.com.easypojo2sheet.exception.PropertyExtractionException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Utilitário para extração de valores de propriedades aninhadas, incluindo listas e agregações.
 * Suporta notações como:
 * - "vendedor.nome" (objeto aninhado)
 * - "produtos[0].nome" (índice específico em lista)
 * - "produtos.first.nome" (primeiro elemento)
 * - "produtos.last.nome" (último elemento)
 * - "produtos.size" (tamanho da lista)
 * - "produtos.sum.preco" (soma valores)
 * - "produtos.join.nome" (concatena valores)
 */
public class PropertyExtractor {
    private static final Pattern INDEX_PATTERN = Pattern.compile("(.+)\\[(\\d+)\\]");

    /**
     * Extrai o valor de uma propriedade aninhada.
     *
     * @param object       o objeto raiz
     * @param propertyPath o caminho da propriedade (ex: "vendedor.nome", "produtos[0].sku")
     * @return o valor extraído ou null
     */
    public static Object extractValue(Object object, String propertyPath) {
        return extractValue(object, propertyPath, ", ");
    }

    public static Object extractValue(Object object, String propertyPath,String separator) {
        if (object == null || propertyPath == null || propertyPath.isEmpty()) {
            return null;
        }

        String[] parts = propertyPath.split("\\.");
        Object current = object;

        for (int i = 0; i < parts.length; i++) {
            if (current == null) {
                return null;
            }

            String part = parts[i];

            // Verifica se e uma agregacao
            AggregationType aggregation = AggregationType.fromString(part);
            if (aggregation != null && current instanceof List) {

                // Se ha mais partes elas representam a propriedade a agregar
                String remainingProperty = null;
                if (i + 1 < parts.length) {
                    remainingProperty = String.join(".", java.util.Arrays.copyOfRange(parts, i + 1, parts.length));
                }

                return aggregation.aggregate((List<?>) current, remainingProperty, separator);
            }

            current = extractSingleProperty(current, part);
        }

        return current;
    }

    /**
     * Extrai uma única propriedade, com suporte a índices de lista.
     */
    private static Object extractSingleProperty(Object object, String property) {
        try {
            // Verifica se é um acesso a índice: produtos[0]
            Matcher indexMatcher = INDEX_PATTERN.matcher(property);
            if (indexMatcher.matches()) {
                String fieldName = indexMatcher.group(1);
                int index = Integer.parseInt(indexMatcher.group(2));
                return extractListElement(object, fieldName, index);
            }

            // Verifica se é um acesso especial a lista
            if (property.equals("first")) {
                return extractFirstElement(object);
            }

            if (property.equals("last")) {
                return extractLastElement(object);
            }

            if (property.equals("size")) {
                return extractListSize(object);
            }

            // Acesso normal a campo
            return extractFieldValue(object, property);

        } catch (Exception e) {
            throw new PropertyExtractionException(
                    "Erro ao extrair propriedade '" + property + "' de " + object.getClass().getName(),
                    e
            );
        }
    }

    /**
     * Extrai elemento de uma lista por índice.
     */
    private static Object extractListElement(Object object, String fieldName, int index)
            throws NoSuchFieldException, IllegalAccessException {
        Object fieldValue = extractFieldValue(object, fieldName);

        if (fieldValue == null) {
            return null;
        }

        if (!(fieldValue instanceof List)) {
            throw new PropertyExtractionException(
                    "Campo '" + fieldName + "' não é uma lista"
            );
        }

        List<?> list = (List<?>) fieldValue;

        if (index < 0 || index >= list.size()) {
            return null; // Retorna null para índices fora do range
        }

        return list.get(index);
    }

    /**
     * Extrai o primeiro elemento de uma lista.
     */
    private static Object extractFirstElement(Object object) {
        if (!(object instanceof List)) {
            throw new PropertyExtractionException(
                    "Objeto não é uma lista: " + object.getClass().getName()
            );
        }

        List<?> list = (List<?>) object;
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Extrai o último elemento de uma lista.
     */
    private static Object extractLastElement(Object object) {
        if (!(object instanceof List)) {
            throw new PropertyExtractionException(
                    "Objeto não é uma lista: " + object.getClass().getName()
            );
        }

        List<?> list = (List<?>) object;
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    /**
     * Retorna o tamanho de uma lista.
     */
    private static Object extractListSize(Object object) {
        if (!(object instanceof List)) {
            throw new PropertyExtractionException(
                    "Objeto não é uma lista: " + object.getClass().getName()
            );
        }

        return ((List<?>) object).size();
    }

    /**
     * Extrai valor de um campo usando reflection.
     */
    private static Object extractFieldValue(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = findField(object.getClass(), fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    /**
     * Busca um campo na classe ou suas superclasses.
     */
    private static Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;

        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }

        throw new NoSuchFieldException(
                "Campo '" + fieldName + "' não encontrado em " + clazz.getName()
        );
    }

}