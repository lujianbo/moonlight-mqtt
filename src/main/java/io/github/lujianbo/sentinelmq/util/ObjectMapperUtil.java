package io.github.lujianbo.sentinelmq.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by jianbo on 2016/3/23.
 */
public class ObjectMapperUtil {

    public static ObjectMapper objectMapper = objectMapper();

    public static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        //格式化时间到ISO-8601
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper;
    }
}
