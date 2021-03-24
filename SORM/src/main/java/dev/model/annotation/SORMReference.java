package dev.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SORMReference annotation used to mark property as a reference type or foreign key.
 * <p>
 *     This is most frequently used to annotate objects within a class,
 *     and denotes a 'has-a' relationship. The object annotated must utilize
 *     the {@link SORMObject} annotation and must have a property marked with a
 *     {@link SORMID} annotation.
 * </p>
 * use {@link SORMObject} to mark class to allow basic CRUD operations
 * use {@link SORMID} to mark property as ID (primary key)
 * use {@link SORMField} to mark property as a field to be stored in the database
 * use {@link SORMNoArgConstructor} to mark a no argument constructor for use recreating retrieved POJO
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SORMReference {}
