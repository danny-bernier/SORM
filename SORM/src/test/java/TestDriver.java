
import dev.SORM;
import dev.model.annotation.*;
import java.util.Optional;
import java.util.concurrent.Future;





public class TestDriver {
    public static void main(String[] args) throws Exception {

        //try with resources
        try(SORM sorm = SORM.createSORM()) {
            //Creating a user object to store in our DB
            System.out.println("Creating user...");
            User u = new User("Bobby", "bob@coolEmail.gov", 23.552, new Car("Toyota", "Truck", 999000));

            //Storing the user then getting the user
            if(sorm.create(u, User.class, Double.class).get()) {
                System.out.println("Getting user by id from User table...");
                Future<Optional<User>> fou = sorm.getByID(23.552, User.class, Double.class);
                System.out.println("Optional.isPresent? = " + fou.get().isPresent());
                u = fou.get().get();
                System.out.println("Value of retrieved user : " + u);

                //finally getting the car by our id (vin)
                System.out.println("\nGetting car by id from Car table...");
                Future<Optional<Car>> foc = sorm.getByID(999000, Car.class, Integer.class);
                System.out.println("Optional.isPresent? = " + foc.get().isPresent());
                Car c = foc.get().get();
                System.out.println("Value of retrieved car : " + c);
            }

            //Creating a user with the same id identifier
            //but different name and email
            System.out.println("\nUpdating User...");
            u = new User("Sally", "newEmail@foo.com", 23.552, new Car("Toyota", "Truck", 999000));

            //Updating the user then getting the now updated user
            if(sorm.update(u, User.class, Double.class).get()) {
                System.out.println("Getting user by id from User table...");
                Future<Optional<User>> fou = sorm.getByID(23.552, User.class, Double.class);
                System.out.println("Optional.isPresent? = " + fou.get().isPresent());
                u = fou.get().get();
                System.out.println("Value of retrieved user : " + u);
            }

            //Creating a new car object with the same id
            //identifier (vin) but different make and model
            System.out.println("\nUpdating Car...");
            Car c = new Car("DMC", "Delorean", 999000);

            //Updating the car and getting the same user who's
            //foreign key now points to a car with different values
            if(sorm.update(c, Car.class, Double.class).get()) {
                System.out.println("Getting user by id from User table...");
                Future<Optional<User>> fou = sorm.getByID(23.552, User.class, Double.class);
                System.out.println("Optional.isPresent? = " + fou.get().isPresent());
                u = fou.get().get();
                System.out.println("Value of retrieved user : " + u);
            }

            //finally getting the car by our id (vin)
            //the Car table was created and user's car added to it
            //and a relationship was created between the two as a foreign key
            System.out.println("\nGetting car by id from Car table...");
            Future<Optional<Car>> foc = sorm.getByID(999000, Car.class, Integer.class);
            System.out.println("Optional.isPresent? = " + foc.get().isPresent());
            c = foc.get().get();
            System.out.println("Value of retrieved car : " + c);
        }
    }
}




//@SORMObject marks objects as entities to be stored.
@SORMObject
class User{

    //@SORMID marks a field as a unique identifier of an object.
    //Each object requires a @SORMID to differentiate objects when retrieving.
    @SORMID
    private double id = 55.55;
    @SORMField
    private String name = "Tony";
    @SORMField
    private String email = "tony@gmail.com";

    //Objects within this object that are not supported SORM
    //field data types can be marked with @SORMReference.
    //Objects marked @SORMReference must themselves conform
    //to SORM requirements for annotating objects and fields.
    @SORMReference
    public Car myCar = new Car("John", "Smith", 1234);

    //All @SORMObjects require a no argument constructor to
    //help when reconstructing objects from the database.
    //The no arg constructor may be private.
    @SORMNoArgConstructor
    private User(){}

    public User(String name, String email, double id, Car car){
        this.name = name;
        this.email = email;
        this.id = id;
        this.myCar = car;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", myCar = " + myCar.toString() +
                '}';
    }
}

//In order to use a Car object as an @SORMReference in User,
//it must also conform to SORM requirements.
//Meaning it must have @SORMObject before the class,
//must have one field marked @SORMID as a unique identifier,
//must have a no argument constructor marked @SORMNoArgConstructor,
//and may mark additional fields with @SORMField or @SORMReference.
@SORMObject
class Car{
    @SORMID
    private int vin = 22151;
    @SORMField
    private String make = "Ford";
    @SORMField
    private String model = "Focus";

    @SORMNoArgConstructor
    private Car(){}

    public Car(String make, String model, int vin){
        this.make = make;
        this.model = model;
        this.vin = vin;
    }

    @Override
    public String toString() {
        return "Car{" +
                "vin=" + vin +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}