package ua.nure.dc.threads;

public class Test5 extends Thread {
	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				Thread.sleep(10);
				System.out.println(Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
				interrupt();
			}
		}
		System.out.println(Thread.currentThread().getName() + " interrupted");
	}

	public static void main(String[] args) throws InterruptedException {

		Test5 thread = new Test5();
		thread.start();
		Thread.sleep(50);
		thread.interrupt();
		System.out.println(Thread.currentThread());
		
	}

}
