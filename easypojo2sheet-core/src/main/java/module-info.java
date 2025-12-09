module easypojsheet.core {
    requires java.base;
    // Apache POI modules for Excel generation
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    // Optionally required by POI at compile-time (safe to include)
    requires transitive java.xml;
    requires transitive java.desktop;

    // Export public API packages of this library
    exports br.com.easypojo2sheet.annotation;
    exports br.com.easypojo2sheet.api;
    exports br.com.easypojo2sheet.core.metadata;
    exports br.com.easypojo2sheet.core.processor;
    exports br.com.easypojo2sheet.core.writer;
    exports br.com.easypojo2sheet.exception;
    exports br.com.easypojo2sheet.model.enums;
    exports br.com.easypojo2sheet.core.writer.excel;
}