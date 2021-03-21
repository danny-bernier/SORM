package dev.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SORM annotation used to mark class to allow basic CRUD operations
 * <p>
 *     Class must also have one SORMID annotated property and
 *     can have several or no SORMField and SORMReference annotations.
 * </p>
 * use {@link SORMID} to mark property as ID (primary key)
 * use {@link SORMField} to mark property as a field to be stored in the database
 * use {@link SORMReference} to mark an object as a foreign key (has-a relationship)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SORMObject {}
