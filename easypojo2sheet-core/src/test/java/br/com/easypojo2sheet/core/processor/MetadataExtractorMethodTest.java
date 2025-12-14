package br.com.easypojo2sheet.core.processor;

import br.com.easypojo2sheet.annotation.SheetColumn;
import br.com.easypojo2sheet.annotation.Spreadsheet;
import br.com.easypojo2sheet.core.metadata.ColumnMetadata;
import br.com.easypojo2sheet.core.metadata.SheetMetadata;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class MetadataExtractorMethodTest {

    @Spreadsheet(name = "Test")
    static class TestClassWithMethods {
        @SheetColumn(name = "Campo", order = 1)
        private String field;

        @SheetColumn(name = "Calculado", order = 2)
        public String getCalculatedValue() {
            return "calculated";
        }

        @SheetColumn(name = "Numero", order = 3, numberFormat = "#,##0.00")
        public BigDecimal getNumber() {
            return new BigDecimal("100.50");
        }

        public TestClassWithMethods(String field) {
            this.field = field;
        }
    }

    @Test
    public void should_extract_method_columns() {
        SheetMetadata metadata = MetadataExtractor.extractMetadata(TestClassWithMethods.class);
        
        Assert.assertEquals(3, metadata.getColumns().size());
        
        // Verifica coluna do campo
        ColumnMetadata fieldColumn = metadata.getColumns().get(0);
        Assert.assertEquals("Campo", fieldColumn.getColumnName());
        Assert.assertFalse(fieldColumn.isMethod());
        
        // Verifica coluna do método 1
        ColumnMetadata methodColumn1 = metadata.getColumns().get(1);
        Assert.assertEquals("Calculado", methodColumn1.getColumnName());
        Assert.assertTrue(methodColumn1.isMethod());

        // Verifica coluna do método 2
        ColumnMetadata methodColumn2 = metadata.getColumns().get(2);
        Assert.assertEquals("Numero", methodColumn2.getColumnName());
        Assert.assertTrue(methodColumn2.isMethod());
        Assert.assertEquals("#,##0.00", methodColumn2.getNumberFormat());
    }

    @Test
    public void should_extract_value_from_method() throws Exception {
        TestClassWithMethods instance = new TestClassWithMethods("test");
        SheetMetadata metadata = MetadataExtractor.extractMetadata(TestClassWithMethods.class);
        
        ColumnMetadata methodColumn = metadata.getColumns().get(1);
        Object value = methodColumn.extractValue(instance);
        
        Assert.assertEquals("calculated", value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_reject_method_with_parameters() {
        @Spreadsheet(name = "Invalid")
        class InvalidClass {
            @SheetColumn(name = "Invalid")
            public String methodWithParams(String param) {
                return param;
            }
        }
        
        MetadataExtractor.extractMetadata(InvalidClass.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_reject_void_method() {
        @Spreadsheet(name = "Invalid")
        class InvalidClass {
            @SheetColumn(name = "Invalid")
            public void voidMethod() {
                // do nothing
            }
        }
        
        MetadataExtractor.extractMetadata(InvalidClass.class);
    }
}
