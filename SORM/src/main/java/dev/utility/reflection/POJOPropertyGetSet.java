package dev.utility.reflection;

import dev.model.annotation.SORMReference;
import dev.model.annotation.SORMField;
import dev.model.annotation.SORMID;
import dev.model.annotation.SORMObject;
import dev.model.database.DataField;
import dev.model.database.DataReference;
import dev.model.exception.NoSORMIDFoundException;
import dev.model.exception.NoSORMObjectFoundException;
import dev.model.exception.SORMAccessException;
import dev.model.exception.SORMReservedKeywordException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * The PropertyGatherer does the gathering and collecting of properties related to objects using SORM annotations
 *  <p>
 *      {@link SORMObject} to mark class to allow basic CRUD operations
 *      {@link SORMID} to mark property as ID (primary key)
 *      {@link SORMField} to mark property as a field to be stored in the database
 *      {@link SORMReference} to mark an object as a foreign key (has-a relationship)
 *  </p>
 */
public abstract class PropertyGatherer {

    /**
     * Gathers list of DataFields of object marked by {@link SORMField}
     * @param o The object who's DataFields marked with {@link SORMField} will be gathered
     * @return Returns a list of DataField objects
     * @throws SORMAccessException Thrown when a field could not be accessed
     * @throws NoSORMObjectFoundException Thrown when the Object parameter does not have a {@link SORMObject} annotation on its class definition
     * @throws SORMReservedKeywordException Thrown when a property is named a reserved SORM keyword
     */
    public static List<DataField<Object>> getFields (Object o) throws SORMAccessException, NoSORMObjectFoundException, SORMReservedKeywordException{
        if(!o.getClass().isAnnotationPresent(SORMObject.class))
            throw new NoSORMObjectFoundException("No @SORMObject annotation found, ensure classes to be stored are marked with @SORMObject");
        List<DataField<Object>> dataFields = new ArrayList<>();
        try {
            for (Field f : o.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(SORMField.class)) {
                    if(f.getName().equalsIgnoreCase("serialization"))
                        throw new SORMReservedKeywordException("Properties/fields with SORM annotations may not be named 'serialization' as that is a reserved keyword for SORM");
                    dataFields.add(DataField.createDataField(f.get(o), f.getName()));
                }
            }
        } catch (IllegalAccessException e){throw new SORMAccessException("One or more fields in " + o.getClass().getSimpleName() + " could not be read, ensure annotated field is accessible and of a supported datatype");}
        return dataFields;
    }

    /**
     * Gathers ID (primary key) of object marked by {@link SORMID}
     * @param o The object who's ID marked with {@link SORMID} will be gathered
     * @return Returns a DataField of the object's {@link SORMID} annotated property
     * @throws NoSORMIDFoundException Thrown when the ID could not be accessed
     * @throws NoSORMObjectFoundException Thrown when the Object parameter does not have a {@link SORMObject} annotation on its class definition
     * @throws SORMAccessException Thrown when an ID field could not be accessed
     * @throws SORMReservedKeywordException Thrown when a property is named a reserved SORM keyword
     */
    public static DataField<Object> getID (Object o) throws NoSORMIDFoundException, NoSORMObjectFoundException, SORMAccessException, SORMReservedKeywordException {
        if(!o.getClass().isAnnotationPresent(SORMObject.class))
            throw new NoSORMObjectFoundException("No @SORMObject annotation found, ensure classes to be stored are marked with @SORMObject");
        try {
            for (Field f : o.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(SORMID.class)) {
                    if (f.getName().equalsIgnoreCase("serialization"))
                        throw new SORMReservedKeywordException("Properties/fields with SORM annotations may not be named 'serialization' as that is a reserved keyword for SORM");
                    return DataField.createDataField(f.get(o), f.getName());
                }
            }
        }catch (IllegalAccessException ignored) {throw new SORMAccessException("One or more fields in " + o.getClass().getSimpleName() + " could not be read, ensure annotated field is accessible and of a supported datatype");}
        throw new NoSORMIDFoundException("No appropriate @SORMID annotation was found, ensure this data field is present and accessible, and that is is of a supported datatype");
    }

    /**
     * Gets the name of the field with {@link SORMID}
     * @param clazz The class who's ID name will be retrieved
     * @return Returns the name of the ID field as String
     * @throws NoSORMIDFoundException  Thrown when the ID could not be accessed/does not exist
     * @throws NoSORMObjectFoundException Thrown when the Object parameter does not have a {@link SORMObject} annotation on its class definition
     */
    public static String getIDNameByClass (Class clazz) throws NoSORMObjectFoundException, SORMAccessException {
        if(!clazz.isAnnotationPresent(SORMObject.class))
            throw new NoSORMObjectFoundException("No @SORMObject annotation found, ensure classes to be stored are marked with @SORMObject");
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(SORMID.class))
                return f.getName();
        }
        throw new NoSORMIDFoundException("No appropriate @SORMID annotation was found, ensure this data field is present and accessible, and that is is of a supported datatype");
    }

    /**
     * Gathers reference objects (foreign keys) marked with {@link SORMReference}
     * @param o The object who's properties marked with {@link SORMReference} will be gathered
     * @return Returns a list of objects marked with {@link SORMReference}, returns an empty list if no appropriate annotations were found
     * @throws NoSORMObjectFoundException Thrown when the Object parameter does not have a {@link SORMObject} annotation on its class definition
     * @throws SORMAccessException Thrown when a field could not be accessed
     * @throws SORMReservedKeywordException Thrown when a property is named a reserved SORM keyword
     */
    public static List<DataReference<Object>> getReference (Object o) throws NoSORMObjectFoundException, SORMAccessException, SORMReservedKeywordException {
        if(!o.getClass().isAnnotationPresent(SORMObject.class))
            throw new NoSORMObjectFoundException("No @SORMObject annotation found, ensure classes to be stored are marked with @SORMObject");
        List<DataReference<Object>> references = new ArrayList<>();
        try {
            for (Field f : o.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(SORMReference.class)) {
                    if (f.getName().equalsIgnoreCase("serialization"))
                        throw new SORMReservedKeywordException("Properties/fields with SORM annotations may not be named 'serialization' as that is a reserved keyword for SORM");
                    references.add(DataReference.createDataReference(f.get(o), f.getName(), getID(f.get(o))));
                }
            }
        }catch (IllegalAccessException ignored) {throw new SORMAccessException("One or more fields in " + o.getClass().getSimpleName() + " could not be read, ensure annotated field is accessible and of a supported datatype");}
        return references;
    }
}
