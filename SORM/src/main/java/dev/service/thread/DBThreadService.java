package dev.service.thread;

import dev.database.SORMDAO;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//todo finish this with thread pooling
public class DBThreadService {
    private final int THREAD_POOL_SIZE = 5;
    ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public <T extends Serializable, I> Future<Optional<T>> getByID(I ID){
//        Callable callable = () -> {
//            SORMDAO<T, I> dao = new SORMDAO<>();
//            return dao.getById(ID);
//        };
//
//        return executorService.submit(callable);
        return null;
    }

    public <T extends Serializable, I> Future<?> create(T object){
//        Runnable runnable = () -> {
//            SORMDAO<T, I> dao = null;
//            try {
//                dao = new SORMDAO<>();
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//            assert dao != null;
//            //dao.create(object);
//        };

        //return executorService.submit(runnable);
        return null;
    }
}
