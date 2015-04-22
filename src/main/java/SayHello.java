import org.apache.commons.lang3.builder.ToStringBuilder;

public class SayHello {

	private final String name;

	public SayHello(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("name", name).
				toString();
	}

	public static void main(String []qsdqd ) {
		System.out.println(new SayHello("Diana"));
	}
}
