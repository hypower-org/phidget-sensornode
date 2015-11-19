package edu.hypower.gatech.phidget;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;

public class Test{
//    private static final String JSON = "{\"field1\":\"value1\", \"field2\":123}";
    private static final String JSON = "{\"field2\":123}";

    // HttpResponse in your case
    public static class ExternalObject {
        @Override
        public String toString() {
            return "MyExternalObject";
        }
    }

    public static class Bean {
        // make fields public to avoid writing getters in this example
        public String field1;
        public int field2;

        private ExternalObject external;

        public Bean(@JacksonInject("ex") final ExternalObject external, 
        		@JacksonInject("f") String f1) {
            this.external = external;
            this.field1 = f1;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "field1='" + field1 + '\'' +
                    ", field2=" + field2 +
                    ", external=" + external +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final InjectableValues.Std injectableValues = new InjectableValues.Std()
        		.addValue("ex", new ExternalObject()).addValue("f", "Yes");
        mapper.setInjectableValues(injectableValues);

        final Bean bean = mapper.readValue(JSON, Bean.class);
        System.out.println(bean);
    }
}