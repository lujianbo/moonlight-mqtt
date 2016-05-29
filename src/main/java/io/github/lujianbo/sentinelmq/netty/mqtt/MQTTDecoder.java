package io.github.lujianbo.sentinelmq.netty.mqtt;

import io.github.lujianbo.sentinelmq.common.protocol.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 考虑到安全防御,哪怕是结构相同的数据，也要根据其状态进行分类处理
 */
public class MQTTDecoder extends ReplayingDecoder {


    public MQTTDecoder() {
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            byte byte1 = in.readByte();
            byte type = (byte) ((byte1 & 0x00F0) >> 4);// type
            int remainingLength = decodeRemainingLength(in);//读取剩余数据的长度
            ByteBuf remaining = ctx.alloc().buffer(remainingLength);
            remaining.writeBytes(in, remainingLength);//读取剩余的数据
            MQTTProtocol message = MQTTDecoderTool.valueOf(type).decode(byte1, remaining);
            out.add(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取变长的数据长度
     * */
    private int decodeRemainingLength(ByteBuf in) {
        int multiplier = 1;
        int value = 0;
        byte digit;
        do {
            digit = in.readByte();
            value += (digit & 0x7F) * multiplier;
            multiplier *= 128;
        } while ((digit & 0x80) != 0);
        return value;
    }


    enum MQTTDecoderTool {

        CONNECT((byte) 1, (b, in) -> {
            try {
                ConnectProtocol message = new ConnectProtocol();
                //读取协议名长度
                String protocolName = decodeUTF8String(in);
                //读取协议版本号
                byte protocolLevel = in.readByte();
                //读取当前连接的参数
                byte connectFlags = in.readByte();
                //读取会话保持时间
                int keepAlive = in.readUnsignedShort();

                message.setProtocolName(protocolName);
                message.setProtocolLevel(protocolLevel);
                message.setKeepAlive(keepAlive);

                //针对flags进行处理
                boolean cleanSession = ((connectFlags & 0x02) >> 1) == 1;
                boolean willFlag = ((connectFlags & 0x04) >> 2) == 1;
                byte willQos = (byte) ((connectFlags & 0x18) >> 3);
                boolean willRetain = ((connectFlags & 0x20) >> 5) == 1;
                boolean passwordFlag = ((connectFlags & 0x40) >> 6) == 1;
                boolean userFlag = ((connectFlags & 0x80) >> 7) == 1;

                message.setCleanSession(cleanSession);
                message.setWillFlag(willFlag);
                message.setWillQos(willQos);
                message.setWillRetain(willRetain);
                message.setPasswordFlag(passwordFlag);
                message.setUserFlag(userFlag);

                //读取clientId
                String clientId = decodeUTF8String(in);
                message.setClientId(clientId);
                //
                if (willFlag) {
                    String willTopic = decodeUTF8String(in);
                    message.setWillTopic(willTopic);
                    byte[] willMessage = readLengthPrefixedField(in);
                    message.setWillMessage(willMessage);
                }
                //读取用户名
                if (userFlag) {
                    String userName = decodeUTF8String(in);
                    message.setUserName(userName);
                }
                //读取密码
                if (passwordFlag) {
                    byte[] password = readLengthPrefixedField(in);
                    message.setPassword(password);
                }
                return message;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }),
        CONNACK((byte) 2, (b, in) -> {
            ConnackProtocol connackMessage = new ConnackProtocol();
            byte acknowledgeFlags = in.readByte();
            byte returnCode = in.readByte();
            connackMessage.setSessionPresentFlag(acknowledgeFlags == 0x01);
            connackMessage.setReturnCode(returnCode);
            return connackMessage;
        }),
        PUBLISH((byte) 3, (b, in) -> {
            try {
                PublishProtocol message = new PublishProtocol();
                String topicName = decodeUTF8String(in);
                message.setTopicName(topicName);
                boolean DUPflag = ((b & 0x08) >> 3) == 1;
                message.setDupFlag(DUPflag);
                byte qosLevel = (byte) ((b & 0x0006) >> 1);
                message.setQosLevel(qosLevel);
                boolean retain = (b & 0x01) == 1;
                message.setRetainFlag(retain);

                if (qosLevel == 1 || qosLevel == 2) {
                    //packetIdentifier 一个数据包的标识符号
                    int packetIdentifier = in.readUnsignedShort();
                    message.setPacketIdentifier(packetIdentifier);
                }

                //根据剩余数据的情况来读取payload
                if (in.readableBytes() > 0) {
                    byte[] payload = new byte[in.readableBytes()];
                    in.readBytes(payload);
                    message.setPayload(payload);
                }
                return message;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }),
        PUBACK((byte) 4, (b, in) -> {
            PubackProtocol pubackMessage = new PubackProtocol();
            int packetIdentifier = in.readUnsignedShort();
            pubackMessage.setPacketIdentifier(packetIdentifier);
            return pubackMessage;
        }),
        PUBREC((byte) 5, (b, in) -> {
            PubrecProtocol pubrecMessage = new PubrecProtocol();
            int packetIdentifier = in.readUnsignedShort();
            pubrecMessage.setPacketIdentifier(packetIdentifier);
            return pubrecMessage;
        }),
        PUBREL((byte) 6, (b, in) -> {
            PubrelProtocol pubrelMessage = new PubrelProtocol();
            int packetIdentifier = in.readUnsignedShort();
            pubrelMessage.setPacketIdentifier(packetIdentifier);
            return pubrelMessage;
        }),
        PUBCOMP((byte) 7, (b, in) -> {
            PubcompProtocol pubcompMessage = new PubcompProtocol();
            int packetIdentifier = in.readUnsignedShort();
            pubcompMessage.setPacketIdentifier(packetIdentifier);
            return pubcompMessage;
        }),
        SUBSCRIBE((byte) 8, (b, in) -> {
            SubscribeProtocol subscribeMessage = new SubscribeProtocol();
            try {
                int packetIdentifier = in.readUnsignedShort();
                subscribeMessage.setPacketIdentifier(packetIdentifier);
                while (in.readableBytes() > 0) {
                    String topicName = decodeUTF8String(in);
                    byte requestedQoS = in.readByte();
                    subscribeMessage.addTopicFilterQoSPair(topicName, requestedQoS);
                }
                return subscribeMessage;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }),
        SUBACK((byte) 9, (b, in) -> {
            SubackProtocol subackMessage = new SubackProtocol();
            int packetIdentifier = in.readUnsignedShort();
            subackMessage.setPacketIdentifier(packetIdentifier);
            while (in.readableBytes() > 0) {
                byte returnCode = in.readByte();
                subackMessage.addReturnCode(returnCode);
            }
            return subackMessage;
        }),
        UNSUBSCRIBE((byte) 10, (b, in) -> {
            try {
                UnsubscribeProtocol unsubscribeMessage = new UnsubscribeProtocol();
                int packetIdentifier = in.readUnsignedShort();
                unsubscribeMessage.setPacketIdentifier(packetIdentifier);
                while (in.readableBytes() > 0) {
                    String topicName = decodeUTF8String(in);
                    unsubscribeMessage.addTopicName(topicName);
                }
                return unsubscribeMessage;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }),
        UNSUBACK((byte) 11, (b, in) -> {
            UnsubackProtocol unsubackMessage = new UnsubackProtocol();
            int packetIdentifier = in.readUnsignedShort();
            unsubackMessage.setPacketIdentifier(packetIdentifier);
            return unsubackMessage;
        }),
        PINGREQ((byte) 12, (b, in) -> {
            return new PingreqProtocol();
        }),
        PINGRESP((byte) 13, (b, in) -> {
            return new PingrespProtocol();
        }),
        DISCONNECT((byte) 14, (b, in) -> {
            return new DisconnectProtocol();
        }),

        NULL((byte) 127, (b, in) -> {
            return null;
        });

        private BiFunction<Byte, ByteBuf, MQTTProtocol> decode;
        private byte type;

        MQTTDecoderTool(byte type, BiFunction<Byte, ByteBuf, MQTTProtocol> decode) {
            this.type = type;
            this.decode = decode;
        }

        public MQTTProtocol decode(byte byte1, ByteBuf in) {
            return decode.apply(byte1, in);
        }

        public static MQTTDecoderTool valueOf(byte type) {
            for (MQTTDecoderTool messageType : values()) {
                if (type == messageType.type) {
                    return messageType;
                }
            }
            return NULL;
        }

        /**
         * 读取带长度的UTF-8 String
         * */
        private static String decodeUTF8String(ByteBuf in) throws UnsupportedEncodingException {
            return new String(readLengthPrefixedField(in), "UTF-8");
        }

        /**
         * 读取带长度的String
         * */
        private static byte[] readLengthPrefixedField(ByteBuf in) {
            int strLen = in.readUnsignedShort();
            byte[] strRaw = new byte[strLen];
            in.readBytes(strRaw);
            return strRaw;
        }
    }

}
