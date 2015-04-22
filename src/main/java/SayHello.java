import org.apache.commons.lang3.builder.ToStringBuilder;

public class SayHello {

	private final String jsonRequest;



	public SayHello(String jsonRequest, String url) throws Exception {
        RestApiConsumer consumer = new RestApiConsumer();
        consumer.execute(consumer.POST, url + "/example/toto", jsonRequest);
        this.jsonRequest=jsonRequest;
    }

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("name", jsonRequest).
				toString();
	}

	public static void main(String []qsdqd ) {
        try {
            System.out.println(new SayHello("{\"say\":\"hello\",\"to\":\"world\"}",""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
