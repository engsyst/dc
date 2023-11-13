package ua.nure.dc.threads.executors;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Test {
	public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> task = () -> {
            // Виконання обчислення
            return 10 + 20;
        };

        Future<Integer> future = executor.submit(task);

        // Отримання результату виконання. 
        // Очікує доки результат обчислюється
        int result = future.get();
        System.out.println("Результат виконання: " + result);
        executor.shutdown();
        System.out.println("Terminated: " + executor.isTerminated());
    }

}
