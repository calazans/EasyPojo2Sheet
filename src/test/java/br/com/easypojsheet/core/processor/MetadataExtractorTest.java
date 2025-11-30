package br.com.easypojsheet.core.processor;

import br.com.easypojsheet.annotation.SheetColumn;
import br.com.easypojsheet.annotation.SheetIgnore;
import br.com.easypojsheet.annotation.Spreadsheet;
import br.com.easypojsheet.core.metadata.ColumnMetadata;
import br.com.easypojsheet.core.metadata.SheetMetadata;
import br.com.easypojsheet.exception.ExcelExportException;
import br.com.easypojsheet.model.enums.HorizontalAlignment;
import br.com.easypojsheet.model.enums.VerticalAlignment;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

public class MetadataExtractorTest {

    @Spreadsheet // usa nome default da classe
    private static class SimpleEntity {
        @SheetColumn(order = 2)
        private String b;

        @SheetColumn(order = 1, name = "A Col", width = 25, property = "nested.value", dateFormat = "dd/MM/yyyy", numberFormat = "#,##0.00",
                align = HorizontalAlignment.RIGHT, valign = VerticalAlignment.TOP)
        private String a;

        @SheetIgnore
        private String ignoreMe;

        private int plainField; // sem anotação
    }

    private static class NotAnnotated {
        private String x;
    }

    @Test
    public void extractMetadata_defaultsAndOverrides() throws Exception {
        SheetMetadata meta = MetadataExtractor.extractMetadata(SimpleEntity.class);

        // sheet name default = class simple name
        Assert.assertEquals("SimpleEntity", meta.getSheetName());
        // defaults da anotação @Spreadsheet
        Assert.assertTrue(meta.isAutoSizeColumns());
        Assert.assertTrue(meta.isFreezeHeader());
        Assert.assertEquals(0, meta.getStartRow());

        List<ColumnMetadata> cols = meta.getColumns();
        // Deve ignorar o campo com @SheetIgnore e ter 3 colunas (a, b, plainField)
        Assert.assertEquals(3, cols.size());

        // Ordenação: por order, depois por nome
        // Coluna 'a' tem order 1
        ColumnMetadata col0 = cols.get(0);
        Assert.assertEquals("A Col", col0.getColumnName());
        Assert.assertEquals(1, col0.getOrder());
        Assert.assertEquals(25, col0.getWidth());
        Assert.assertEquals("nested.value", col0.getPropertyPath());
        Assert.assertEquals("dd/MM/yyyy", col0.getDateFormat());
        Assert.assertEquals("#,##0.00", col0.getNumberFormat());
        Assert.assertEquals(HorizontalAlignment.RIGHT, col0.getHorizontalAlignment());
        Assert.assertEquals(VerticalAlignment.TOP, col0.getVerticalAlignment());

        // Coluna 'b' tem order 2
        ColumnMetadata col1 = cols.get(1);
        Assert.assertEquals("b", col1.getColumnName());
        Assert.assertEquals(2, col1.getOrder());

        // Campo sem anotação deve vir por último com defaults
        ColumnMetadata col2 = cols.get(2);
        Assert.assertEquals("plainField", col2.getColumnName());
        Assert.assertEquals(Integer.MAX_VALUE, col2.getOrder());
        Assert.assertEquals(-1, col2.getWidth());
        Assert.assertEquals("", col2.getPropertyPath());
        Assert.assertEquals("", col2.getDateFormat());
        Assert.assertEquals("", col2.getNumberFormat());
        Assert.assertEquals(HorizontalAlignment.AUTO, col2.getHorizontalAlignment());
        Assert.assertEquals(VerticalAlignment.CENTER, col2.getVerticalAlignment());

        // Os Field devem apontar para os campos corretos
        Field f2 = col2.getField();
        Assert.assertEquals("plainField", f2.getName());
    }

    @Spreadsheet
    private static class OrderTie {
        @SheetColumn(order = 5)
        private String beta;
        @SheetColumn(order = 5)
        private String alpha;
    }

    @Test
    public void sortingByOrderThenName() throws Exception {
        SheetMetadata meta = MetadataExtractor.extractMetadata(OrderTie.class);
        List<ColumnMetadata> cols = meta.getColumns();
        // order igual (5), então ordena por nome da coluna/nome do campo
        Assert.assertEquals("alpha", cols.get(0).getColumnName());
        Assert.assertEquals("beta", cols.get(1).getColumnName());
    }

    @Test(expected = ExcelExportException.class)
    public void mustThrowWhenClassNotAnnotated() throws Exception {
        MetadataExtractor.extractMetadata(NotAnnotated.class);
    }
}
