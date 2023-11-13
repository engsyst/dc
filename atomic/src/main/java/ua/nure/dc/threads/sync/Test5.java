package ua.nure.dc.threads.sync;

import java.util.LinkedList;
import java.util.Queue;

public class Test5 {

	public static void main(String[] args) throws InterruptedException {
		Worker worker = new Worker(new LinkedList<>());
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

		final Queue<String> queue;

		public Worker(Queue<String> queue) {
			super();
			this.queue = queue;
		}

		public void addMessage(String message) {
			synchronized (queue) {
				queue.add(message);
				System.out.println(Thread.currentThread().getName() + ": notifyAll");
				queue.notifyAll();
			}
		}

		@Override
		public void run() {
			Thread thread = Thread.currentThread();
			while (!thread.isInterrupted()) {
				synchronized (queue) {
					try {
						String message = null;
						while ((message = queue.poll()) == null) {
							queue.wait();
						}
						System.out.println(thread.getName() + ": " + message);
					} catch (InterruptedException e) {
						thread.interrupt();
					}
				}
			}
			System.out.println(thread.getName() + ": finished");
		}

	}

}
