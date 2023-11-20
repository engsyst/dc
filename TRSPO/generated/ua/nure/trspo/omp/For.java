package ua.nure.trspo.omp;

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
		for (int i_BD6 = 2; i_BD6 < ompContext.field_field; i_BD6 += 3) {
			final int i = i_BD6;
			ompExecutor.execute(new Runnable() {
				@Override
				public void run() {

					System.out.println("hello @" + i);
				}
			});
		}
		ompExecutor.waitForExecution();

	}

	private int field = 40;

	public void test2() {

		/* === OMP CONTEXT === */
		class OMPContext_yId {
			public int field_field;
			public For THAT;
		}
		final OMPContext_yId ompContext_6bN = new OMPContext_yId();
		ompContext_6bN.THAT = this;
		ompContext_6bN.field_field = field;
		final org.omp4j.runtime.IOMPExecutor ompExecutor_Irw = new org.omp4j.runtime.DynamicExecutor(
				Runtime.getRuntime().availableProcessors());
		/* === /OMP CONTEXT === */
		for (int i_DNP = 2; i_DNP < ompContext_6bN.field_field; i_DNP += 3) {
			final int i = i_DNP;
			ompExecutor_Irw.execute(new Runnable() {
				@Override
				public void run() {

					System.out.println(Thread.currentThread().getName() + "hello @" + i);
				}
			});
		}
		ompExecutor_Irw.waitForExecution();

	}

	public static void main(String[] args) {
		For f = new For();
		f.test1();
		System.out.println("\n**********");
		f.test2();
	}
}
