package dev.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SORMID to mark property as ID (primary key)
 * <p>
 *     The property marked as SORMID must be unique and cannot be null.
 *     It acts as the primary identifier of an object in the database.
 * </p>
 * use {@link SORMObject} to mark class to allow basic CRUD operations
 * use {@link SORMField} to mark property as a field to be stored in the database
 * use {@link SORMReference} to mark an object as a foreign key (has-a relationship)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SORMID {}
