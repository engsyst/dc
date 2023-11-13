package ua.nure.dc.threads;

public class Test4 extends Thread {
	@Override
	public void run() {
		while (!isInterrupted()) {
			System.out.println(Thread.currentThread().getName());
		}
		System.out.println(Thread.currentThread().getName() + " interrupted");
	}

	public static void main(String[] args) throws InterruptedException {

		Test4 thread = new Test4();
		thread.start();
		Thread.sleep(50);
		thread.interrupt();
		System.out.println(Thread.currentThread());

	}

}
