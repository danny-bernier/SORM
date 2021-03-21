package dev.service.thread;

import dev.database.GenericDAO;

import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//todo finish this
public class DBThreadService {
    private final int THREAD_POOL_SIZE = 5;
    ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public <T, I> Future<Optional<T>> getByID(I ID){
        Callable callable = () -> {
            GenericDAO<T, I> dao = new GenericDAO<>();
            return dao.getById(ID);
        };

        return executorService.submit(callable);
    }

    public <T, I> Future<?> create(T object){
        Runnable runnable = () -> {
            GenericDAO<T, I> dao = null;
            try {
                dao = new GenericDAO<>();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            assert dao != null;
            dao.create(object);
        };

        return executorService.submit(runnable);
    }
}
