package br.com.easypojo2sheet.core.metadata;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

public class SheetMetadataTest {

    static class Dummy { int id; }

    @Test
    public void constructorAndGetters() throws NoSuchFieldException {
        Field f = Dummy.class.getDeclaredField("id");
        ColumnMetadata col = ColumnMetadata.builder()
                .field(f)
                .columnName("ID")
                .build();

        SheetMetadata sm = new SheetMetadata(
                Dummy.class,
                "Planilha",
                true,
                true,
                2,
                List.of(col)
        );

        Assert.assertEquals(Dummy.class, sm.getEntityClass());
        Assert.assertEquals("Planilha", sm.getSheetName());
        Assert.assertTrue(sm.isAutoSizeColumns());
        Assert.assertTrue(sm.isFreezeHeader());
        Assert.assertEquals(2, sm.getStartRow());
        Assert.assertEquals(1, sm.getColumns().size());
        Assert.assertEquals("ID", sm.getColumns().get(0).getColumnName());
    }
}
