package br.com.easypojo2sheet.core.util;

import br.com.easypojo2sheet.exception.PropertyExtractionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PropertyExtractorTest {

    static class Parent {
        Child child;
        List<String> tags;
        Parent(){ }
        Parent(Child child, List<String> tags){
            this.child = child;
            this.tags = tags;
        }
    }

    static class Child extends BaseChild {
        String name;
        List<Item> items;
        Child(String name, List<Item> items, String base){
            super(base);
            this.name = name;
            this.items = items;
        }
    }

    static class BaseChild {
        String baseField;
        BaseChild(String base){ this.baseField = base; }
    }

    static class Item {
        String sku;
        Item(String sku){ this.sku = sku; }
    }

    @Test
    public void extract_nested_fields_and_list_index() {
        Parent p = new Parent(
                new Child("John", Arrays.asList(new Item("A"), new Item("B")), "base"),
                Arrays.asList("t1","t2")
        );

        Assert.assertEquals("John", PropertyExtractor.extractValue(p, "child.name"));
        Assert.assertEquals("A", ((Item) PropertyExtractor.extractValue(p, "child.items[0]")).sku);
        Assert.assertEquals("B", ((Item) PropertyExtractor.extractValue(p, "child.items[1]")).sku);
        Assert.assertNull(PropertyExtractor.extractValue(p, "child.items[2]"));
        Assert.assertEquals("t1", PropertyExtractor.extractValue(p, "tags[0]"));
        // superclass field
        Assert.assertEquals("base", PropertyExtractor.extractValue(p, "child.baseField"));
    }

    @Test
    public void extract_list_special_tokens_first_last_size() {
        Parent p = new Parent(
                new Child("John", Arrays.asList(new Item("A"), new Item("B")), "base"),
                Arrays.asList("x","y","z")
        );

        Assert.assertEquals("x", PropertyExtractor.extractValue(p.tags, "first"));
        Assert.assertEquals("z", PropertyExtractor.extractValue(p.tags, "last"));
        Assert.assertEquals(3, PropertyExtractor.extractValue(p.tags, "size"));
    }

    @Test
    public void null_and_empty_path_returns_null() {
        Assert.assertNull(PropertyExtractor.extractValue(null, "anything"));
        Assert.assertNull(PropertyExtractor.extractValue(new Parent(), null));
        Assert.assertNull(PropertyExtractor.extractValue(new Parent(), ""));
    }

    @Test(expected = PropertyExtractionException.class)
    public void invalid_field_throws() {
        PropertyExtractor.extractValue(new Parent(), "unknown");
    }

    @Test(expected = PropertyExtractionException.class)
    public void list_index_on_non_list_throws() {
        Parent p = new Parent(new Child("n", Arrays.asList(), "b"), Arrays.asList());
        PropertyExtractor.extractValue(p, "child[0]");
    }

    @Test(expected = PropertyExtractionException.class)
    public void first_on_non_list_throws() {
        PropertyExtractor.extractValue(new Object(), "first");
    }

    @Test(expected = PropertyExtractionException.class)
    public void last_on_non_list_throws() {
        PropertyExtractor.extractValue(new Object(), "last");
    }

    @Test(expected = PropertyExtractionException.class)
    public void size_on_non_list_throws() {
        PropertyExtractor.extractValue(new Object(), "size");
    }
}
