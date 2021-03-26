
import dev.SORM;
import dev.model.annotation.*;
import java.util.Optional;
import java.util.concurrent.Future;

public class TestDriver {
    public static void main(String[] args) throws Exception {
        try(SORM sorm = SORM.createSORM();) {
            User u = new User("Bob", "b@g.com", 23, new Car("Toyota", "Truck", 999000));
            if(sorm.create(u, User.class, Integer.class).get()) {
                Future<Optional<User>> fou = sorm.getByID(23, User.class, Integer.class);
                System.out.println(fou.get().isPresent());
                System.out.println(fou.get().get());
            }
        }
    }
}

@SORMObject
class User{
    @SORMID
    private int id;
    @SORMField
    private String name = "";
    @SORMField
    private String email = "";
    @SORMReference
    public Car myCar = new Car("", "", 0);

    @SORMNoArgConstructor
    private User(){}

    public User(String name, String email, int id, Car car){
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
                '}';
    }
}

@SORMObject
class Car{
    @SORMID
    private int vin;
    @SORMField
    private String make = "";
    @SORMField
    private String model = "";

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