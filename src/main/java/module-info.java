module easypojsheet.core {
    requires java.base;
    // Apache POI modules for Excel generation
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    // Optionally required by POI at compile-time (safe to include)
    requires transitive java.xml;
    requires transitive java.desktop;

    // Export public API packages of this library
    exports br.com.easypojsheet.annotation;
    exports br.com.easypojsheet.api;
    exports br.com.easypojsheet.core.metadata;
    exports br.com.easypojsheet.core.processor;
    exports br.com.easypojsheet.core.writer;
    exports br.com.easypojsheet.exception;
    exports br.com.easypojsheet.model.enums;
    exports br.com.easypojsheet.core.writer.excel;
}