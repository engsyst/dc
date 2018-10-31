package ua.nure.trspo.omp;

public class For {
	public void test1() {
		// omp parallel for schedule(dynamic)
		for (int i = 2; i < field; i += 3) {
			System.out.println("hello @" + i);
		}
	}

	private int field = 40;

	public void test2() {
		// omp parallel for
		for (int i = 2; i < this.field; i += 3) {
			System.out.println("hello @" + i);
		}
	}

	public static void main(String[] args) {
		For f = new For();
		f.test1();
		System.out.println("\n**********");
		f.test2();
	}
}
