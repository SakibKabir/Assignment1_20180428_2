package main;

public class test {
	private String a;

	public void test(double c) {
		a = "1";
		System.out.println(a);
		a = "2";
		System.out.println(a);
		System.out.println(this.a);
		test2(2);
	}

	public void test2(double c) {
		System.out.println(a);
	}
}
