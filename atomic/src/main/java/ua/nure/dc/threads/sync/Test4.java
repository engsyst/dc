package ua.nure.dc.threads.sync;

public class Test4 {
	private boolean flag; // shared object
	
	public void m(boolean flag) {  // critical section
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
	
	static class Worker extends Thread {
		Test4 t1;
		Test4 t2;
		public Worker(Test4 t1, Test4 t2) {
			super();
			this.t1 = t1;
			this.t2 = t2;
		}
		
		@Override
		public void run() {
			synchronized (t1) { 
				System.out.println(getName() + " lock t1: " + t1);
				synchronized (t2) {
					System.out.println(getName() + " lock t2: " + t2);
				}
				System.out.println(getName() + " unlock t2: " + t2);
			}
			System.out.println(getName() + " unlock t1: " + t1);
		}
	}

	public static void main(String[] argv) {
		Test4 shared1 = new Test4();
		Test4 shared2 = new Test4();
		Thread thread1 = new Worker(shared1, shared2);
		Thread thread2 = new Worker(shared2, shared1);
		thread1.start(); // start the first thread
		thread2.start(); // start the second thread
	}
}