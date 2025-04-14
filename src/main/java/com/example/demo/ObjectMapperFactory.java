package com.example.demo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectMapperFactory {

    public static ObjectMapper newDefault() {
        var mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.coercionConfigFor(LogicalType.POJO).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        mapper.setDateFormat(dateFormat);

        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new SimpleModule()
            .addSerializer(OffsetDateTime.class, createOffsetDateSerializer())
            .addDeserializer(OffsetDateTime.class, createOffsetDateDeserializer())
        );

        return mapper;
    }

    public static ObjectMapper newDefaultFroExternalMatchback() {
        var mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.coercionConfigFor(LogicalType.POJO).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        mapper.setDateFormat(dateFormat);

        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new SimpleModule()
            .addSerializer(OffsetDateTime.class, createOffsetDateSerializer())
            .addDeserializer(OffsetDateTime.class, createOffsetDateDeserializer())
        );

        return mapper;
    }

    public static JsonSerializer<OffsetDateTime> createOffsetDateSerializer() {
        return new JsonSerializer<>() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx");

            @Override
            public void serialize(
                OffsetDateTime offsetDateTime,
                JsonGenerator jsonGenerator,
                SerializerProvider serializerProvider
            ) throws IOException {
                jsonGenerator.writeString(formatter.format(offsetDateTime));
            }
        };
    }

    public static JsonDeserializer<OffsetDateTime> createOffsetDateDeserializer() {
        return new JsonDeserializer<>() {
            @Override
            public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
                return OffsetDateTime.parse(p.getText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    .withOffsetSameInstant(ZoneOffset.ofHours(0));
            }
        };
    }
}
