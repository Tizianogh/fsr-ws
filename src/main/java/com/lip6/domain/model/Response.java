package com.lip6.domain.model;

import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Response class builder, inspired by Sebastian Kaiser and adapted by Tiziano GHISOTTI.
 * source : https://www.linkedin.com/pulse/java-builder-pattern-sebastian-kaiser/
 * 
 * @author Tiziano GHISOTTI
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private final Date timeStamp;
    private final Map.Entry<?, ?> data;
    private final String message;
    private final HttpStatus status;
    private final int statusCode;

    private Response(Builder builder) {
        timeStamp = builder.timeStamp;
        data = builder.data;
        message = builder.message;
        status = builder.status;
        statusCode = builder.statusCode;
    }

    public static class Builder {
        public String message;
        public Date timeStamp;
        public Map.Entry<?, ?> data;
        public HttpStatus status;
        public int statusCode;

        public Builder() {
            this.message = message;
            this.timeStamp = timeStamp;
            this.data = data;
        }

        public Builder with(Consumer<Builder> consumer) {
            consumer.accept(this);
            return this;
        }

        public Response build() {
            return new Response(this);
        }

    }
}
