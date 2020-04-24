package org.loose.fis.project.proofing.tool.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonMapperTest {

    public static final String JSON_TEST_OBJECT_STRING = "{\n" +
            "\t\"id\" : \"test\",\n" +
            "\t\"number\" : 3,\n" +
            "\t\"something\" : 10,\n" +
            "\t\"someOtherString\" : \"yes\",\n" +
            "\t\"someEmbeddedObject\" : {\n" +
            "\t\t\"embField\" : \"x\",\n" +
            "\t\t\"embArray\" : [ 1, 2, 3 ]\n" +
            "\t}\n" +
            "}";

    private JsonMapper jsonMapper;

    @BeforeEach
    void setUp() {
        jsonMapper = new JsonMapper();
    }

    @Test
    void readJSON() throws IOException {
        JsonTestEntity jsonTestEntity = jsonMapper.readJSON(JSON_TEST_OBJECT_STRING, JsonTestEntity.class);

        assertEquals("test", jsonTestEntity.getId());
        assertEquals(3, jsonTestEntity.getNumber());
        assertEquals(new BigDecimal(10), jsonTestEntity.get("something"));
        assertEquals("yes", jsonTestEntity.get("someOtherString"));
        assertEquals("x", ((Map<String, Object>) jsonTestEntity.get("someEmbeddedObject")).get("embField"));
        assertEquals(ImmutableSet.of(new BigDecimal(1), new BigDecimal(2), new BigDecimal(3)),
                new HashSet<BigDecimal>((Collection<? extends BigDecimal>) ((Map<String, Object>) jsonTestEntity.get("someEmbeddedObject")).get("embArray")));
    }

    @Test
    void getPrettySerializedObject() throws IOException {
        String serializedObject = getJsonString();
        assertEquals(JSON_TEST_OBJECT_STRING.replace("\t", "  "), serializedObject);
    }

    @Test
    void getSerializedObject() throws IOException {
        String serializedObject = getJsonString();
        assertEquals(JSON_TEST_OBJECT_STRING.replaceAll("\\s", ""), serializedObject.replaceAll("\\s", ""));
    }

    private String getJsonString() throws IOException {
        JsonTestEntity jsonTestEntity = new JsonTestEntity();
        jsonTestEntity.setId("test");
        jsonTestEntity.setNumber(3);
        jsonTestEntity.set("something", 10);
        jsonTestEntity.set("someOtherString", "yes");
        jsonTestEntity.set("someEmbeddedObject", ImmutableMap.of("embField", "x", "embArray", Arrays.asList(1, 2, 3)));

        return jsonMapper.getPrettySerializedObject(jsonTestEntity);
    }
}
