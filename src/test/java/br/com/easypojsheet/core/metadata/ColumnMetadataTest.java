package br.com.easypojsheet.core.metadata;

import br.com.easypojsheet.model.enums.HorizontalAlignment;
import br.com.easypojsheet.model.enums.VerticalAlignment;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class ColumnMetadataTest {

    private static class Dummy {
        String prop;
    }

    @Test
    public void hasPropertyPath_true_whenNotEmpty() throws Exception {
        Field f = Dummy.class.getDeclaredField("prop");
        ColumnMetadata meta = new ColumnMetadata(
                f, "prop", 1, 10, "nested.value", "", "",
                HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM
        );
        Assert.assertTrue(meta.hasPropertyPath());
    }

    @Test
    public void hasPropertyPath_false_whenEmpty() throws Exception {
        Field f = Dummy.class.getDeclaredField("prop");
        ColumnMetadata meta = new ColumnMetadata(
                f, "prop", 1, 10, "", "", "",
                HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM
        );
        Assert.assertFalse(meta.hasPropertyPath());
    }
}
