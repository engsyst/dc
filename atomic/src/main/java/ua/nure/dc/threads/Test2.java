package ua.nure.dc.threads;

public class Test2 extends Thread {
	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(100);
				System.out.println(Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {

		Test2 thread = new Test2();
		thread.setDaemon(true);
		thread.start();
		System.out.println(Thread.currentThread());
		Thread.sleep(300);
	}

}
