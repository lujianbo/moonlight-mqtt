package io.github.lujianbo.sentinel.netty.mqtt;

import io.github.lujianbo.sentinel.protocol.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.UnsupportedEncodingException;

public class MQTTEncoder extends MessageToByteEncoder<MQTTProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MQTTProtocol msg, ByteBuf out) throws Exception {
        if (msg instanceof ConnectProtocol) {
            ByteBuf variableHeader = ctx.alloc().buffer(10);
            ByteBuf payLoad = ctx.alloc().buffer();
            try {
                ConnectProtocol message = (ConnectProtocol) msg;
                //Protocol Name
                variableHeader.writeBytes(encodeString(message.getProtocolName()));
                //Protocol Level
                variableHeader.writeByte(message.getProtocolLevel());

                //flags
                byte connectionFlags = 0;
                if (message.isCleanSession()) {
                    connectionFlags |= 0x02;
                }
                if (message.isWillFlag()) {
                    connectionFlags |= 0x04;
                }
                connectionFlags |= ((message.getWillQos() & 0x03) << 3);
                if (message.isWillRetain()) {
                    connectionFlags |= 0x020;
                }
                if (message.isPasswordFlag()) {
                    connectionFlags |= 0x040;
                }
                if (message.isUserFlag()) {
                    connectionFlags |= 0x080;
                }
                variableHeader.writeByte(connectionFlags);

                //keepAlive
                variableHeader.writeShort(message.getKeepAlive());

                //payLoad
                if (message.getClientId() != null) {
                    payLoad.readBytes(encodeString(message.getClientId()));
                    if (message.isWillFlag()) {
                        payLoad.writeBytes(encodeString(message.getWillTopic()));
                        payLoad.writeBytes(encodeFixedLengthContent(message.getWillMessage()));
                    }
                    if (message.isUserFlag() && message.getUserName() != null) {
                        payLoad.writeBytes(encodeString(message.getUserName()));
                        if (message.isPasswordFlag() && message.getPassword() != null) {
                            payLoad.writeBytes(encodeFixedLengthContent(message.getPassword()));
                        }
                    }
                }
                //写入数据
                out.writeByte(MQTTProtocol.CONNECT << 4);
                //写入长度
                out.writeBytes(encodeRemainingLength(variableHeader.readableBytes() + payLoad.readableBytes()));
                //可变头部
                out.writeBytes(variableHeader);
                //payload
                out.writeBytes(payLoad);
            } finally {
                variableHeader.release();
                payLoad.release();
            }
            return;
        }
        if (msg instanceof ConnackProtocol) {
            ConnackProtocol message = (ConnackProtocol) msg;

            out.writeByte(MQTTProtocol.CONNACK << 4);
            out.writeBytes(encodeRemainingLength(2));
            out.writeByte(message.isSessionPresentFlag() ? 0x01 : 0x00);
            out.writeByte(message.getReturnCode());
            return;
        }
        if (msg instanceof PublishProtocol) {
            PublishProtocol message = (PublishProtocol) msg;
            ByteBuf variableHeader = ctx.alloc().buffer(10);
            //flags
            byte flags = 0;
            if (message.isDupFlag()) {
                flags |= 0x08;
            }
            if (message.isRetainFlag()) {
                flags |= 0x01;
            }
            flags |= ((message.getQosLevel() & 0x03) << 1);

            //variableHeader
            variableHeader.writeBytes(encodeString(message.getTopicName()));//topic
            variableHeader.writeShort(message.getPacketIdentifier());//packet id

            //write
            out.writeByte(MQTTProtocol.PUBLISH << 4 | flags);
            out.writeBytes(encodeRemainingLength(variableHeader.readableBytes() + message.getPayload().length));
            out.writeBytes(variableHeader);
            out.writeBytes(message.getPayload());

            //release
            variableHeader.release();
            return;
        }
        if (msg instanceof PubackProtocol) {
            PubackProtocol message = (PubackProtocol) msg;
            out.writeByte(MQTTProtocol.PUBACK << 4);
            out.writeBytes(encodeRemainingLength(2));
            out.writeShort(message.getPacketIdentifier());
        }
        if (msg instanceof PubrecProtocol) {
            PubrecProtocol message = (PubrecProtocol) msg;
            out.writeByte(MQTTProtocol.PUBREC << 4);
            out.writeBytes(encodeRemainingLength(2));
            out.writeShort(message.getPacketIdentifier());
            return;
        }
        if (msg instanceof PubrelProtocol) {
            PubrelProtocol message = (PubrelProtocol) msg;
            out.writeByte(MQTTProtocol.PUBREL << 4);
            out.writeBytes(encodeRemainingLength(2));
            out.writeShort(message.getPacketIdentifier());
            return;
        }

