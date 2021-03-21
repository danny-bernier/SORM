package dev.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SORMField annotation used to mark property as a field to be stored in the database
 * use {@link SORM} to mark class to allow basic CRUD operations
 * use {@link SORMID} to mark property as ID (primary key)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SORMField {}
