import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * Created by Diana on 22/04/2015.
 */


public class SayHelloTest {

    private static final String url="http://192.168.59.103:9200";

    protected void setup(){

    }

    protected String getHello(){
        String select="{\n" +
                "    \"query\": {\n" +
                "        \"query_string\": {\n" +
                "            \"query\": \"kill\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        RestApiConsumer consumer = new RestApiConsumer();
        try {
            consumer.execute(consumer.GET, url + "/example/toto", select);
            return consumer.getResponse();
        } catch (Exception e) {
            fail();
        }
        return "";
    }

    @Test
    public void testSayHello(){
        try {
            SayHello sayHello=new SayHello("{\"say\":\"hello\",\"to\":\"world\"}",url);
        } catch (Exception e) {
            fail();
        };
        assertTrue(getHello().contains("\"say\":\"hello\""));

    }

}
