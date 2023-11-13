package ua.nure.dc.threads;

public class Test3 extends Thread {
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

		Test3 thread = new Test3();
		thread.start();
		thread.join(); // it's executed in the 'main' thread
		System.out.println(Thread.currentThread());
		
	}

}
