package dev;

import dev.model.annotation.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.Future;

/**
 * testing {@link SORM}
 */
public class SORMTest {

    @Test
    public void SORMObjectCreationTest() throws Exception {
        try(SORM sorm = SORM.createSORM()) {
            User u = new User("Bob", "b@g.com", 23, new Car("Toyota", "Truck", 999000));
            if(sorm.create(u, User.class, Integer.class).get()) {
                Future<Optional<User>> fou = sorm.getByID(23, User.class, Integer.class);
                Assert.assertTrue(fou.get().isPresent());
                Assert.assertEquals("User{id=23, name='Bob', email='b@g.com'}",fou.get().get().toString());
            }
        }
    }

    @Test
    public void SORMObjectUpdateTest() throws Exception {
        try(SORM sorm = SORM.createSORM()) {
            User u = new User("Bob", "b@g.com", 23, new Car("Toyota", "Truck", 999000));
            if(sorm.create(u, User.class, Integer.class).get()) {
                Future<Optional<User>> fou = sorm.getByID(23, User.class, Integer.class);
                Assert.assertTrue(fou.get().isPresent());
                Assert.assertEquals("User{id=23, name='Bob', email='b@g.com'}",fou.get().get().toString());
            }
            u = new User("Jane", "J@g.com", 23, new Car("Ford", "Car", 999000));
            if(sorm.update(u, User.class, Integer.class).get()) {
                Future<Optional<User>> fou = sorm.getByID(23, User.class, Integer.class);
                Assert.assertTrue(fou.get().isPresent());
                Assert.assertEquals("User{id=23, name='Jane', email='J@g.com'}",fou.get().get().toString());
            }
        }
    }

    @Test
    public void SORMObjectDeleteTest() throws Exception {
        try(SORM sorm = SORM.createSORM()) {
            User u = new User("Bob", "b@g.com", 23, new Car("Toyota", "Truck", 999000));
            if(sorm.create(u, User.class, Integer.class).get()) {
                Future<Optional<User>> fou = sorm.getByID(23, User.class, Integer.class);
                Assert.assertTrue(fou.get().isPresent());
                Assert.assertEquals("User{id=23, name='Bob', email='b@g.com'}",fou.get().get().toString());
            }
            if(sorm.delete(u, User.class, Integer.class).get()) {
                Future<Optional<User>> fou = sorm.getByID(23, User.class, Integer.class);
                Assert.assertFalse(fou.get().isPresent());
            }
        }
    }

    @Test
    public void SORMObjectGetByIDTest() throws Exception {
        try(SORM sorm = SORM.createSORM()) {
            User u = new User("Bob", "b@g.com", 23, new Car("Toyota", "Truck", 999000));
            if(sorm.create(u, User.class, Integer.class).get()) {
                Future<Optional<User>> fou = sorm.getByID(23, User.class, Integer.class);
                Assert.assertTrue(fou.get().isPresent());
                Assert.assertEquals("User{id=23, name='Bob', email='b@g.com'}",fou.get().get().toString());
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
