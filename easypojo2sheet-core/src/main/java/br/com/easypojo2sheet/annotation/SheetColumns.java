package br.com.easypojo2sheet.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SheetColumns{
    SheetColumn[] value();
}