        if (msg instanceof PubcompProtocol) {
            PubcompProtocol message = (PubcompProtocol) msg;
            out.writeByte(MQTTProtocol.PUBCOMP << 4);
            out.writeBytes(encodeRemainingLength(2));
            out.writeShort(message.getPacketIdentifier());
            return;
        }

        if (msg instanceof SubscribeProtocol) {
            SubscribeProtocol message = (SubscribeProtocol) msg;

            ByteBuf payload = ctx.alloc().buffer();
            for (SubscribeProtocol.TopicFilterQoSPair pair : message.getPairs()) {
                payload.writeBytes(encodeString(pair.getTopicName()));
                payload.writeByte(pair.getQos());
            }
            out.writeByte(MQTTProtocol.SUBSCRIBE << 4);
            out.writeBytes(encodeRemainingLength(2 + payload.readableBytes()));
            out.writeShort(message.getPacketIdentifier());
            out.writeBytes(payload);

            payload.release();
            return;
        }
        if (msg instanceof SubackProtocol) {
            SubackProtocol message = (SubackProtocol) msg;

            ByteBuf payload = ctx.alloc().buffer();
            for (byte b : message.getReturnCodes()) {
                payload.writeByte(b);
            }
            out.writeByte(MQTTProtocol.SUBACK << 4);
            out.writeBytes(encodeRemainingLength(2 + payload.readableBytes()));
            out.writeShort(message.getPacketIdentifier());
            out.writeBytes(payload);

            payload.release();
            return;
        }
        if (msg instanceof UnsubscribeProtocol) {
            UnsubscribeProtocol message = (UnsubscribeProtocol) msg;

            ByteBuf payload = ctx.alloc().buffer();
            for (String topicName : message.getTopicNames()) {
                payload.writeBytes(encodeString(topicName));
            }
            out.writeByte(MQTTProtocol.UNSUBSCRIBE << 4);
            out.writeBytes(encodeRemainingLength(2 + payload.readableBytes()));
            out.writeShort(message.getPacketIdentifier());
            out.writeBytes(payload);

            payload.release();
            return;
        }

        if (msg instanceof UnsubackProtocol) {
            UnsubackProtocol message = (UnsubackProtocol) msg;
            out.writeByte(MQTTProtocol.UNSUBACK << 4)
                    .writeBytes(encodeRemainingLength(2))
                    .writeShort(message.getPacketIdentifier());
            return;
        }
        if (msg instanceof PingreqProtocol) {
            out.writeByte(MQTTProtocol.PINGREQ << 4).writeByte(0);
            return;
        }
        if (msg instanceof PingrespProtocol) {
            out.writeByte(MQTTProtocol.PINGRESP << 4).writeByte(0);
            return;
        }
        if (msg instanceof DisconnectProtocol) {
            out.writeByte(MQTTProtocol.DISCONNECT << 4).writeByte(0);
            ctx.close();
            return;
        }
        /**
         * ERROR
         * */

    }

    private ByteBuf encodeString(String str) {
        byte[] raw;
        try {
            raw = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
        return encodeFixedLengthContent(raw);
    }

    private ByteBuf encodeFixedLengthContent(byte[] content) {
        ByteBuf out = Unpooled.buffer(2);
        out.writeShort(content.length);
        out.writeBytes(content);
        return out;
    }

    private ByteBuf encodeRemainingLength(int value) {
        ByteBuf encoded = Unpooled.buffer(4);
        byte digit;
        do {
            digit = (byte) (value % 128);
            value = value / 128;
            // if there are more digits to encode, set the top bit of this digit
            if (value > 0) {
                digit = (byte) (digit | 0x80);
            }
            encoded.writeByte(digit);
        } while (value > 0);
        return encoded;
    }

}
