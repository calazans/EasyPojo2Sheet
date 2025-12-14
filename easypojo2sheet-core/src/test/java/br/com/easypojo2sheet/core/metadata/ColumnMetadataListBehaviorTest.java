package br.com.easypojo2sheet.core.metadata;

import br.com.easypojo2sheet.model.enums.ListRenderStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

public class ColumnMetadataListBehaviorTest {

    static class Holder {
        List<String> items;
        String name;
    }

    @Test
    public void isListField_true_whenFieldIsList() throws Exception {
        Field f = Holder.class.getDeclaredField("items");
        ColumnMetadata meta = ColumnMetadata.builder()
                .field(f)
                .columnName("items")
                .listStrategy(ListRenderStrategy.AGGREGATE)
                .build();

        Assert.assertTrue(meta.isListField());
        Assert.assertFalse(meta.shouldExpandRows());
        Assert.assertFalse(meta.shouldMergeRows());
    }

    @Test
    public void expandRows_true_whenStrategyIsExpandRows() throws Exception {
        Field f = Holder.class.getDeclaredField("items");
        ColumnMetadata meta = ColumnMetadata.builder()
                .field(f)
                .columnName("items")
                .listStrategy(ListRenderStrategy.EXPAND_ROWS)
                .build();

        Assert.assertTrue(meta.isListField());
        Assert.assertTrue(meta.shouldExpandRows());
        Assert.assertFalse(meta.shouldMergeRows());
    }

    @Test
    public void expandRowsWithMerge_true_whenStrategyIsExpandRowsWithMergedRows() throws Exception {
        Field f = Holder.class.getDeclaredField("items");
        ColumnMetadata meta = ColumnMetadata.builder()
                .field(f)
                .columnName("items")
                .listStrategy(ListRenderStrategy.EXPAND_ROWS_WITH_MERGED_ROWS)
                .build();

        Assert.assertTrue(meta.isListField());
        Assert.assertTrue(meta.shouldExpandRows());
        Assert.assertTrue(meta.shouldMergeRows());
    }

    @Test
    public void nonListField_neverExpandsOrMerges() throws Exception {
        Field f = Holder.class.getDeclaredField("name");
        ColumnMetadata meta = ColumnMetadata.builder()
                .field(f)
                .columnName("name")
                .listStrategy(ListRenderStrategy.EXPAND_ROWS_WITH_MERGED_ROWS)
                .build();

        Assert.assertFalse(meta.isListField());
        Assert.assertFalse(meta.shouldExpandRows());
        Assert.assertFalse(meta.shouldMergeRows());
    }
}
