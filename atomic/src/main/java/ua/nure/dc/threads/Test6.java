package ua.nure.dc.threads;

public class Test6 {

	public static void main(String[] args) throws InterruptedException {
		Run r = new Run();
		Thread thread = new Thread(r);
		thread.start();
		Thread.sleep(50);
		thread.interrupt();
		System.out.println(Thread.currentThread());

	}

	static class Run implements Runnable {
		@Override
		public void run() {
			Thread thread = Thread.currentThread();
			while (!thread.isInterrupted()) {
				try {
					Thread.sleep(10);
					System.out.println(Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
					thread.interrupt();
				}
			}
			System.out.println(Thread.currentThread().getName() + " interrupted");
		}
	}

}
