package ua.nure.dc.threads.sync;

public class Test8 {

	public static void main(String[] args) throws InterruptedException {
		// Deadlock
		Object a = new Object();
		Object b = new Object();
		Worker worker1 = new Worker(a, b);
		Worker worker2 = new Worker(b, a);
		Thread thread1 = new Thread(worker1);
		Thread thread2 = new Thread(worker2);
		thread1.start();
		thread2.start();
		Thread.sleep(20);
	}

	static class Worker implements Runnable {

		Object a;
		Object b;

		public Worker(Object a, Object b) {
			super();
			this.a = a;
			this.b = b;
		}

		@Override
		public void run() {
			synchronized (a) {
				System.out.println(Thread.currentThread().getName() + ": a");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				synchronized (b) {
					System.out.println(Thread.currentThread().getName() + ": b");
				}

			}
		}

	}

}
