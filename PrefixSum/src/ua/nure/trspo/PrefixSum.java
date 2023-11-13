package ua.nure.trspo;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class PrefixSum {

    private static final int SIZE = 1_000;

    private static final int CORES = Runtime.getRuntime().availableProcessors();

    private static class SlicePoints {
        int start;
        int end;

        public SlicePoints(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private class ShiftTask implements Callable<Boolean> {

        private int start;
        private int end;
        private int places;

        public ShiftTask(int start, int end, int places) {
            this.start = start;
            this.end = end + places < values.length ? end + places : values.length;
            this.places = places;
        }

        public void setShiftPlaces(int places) {
            this.places = places;
        }

        @Override
        public Boolean call() throws Exception {
            for (int i = start; i < end; i++) {
                temp[i] = i < places ? 0 : values[i - places];
            }
            return true;
        }
    }

    private class SumTask implements Callable<Boolean> {

        private int start;
        private int end;

        public SumTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Boolean call() throws Exception {
            for (int i = start; i < end; i++) {
                values[i] += temp[i];
            }
            return true;
        }
    }

    private final ExecutorService pool;
    private final int cores;

    private double[] values;
    private double[] temp;

    private SlicePoints[] slices;

    public PrefixSum(double[] values) {
        this(values, CORES);
    }

    public PrefixSum(double[] values, int cores) {
        this.values = values;
        this.cores = cores;
        pool = Executors.newFixedThreadPool(cores);
        temp = new double[values.length];

        // define start and end points for each slice
        int sliceSize = values.length / cores;
        sliceSize = (sliceSize * cores) < values.length ? sliceSize + 1 : sliceSize;
        slices = new SlicePoints[cores];
        for (int i = 0; i < cores; i++) {
            int start = i * sliceSize;
            int end = start + sliceSize;
            end = end < values.length ? end : values.length;
            slices[i] = new SlicePoints(start, end);
        }
    }

    public double[] parallelSum() {
        int iter = 0;
        int shiftPlaces = 0;
        List<ShiftTask> shiftTasks = new ArrayList<>();
        for (int i = 0; i < cores; i++) {
            shiftTasks.add(new ShiftTask(slices[i].start, slices[i].end, shiftPlaces));
        }
        List<SumTask> sumTasks = new ArrayList<>();
        for (int i = 0; i < cores; i++) {
            sumTasks.add(new SumTask(slices[i].start, slices[i].end));
        }
        while (shiftPlaces < values.length) {
            shiftPlaces = (int) Math.round(Math.pow(2, iter++));
            try {
                for (int i = 0; i < cores; i++) {
                    shiftTasks.get(i).setShiftPlaces(shiftPlaces);
                }
                await(pool.invokeAll(shiftTasks));
//                for (int i = 0; i < cores; i++) {
//                    sumTasks.add(new SumTask(slices[i].start, slices[i].end));
//                }
                await(pool.invokeAll(sumTasks));
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                break;
            }
        }
        pool.shutdown();
        return values;
    }

    public double[] sequentalSum() {
        for (int i = 1; i < values.length; i++) {
            values[i] = values[i - 1] + values[i];
        }
        return values;
    }

    private void await(List<Future<Boolean>> res) throws InterruptedException, ExecutionException {
        for (Future<Boolean> future : res) {
            future.get();
        }
    }

    private static double[] init(int size) {
        double[] vals = new double[size];
        for (int i = 0; i < vals.length; i++) {
            vals[i] = i;
        }
        return vals;
    }

    public static void main(String[] args) {
        int cores = Runtime.getRuntime().availableProcessors();
        int size = SIZE;
        long st = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis() - st);
        test(1_000_000, cores);
//        int MAX_SIZE = 10_000;
//        for (int j = 1; j < 6; j++) {
//            for (int i = 0; i < 50; i++) {
//                System.out.println("Size: " + size);
//                System.out.println("Iteration: " + i);
//                test(size, cores);
//            }
//            size *= 10;
//        }
    }

    private static void test(int size, int cores) {
        PrefixSum ps = new PrefixSum(init(size), cores);
        long startTime = System.nanoTime();
        double[] res = ps.sequentalSum();
        long sequentalTime = System.nanoTime() - startTime;
        System.out.println("Sequental time: " + sequentalTime);
//        System.out.println(Arrays.toString(Arrays.stream(res).limit(10).toArray()));

        ps = new PrefixSum(init(size), cores);
        startTime = System.nanoTime();
        res = ps.parallelSum();
        long parallelTime = System.nanoTime() - startTime;
        System.out.println("Parallel time: " + parallelTime);
        System.out.println("Ratio: " + (sequentalTime / (double) parallelTime));
        System.out.println(Arrays.toString(Arrays.stream(res).limit(10).toArray()));
    }
}
