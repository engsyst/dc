package ua.nure.dc.threads.sync;

public class Test {
	private boolean flag; // shared object
	// this method runs on two threads
	
	
	static synchronized void m2(boolean flag) { // monitor is Class<Test>
		// ...
	}

	public synchronized void m(boolean flag) {  // critical section
		// monitor is 'this'
		this.flag = flag; // critical resource
		try {
			Thread.sleep(200);
		} // wait
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(this.flag + " == " + flag);
	} // true == false

	public Test() { // the constructor creates two threads
		new Thread() { // create the first thread
			public void run() {
				while (true) {
					m(true);
				}
			}
		}.start(); // start the first thread
		new Thread() { // create the second thread
			public void run() {
				while (true) {
					m(false);
				}
			}
		}.start(); // start the second thread
	}

	public static void main(String[] argv) {
		new Test();
	}
}