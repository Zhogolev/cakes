package app.utils;

import app.repository.CakeFilter;
import com.google.gson.Gson;

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
}
