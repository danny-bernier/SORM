package dev.utility.reflection;

import dev.model.annotation.SORMField;
import dev.model.annotation.SORMID;
import dev.model.annotation.SORMObject;
import dev.model.database.DataField;
import dev.model.exception.NoSORMIDFoundException;
import dev.model.exception.NoSORMObjectFoundException;
import dev.model.exception.SORMAccessException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * The PropertyGatherer does the gathering and collecting of properties related to objects
 */
public abstract class PropertyGatherer {

    /**
     *
     * @param o
     * @return
     * @throws SORMAccessException
     * @throws NoSORMObjectFoundException
     */
    public static List<DataField<?>> getFields (Object o) throws SORMAccessException, NoSORMObjectFoundException{
        if(!o.getClass().isAnnotationPresent(SORMObject.class))
            throw new NoSORMObjectFoundException("No @SORMObject annotation found, ensure classes to be stored are marked with @SORMObject");
        List<DataField<?>> dataFields = new ArrayList<>();
        try {
            for (Field f : o.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(SORMField.class))
                    dataFields.add(DataField.createDataField(f.get(o)));
            }
        } catch (IllegalAccessException e){
            throw new SORMAccessException("One or more fields in " + o.getClass().getSimpleName() + " could not be read, ensure annotated field is accessible and of a supported datatype");
        }
        return dataFields;
    }

    /**
     *
     * @param o
     * @return
     * @throws NoSORMIDFoundException
     * @throws NoSORMObjectFoundException
     */
    public static DataField<?> getID (Object o)throws NoSORMIDFoundException, NoSORMObjectFoundException{
        if(!o.getClass().isAnnotationPresent(SORMObject.class))
            throw new NoSORMObjectFoundException("No @SORMObject annotation found, ensure classes to be stored are marked with @SORMObject");
        try {
            for (Field f : o.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(SORMID.class))
                    return DataField.createDataField(f.get(o));
            }
        }catch (IllegalAccessException ignored) {}
        throw new NoSORMIDFoundException("No appropriate @SORMID annotation was found, ensure this data field is present and accessible, and that is is of a supported datatype");
    }
}
