package dev.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SORMField annotation used to mark property as a field to be stored in the database
 * <p>
 *     Marked properties must be of a supported data type:
 *     char, String, boolean, BigDecimal, byte, short,
 *     int, long, float, double, Date, Time, Timestamp
 *
 *     More complex datatypes or objects can be marked
 *     with SORMReference annotation to denote a has-a
 *     relationship.
 * </p>
 * use {@link SORMObject} to mark class to allow basic CRUD operations
 * use {@link SORMID} to mark property as ID (primary key)
 * use {@link SORMReference} to mark an object as a foreign key (has-a relationship)
 * use {@link SORMNoArgConstructor} to mark a no argument constructor for use recreating retrieved POJO
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SORMField {}
