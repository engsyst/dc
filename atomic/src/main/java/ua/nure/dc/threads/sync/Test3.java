package ua.nure.dc.threads.sync;

public class Test3 {
	private boolean flag; // shared object
	// this method runs on two threads
	
	private static final Object mutex = new Object();
	
	
	static synchronized void m2(boolean flag) { // monitor is Class<Test>
		// ...
	}

	public void m(boolean flag) {  // critical section
		// monitor is 'this'
//		synchronized (this.getClass()) {
//		synchronized (mutex) {
		synchronized (this) {
			this.flag = flag; // critical resource
			try {
				Thread.sleep(200);
			} // wait
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(this.flag + " == " + flag);
		}
	} // true == false

	public Test3() { // the constructor creates two threads
	}

	public static void main(String[] argv) {
		Test3 shared = new Test3();
		Thread thread1 = new Thread(new Runnable() {
			 // create the first thread
			public void run() {
				while (true) {
					shared.m(true);
				}
			}
		});
		thread1.start(); // start the first thread
		
		Thread thread2 = new Thread(new Runnable() {
			// create the second thread
			public void run() {
				while (true) {
					shared.m(false);
				}
			}
		});
		thread2.start(); // start the second thread
	}
}