package app.utils;

import app.entity.Cake;
import app.repository.CakeFilter;
import com.google.gson.Gson;
import com.sun.istack.NotNull;
import org.hibernate.Session;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public abstract class Utils{

    public static CakeFilter getObjectFromJson(Gson gson,String jsonString){
        return  gson.fromJson(jsonString, CakeFilter.class);
    }


    public static  <T> T getData(CompletableFuture<T> cf, T exceptionObject){
        try {
            return cf.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("CakeController: execution data exception");
            System.out.println("request to bd is done = " + cf.isDone());
            System.out.println("request to bd is canceled = " + cf.isCancelled());
            System.out.println("request to bd is exception = " + cf.isCompletedExceptionally());
            return exceptionObject;
        }

    }
    public static void execute(CompletableFuture<?> cf){
        try {
            cf.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void removeItem(@NotNull Session session, @NotNull Cake cake){
        session.beginTransaction();
        session.remove(cake);
        session.flush();
        session.getTransaction().commit();
    }
}
