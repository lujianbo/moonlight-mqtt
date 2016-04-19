package io.github.lujianbo.mqtt.common.protocol;

/**
 * Created by jianbo on 2016/3/24.
 */
public class ConnackProtocol extends MQTTProtocol {

    //连接返回的状态编码
    public static final byte CONNECTION_ACCEPTED = 0x00;
    public static final byte UNNACEPTABLE_PROTOCOL_VERSION = 0x01;
    public static final byte IDENTIFIER_REJECTED = 0x02;
    public static final byte SERVER_UNAVAILABLE = 0x03;
    public static final byte BAD_USERNAME_OR_PASSWORD = 0x04;
    public static final byte NOT_AUTHORIZED = 0x05;

    boolean SessionPresentFlag;

    //返回的状态码
    byte ReturnCode;


    public boolean isSessionPresentFlag() {
        return SessionPresentFlag;
    }

    public void setSessionPresentFlag(boolean sessionPresentFlag) {
        SessionPresentFlag = sessionPresentFlag;
    }

    public byte getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(byte returnCode) {
        ReturnCode = returnCode;
    }
}
