package ua.nure.dc.threads.sync;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Test7 {

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<String> queue = new LinkedBlockingQueue<>(10);
		Worker worker = new Worker(queue);
		Thread[] threads = new Thread[] {
			new Thread(worker),
			new Thread(worker),
			new Thread(worker),
			new Thread(worker),
			new Thread(worker),
			new Thread(worker),				
		};
		for (Thread thread : threads) {
			thread.start();
		}
		Thread.sleep(20);
		for (int i = 0; i < 100; i++) {
			System.out.println("main is waiting to put a message " + queue.remainingCapacity());
			queue.put(String.valueOf(i));
		}
		Thread.sleep(20);
		for (Thread thread : threads) {
			thread.interrupt();
		}
	}

	static class Worker implements Runnable {

		final BlockingQueue<String> queue;

		public Worker(BlockingQueue<String> queue) {
			super();
			this.queue = queue;
		}

		@Override
		public void run() {
			Thread thread = Thread.currentThread();
			while (!thread.isInterrupted()) {
				String message;
				try {
					message = queue.take();
					System.out.println(thread.getName() + ": " + message);
					Thread.sleep(1);
				} catch (InterruptedException e) {
					thread.interrupt();
				}
			}
			System.out.println(thread.getName() + ": finished");
		}

	}

}
