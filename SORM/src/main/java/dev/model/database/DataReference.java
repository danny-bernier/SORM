package dev.model.database;

import java.util.Objects;

/**
 * Data reference is the representation of a foreign key
 * <p>
 *     It stores an object and a string field
 *     name to refer to that object
 * </p>
 */
public class DataReference<T> {
    private final T REFERENCE;
    private final String REFERENCE_NAME;
    private final DataField<Object> REFERENCES_ID;

    /**
     * Data reference is the representation of a foreign key
     * @param reference The object to be referenced
     * @param referenceName The name of that object variable/field
     * @throws IllegalArgumentException Thrown when either parameter is null or when referenceName is an empty String
     */
    private DataReference(T reference, String referenceName, DataField<Object> referenceID) throws IllegalArgumentException{
        if(reference == null)
            throw new IllegalArgumentException("Parameter reference cannot be null");
        if(referenceName == null)
            throw new IllegalArgumentException("Parameter referenceName cannot be null");
        if(referenceName.equals(""))
            throw new IllegalArgumentException("Parameter referenceName cannot be an empty String");
        if(referenceID == null)
            throw new IllegalArgumentException("Parameter referenceID cannot be null");

        this.REFERENCES_ID = referenceID;
        this.REFERENCE = reference;
        this.REFERENCE_NAME = referenceName;
    }

    /**
     * Creates a new DataReference object with specified object reference and field name
     * @param reference The object to be referenced
     * @param referenceName The name of that object variable/field
     * @throws IllegalArgumentException Thrown when either parameter is null or when referenceName is an empty String
     */
    public static <T> DataReference<T> createDataReference(T reference, String referenceName, DataField<Object> referenceID) throws IllegalArgumentException {
        return new DataReference<T>(reference, referenceName, referenceID);
    }

    public T getREFERENCE() {
        return REFERENCE;
    }

    public String getREFERENCE_NAME() {
        return REFERENCE_NAME;
    }

    public DataField<Object> getREFERENCES_ID() {
        return REFERENCES_ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataReference<?> that = (DataReference<?>) o;
        return REFERENCE.equals(that.REFERENCE) && REFERENCE_NAME.equals(that.REFERENCE_NAME) && REFERENCES_ID.equals(that.REFERENCES_ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(REFERENCE, REFERENCE_NAME, REFERENCES_ID);
    }

    @Override
    public String toString() {
        return "DataReference{" +
                "REFERENCE=" + REFERENCE +
                ", REFERENCE_NAME='" + REFERENCE_NAME + '\'' +
                ", REFERENCES_ID=" + REFERENCES_ID +
                '}';
    }
}
