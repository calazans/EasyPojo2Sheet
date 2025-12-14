package br.com.easypojo2sheet.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AggregationTypeTest {

    static class Item {
        BigDecimal price;
        Integer qty;
        String name;
        String category;

        Item(BigDecimal price, Integer qty, String name, String category) {
            this.price = price;
            this.qty = qty;
            this.name = name;
            this.category = category;
        }
    }

    private List<Item> sampleItems() {
        return Arrays.asList(
                new Item(new BigDecimal("10.50"), 2, "A", "X"),
                new Item(new BigDecimal("5.25"), 3, "B", "Y"),
                new Item(null, 1, "C", "X"),
                new Item(new BigDecimal("4.25"), null, null, "Y")
        );
    }

    @Test
    public void sum_returnsZeroForNullOrEmptyAndSumsNumerics() {
        Assert.assertEquals(new BigDecimal("0"), AggregationType.SUM.aggregate(null, "price"));
        Assert.assertEquals(new BigDecimal("0"), AggregationType.SUM.aggregate(Collections.emptyList(), "price"));

        Object result = AggregationType.SUM.aggregate(sampleItems(), "price");
        Assert.assertTrue(result instanceof BigDecimal);
        Assert.assertEquals(new BigDecimal("20.00"), result);
    }

    @Test
    public void avg_returnsZeroForNoValuesAndRoundsHalfUpTo2() {
        Assert.assertEquals(new BigDecimal("0"), AggregationType.AVG.aggregate(null, "price"));
        Assert.assertEquals(new BigDecimal("0"), AggregationType.AVG.aggregate(Collections.emptyList(), "price"));

        Object result = AggregationType.AVG.aggregate(sampleItems(), "price");
        Assert.assertTrue(result instanceof BigDecimal);
        Assert.assertEquals(new BigDecimal("6.67"), result);
    }

    @Test
    public void min_returnsNullForEmptyAndCorrectMinimum() {
        Assert.assertNull(AggregationType.MIN.aggregate(null, "price"));
        Assert.assertNull(AggregationType.MIN.aggregate(Collections.emptyList(), "price"));

        Object result = AggregationType.MIN.aggregate(sampleItems(), "price");
        Assert.assertTrue(result == null || result instanceof BigDecimal);
        Assert.assertEquals(new BigDecimal("4.25"), result);
    }

    @Test
    public void max_returnsNullForEmptyAndCorrectMaximum() {
        Assert.assertNull(AggregationType.MAX.aggregate(null, "price"));
        Assert.assertNull(AggregationType.MAX.aggregate(Collections.emptyList(), "price"));

        Object result = AggregationType.MAX.aggregate(sampleItems(), "price");
        Assert.assertTrue(result == null || result instanceof BigDecimal);
        Assert.assertEquals(new BigDecimal("10.50"), result);
    }

    @Test
    public void join_usesDefaultSeparatorAndSkipsNullsOrErrors() {
        Object result = AggregationType.JOIN.aggregate(sampleItems(), "name");
        Assert.assertTrue(result instanceof String);
        Assert.assertEquals("A, B, C", result);

        Assert.assertEquals("", AggregationType.JOIN.aggregate(null, "name"));
        Assert.assertEquals("", AggregationType.JOIN.aggregate(Collections.emptyList(), "name"));
    }

    @Test
    public void join_withCustomSeparator() {
        Object result = AggregationType.JOIN.aggregate(sampleItems(), "name", " | ");
        Assert.assertEquals("A | B | C", result);
    }

    @Test
    public void distinct_countsDistinctNonNullValues() {
        Object result = AggregationType.DISTINCT.aggregate(sampleItems(), "category");
        Assert.assertTrue(result instanceof Long);
        Assert.assertEquals(Long.valueOf(2L), result);

        Object namesCount = AggregationType.DISTINCT.aggregate(sampleItems(), "name");
        Assert.assertEquals(Long.valueOf(3L), namesCount);
    }

    @Test
    public void distinctJoin_concatenatesDistinctValues() {
        Object result = AggregationType.DISTINCT_JOIN.aggregate(sampleItems(), "category");
        Assert.assertTrue(result instanceof String);
        Assert.assertEquals("X, Y", result);

        Object custom = AggregationType.DISTINCT_JOIN.aggregate(sampleItems(), "category", ";");
        Assert.assertEquals("X;Y", custom);

        Assert.assertEquals("", AggregationType.DISTINCT_JOIN.aggregate(Collections.emptyList(), "category"));
    }

    @Test
    public void fromString_isCaseInsensitive_andAcceptsHyphen() {
        Assert.assertEquals(AggregationType.SUM, AggregationType.fromString("sum"));
        Assert.assertEquals(AggregationType.DISTINCT_JOIN, AggregationType.fromString("distinct-join"));
        Assert.assertEquals(AggregationType.MAX, AggregationType.fromString("MaX"));
        Assert.assertNull(AggregationType.fromString(null));
        Assert.assertNull(AggregationType.fromString("unknown"));
    }
}
