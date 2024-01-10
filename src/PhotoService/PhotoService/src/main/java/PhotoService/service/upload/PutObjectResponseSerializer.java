package PhotoService.service.upload;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;

public class PutObjectResponseSerializer extends StdSerializer<PutObjectResponse> {
    public PutObjectResponseSerializer() {
        this(null);
    }

    public PutObjectResponseSerializer(Class<PutObjectResponse> t) {
        super(t);
    }

    @Override
    public void serialize(PutObjectResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("eTag", value.eTag());
        gen.writeEndObject();
    }
}
