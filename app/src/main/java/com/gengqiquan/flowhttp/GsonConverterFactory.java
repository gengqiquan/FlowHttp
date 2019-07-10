package com.gengqiquan.flowhttp;

import com.gengqiquan.flow.converter.Converter;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

public class GsonConverterFactory {
    public static Converter create() {

        return new GsonConverter();
    }

    public static class GsonConverter<T> implements Converter<T> {
        Gson gson = new Gson();

        @Override
        public T convert(ResponseBody body, Type type) throws IOException {
            TypeAdapter<T> adapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(type));
            JsonReader jsonReader = gson.newJsonReader(body.charStream());
            try {
                return adapter.read(jsonReader);
            } finally {
                body.close();
            }
        }
    }
}
