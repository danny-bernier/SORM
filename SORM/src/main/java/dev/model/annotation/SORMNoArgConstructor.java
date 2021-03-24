package dev.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SORMNoArgConstructor to mark constructor
 * <p>
 *     The annotated constructor must take no arguments, and may be private.
 * </p>
 * use {@link SORMObject} to mark class to allow basic CRUD operations
 * use {@link SORMID} to mark property as ID (primary key)
 * use {@link SORMField} to mark property as a field to be stored in the database
 * use {@link SORMReference} to mark an object as a foreign key (has-a relationship)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface SORMNoArgConstructor {}
