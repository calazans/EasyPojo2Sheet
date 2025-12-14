package br.com.easypojo2sheet.annotation;

import br.com.easypojo2sheet.model.enums.HorizontalAlignment;
import br.com.easypojo2sheet.model.enums.ListRenderStrategy;
import br.com.easypojo2sheet.model.enums.VerticalAlignment;

import java.lang.annotation.*;

/**
 * Anotação para configurar como um campo é exportado para uma coluna da planilha.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
@Repeatable(SheetColumns.class)
public @interface SheetColumn {
    
    /**
     * Nome da coluna no header.
     * @return nome da coluna, ou string vazia para usar o nome do campo
     */
    String name() default "";
    
    /**
     * Chave de internacionalização para o nome da coluna.
     * @return chave i18n, ou string vazia se não usar
     */
    String nameKey() default "";
    
    /**
     * Ordem da coluna (colunas são ordenadas em ordem crescente).
     * @return ordem da coluna, Integer.MAX_VALUE para ordem natural
     */
    int order() default Integer.MAX_VALUE;
    
    /**
     * Largura da coluna em caracteres.
     * @return largura em caracteres, -1 para auto-size
     */
    int width() default -1;
    
    /**
     * Caminho para propriedade em objetos aninhados.
     * Exemplo: "vendedor.nome" para acessar o nome do objeto vendedor.
     * @return caminho da propriedade, ou string vazia para usar o próprio campo
     */
    String property() default "";
    
    /**
     * Formato para datas (padrão SimpleDateFormat).
     * @return formato de data, ou string vazia para usar padrão do Locale
     */
    String dateFormat() default "";
    
    /**
     * Formato para números (padrão DecimalFormat).
     * @return formato numérico, ou string vazia para usar padrão do Locale
     */
    String numberFormat() default "";
    
    /**
     * Alinhamento horizontal da célula.
     * @return alinhamento horizontal
     */
    HorizontalAlignment align() default HorizontalAlignment.AUTO;
    
    /**
     * Alinhamento vertical da célula.
     * @return alinhamento vertical
     */
    VerticalAlignment valign() default VerticalAlignment.CENTER;

    /**
     * Separador para agregações do tipo JOIN.
     * Exemplo: separator = "; " para produtos.join.nome
     *
     * @return separador, ", " por padrão
     */
    String separator() default ", ";

    /**
     * Estratégia de renderização para listas.
     * - AGGREGATE: usa property com agregações (padrão)
     * - EXPAND_ROWS: cria uma linha para cada item da lista
     * - IGNORE: ignora a lista
     *
     * @return estratégia de renderização
     */
    ListRenderStrategy listStrategy() default ListRenderStrategy.AGGREGATE;
}
