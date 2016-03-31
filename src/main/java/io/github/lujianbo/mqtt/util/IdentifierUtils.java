package io.github.lujianbo.mqtt.util;

import java.util.UUID;

/**
 * Created by jianbo on 2016/3/30.
 */
public class IdentifierUtils {

    /**
     * 生成一个UUID
     * */
    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }
}
