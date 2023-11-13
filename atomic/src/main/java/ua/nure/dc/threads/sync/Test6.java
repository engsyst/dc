package ua.nure.dc.threads.sync;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Test6 {

	public static void main(String[] args) throws InterruptedException {
		Worker worker = new Worker(new LinkedBlockingQueue<>());
		Thread thread = new Thread(worker);
		thread.start();
		String[] messages = { "A", "B", "C", "D", };
//		for (String message : messages) {
//			worker.addMessage(message);
//		}
		Thread.sleep(20);
		for (String message : messages) {
			worker.addMessage(message);
		}
		Thread.sleep(20);
		thread.interrupt();
	}

	static class Worker implements Runnable {

		final BlockingQueue<String> queue;

		public Worker(BlockingQueue<String> queue) {
			super();
			this.queue = queue;
		}

		public void addMessage(String message) {
			queue.add(message);
			System.out.println(Thread.currentThread().getName() + ": add message");
		}

		@Override
		public void run() {
			Thread thread = Thread.currentThread();
			while (!thread.isInterrupted()) {
				String message;
				try {
					message = queue.take();
					System.out.println(thread.getName() + ": " + message);
				} catch (InterruptedException e) {
					thread.interrupt();
				}
			}
			System.out.println(thread.getName() + ": finished");
		}

	}

}
