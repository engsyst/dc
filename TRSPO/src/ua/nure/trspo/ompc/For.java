package ua.nure.trspo.ompc;

public class For {
	public void test1() {

		/* === OMP CONTEXT === */
		class OMPContext {
			public int field_field;
		}
		final OMPContext ompContext = new OMPContext();
		ompContext.field_field = field;
		final org.omp4j.runtime.IOMPExecutor ompExecutor = new org.omp4j.runtime.DynamicExecutor(
				Runtime.getRuntime().availableProcessors());
		/* === /OMP CONTEXT === */
		for (int i_zk8 = 2; i_zk8 < ompContext.field_field; i_zk8 += 3) {
			final int i = i_zk8;
			ompExecutor.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName());
					System.out.println("hello @" + i);
				}
			});
		}
		ompExecutor.waitForExecution();

	}

	private int field = 40;

	public void test2() {

		/* === OMP CONTEXT === */
		class OMPContext_chZ {
			public int field_field;
			public For THAT;
		}
		final OMPContext_chZ ompContext_pt3 = new OMPContext_chZ();
		ompContext_pt3.THAT = this;
		ompContext_pt3.field_field = field;
		final org.omp4j.runtime.IOMPExecutor ompExecutor_guw = new org.omp4j.runtime.DynamicExecutor(
				Runtime.getRuntime().availableProcessors());
		/* === /OMP CONTEXT === */
		for (int i_pIq = 2; i_pIq < ompContext_pt3.field_field; i_pIq += 3) {
			final int i = i_pIq;
			ompExecutor_guw.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName());
					System.out.println("hello @" + i);
				}
			});
		}
		ompExecutor_guw.waitForExecution();

	}

	public static void main(String[] args) {
		For f = new For();
		f.test1();
		System.out.println("\n**********");
		f.test2();
	}
}
