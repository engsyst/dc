package ua.nure.dc.threads;

public class Test extends Thread {
	
	private AClass value;

	Test(AClass value) {
		this.value = value;
	}

	@Override
	public void run() {
		m();
		value.m();
		System.out.println(Thread.currentThread().getName());
	}

	static void m() {
		System.out.println("m(): " + Thread.currentThread());
	}

	public static void main(String[] args) {
		AClass aClass = new AClass();
		Test test = new Test(aClass);
		Test test2 = new Test(aClass);
		Test test3 = new Test(aClass);
		test.run();
		test.start();
		test2.start();
		test3.start();

		Thread thread = new Thread(new MyRunnable(aClass));
		thread.start();
		System.out.println(Thread.currentThread());
	}

}

class MyRunnable implements Runnable {
	AClass val;

	public MyRunnable(AClass val) {
		this.val = val;
	}

	public void run() {
		Test.m();
		val.m();
		System.out.println(Thread.currentThread().getName());
	}

}

class AClass {

	void m() {
		System.out.println("AClass#m(): " + Thread.currentThread());
	}
}