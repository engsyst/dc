package ua.nure.dc.threads.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class TestSyncPerformance {
	private static final int START_VALUE = 1_000_000;
	private static final int NUMBER_OF_WORKERS = 4;

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			test(NUMBER_OF_WORKERS, 
					new Worker(START_VALUE, new AtomicCounter(new AtomicInteger(START_VALUE))), 
					"Atomic");
			test(NUMBER_OF_WORKERS, 
					new Worker(START_VALUE, new LockCounter(START_VALUE)), 
					"Lock");
			test(NUMBER_OF_WORKERS, 
					new Worker(START_VALUE, new SynchronizedBlockCounter(START_VALUE)), 
					"SynchronizedBlock");
			test(NUMBER_OF_WORKERS, 
					new Worker(START_VALUE, new SynchronizedCounter(START_VALUE)), 
					"Synchronized");
		}
	}

	private static void test(int numberOfWorkers, Worker worker, String message) 
			throws InterruptedException {
		
		Thread[] threads = new Thread[numberOfWorkers];
		for (int i = 0; i < numberOfWorkers; i++) {
			threads[i] = new Thread(worker);
		}
		
		long start = System.nanoTime(); // System.currentTimeMillis(); // 
		for (Thread thread : threads) {
			thread.start();
		}
		for (Thread thread : threads) {
			thread.join();
		}
		System.out.printf("%s:\t%s%n", message, System.nanoTime() /* System.currentTimeMillis() */ - start);
		System.out.printf("Value\t%s%n", worker.get());
	}

	interface Counter {
		int decrement();
		int get();
	}

	static class AtomicCounter implements Counter {

		private final AtomicInteger counter;

		public AtomicCounter(AtomicInteger counter) {
			super();
			this.counter = counter;
		}

		@Override
		public int decrement() {
			return counter.addAndGet(-1);
		}

		@Override
		public int get() {
			return counter.get();
		}

	}
	
	static class LockCounter implements Counter {
		private int counter;
		private final ReentrantLock lock = new ReentrantLock();

		public LockCounter(int counter) {
			super();
			this.counter = counter;
		}

		@Override
		public int decrement() {
			try {
				lock.lock();
				return --counter;
			} finally {
				lock.unlock();
			}
		}

		@Override
		public int get() {
			return counter;
		}
	}
	
	static class SynchronizedBlockCounter implements Counter {
		private int counter;
		
		public SynchronizedBlockCounter(int counter) {
			super();
			this.counter = counter;
		}
		
		@Override
		public int decrement() {
			synchronized (this) {
				return --counter;
			}
		}

		@Override
		public int get() {
			return counter;
		}
	}

	static class SynchronizedCounter implements Counter {
		private int counter;

		public SynchronizedCounter(int counter) {
			super();
			this.counter = counter;
		}

		@Override
		public synchronized int decrement() {
			return --counter;
		}

		@Override
		public int get() {
			return counter;
		}
	}
	
	static class Worker implements Runnable {

		private final Counter counter;
		private final int times;

		public Worker(int times, Counter counter) {
			this.counter = counter;
			this.times = times;
		}

		@Override
		public void run() {
			for (int i = 0; i < times; i++) {
				counter.decrement();
			}
		}
		
		public int get() {
			return counter.get();
		}
	}
}
//public final int get();
//public final void set(int newValue);
//public final void lazySet(int newValue);
//public final int getAndSet(int newValue);
//public final boolean compareAndSet(int expectedValue, int newValue);
//public final boolean weakCompareAndSetPlain(int expectedValue, int newValue);
//public final int getAndIncrement();
//public final int getAndDecrement();
//public final int getAndAdd(int delta);
//public final int incrementAndGet();
//public final int decrementAndGet();
//public final int addAndGet(int delta);
//public final int getAndUpdate(IntUnaryOperator updateFunction);
//public final int updateAndGet(IntUnaryOperator updateFunction);
//public final int getAndAccumulate(int x, IntBinaryOperator accumulatorFunction);
//public final int accumulateAndGet(int x, IntBinaryOperator accumulatorFunction);