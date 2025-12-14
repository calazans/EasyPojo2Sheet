package br.com.easypojo2sheet.core.writer.excel;

import br.com.easypojo2sheet.annotation.SheetColumn;
import br.com.easypojo2sheet.annotation.Spreadsheet;
import br.com.easypojo2sheet.core.metadata.SheetMetadata;
import br.com.easypojo2sheet.core.processor.MetadataExtractor;
import br.com.easypojo2sheet.model.enums.ListRenderStrategy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ExcelStreamingWriterTest {

    @Spreadsheet
    static class Order {
        @SheetColumn(order = 1)
        String id;

        @SheetColumn(order = 2)
        String customer;

        // expandirá linhas
        @SheetColumn(order = 3, listStrategy = ListRenderStrategy.EXPAND_ROWS_WITH_MERGED_ROWS, property = "sku")
        List<Item> items;

        Order(String id, String customer, List<Item> items) {
            this.id = id;
            this.customer = customer;
            this.items = items;
        }
    }

    static class Item { String sku; Item(String s){ this.sku = s; } }

    @Test
    public void writesHeaderAndExpandsRowsWithMerge() throws Exception {
        SheetMetadata metadata = MetadataExtractor.extractMetadata(Order.class);
        ExcelStreamingWriter writer = new ExcelStreamingWriter(metadata, 10);

        List<Order> data = List.of(
                new Order("1", "Alice", Arrays.asList(new Item("A"), new Item("B"))),
                new Order("2", "Bob", List.of(new Item("C")))
        );

        writer.write(data);

        SXSSFWorkbook wb = writer.getWorkbook();
        Sheet sheet = wb.getSheetAt(0);

        // header at startRow (default 0)
        Assert.assertEquals("id", sheet.getRow(0).getCell(0).getStringCellValue());
        Assert.assertEquals("customer", sheet.getRow(0).getCell(1).getStringCellValue());
        Assert.assertEquals("items", sheet.getRow(0).getCell(2).getStringCellValue());

        // rows: header + expanded data = 1 + (2 for first order) + (1 for second) = 4
        // SXSSF may not expose physicalNumberOfRows reliably; check presence of expected rows
        Assert.assertNotNull(sheet.getRow(1));
        Assert.assertNotNull(sheet.getRow(2));
        Assert.assertNotNull(sheet.getRow(3));

        // First data group should have merge regions for first two columns (id, customer)
        // There should be at least one merged region when merging is applicable
        Assert.assertTrue(sheet.getNumMergedRegions() >= 2);

        // The expanded items should be set in third column
        Assert.assertEquals("A", sheet.getRow(1).getCell(2).getStringCellValue());
        Assert.assertEquals("B", sheet.getRow(2).getCell(2).getStringCellValue());

        // Second order with single item, no additional row
        Assert.assertEquals("C", sheet.getRow(3).getCell(2).getStringCellValue());
    }
}
