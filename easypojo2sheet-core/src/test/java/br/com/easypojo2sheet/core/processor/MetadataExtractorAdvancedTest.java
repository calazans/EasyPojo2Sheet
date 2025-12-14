package br.com.easypojo2sheet.core.processor;

import br.com.easypojo2sheet.annotation.SheetColumn;
import br.com.easypojo2sheet.annotation.SheetColumns;
import br.com.easypojo2sheet.annotation.Spreadsheet;
import br.com.easypojo2sheet.core.metadata.ColumnMetadata;
import br.com.easypojo2sheet.core.metadata.SheetMetadata;
import br.com.easypojo2sheet.model.enums.HorizontalAlignment;
import br.com.easypojo2sheet.model.enums.ListRenderStrategy;
import br.com.easypojo2sheet.model.enums.VerticalAlignment;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MetadataExtractorAdvancedTest {

    @Spreadsheet
    static class Complex {
        @SheetColumns({
                @SheetColumn(name = "Nome do Vendedor", property = "seller.name", order = 2,
                        align = HorizontalAlignment.LEFT, valign = VerticalAlignment.CENTER),
                @SheetColumn(name = "Cidade do Vendedor", property = "seller.address.city", order = 3)
        })
        private Seller seller;

        @SheetColumn(listStrategy = ListRenderStrategy.EXPAND_ROWS, order = 1)
        private java.util.List<Item> items;
    }

    static class Seller { String name; Address address; }
    static class Address { String city; }
    static class Item { String sku; }

    @Test
    public void repeatableAnnotation_createsMultipleColumns_forSingleField() throws Exception {
        SheetMetadata meta = MetadataExtractor.extractMetadata(Complex.class);
        List<ColumnMetadata> cols = meta.getColumns();

        // Orders: items(order=1), Nome do Vendedor(order=2), Cidade do Vendedor(order=3)
        Assert.assertEquals(3, cols.size());

        ColumnMetadata c0 = cols.get(0); // items
        Assert.assertEquals("items", c0.getColumnName());
        Assert.assertTrue(c0.isListField());
        Assert.assertTrue(c0.shouldExpandRows());
        Assert.assertFalse(c0.shouldMergeRows());

        ColumnMetadata c1 = cols.get(1); // seller name
        Assert.assertEquals("Nome do Vendedor", c1.getColumnName());
        Assert.assertEquals("seller.name", c1.getPropertyPath());
        Assert.assertEquals(2, c1.getOrder());

        ColumnMetadata c2 = cols.get(2); // seller city
        Assert.assertEquals("Cidade do Vendedor", c2.getColumnName());
        Assert.assertEquals("seller.address.city", c2.getPropertyPath());
        Assert.assertEquals(3, c2.getOrder());
    }
}
