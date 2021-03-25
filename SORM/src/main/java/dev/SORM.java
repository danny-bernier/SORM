package dev;

import dev.service.thread.DBThreadService;

import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Primary Class SnailORM (SORM)
 * <p>This is the primary way for users of this
 * framework to interact with and utilize its features</p>
 */
public class SORM implements AutoCloseable{

    private final DBThreadService THREAD_SERVICE;

    private SORM(){
        this.THREAD_SERVICE = DBThreadService.INSTANCE;
    }

    public static SORM createSORM(){
        return new SORM();
    }

    public <T, I> Future<Optional<T>> getByID(I id, Class<T> tClass, Class<I> iClass){
        return THREAD_SERVICE.getByID(id, tClass, iClass);
    }

    public <T, I> Future<Boolean> create(T object, Class<T> tClass, Class<I> iClass){
        return THREAD_SERVICE.create(object, tClass, iClass);
    }

    public <T, I> Future<Boolean> update(T object, Class<T> tClass, Class<I> iClass){
        return THREAD_SERVICE.update(object, tClass, iClass);
    }

    public <T, I> void delete(T object, Class<T> tClass, Class<I> iClass){
        THREAD_SERVICE.delete(object, tClass, iClass);
    }

    @Override
    public void close() throws Exception {
        THREAD_SERVICE.close();
    }
}
