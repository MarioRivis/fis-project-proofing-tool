package org.loose.fis.project.proofing.tool.utils;

import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.*;
import java.lang.reflect.Type;

public class JsonMapper {

    private final JacksonFactory jsonFactory = new JacksonFactory();

    public <T> T readJSON(String jsonString, Class<T> type) throws IOException {
        return new JsonObjectParser(jsonFactory).parseAndClose(new StringReader(jsonString), type);
    }

    public <T> T readJSONfromFile(File file, Class<T> type) throws IOException {
        return new JsonObjectParser(jsonFactory).parseAndClose(new FileReader(file), type);
    }

    public <T> T readJSON(Reader reader, Class<T> type) throws IOException {
        return new JsonObjectParser(jsonFactory).parseAndClose(reader, type);
    }

    public Object readJSON(String jsonString, Type type) throws IOException {
        return new JsonObjectParser(jsonFactory).parseAndClose(new StringReader(jsonString), type);
    }

    public Object readJSONfromFile(File file, Type type) throws IOException {
        return new JsonObjectParser(jsonFactory).parseAndClose(new FileReader(file), type);
    }

    public Object readJSON(Reader reader, Type type) throws IOException {
        return new JsonObjectParser(jsonFactory).parseAndClose(reader, type);
    }

    public String getSerializedObject(Object o) throws IOException {
        Writer out = new StringWriter();
        writeJSON(out, o);
        return out.toString();
    }

    public String getPrettySerializedObject(Object o) throws IOException {
        Writer out = new StringWriter();
        writePrettyJSON(out, o);
        return out.toString();
    }

    public void writePrettyJSON(Writer writer, Object o) throws IOException {
        JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
        generator.enablePrettyPrint();
        generator.serialize(o);
        generator.flush();
    }

    public void writeJSON(Writer writer, Object o) throws IOException {
        JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
        generator.serialize(o);
        generator.flush();
    }
}
